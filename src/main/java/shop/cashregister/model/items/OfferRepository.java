package shop.cashregister.model.items;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OfferRepository extends CrudRepository<Offer, Long>{
    @Query(value = "SELECT i FROM Item i")
    List<Offer> getAll();
}
