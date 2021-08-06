# A list of offers applied to the basket, completely refreshes on each add_item / remove_item operation

from checkout_components.containers import StaticContainer, StaticContainerElement


class OffersApplied(StaticContainer):
    def __init__(self, parent, width, height, background=None):
        StaticContainer.__init__(self, parent, width=width, height=height, title="Offers Applied", background=background)


# Wrapper around the offer description string, display the description of the offer
class OffersAppliedEntry(StaticContainerElement):
    def __init__(self, offer_description: str, parent: OffersApplied):
        StaticContainerElement.__init__(self, parent, offer_description)
