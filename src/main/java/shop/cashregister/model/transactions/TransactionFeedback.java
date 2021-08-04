package shop.cashregister.model.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class TransactionFeedback{
    // generally is equivalent to subtotal, when returned by /checkout/end implies the final cost of the items
    private double amountToPay;

    // offers already applied to the basket, may be appended to or changed entirely when another item is added
    private List<String> offers = new ArrayList<>();

    // suggestions to purchase an additional item, which would trigger an additional offer
    private List<String> suggestions = new ArrayList<>();
}
