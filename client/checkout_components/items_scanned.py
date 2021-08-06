import tkinter as tk
from functools import partial


# Container type for an item at the cash register
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
        tk.Frame.__init__(self, parent, width=200, height=400)
        self.parent = parent
        tk.Label(master=self, text='Scanned', font=("Verdana", 20)).pack(side=tk.TOP)
        self.scanned_items = []
        self.pack_propagate(0)
        self._remove_item_endpoint = f'{self.parent.route}/remove_item'

    def add_item_to_list(self, item):
        was_new_item_category_added=False
        for i, entry in enumerate(self.scanned_items):
            entry.pack()
            if entry.item == item:
                quantity_post_add = entry.quantity + 1
                entry.destroy()
                self.scanned_items[i] = ScannedItemsEntry(self, item, quantity_post_add)
                self.scanned_items[i].pack()
                was_new_item_category_added = True

        if not was_new_item_category_added:
            self.scanned_items.append(ScannedItemsEntry(self, item))
            self.scanned_items[-1].pack(side=tk.TOP)

    def remove_item_from_list(self, item):
        server_response = self.parent.controller.post(self._remove_item_endpoint, json={'itemCode':item.code,
                                                                                        'changeBy':'-1'})
        if server_response.status_code == 200:
            for i, entry in enumerate(self.scanned_items):
                entry.pack(side=tk.TOP)
                if entry.item == item:
                    quantity_post_remove = entry.quantity - 1
                    entry.destroy()
                    if quantity_post_remove > 0:
                        self.scanned_items[i] = ScannedItemsEntry(self, item, quantity_post_remove)
                        self.scanned_items[i].pack()
                    else:
                        del self.scanned_items[i]
                    self.parent.on_item_removed(server_response)

    def clear(self):
        for i, entry in enumerate(self.scanned_items):
            entry.destroy()
            del self.scanned_items[i]


# Wrapper around SellableItem, display the item, quantity currently selected, a remove button to decrement quantity by 1
class ScannedItemsEntry(tk.Frame):
    def __init__(self, parent: ScannedItems, item: SellableItem, quantity=1):
        tk.Frame.__init__(self, parent)
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
        self.parent.remove_item_from_list(self.item)
