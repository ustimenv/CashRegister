package shop.cashregister.model.transactions;

import lombok.Getter;
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
    private @Getter long id;

    @Column(name="value", nullable=false)
    private @Setter double value=0;                      // (subtotal for STARTED transactions) money value of the transaction

    @Enumerated(EnumType.STRING)                // TODO convert to ORDINAL
    private @Setter Status status =Status.STARTED;

    @Column(name = "completion_time")
    private @Setter LocalDateTime completionTime;

    @ManyToOne
    @JoinColumn(name="transactions")
    private @Setter Cashier transactionExecutor;

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

    public String toString(){
        return String.valueOf(id);
    }
}
