package shop.cashregister.model.transactions;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import shop.cashregister.model.items.SellableItem;

public interface HistoricalItemsSoldRepository extends CrudRepository<HistoricalItemsSold, Long>{
    @Query(value = "SELECT his FROM HistoricalItemsSold his WHERE his.transaction = :t AND his.item.code = :item")
    HistoricalItemsSold getByItemAndCheckOut(@Param("t") CashRegisterTransaction transaction,
                                             @Param("item") SellableItem item);
}
