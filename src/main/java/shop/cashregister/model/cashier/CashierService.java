package shop.cashregister.model.cashier;

import org.springframework.beans.factory.annotation.Autowired;

import javax.naming.InvalidNameException;

import static java.text.MessageFormat.format;

public class CashierService{

    @Autowired
    private CashierRepository repository;

    public Cashier save(Cashier cashier){
        return repository.save(cashier);
    }

    public void delete(Cashier cashier){
        repository.delete(cashier);
    }

    public Cashier getByUsername(String username) throws InvalidNameException{
        if(Cashier.isUsernameValid(username)){
            return repository.getCashierByUsername(username);
        } else    throw new InvalidNameException(format("{0} is an invalid username", username));
    }

    public boolean isUsernameAvailable(String username) throws InvalidNameException{
        if(Cashier.isUsernameValid(username)){
            return repository.countCashiersWithUsername(username) == 0;
        } else    throw new InvalidNameException(format("{0} is an invalid username", username));
    }

}
