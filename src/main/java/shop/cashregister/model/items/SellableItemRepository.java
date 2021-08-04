package shop.cashregister.model.items;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SellableItemRepository extends CrudRepository<SellableItem, Long>{
    @Query(value = "SELECT si FROM SellableItem si WHERE si.code = :code")
    SellableItem getItemByCode(@Param("code") String itemCode);

    @Query(value = "SELECT COUNT(si) FROM SellableItem si WHERE si.code = :code")
    int countItemsWithCode(@Param("code") String code);

    @Query(value = "SELECT si FROM SellableItem si")
    List<SellableItem> getAll();

}
