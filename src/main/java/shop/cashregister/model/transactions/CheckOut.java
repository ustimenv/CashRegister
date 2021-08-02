package shop.cashregister.model.transactions;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor
public class CheckOut{
    public enum Status{
        STARTED, DONE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double amount;                      // (current for STARTED transactions) money value of the transaction

    @Enumerated(EnumType.STRING)                // TODO convert to ORDINAL
    private Status status =Status.STARTED;

    private LocalDateTime completionTime;

    @Override
    public boolean equals(Object other){
        if(other instanceof CheckOut){
            return ((CheckOut) other).id == id;
        } else return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}
