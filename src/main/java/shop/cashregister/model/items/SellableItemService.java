package shop.cashregister.model.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;
import java.util.List;

import static java.text.MessageFormat.format;

@Service
public class SellableItemService{

    @Autowired
    private SellableItemRepository repository;

    public SellableItem save(SellableItem item){
        return repository.save(item);
    }

    public void delete(SellableItem item){
        repository.delete(item);
    }

    public SellableItem getByCode(String itemCode) throws InvalidNameException{
        if(SellableItem.isCodeValid(itemCode)){
            return repository.getItemByCode(itemCode);
        } else    throw new InvalidNameException(format("{0} is an invalid item code", itemCode));
    }

    public List<SellableItem> getAll(){
        return repository.getAll();
    }

}
