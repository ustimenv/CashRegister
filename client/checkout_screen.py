import ast
import tkinter as tk
from functools import partial
from tkinter import ttk
from tkinter.ttk import Entry
import requests


class OffersApplied(tk.Frame):
    def __init__(self, parent):
        tk.Frame.__init__(self, parent, width=400, height=1000, background="#b22222")
        self.parent = parent
        tk.Label(self, text='Offers', font=("Verdana", 20))  # .grid(row=0, column=0)
        self.scanned_items = []
        # self.scanned_items.pack(side='left', padx=0, pady=0, anchor='w')

    def add_item(self, item):
        for entry in self.scanned_items:
            if entry.item == item:
                entry.quantity += 1
                return
        self.scanned_items.append(ScannedItemsEntry(item, self.parent))


class ScannedItems(tk.Frame):
    def __init__(self, parent):
        tk.Frame.__init__(self, parent, width=300, height=800)
        self.parent = parent
        self.title = tk.Label(master=self, text='Scanned', font=("Verdana", 20))
        self.title.pack(side=tk.TOP)
        self.scanned_items = []

    def _pack_all(self):
        for scanned_item in self.scanned_items:
            scanned_item.pack(side=tk.TOP)

    def add_item(self, item):
        for i, entry in enumerate(self.scanned_items):
            if entry.item == item:
                quantity = entry.quantity
                entry.destroy()
                self.scanned_items[i] = ScannedItemsEntry(item, self, quantity + 1)
                self._pack_all()
                return

        self.scanned_items.append(ScannedItemsEntry(item, self))
        self._pack_all()

    def remove_item(self, item):
        for i, entry in enumerate(self.scanned_items):
            if entry.item == item:
                quantity = entry.quantity
                entry.destroy()
                if quantity > 1:
                    self.scanned_items[i] = ScannedItemsEntry(item, self, quantity - 1)
                    self._pack_all()
                else:
                    del self.scanned_items[i]
                self._pack_all()
                return


class ScannedItemsEntry(tk.Frame):
    def __init__(self, item, parent, quantity=1):
        tk.Frame.__init__(self, parent, background='green')
        self.parent= parent
        self.item = item  # actual item stored in this entry
        self.quantity = quantity

        self.item_label = tk.Label(self, text=item.full_name)

        delete_function = partial(self._delete)
        self.delete_button = tk.Button(self, text='Remove', command=delete_function)

        self.quantity_label = tk.Label(self, text=self.quantity)

        self.item_label.pack(side=tk.LEFT)
        self.delete_button.pack(side=tk.LEFT)
        self.quantity_label.pack(side=tk.LEFT)

    def _delete(self):
        self.parent.remove_item(self.item)


class DropDownMenuWithSearch(tk.Frame):
    def __init__(self, parent):
        tk.Frame.__init__(self, parent)
        self.parent = parent

        # Combobox searchbar
        self.user_input = tk.StringVar()
        self.entry = Entry(self, textvariable=self.user_input)
        self.entry.bind("<KeyRelease>", self._search)
        self.entry.pack()

        # Combobox multiple choice
        self.combobox = ttk.Combobox(self, state='readonly')
        self.combobox['values'] = [f'{item.code}' for item in self.parent.sellable_items]
        self.combobox.bind("<<ComboboxSelected>>", self._add_item)
        self.combobox.pack()

    def _search(self, _):
        user_input = self.user_input.get()
        self.combobox['values'] = [f'{item.code}' for item in self.parent.sellable_items
                                   if item.code.startswith(user_input) or item.full_name.startswith(user_input)]

    def _add_item(self, _):
        self.parent.scan_item(self.combobox.get())


class SellableItem:
    def __init__(self, code, full_name, price):
        self.code = code
        self.full_name = full_name
        self.price = price

    def __eq__(self, other):
        if isinstance(other, SellableItem):
            return self.code == other.code
        return False

    def __hash__(self):
        return self.code.__hash__()


class CheckoutScreen(tk.Frame):
    # Endpoints
    _list_items_endpoint = None
    _begin_transaction_endpoint = None
    _end_transaction_endpoint = None
    _add_item_endpoint = None
    _remove_item_endpoint = None

    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)

        self.controller = controller
        # Frame elements
        self.name_label = None  # logged in cashier's name, top left corner
        self.scanned_items = ScannedItems(self)  # list of entries of type ScannedItem, on the left side

        self.item_select = None  # dropdown menu & searchbar to select items to scan
        self.sellable_items = set()

    # A number of variables depend on LoginScreen, so since __init__ doesn't get called on frame change,
    # the root controller will have to call prepare() explicitly
    def prepare(self):
        self._list_items_endpoint = f'{self.controller.checkout_url}/{self.controller.username}/list_items'
        self._begin_transaction_endpoint = f'{self.controller.checkout_url}/{self.controller.username}/begin'
        self._end_transaction_endpoint = f'{self.controller.checkout_url}/{self.controller.username}/end'
        self._add_item_endpoint = f'{self.controller.checkout_url}/{self.controller.username}/add_item'
        self._remove_item_endpoint = f'{self.controller.checkout_url}/{self.controller.username}/remove_item'

        self.sellable_items = self._list_items()
        # Child components
        self.item_select = DropDownMenuWithSearch(self)
        self.finalise_button = tk.Button(self, text="Finalise Transaction", command=self.finalise)

        # Wacky geometry
        tk.Label(self, text=f'Cashier: {self.controller.username}', font=("Verdana", 20)).pack(side=tk.TOP,
                                                                                               anchor=tk.NW)
        self.item_select.pack(side=tk.TOP)
        self.scanned_items.pack(side=tk.TOP, anchor=tk.NW)
        self.finalise_button.pack(side=tk.BOTTOM, anchor=tk.S)

    def finalise(self):
        pass

    def scan_item(self, item_code):
        for item in self.sellable_items:
            if item.code == item_code:
                self.scanned_items.add_item(item)
                return

    def _list_items(self):  # Initialise the set of all possible scanable items
        headers = {'Content-type': 'application/json', 'Authorization': f'Bearer {self.controller.session_token}'}
        req = requests.get(self._list_items_endpoint, headers=headers)
        items_info = ast.literal_eval(req.content.decode('utf-8'))

        return {SellableItem(item['code'], item['fullName'], item['defaultPrice']) for item in items_info}
