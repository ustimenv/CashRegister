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
    }

    @Override
    public String getDescription(){
        return format("If you buy more than {0} {1}, you only pay {2} for each one!",
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
            int numTShirts = basket.getNumberOf(item.getCode());
            return numTShirts * item.getDefaultPrice() - numTShirts * discountPrice;
        }
    }

    @Override
    public boolean isAlmostApplicableToBasket(Basket basket){
        return basket.getNumberOf(item.getCode()) == 2;
    }

    @Override
    public boolean isApplicableToBasket(Basket basket){
        return basket.getNumberOf(item.getCode()) >= 3;
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
