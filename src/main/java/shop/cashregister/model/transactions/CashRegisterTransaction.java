package shop.cashregister.model.transactions;

import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.cashregister.model.cashier.Cashier;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor
public class CashRegisterTransaction{
    public enum Status{
        STARTED, DONE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double amount;                      // (subtotal for STARTED transactions) money value of the transaction

    @Enumerated(EnumType.STRING)                // TODO convert to ORDINAL
    private Status status =Status.STARTED;

    @Column(name = "completion_time")
    private LocalDateTime completionTime;

    @ManyToOne
    @JoinColumn(name="transactions")
    private @Setter
    Cashier transactionExecutor;

    @Override
    public boolean equals(Object other){
        if(other instanceof CashRegisterTransaction){
            return ((CashRegisterTransaction) other).id == id;
        } else return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}
