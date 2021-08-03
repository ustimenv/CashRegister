package shop.cashregister.model.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class IntermediateTransactionFeedback{
    private double subtotal;
    private List<String> suggestions = new ArrayList<>();
}
