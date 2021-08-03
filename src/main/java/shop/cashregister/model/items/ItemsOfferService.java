package shop.cashregister.model.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemsOfferService{
    @Autowired
    private ItemsOfferRepository repository;

    public ItemsOffer save(ItemsOffer offer){
        return repository.save(offer);
    }

    public void delete(ItemsOffer offer){
        repository.delete(offer);
    }

    public List<ItemsOffer> getAll(){
        return repository.getAll();
    }

}
