package shop.cashregister.model.offers;

import shop.cashregister.model.items.SellableItem;

public abstract class SingleItemOffer{
    protected SellableItem item;

    public abstract String getDescription();
    public abstract String getSuggestion(Basket basket);

    public abstract double getMaxDiscount(Basket  basket);
    public abstract boolean isAlmostApplicableToBasket(Basket  basket);
    public abstract boolean isApplicableToBasket(Basket  basket);

    public abstract Basket apply(Basket  basket);

    protected SingleItemOffer(SellableItem item){
        this.item = item;
    }
}
