package shop.cashregister.model.offers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.cashregister.model.items.SellableItem;
import shop.cashregister.model.items.SellableItemService;

import javax.naming.InvalidNameException;

@Service
public class OfferService{
    @Autowired
    private SellableItemService itemService;

    public WholesaleDiscountOffer getTshirtsFor19EachIfMoreThan2(){
        try{
            SellableItem tShirt = itemService.getByCode("TSHIRT");
            return new WholesaleDiscountOffer(tShirt, 3, 19);
        } catch(InvalidNameException e){
            e.printStackTrace();
            return null;
        }
    }

    public XForThePriceOfYOffer get2VouchersForThePriceOf1(){
        try{
            SellableItem voucher = itemService.getByCode("VOUCHER");
            return new XForThePriceOfYOffer(voucher, 1, 2);
        } catch(InvalidNameException e){
            e.printStackTrace();
            return null;
        }
    }
}
