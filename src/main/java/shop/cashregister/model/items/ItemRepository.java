package shop.cashregister.model.items;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends CrudRepository<Item, Long>{
    @Query(value = "SELECT i FROM Item i WHERE i.code = :code")
    Item getItemByCode(@Param("code") String itemCode);

    @Query(value = "SELECT COUNT(i) FROM Item i WHERE i.code = :code")
    int countItemsWithCode(@Param("code") String code);
}
