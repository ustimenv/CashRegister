package shop.cashregister.model.cashier;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CashierRepository extends CrudRepository<Cashier, Long>{
    @Query(value = "SELECT c FROM Cashier c WHERE c.username = :username")
    Cashier getCashierByUsername(@Param("username") String username);

    @Query(value = "SELECT COUNT(c) FROM Cashier c WHERE c.username = :username")
    int countCashiersWithUsername(@Param("username") String username);


}
