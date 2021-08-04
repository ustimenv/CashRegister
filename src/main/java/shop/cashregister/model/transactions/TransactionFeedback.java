package shop.cashregister.model.transactions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TransactionFeedback{
    // generally is equivalent to subtotal, when returned by /checkout/end implies the final cost of the items
    private @Setter
    double amountToPay;

    // offers already applied to the basket, may be appended to or changed entirely when another item is added
    private List<String> offers = new ArrayList<>();

    // suggestions to purchase an additional item, which would trigger an additional offer
    private List<String> suggestions = new ArrayList<>();

    public TransactionFeedback(double amountToPay){
        this.amountToPay = amountToPay;
    }


    public void addOffer(String offerDescription){
        offers.add(offerDescription);
    }

    public void addSuggestion(String suggestion){
        suggestions.add(suggestion);
    }
}
