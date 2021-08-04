package shop.cashregister.model.transactions;

import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.cashregister.model.items.SellableItem;
import shop.cashregister.model.offers.Basket;

import java.util.List;

import static java.text.MessageFormat.format;

@Service
public class TransactionItemsService{
    @Autowired
    private TransactionItemsRepository repository;

    public TransactionItems save(TransactionItems transaction){
        return repository.save(transaction);
    }

    public void delete(TransactionItems itemsSold){
        repository.delete(itemsSold);
    }

    public TransactionItems getByCheckOutAndItem(CashRegisterTransaction transaction, SellableItem item){
        return repository.getByItemAndCheckOut(transaction, item);
    }

    // return a basket, with no offers applied
    public Basket getBasketByTransaction(CashRegisterTransaction transaction){
        List<TransactionItems> transactionItems = repository.getBasketByTransaction(transaction);
        Basket basket = new Basket();
        for(TransactionItems itemInfo : transactionItems){
            String itemCode = itemInfo.getItem().getCode();
            int itemQuantity = itemInfo.getNumberSold();
            basket.setTotal(basket.getTotal() + itemQuantity * itemInfo.getItem().getDefaultPrice());
            basket.setNumberOf(itemCode, itemQuantity);

        }
        return basket;
    }

    public void putInBasket(CashRegisterTransaction transaction, SellableItem item, int quantity){
        if(quantity < 1)    throw new TransactionException("Number of items to put in a positive number, provided "+quantity);
        TransactionItems itemInfo = repository.getByItemAndCheckOut(transaction, item);
        if(itemInfo == null){       // the first item of this type being placed in the basket
            itemInfo = new TransactionItems();
            itemInfo.setItem(item);
            itemInfo.setParentTransaction(transaction);
            itemInfo.setNumberSold(quantity);
        } else{
            itemInfo.setNumberSold(itemInfo.getNumberSold()+quantity);
        }
        save(itemInfo);
    }

    public void removeFromBasket(CashRegisterTransaction transaction, SellableItem item, int quantity){
        if(quantity < 1)    throw new TransactionException("Number of items to remove must be a positive number, provided "+quantity);

        TransactionItems itemInfo = repository.getByItemAndCheckOut(transaction, item);
        if(itemInfo == null)    throw new TransactionException(format("Can't remove item {0} from transaction {1}, " +
                                        "as has not been put in the basket", item.getCode(), transaction.getId()));

        int numItemsActual = itemInfo.getNumberSold();

        if(numItemsActual < quantity){
            throw new TransactionException(format("Can't remove {0} items in transaction id={1}, there are only {2} of them!",
                    quantity, transaction.getId(), numItemsActual));
        } else if(numItemsActual > quantity){
            itemInfo.setNumberSold(numItemsActual - quantity);
            save(itemInfo);
        } else{                 // no use in keeping empty rows in the table
            delete(itemInfo);
        }
    }

}
