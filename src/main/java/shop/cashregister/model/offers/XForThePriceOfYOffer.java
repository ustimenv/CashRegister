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
        this.offerCode = "XFY_"+item.getCode()+"_"+numItemsPaidFor+"_"+numItemsReceived;
    }


    @Override
    public String getDescription(){
        return format("{0} {1}s for the price of {2}",
                        numItemsReceived, item.getFullName(), numItemsPaidFor);
    }

    @Override
    public String getSuggestion(Basket basket){
        return "If you buy just one more " + item + ", you will increase your total discount by"+item.getDefaultPrice()+" € !";
    }

    @Override
    public double getMaxDiscount(Basket basket){
        return item.getDefaultPrice() * ((int)(basket.getNumberOf(item.getCode()) / numItemsReceived));
    }

    @Override
    public boolean isAlmostApplicableToBasket(Basket basket){
        return basket.getNumberOf(item.getCode()) % numItemsReceived == 1;
    }

    @Override
    public boolean isApplicableToBasket(Basket basket){
        return basket.getNumberOf(item.getCode()) >= numItemsReceived;
    }

    @Override
    public Basket apply(Basket basket){
        if(isApplicableToBasket(basket)){ ;
            basket.setTotal(basket.getTotal() - getMaxDiscount(basket));
            basket.setNumberOf(item.getCode(),  basket.getNumberOf(item.getCode()) % numItemsReceived);
        }
        return basket;
    }

}
