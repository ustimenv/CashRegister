package shop.cashregister.model.transactions;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.InvalidAttributeValueException;

@Data
@NoArgsConstructor
public class ChangeItemQuantityRequest{
    private String itemCode;
    private int changeBy;   // may be positive or negative, but not equal to 0 since that would be an entirely redundant request

    public ChangeItemQuantityRequest(String itemCode, int changeBy) throws InvalidAttributeValueException{
        if(changeBy == 0)   throw new InvalidAttributeValueException("Quantity change cannot be 0!");
        this.changeBy = changeBy;
        this.itemCode = itemCode;
    }
}
