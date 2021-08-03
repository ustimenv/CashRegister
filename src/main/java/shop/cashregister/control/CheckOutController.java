package shop.cashregister.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.cashregister.model.cashier.Cashier;
import shop.cashregister.model.cashier.CashierService;
import shop.cashregister.model.items.SellableItemService;
import shop.cashregister.model.offers.OfferService;
import shop.cashregister.model.transactions.CashRegisterTransaction;
import shop.cashregister.model.transactions.CashRegisterTransactionService;
import shop.cashregister.model.transactions.HistoricalItemsSoldService;
import shop.cashregister.model.transactions.IntermediateTransactionFeedback;

import javax.naming.InvalidNameException;
import java.security.Principal;

/**
 *  Controller responsible for all requests made by the cashier during the checkout process
 */
@RestController
@RequestMapping("/checkout")
public class CheckOutController{
    private static Logger log = LogManager.getLogger(CheckOutController.class);

    @Autowired
    private CheckOutController checkOutController;

    @Autowired
    private SellableItemService itemService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private CashierService cashierService;

    @Autowired
    private CashRegisterTransactionService transactionsService;

    @Autowired
    private HistoricalItemsSoldService historicalItemsSoldService;

    @Transactional
    @PostMapping(value="/{username}/begin", produces="application/json")
    public ResponseEntity<String> initiateTransaction(@PathVariable(value = "username") String username, Principal principal) throws InvalidNameException{
        if(!principal.getName().equals(username)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
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
    public ResponseEntity<IntermediateTransactionFeedback> endTransaction(@PathVariable(value = "username") String username, Principal principal) throws InvalidNameException{
        if(!principal.getName().equals(username)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Cashier cashier = cashierService.getByUsername(username);
        if(!transactionsService.doesCashierHaveActiveAnTransaction(cashier)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        CashRegisterTransaction transactionToEnd = transactionsService.getActiveTransactionByUser(cashier);
        //todo update long-term records
        transactionsService.delete(transactionToEnd);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping(value="/{username}/add_item", produces="application/json")
    public Object addItem(@PathVariable(value = "username") String username){
        return null;
    }

    @Transactional
    @PostMapping(value="/{username}/remove_item", produces="application/json")
    public Object removeItem(@PathVariable(value = "username") String username){
        return null;
    }
}
