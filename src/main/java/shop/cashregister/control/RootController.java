package shop.cashregister.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shop.cashregister.model.items.SellableItem;
import shop.cashregister.model.offers.Basket;
import shop.cashregister.model.offers.SingleItemOffer;
import shop.cashregister.model.transactions.*;
import shop.cashregister.model.cashier.Cashier;
import shop.cashregister.model.cashier.CashierService;
import shop.cashregister.model.items.SellableItemService;
import shop.cashregister.model.offers.OfferService;

import javax.naming.InvalidNameException;
import java.util.List;
import java.util.Map;

/**
 *  Controller responsible for all requests made by the cashier during the checkout process
 */
@RestController
@RequestMapping("/checkout")
public class RootController{
    private static Logger log = LogManager.getLogger(RootController.class);

    @Autowired
    private RootController rootController;

    @Autowired
    private SellableItemService itemService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private CashierService cashierService;

    @Autowired
    private CashRegisterTransactionService transactionsService;

    @Autowired
    private TransactionItemsService transactionItemsService;


    @Transactional
    @PostMapping(value="/{username}/begin", produces="application/json")
    public ResponseEntity<String> initiateTransaction(@PathVariable(value = "username") String username) throws InvalidNameException{
        Cashier cashier = cashierService.getByUsername(username);
        if(transactionsService.doesCashierHaveActiveAnTransaction(cashier)){
            return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        }

        CashRegisterTransaction newTransaction = new CashRegisterTransaction();
        newTransaction.setTransactionExecutor(cashier);
        transactionsService.save(newTransaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping(value="/{username}/end", produces="application/json")
    public ResponseEntity<TransactionFeedback> endTransaction(@PathVariable(value = "username") String username) throws InvalidNameException{
        Cashier cashier = cashierService.getByUsername(username);

        CashRegisterTransaction transactionToEnd = transactionsService.getActiveTransactionByUser(cashier);
        transactionsService.delete(transactionToEnd);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping(value="/{username}/add_item", produces="application/json")
    public ResponseEntity<TransactionFeedback> addItem(@PathVariable(value = "username") String username, @RequestBody ChangeItemQuantityRequest request) throws InvalidNameException{
        Cashier cashier = cashierService.getByUsername(username);
        if(cashier == null)         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CashRegisterTransaction transaction = transactionsService.getActiveTransactionByUser(cashier);
        if(transaction == null)     return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        SellableItem item = itemService.getByCode(request.getItemCode());
        if(item == null)            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        // add item to the 'basket'
        transactionItemsService.putInBasket(transaction, item, request.getChangeBy());
        Basket basket = transactionItemsService.getBasketByTransaction(transaction);

        // apply offers
        TransactionFeedback feedback = applyOffersToBasket(basket);

        // update transaction subtotal
        transaction.setValue(feedback.getAmountToPay());
        transactionsService.save(transaction);
        return ResponseEntity.ok(feedback);
    }

    private TransactionFeedback applyOffersToBasket(Basket basket){
        TransactionFeedback feedback = new TransactionFeedback(-1);
        List<SingleItemOffer> offers = offerService.getCurrentlyAvailableOffers();
        double discount = 0;
        for(SingleItemOffer offer : offers){
            if(offer.isApplicableToBasket(basket)){
                basket = offer.apply(basket);
                discount += offer.getMaxDiscount(basket);
                feedback.addOffer(offer.getDescription());
            }
            if(offer.isAlmostApplicableToBasket(basket)){
                feedback.addSuggestion(offer.getSuggestion(basket));
            }
        }
        feedback.setAmountToPay(basket.getTotal() - discount);
        return feedback;
    }

    @Transactional
    @PostMapping(value="/{username}/remove_item", produces="application/json")
    public Object removeItem(@PathVariable(value = "username") String username,
                             @RequestHeader Map<String, String> headers){
        return null;
    }


}
