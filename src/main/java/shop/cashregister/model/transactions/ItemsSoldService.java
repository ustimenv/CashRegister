package shop.cashregister.model.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemsSoldService{
    @Autowired
    private ItemsSoldRepository repository;

    public ItemsSold save(ItemsSold itemsSold){
        return repository.save(itemsSold);
    }

    public void delete(ItemsSold itemsSold){
        repository.delete(itemsSold);
    }

    public ItemsSold getByCheckOutAndItem(long checkOutId, String itemCode){
        return repository.getByItemAndCheckOut(checkOutId, itemCode);
    }
}
