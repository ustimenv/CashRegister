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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.cashregister.model.cashier.AuthorisationRequest;
import shop.cashregister.model.cashier.Cashier;
import shop.cashregister.model.cashier.CashierService;
import shop.cashregister.model.offers.OfferService;

import javax.naming.InvalidNameException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cashier")
public class CashierController{
    private static Logger log = LogManager.getLogger(CashierController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CashierService cashierService;

    @Autowired
    private OfferService offerService;

    @PostMapping(value="/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<String>> login(@RequestBody AuthorisationRequest authReq) throws InvalidNameException{
        String username = authReq.getUsername();
        String password = authReq.getPassword();
        Cashier cashier = cashierService.getByUsername(username);

        if(cashier!=null && passwordEncoder.matches(password, cashier.getPassword())){
            Authentication authentication = new UsernamePasswordAuthenticationToken(cashier, null, cashier.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info(MessageFormat.format("Cashier with username {0} has successfully logged in", username));
            return ResponseEntity.ok(new ArrayList<>());
        }
        log.info(MessageFormat.format("Invalid credentials for username {0}", username));
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

//    @GetMapping(value="/list_offers", produces = "application/json")
//    public List<String> listOffers() throws InvalidNameException{
//        List<SingleItemOffer> singleItemOffers = offerService.getAll();
//        if(singleItemOffers == null || singleItemOffers.size() == 0)    return List.of("No singleItemOffers are currently available");
//        return singleItemOffers.stream().map(SingleItemOffer::getDescription).collect(Collectors.toList());
//    }

}
