package shop.cashregister.model.items;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemsOfferRepository extends CrudRepository<ItemsOffer, Long>{
    @Query(value = "SELECT io FROM ItemsOffer io")
    List<ItemsOffer> getAll();
}
