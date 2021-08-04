package shop.cashregister.model.transactions;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import shop.cashregister.model.items.SellableItem;

import java.util.List;

public interface TransactionItemsRepository extends CrudRepository<TransactionItems, Long>{
    @Query(value = "SELECT ti FROM TransactionItems ti WHERE ti.parentTransaction = :t AND ti.item = :item")
    TransactionItems getByItemAndCheckOut(@Param("t") CashRegisterTransaction transaction,
                                          @Param("item") SellableItem item);

    @Query(value = "SELECT ti FROM TransactionItems ti WHERE ti.parentTransaction = :t")
    List<TransactionItems> getBasketByTransaction(@Param("t") CashRegisterTransaction transaction);
}
