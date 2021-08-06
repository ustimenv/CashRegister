import ast
import tkinter as tk
from functools import partial


# Container type for an item at the cash register
import requests


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


class ScannedItems(tk.Frame):
    def __init__(self, parent):
        tk.Frame.__init__(self, parent, width=300, height=800)
        self.parent = parent
        self.title = tk.Label(master=self, text='Scanned', font=("Verdana", 20))
        self.title.pack(side=tk.TOP)
        self.scanned_items = []

        self._add_item_endpoint = f'{self.parent.route}/add_item'
        self._remove_item_endpoint = f'{self.parent.route}/remove_item'

    def add_item(self, item):
        for i, entry in enumerate(self.scanned_items):
            if entry.item == item:
                quantity = entry.quantity
                entry.destroy()
                self.scanned_items[i] = ScannedItemsEntry(item, self, quantity + 1)
            entry.pack(side=tk.TOP)

        self.scanned_items.append(ScannedItemsEntry(item, self))
        self.scanned_items[-1].pack(side=tk.TOP)


    def remove_item(self, item):
        for i, entry in enumerate(self.scanned_items):
            entry.pack(side=tk.TOP)
            if entry.item == item:
                quantity_post_remove = entry.quantity - 1
                entry.destroy()
                if quantity_post_remove > 0:
                    self.scanned_items[i] = ScannedItemsEntry(item, self, quantity_post_remove)
                else:
                    del self.scanned_items[i]


# Wrapper around SellableItem, display the item, quantity currently selected, a remove button to decrement quantity by 1
class ScannedItemsEntry(tk.Frame):
    def __init__(self, item, parent: ScannedItems, quantity=1):
        tk.Frame.__init__(self, parent, background='green')
        self.parent = parent
        self.item = item
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
