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
import shop.cashregister.model.cashier.Cashier;
import shop.cashregister.model.cashier.CashierService;
import shop.cashregister.security.AuthorisationRequest;
import shop.cashregister.security.JwtTokenManager;

import java.text.MessageFormat;

@RestController
@RequestMapping("/cashier")
public class AuthController{
    private static Logger log = LogManager.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenManager tokenManager;

    @Autowired
    private CashierService cashierService;


    @PostMapping(value="/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> login(@RequestBody AuthorisationRequest authReq){
        String username = authReq.getUsername();
        String password = authReq.getPassword();
        try{
            Cashier cashier = cashierService.getByUsername(username);
            // attempt to log in
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword()));
            // if successful, update the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // and respond with the assigned jwt token to allow the client to sign their future requests
            return ResponseEntity.ok(tokenManager.generateToken(authentication));

        } catch(Exception e){
            log.info(MessageFormat.format("Invalid credentials for username {0}", username));
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
