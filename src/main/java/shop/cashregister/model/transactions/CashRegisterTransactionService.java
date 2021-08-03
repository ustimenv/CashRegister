package shop.cashregister.model.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashRegisterTransactionService{
    @Autowired
    private CashRegisterTransactionRepository repository;

    public CashRegisterTransaction save(CashRegisterTransaction transaction){
        return repository.save(transaction);
    }

    public void delete(CashRegisterTransaction transaction){
        repository.delete(transaction);
    }
}
