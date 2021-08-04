package shop.cashregister.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shop.cashregister.model.cashier.Cashier;
import shop.cashregister.model.cashier.CashierService;

import javax.naming.InvalidNameException;

import static java.text.MessageFormat.format;

@Service
public class CashierDetailsService implements UserDetailsService{
    private static Logger log = LogManager.getLogger(CashierDetailsService.class);

    @Autowired
    private CashierService cashierService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        try{
            Cashier cashier = cashierService.getByUsername(username);
            if(cashier == null) throw new UsernameNotFoundException(username);
            return User.withUsername(cashier.getUsername()).password(cashier.getPassword()).roles("Cashier").build();

        } catch (InvalidNameException e) {
            throw new UsernameNotFoundException(format("Cashier with username {0} doesn't exist!", username));
        }
    }
}

