package shop.cashregister.model.transactions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.cashregister.model.items.SellableItem;

import javax.persistence.*;

/**
 * Used to store historical transactions, number of items sold in a given transaction
 */
@Entity
@NoArgsConstructor
public class HistoricalItemsSold{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="item", referencedColumnName="code")
    private @Getter SellableItem item;

    @ManyToOne
    @JoinColumn(name="checkout", referencedColumnName="id")
    private @Getter CashRegisterTransaction transaction;

    @Column(name = "number_sold", nullable = false)
    private int numberSold = 0;
}
