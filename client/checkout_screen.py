import ast
import tkinter as tk

import requests

from checkout_components.item_select import DropDownMenuWithSearch
from checkout_components.items_scanned import ScannedItems, SellableItem
from checkout_components.offers_applied import OffersApplied
from checkout_components.suggestions import Suggestions


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
        self.name_label = None                  # logged in cashier's name, top left corner
        self.finalise_button = None             # click to end the transaction and go to 'payment'
        self.scanned_items_container = None     # list of entries of type ScannedItem, on the left side
        self.item_select_container = None       # dropdown menu & searchbar to select items to scan
        self.offers_applied_container = None    # list of offers already applied to the basket
        self.suggestions_container = None       # based on the items already in the basket, suggest additional purchases
                                                # to activate futher offers

    # A number of variables depend on LoginScreen, so since __init__ doesn't get called on frame change,
    # the root controller will have to call prepare() explicitly
    def prepare(self):
        self.route = f'{self.controller.route}/checkout/{self.controller.username}'

        self._begin_transaction_endpoint = f'{self.route}/begin'
        self._end_transaction_endpoint = f'{self.route}/end'

        # Child components
        self.finalise_button = tk.Button(self, text="Finalise Transaction", command=self.finalise)
        self.scanned_items_container = ScannedItems(self)
        self.item_select_container = DropDownMenuWithSearch(self)
        self.offers_applied_container = OffersApplied(self, width=400, height=.4*self.winfo_height())
        self.suggestions_container = Suggestions(self, width=400, height=.4*self.winfo_height())

        # Wacky geometry
        tk.Label(self, text=f'Cashier: {self.controller.username}', font=("Verdana", 20)).pack(side=tk.TOP, anchor=tk.NW)
        self.item_select_container.pack(side=tk.TOP)
        self.scanned_items_container.pack(side=tk.TOP, anchor=tk.NW)
        self.offers_applied_container.pack(side=tk.TOP, anchor=tk.E)
        self.finalise_button.pack(side=tk.BOTTOM, anchor=tk.S)
        self.suggestions_container.pack(side=tk.BOTTOM, anchor=tk.E)
        self._begin_transaction()

    def _begin_transaction(self):
        headers = {'Content-type': 'application/json',
                   'Authorization': f'Bearer {self.controller.session_token}'}
        requests.post(self._begin_transaction_endpoint, headers=headers)

    def finalise(self):
        headers = {'Content-type': 'application/json',
                   'Authorization': f'Bearer {self.controller.session_token}'}
        req = requests.post(self._end_transaction_endpoint, headers=headers)
        print("Transaction ended")

    def update_offers(self, offers):
        self.offers_applied_container.refresh_offers(offers)

    def update_suggestions(self, suggestions):
        self.offers_applied_container.refresh_suggestions(suggestions)


    def scan_item(self, item):
        self.scanned_items_container.add_item(item)

