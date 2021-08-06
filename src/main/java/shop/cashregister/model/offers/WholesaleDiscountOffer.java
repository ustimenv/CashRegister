package shop.cashregister.model.offers;


import shop.cashregister.model.items.SellableItem;

import static java.text.MessageFormat.format;

public class WholesaleDiscountOffer extends SingleItemOffer{
    private int quantityThreshold;  // number of items above which this offer will apply (inclusive)
    private int discountPrice;      // price of each individual item once quantityThreshold is exceeded

    protected WholesaleDiscountOffer(SellableItem item, int quantityThreshold, int discountPrice){
        super(item);
        this.quantityThreshold = quantityThreshold;
        this.discountPrice = discountPrice;
        offerCode = "WDI_"+item.getCode() + "_" +quantityThreshold + "_" + discountPrice;
    }

    @Override
    public String getDescription(){
        return format("Buy more than {0} {1}s, pay {2} for each one",
                quantityThreshold-1, item.getFullName(), discountPrice);
    }

    @Override
    public String getSuggestion(Basket basket){
        return format("If you buy just one more {0}, you will increase your total discount by {1}!",
                item.getFullName(), getMaxDiscount(basket) - getMaxDiscount(basket.getBasketWithOneMoreOf(item.getCode())));
    }

    @Override
    public double getMaxDiscount(Basket basket){
        if(!isApplicableToBasket(basket)){
            return 0;
        } else{
            return basket.getNumberOf(item.getCode()) * (item.getDefaultPrice() - discountPrice);
        }
    }

    @Override
    public boolean isAlmostApplicableToBasket(Basket basket){
        return basket.getNumberOf(item.getCode()) == quantityThreshold-1;
    }

    @Override
    public boolean isApplicableToBasket(Basket basket){
        return basket.getNumberOf(item.getCode()) >= quantityThreshold;
    }

    @Override
    public Basket apply(Basket basket){         // apply the discount for all t shirts
        if(isApplicableToBasket(basket)){
            basket.setTotal(basket.getTotal() - getMaxDiscount(basket));
            basket.setNumberOf(item.getCode(), 0);
        }
        return basket;
    }
}
