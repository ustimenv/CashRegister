import ast
import tkinter as tk
from tkinter import ttk
from tkinter.ttk import Entry

from checkout_components.items_scanned import SellableItem


# Simple dropdown selection field that fitetrs items shown based on characters types in the search bar


class DropDownMenuWithSearch(tk.Frame):
    def __init__(self, parent):
        tk.Frame.__init__(self, parent)
        self.parent = parent

        self._add_item_endpoint = f'{self.parent.route}/add_item'
        self._list_items_endpoint = f'{self.parent.route}/list_items'
        self.sellable_items = self._list_items()

        # Combobox searchbar
        self.user_input = tk.StringVar()
        self.entry = Entry(self, textvariable=self.user_input)
        self.entry.bind("<KeyRelease>", self._search)
        self.entry.pack()

        # Combobox multiple choice
        self.combobox = ttk.Combobox(self, state='readonly')
        self.combobox['values'] = [f'{item.code}' for item in self.sellable_items]
        self.combobox.bind("<<ComboboxSelected>>", self._add_item)
        self.combobox.pack()

    def _search(self, _):
        user_input = self.user_input.get()
        self.combobox['values'] = [f'{item.code}' for item in self.sellable_items
                                   if item.code.startswith(user_input) or item.full_name.startswith(user_input)]

    def _add_item(self, _):
        for item in self.sellable_items:
            if item.code == self.combobox.get():
                server_response = self.parent.controller.post(self._add_item_endpoint,
                                                              json={'itemCode': item.code, 'changeBy': '1'})
                self.parent.on_item_added(item, server_response)

    # Initialise the set of all possible scanable items
    def _list_items(self):
        req = self.parent.controller.get(self._list_items_endpoint)
        items_info = ast.literal_eval(req.content.decode('utf-8'))

        return {SellableItem(item['code'], item['fullName'], item['defaultPrice']) for item in items_info}
