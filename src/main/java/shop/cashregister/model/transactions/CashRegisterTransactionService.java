package shop.cashregister.model.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.cashregister.model.cashier.Cashier;

import java.util.List;

import static shop.cashregister.model.transactions.CashRegisterTransaction.Status.STARTED;

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

    public boolean doesCashierHaveActiveAnTransaction(Cashier cashier){
        return repository.countTransactionsWithStatusByCashier(STARTED, cashier) > 0;
    }

    public CashRegisterTransaction getActiveTransactionByUser(Cashier cashier){
        return repository.getTransactionsWithStatusByCashier(STARTED, cashier).get(0);
    }
    public List<CashRegisterTransaction> getAll(){
        return repository.getAll();
    }
}
