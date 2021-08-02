package shop.cashregister.model.transactions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.cashregister.model.items.Item;

import javax.persistence.*;

/**
 * Used to store historical transactions, number of items
 */
@Entity
@NoArgsConstructor
public class ItemsSold{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="item", referencedColumnName="code")
    private @Getter Item item;

    @ManyToOne
    @JoinColumn(name="checkout", referencedColumnName="id")
    private @Getter CheckOut checkOut;

    private int numberSold = 0;
}
