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

    @Test
    public void testTransactionBegin(){
        String token = authenticateUser(validCashierUsername, validCashierPassword).getBody();
        ResponseEntity<String> result = beginTransaction(token);
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());           // with an OK status code
    }

    @Test
    public void testAddItemsNoOffers() throws InvalidAttributeValueException{
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
        testTransactionFeedbackEvolution(items, expectedFeedback);
    }
}
