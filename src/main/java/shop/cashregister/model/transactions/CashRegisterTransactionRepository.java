package shop.cashregister.model.transactions;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import shop.cashregister.model.cashier.Cashier;

import java.util.List;

public interface CashRegisterTransactionRepository extends CrudRepository<CashRegisterTransaction, Long>{
    @Query(value = "SELECT t FROM CashRegisterTransaction t WHERE t.status = :status AND t.transactionExecutor = :cashier")
    List<CashRegisterTransaction> getTransactionsWithStatusByCashier(@Param("status") CashRegisterTransaction.Status transactionStatus,
                                                                     @Param("cashier") Cashier cashier);

    @Query(value = "SELECT COUNT(t) FROM CashRegisterTransaction t WHERE t.status = :status AND t.transactionExecutor = :cashier")
    int countTransactionsWithStatusByCashier(@Param("status") CashRegisterTransaction.Status transactionStatus,
                                             @Param("cashier") Cashier cashier);
}
