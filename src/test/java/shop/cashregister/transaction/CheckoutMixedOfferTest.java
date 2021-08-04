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
public class CheckoutMixedOfferTest extends AbstractTest{
    //todo better test names

    @Test
    public void test1() throws InvalidAttributeValueException{
        List<ChangeItemQuantityRequest> items = List.of(
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT", 1),
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("VOUCHER", 1),
                new ChangeItemQuantityRequest("PANTS", 1), new ChangeItemQuantityRequest("TSHIRT", 1),
                new ChangeItemQuantityRequest("TSHIRT", 1)
        );
        List<TransactionFeedback> expectedFeedback = List.of(
                new TransactionFeedback(5),  new TransactionFeedback(25),
                new TransactionFeedback(25),  new TransactionFeedback(30),
                new TransactionFeedback(37.5),  new TransactionFeedback(57.5),
                new TransactionFeedback(74.5)
        );
        testTransactionFeedbackEvolution(items, expectedFeedback);
    }

    @Test
    public void test2() throws InvalidAttributeValueException{
        List<ChangeItemQuantityRequest> items = List.of(
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT", 1),
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT", 1),
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT", 1),
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT", -1)
        );
        List<TransactionFeedback> expectedFeedback = List.of(
                new TransactionFeedback(5),  new TransactionFeedback(25),
                new TransactionFeedback(25), new TransactionFeedback(45),
                new TransactionFeedback(50), new TransactionFeedback(67),
                new TransactionFeedback(67), new TransactionFeedback(50)
        );
        testTransactionFeedbackEvolution(items, expectedFeedback);
    }

}
