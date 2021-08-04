package shop.cashregister.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shop.cashregister.security.AuthorisationRequest;
import shop.cashregister.model.cashier.Cashier;
import shop.cashregister.model.cashier.CashierService;
import shop.cashregister.model.items.SellableItemService;
import shop.cashregister.model.offers.OfferService;
import shop.cashregister.model.transactions.CashRegisterTransaction;
import shop.cashregister.model.transactions.CashRegisterTransactionService;
import shop.cashregister.model.transactions.HistoricalItemsSoldService;
import shop.cashregister.model.transactions.TransactionFeedback;
import shop.cashregister.security.JwtTokenManager;

import javax.naming.InvalidNameException;
import java.text.MessageFormat;
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
    private HistoricalItemsSoldService historicalItemsSoldService;



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
    public Object addItem(@PathVariable(value = "username") String username,
                          @RequestHeader Map<String, String> headers){
        return null;
    }

    @Transactional
    @PostMapping(value="/{username}/remove_item", produces="application/json")
    public Object removeItem(@PathVariable(value = "username") String username,
                             @RequestHeader Map<String, String> headers){
        return null;
    }


}
