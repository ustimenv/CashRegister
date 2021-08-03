package shop.cashregister.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.cashregister.model.cashier.AuthorisationRequest;
import shop.cashregister.model.cashier.Cashier;
import shop.cashregister.model.cashier.CashierService;

import javax.naming.InvalidNameException;
import java.text.MessageFormat;
import java.util.Map;

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

    @GetMapping(value="/login", consumes = "application/json", produces = "application/json")
    public Map<String, String> login(@RequestBody AuthorisationRequest authReq) throws InvalidNameException{
        String username = authReq.getUsername();
        String password = authReq.getPassword();
        Cashier cashier = cashierService.getByUsername(username);

        if(passwordEncoder.matches(password, cashier.getPassword())){
            Authentication authentication = new UsernamePasswordAuthenticationToken(cashier, null, cashier.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info(MessageFormat.format("Cashier with username {0} has successfully logged in", username));

        } else{
            System.out.println("NPOOOOOOO");
        }
        return null;
    }

}
