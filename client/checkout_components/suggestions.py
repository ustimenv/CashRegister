from checkout_components.containers import StaticContainer, StaticContainerElement


# A list of offers applied to the basket, completely refreshes on each add_item / remove_item operation
class Suggestions(StaticContainer):
    def __init__(self, parent, width, height, background=None):
        StaticContainer.__init__(self, parent, width=width, height=height, title='Offer Suggestions', background=background)


# Wrapper around the suggestion string string, something the cashier could suggest to the client
class SuggestionsEntry(StaticContainerElement):
    def __init__(self, offer_description: str, parent: Suggestions):
        StaticContainerElement.__init__(self, parent, offer_description)
