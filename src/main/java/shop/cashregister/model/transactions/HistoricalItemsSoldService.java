package shop.cashregister.model.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.cashregister.model.items.SellableItem;

@Service
public class HistoricalItemsSoldService{
    @Autowired
    private HistoricalItemsSoldRepository repository;

    public HistoricalItemsSold save(HistoricalItemsSold transaction){
        return repository.save(transaction);
    }

    public void delete(HistoricalItemsSold itemsSold){
        repository.delete(itemsSold);
    }

    public HistoricalItemsSold getByCheckOutAndItem(CashRegisterTransaction transaction, SellableItem item){
        return repository.getByItemAndCheckOut(transaction, item);
    }
}
