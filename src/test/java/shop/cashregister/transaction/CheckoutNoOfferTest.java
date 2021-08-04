package shop.cashregister.transaction;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import shop.cashregister.AbstractTest;
import shop.cashregister.model.transactions.ChangeItemQuantityRequest;
import shop.cashregister.model.transactions.TransactionFeedback;

import javax.management.InvalidAttributeValueException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CheckoutNoOfferTest extends AbstractTest{
    private String username="A";
    private String password="pass";

    @Test
    public void testAddItemsNoRemoving() throws InvalidAttributeValueException{
        List<ChangeItemQuantityRequest> items = List.of(
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT",1),
                new ChangeItemQuantityRequest("TSHIRT", 1),  new ChangeItemQuantityRequest("PANTS", 2),
                new ChangeItemQuantityRequest("PANTS", 1)
        );
        List<TransactionFeedback> expectedFeedback = List.of(
                new TransactionFeedback(5),    new TransactionFeedback(25),
                new TransactionFeedback(45),   new TransactionFeedback(60),
                new TransactionFeedback(67.5)
        );

        // The input items list doesn't contain any offers, we check to ensure no offer has been triggered
        // Since we only add 1 type of item per scan, a numerical equivalence is enough here
        testTransactionFeedbackEvolution(items, expectedFeedback, username, password);
    }

    @Test
    public void testAddItemsWithRemoving() throws InvalidAttributeValueException{
        List<ChangeItemQuantityRequest> items = List.of(
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT",1),
                new ChangeItemQuantityRequest("VOUCHER", -1),  new ChangeItemQuantityRequest("PANTS", 2),
                new ChangeItemQuantityRequest("PANTS", -1)
        );

        List<TransactionFeedback> expectedFeedback = List.of(
                new TransactionFeedback(5),    new TransactionFeedback(25),
                new TransactionFeedback(20),   new TransactionFeedback(35),
                new TransactionFeedback(27.5)
        );

        // The input items list doesn't contain any offers, we check to ensure no offer has been triggered
        // Since we only add 1 type of item per scan, a numerical equivalence is enough here
        testTransactionFeedbackEvolution(items, expectedFeedback, username, password);
    }

    @Test
    public void testAddItemsWithIllegalRemoving() throws InvalidAttributeValueException{
        List<ChangeItemQuantityRequest> items = List.of(
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("VOUCHER", -1) ,
                new ChangeItemQuantityRequest("PANTS", -1)
        );

        List<TransactionFeedback> expectedFeedback = List.of(
                new TransactionFeedback(5),    new TransactionFeedback(),
                new TransactionFeedback()
        );
        String token = authenticateUser(username, password).getBody();
        beginTransaction(username, token);

        var result = addItem(username, token, items.get(0));
        assertEquals(expectedFeedback.get(0).getAmountToPay(), result.getBody().getAmountToPay());
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());

        result = addItem(username, token, items.get(1));
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCodeValue());

        result = addItem(username, token, items.get(2));
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCodeValue());

        endTransaction(username, token);
    }

}
