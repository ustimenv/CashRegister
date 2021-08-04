import ast
import tkinter as tk
from tkinter import ttk
from tkinter.ttk import Entry

import requests


class ScannedItems(tk.Frame):
    def __init__(self, parent):
        tk.Frame.__init__(self, parent)
        self.parent = parent
        self.scanned_items = []

    def add_item(self, item):
        for entry in self.scanned_items:
            if entry.item == item:
                entry.quantity += 1
                return
        self.scanned_items.append(ScannedItemsEntry(item, self.parent))


class ScannedItemsEntry(tk.Frame):
    def __init__(self, item, parent, quantity=1):
        tk.Frame.__init__(self, parent)
        self.item = item                        # actual item stored in this entry
        self.quantity = quantity

        self.item_label = tk.Label(self, text=item.full_name)
        self.delete_button = tk.Button(self, text='Remove', command=self.delete_one)
        self.quantity_label = tk.Label(self, text=item.full_name)

        self.item_label.grid(row=0, column=0)
        self.delete_button.grid(row=0, column=1)
        self.quantity_label.grid(row=0, column=2)

    def delete_one(self):
        self.quantity -= 1
        if self.quantity > 0:
            self.quantity_label['text'] = self.quantity
        else:
            self.destroy()


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
        self.name_label = None                              # logged in cashier's name, top left corner
        self.scanned_items = ScannedItems(parent)           # list of entries of type ScannedItem, on the left side
        self.scanner_frame = tk.Frame(self, width=100, height=100)
        self.scanner_frame.place(relx=.5, rely=.5, anchor=tk.CENTER)

        self.item_select = None                             # dropdown menu & searchbar to select items to scan
        self.sellable_items = set()

    # A number of variables depend on LoginScreen, so since __init__ doesn't get called on frame change,
    # the root controller will have call prepare explicitly
    def prepare(self):
        self._list_items_endpoint = f'{self.controller.checkout_url}/{self.controller.username}/list_items'
        self._begin_transaction_endpoint = f'{self.controller.checkout_url}/{self.controller.username}/begin'
        self._end_transaction_endpoint = f'{self.controller.checkout_url}/{self.controller.username}/end'
        self._add_item_endpoint = f'{self.controller.checkout_url}/{self.controller.username}/add_item'
        self._remove_item_endpoint = f'{self.controller.checkout_url}/{self.controller.username}/remove_item'

        self.name_label = tk.Label(self, text=f'Cashier: {self.controller.username}', font=("Verdana", 20))
        self.name_label.place(anchor=tk.NW)

        self.sellable_items = self._list_items()
        self.item_select = DropDownMenuWithSearch(self)
        self.item_select.place(relx=.5, rely=.1, anchor=tk.N)

    def scan_item(self, item_code):
        for item in self.sellable_items:
            if item.code == item_code:
                print('scann')
                self.scanned_items.add_item(item)
                return


    # Initialise the set of all possible scannable items
    def _list_items(self):
        headers = {'Content-type': 'application/json', 'Authorization': f'Bearer {self.controller.session_token}'}
        req = requests.get(self._list_items_endpoint, headers=headers)
        items_info = ast.literal_eval(req.content.decode('utf-8'))

        return {SellableItem(item['code'], item['fullName'], item['defaultPrice']) for item in items_info}
