package shop.cashregister.model.offers;

import shop.cashregister.model.items.SellableItem;

import static java.text.MessageFormat.format;

public class XForThePriceOfYOffer extends SingleItemOffer{
    // eg in a 2 for the price of 1 deal, then numItemsPaidFor is 1 and numItemsReceived is 2
    private int numItemsPaidFor;
    private int numItemsReceived;

    protected XForThePriceOfYOffer(SellableItem item, int numItemsPaidFor, int numItemsReceived){
        super(item);
        this.numItemsPaidFor = numItemsPaidFor;
        this.numItemsReceived = numItemsReceived;
    }


    @Override
    public String getDescription(){
        return format("If you buy {0} {1}, you get {2} for the same price!",
                        numItemsPaidFor, item.getFullName(), numItemsReceived);
    }

    @Override
    public String getSuggestion(Basket basket){
        return "If you buy just one more voucher, you will increase your total discount by 5€ !";
    }

    @Override
    public double getMaxDiscount(Basket basket){
        return item.getDefaultPrice() * (int)(basket.getNumberOf(item.getCode()) / 2);
    }

    @Override
    public boolean isAlmostApplicableToBasket(Basket basket){
        return basket.getNumberOf(item.getCode()) % 2 == 1;
    }

    @Override
    public boolean isApplicableToBasket(Basket basket){
        return basket.getNumberOf(item.getCode()) > 1;
    }

    @Override
    public Basket apply(Basket basket){
        if(isApplicableToBasket(basket)){ ;
            basket.setTotal(basket.getTotal() - getMaxDiscount(basket));
            basket.setNumberOf(item.getCode(), basket.getNumberOf(item.getCode()) % 2); // if odd number, set to 1, if even reset to 0
        }
        return basket;
    }

}
