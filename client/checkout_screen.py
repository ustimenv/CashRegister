import ast
import tkinter as tk

import requests

from checkout_components.item_select import DropDownMenuWithSearch
from checkout_components.items_scanned import ScannedItems, SellableItem


class CheckoutScreen(tk.Frame):
    # Endpoints
    route = None
    _list_items_endpoint = None
    _begin_transaction_endpoint = None
    _end_transaction_endpoint = None
    _add_item_endpoint = None
    _remove_item_endpoint = None

    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)

        self.controller = controller
        # Frame elements
        self.name_label = None      # logged in cashier's name, top left corner
        self.scanned_items = None   # list of entries of type ScannedItem, on the left side

        self.item_select = None     # dropdown menu & searchbar to select items to scan

    # A number of variables depend on LoginScreen, so since __init__ doesn't get called on frame change,
    # the root controller will have to call prepare() explicitly
    def prepare(self):
        self.route = f'{self.controller.route}/checkout/{self.controller.username}'

        self._begin_transaction_endpoint = f'{self.route}/begin'
        self._end_transaction_endpoint = f'{self.route}/end'

        # Child components
        self.item_select = DropDownMenuWithSearch(self)
        self.scanned_items = ScannedItems(self)
        self.finalise_button = tk.Button(self, text="Finalise Transaction", command=self.finalise)


        # Wacky geometry
        tk.Label(self, text=f'Cashier: {self.controller.username}', font=("Verdana", 20)).pack(side=tk.TOP,
                                                                                               anchor=tk.NW)
        self.item_select.pack(side=tk.TOP)
        self.scanned_items.pack(side=tk.TOP, anchor=tk.NW)
        self.finalise_button.pack(side=tk.BOTTOM, anchor=tk.S)

    def finalise(self):
        pass

    def scan_item(self, item):
        self.scanned_items.add_item(item)

