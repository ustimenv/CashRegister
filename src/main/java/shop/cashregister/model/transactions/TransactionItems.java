package shop.cashregister.model.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.cashregister.model.items.SellableItem;

import javax.persistence.*;

/**
 * Store the number of items of the given type in a transaction
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionItems{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="item", referencedColumnName="code")
    private SellableItem item;

    @ManyToOne
    @JoinColumn(name="parent_transaction", referencedColumnName="id")
    private CashRegisterTransaction parentTransaction;

    @Column(name = "number_sold", nullable = false)
    private int numberSold = 0;
}
