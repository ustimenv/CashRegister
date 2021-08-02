package shop.cashregister.model.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;

import static java.text.MessageFormat.format;

@Service
public class ItemService{

    @Autowired
    private ItemRepository repository;

    public Item save(Item item){
        return repository.save(item);
    }

    public void delete(Item item){
        repository.delete(item);
    }

    public Item getByCode(String itemCode) throws InvalidNameException{
        if(Item.isCodeValid(itemCode)){
            return repository.getItemByCode(itemCode);
        } else    throw new InvalidNameException(format("{0} is an invalid item code", itemCode));
    }

}
