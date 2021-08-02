package shop.cashregister.model.transactions;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ItemsSoldRepository extends CrudRepository<ItemsSold, Long>{
    @Query(value = "SELECT i FROM ItemsSold i WHERE i.checkOut.id = :checkoutID AND i.item.code = :itemCode")
    ItemsSold getByItemAndCheckOut(@Param("checkoutID") long checkoutId, @Param("itemCode") String itemCode);
}
