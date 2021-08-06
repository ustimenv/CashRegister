package shop.cashregister.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shop.cashregister.model.cashier.Cashier;
import shop.cashregister.model.cashier.CashierService;
import shop.cashregister.model.items.SellableItem;
import shop.cashregister.model.items.SellableItemService;
import shop.cashregister.model.offers.Basket;
import shop.cashregister.model.offers.OfferService;
import shop.cashregister.model.offers.SingleItemOffer;
import shop.cashregister.model.transactions.*;

import javax.naming.InvalidNameException;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

import static java.text.MessageFormat.format;

/**
 *  Controller responsible for all requests made by the cashier during the checkout process
 */
@RestController
@RequestMapping("/checkout")
public class RootController{
    private static Logger log = LogManager.getLogger(RootController.class);

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

    @GetMapping(value="/{username}/list_items", produces="application/json")
    public ResponseEntity<List<SellableItem>> listItems(@PathVariable(value = "username") String username) throws InvalidNameException{
        return ResponseEntity.ok(itemService.getAll());
    }

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
        transactionToEnd.setCompletionTime(LocalDateTime.now());
        transactionToEnd.setStatus(CashRegisterTransaction.Status.DONE);
        transactionsService.save(transactionToEnd);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping(value="/{username}/add_item", produces="application/json")
    public ResponseEntity<TransactionFeedback> addItem(@PathVariable(value = "username") String username, @RequestBody ChangeItemQuantityRequest request)
            throws InvalidNameException{
        Cashier cashier = cashierService.getByUsername(username);
        if(cashier == null){        // TODO check username against the security context
            log.error(format("Error starting transaction for cashier {0}: Failed to verify cashier identity",
                    username));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CashRegisterTransaction transaction = transactionsService.getActiveTransactionByUser(cashier);
        if(transaction == null){
            log.error(format("Error starting transaction for cashier {0}: Transaction hasn't been started",
                    cashier.getUsername()));

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        SellableItem item = itemService.getByCode(request.getItemCode());
        if(item == null){
            log.error(format("Error adding item by cashier {0}: Item code {1} specified is unknown",
                    cashier.getUsername(), request.getItemCode()));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // add item to the 'basket'
        try{
            transactionItemsService.putInBasket(transaction, item, request.getChangeBy());
        } catch(TransactionQuantityException e){    // invalid input
            log.error(format("Error adding item by cashier {0}: Invalid number of items {1}",
                    cashier.getUsername(), request.getChangeBy()));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Basket basket = transactionItemsService.getBasketByTransaction(transaction);

        // apply offers
        TransactionFeedback feedback = applyOffersToBasket(basket);

        // update transaction subtotal
        transaction.setValue(feedback.getAmountToPay());
        transactionsService.save(transaction);
        return ResponseEntity.ok(feedback);
    }

    @Transactional
    @PostMapping(value="/{username}/remove_item", produces="application/json")
    public ResponseEntity<TransactionFeedback> removeItem(@PathVariable(value = "username") String username, @RequestBody ChangeItemQuantityRequest request)
            throws InvalidNameException{

        Cashier cashier = cashierService.getByUsername(username);
        if(cashier == null)         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CashRegisterTransaction transaction = transactionsService.getActiveTransactionByUser(cashier);
        if(transaction == null)     return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        SellableItem item = itemService.getByCode(request.getItemCode());
        if(item == null)            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        // remove item from the 'basket'
        try{
            transactionItemsService.removeFromBasket(transaction, item, request.getChangeBy());
        } catch (TransactionQuantityException e){   // attempting to remove a non-existent item or invalid input
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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
        for(SingleItemOffer offer : offers){
            if(offer.isApplicableToBasket(basket)){
                basket = offer.apply(basket);
                feedback.addOffer(offer.getDescription());
            }
            if(offer.isAlmostApplicableToBasket(basket)){
                feedback.addSuggestion(offer.getSuggestion(basket));
            }
        }
        feedback.setAmountToPay(basket.getTotal());
        return feedback;
    }

}
