package shop.cashregister.model.offers;

import shop.cashregister.model.items.SellableItem;

public abstract class SingleItemOffer{
    protected SellableItem item;
    protected String offerCode;

    protected SingleItemOffer(SellableItem item){
        this.item = item;
    }

    public abstract String getDescription();
    public abstract String getSuggestion(Basket basket);

    public abstract double getMaxDiscount(Basket  basket);
    public abstract boolean isAlmostApplicableToBasket(Basket  basket);
    public abstract boolean isApplicableToBasket(Basket  basket);

    public abstract Basket apply(Basket  basket);


    public String getOfferCode(){
        return offerCode;
    }
    @Override
    public int hashCode(){
        return offerCode.hashCode();
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof SingleItemOffer){
            return ((SingleItemOffer) other).offerCode.equals(offerCode);
        }
        return false;
    }
}
