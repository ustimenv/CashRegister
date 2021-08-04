package shop.cashregister.transaction;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import shop.cashregister.AbstractTest;
import shop.cashregister.model.transactions.ChangeItemQuantityRequest;
import shop.cashregister.model.transactions.TransactionFeedback;

import javax.management.InvalidAttributeValueException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CheckoutWholesaleDiscountOfferTest extends AbstractTest{

    @Test
    public void testAddItemsWithoutRemoving() throws InvalidAttributeValueException{
        List<ChangeItemQuantityRequest> items = List.of(
                new ChangeItemQuantityRequest("TSHIRT", 1), new ChangeItemQuantityRequest("TSHIRT", 1),
                new ChangeItemQuantityRequest("TSHIRT", 1), new ChangeItemQuantityRequest("VOUCHER", 1),
                new ChangeItemQuantityRequest("TSHIRT", 1)
        );
        List<TransactionFeedback> expectedFeedback = List.of(
                new TransactionFeedback(20),  new TransactionFeedback(40),
                new TransactionFeedback(57),  new TransactionFeedback(62),
                new TransactionFeedback(81)
        );
        testTransactionFeedbackEvolution(items, expectedFeedback);
    }



    @Test
    public void testAddItemsWithRemoving() throws InvalidAttributeValueException{
    }

    @Test
    public void testAddItemsWithIllegalRemoving() throws InvalidAttributeValueException{
    }
}
