package shop.cashregister.model.offers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Basket{
    private Map<String, Integer> items;     // map item code to item quantity
    private double total;                   // effective price of the items in the basket

    public Basket getBasketWithOneMoreOf(String itemCode){
        Basket newBasket = new Basket(items, total);
        newBasket.setNumberOf(itemCode, newBasket.getNumberOf(itemCode)+1);
        return newBasket;
    }

    public int getNumberOf(String itemCode){
        return items.getOrDefault(itemCode, 0);
    }

    public int setNumberOf(String itemCode, int newNumberOf){
        return items.put(itemCode, newNumberOf);
    }

    public double getTotal(){
        return total;
    }

    public void setTotal(double total){
        this.total = total;
    }
}
