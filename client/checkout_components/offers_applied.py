import tkinter as tk


# A list of offers applied to the basket, completely refreshes on each add_item / remove_item operation
class OffersApplied(tk.Frame):
    def __init__(self, parent):
        tk.Frame.__init__(self, parent, width=400, height=1000, background="#b22222")
        self.parent = parent
        tk.Label(self, text='Offers', font=("Verdana", 20))  # .grid(row=0, column=0)
        self.offers = []

    def refresh_offers(self, offers):
        [offer.destroy() for offer in self.offers]
        for i, offer in enumerate(offers):
            self.offers.append(OffersAppliedEntry(offer, self))
            self.offers[i].pack(side=tk.TOP)


# Wrapper around SellableItem, display the description of the offer
class OffersAppliedEntry(tk.Frame):
    def __init__(self, offer_description: str, parent: OffersApplied, quantity=1):
        tk.Frame.__init__(self, parent, background='blue')
        self.parent = parent
        tk.Label(self, text=offer_description)
