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
import org.springframework.web.bind.annotation.*;
import shop.cashregister.model.cashier.AuthorisationRequest;
import shop.cashregister.model.cashier.Cashier;
import shop.cashregister.model.cashier.CashierService;
import shop.cashregister.model.items.ItemsOffer;
import shop.cashregister.model.items.ItemsOfferService;

import javax.naming.InvalidNameException;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

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
    private ItemsOfferService offerService;

    @PostMapping(value="/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<String>> login(@RequestBody AuthorisationRequest authReq) throws InvalidNameException{
        String username = authReq.getUsername();
        String password = authReq.getPassword();
        Cashier cashier = cashierService.getByUsername(username);

        if(cashier!=null && passwordEncoder.matches(password, cashier.getPassword())){
            Authentication authentication = new UsernamePasswordAuthenticationToken(cashier, null, cashier.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info(MessageFormat.format("Cashier with username {0} has successfully logged in", username));
            return ResponseEntity.ok(listOffers());
        }
        log.info(MessageFormat.format("Invalid credentials for username {0}", username));
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value="/list_offers", produces = "application/json")
    public List<String> listOffers() throws InvalidNameException{
        List<ItemsOffer> offers = offerService.getAll();
        if(offers == null || offers.size() == 0)    return List.of("No offers are currently available");
        return offers.stream().map(ItemsOffer::getDescription).collect(Collectors.toList());
    }

}
