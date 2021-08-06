import ast
import tkinter as tk
from http import server

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

    subtotal=0

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
        self.finalise_button = tk.Button(self, text="Finalise Transaction", command=self.end_transaction)
        self.scanned_items_container = ScannedItems(self)
        self.item_select_container = DropDownMenuWithSearch(self)
        self.offers_applied_container = OffersApplied(self, width=400, height=.3*self.winfo_height(), background='red')
        self.suggestions_container = Suggestions(self, width=400, height=.4*self.winfo_height(), background='blue')

        # Wacky geometry
        tk.Label(self, text=f'Cashier: {self.controller.username}', font=("Verdana", 20)).pack(side=tk.TOP, anchor=tk.NW)
        self.item_select_container.pack(side=tk.TOP)
        self.scanned_items_container.pack(side=tk.TOP, anchor=tk.NW)
        self.finalise_button.pack(side=tk.BOTTOM, anchor=tk.S)
        self.offers_applied_container.pack(side=tk.BOTTOM, anchor=tk.CENTER)
        self.suggestions_container.pack(side=tk.TOP, anchor=tk.E)
        self._begin_transaction()

    def _begin_transaction(self):
        self.controller.post(self._begin_transaction_endpoint, json={})

    def end_transaction(self):
        req = requests.post(self._end_transaction_endpoint)
        print("Transaction ended")

    def on_item_added(self, item, server_response):
        if server_response.status_code == 200:
            self._apply_feedback(ast.literal_eval(server_response.content.decode('utf-8')))
            self.scanned_items_container.add_item_to_list(item)

    def on_item_removed(self, server_response):
        if server_response.status_code == 200:
            self._apply_feedback(ast.literal_eval(server_response.content.decode('utf-8')))

    def _apply_feedback(self, feedback):
        print(feedback['amountToPay'])
        print(feedback['offers'])
        print(feedback['suggestions'])
        self.subtotal = feedback['amountToPay']
        self.offers_applied_container.refresh_elements(feedback['offers'])
        self.offers_applied_container.refresh_elements(feedback['suggestions'])
