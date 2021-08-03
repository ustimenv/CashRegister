package shop.cashregister;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import shop.cashregister.model.transactions.ChangeItemQuantityRequest;
import shop.cashregister.model.transactions.IntermediateTransactionFeedback;

import javax.management.InvalidAttributeValueException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CheckoutControllerTest extends AbstractTest{

    // BEGIN
    @Test
    public void testTransactionBegin(){
        ResponseEntity<String> result = beginTransaction();
        assertTrue(result.getBody() == null || result.getBody().length() < 1);      // empty response
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());           // with an OK status code
    }

    @Test
    public void testTransactionConcurrentBegin(){
        ResponseEntity<String> result = beginTransaction();
        assertTrue(result.getBody() == null || result.getBody().length() < 1);      // empty response
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCodeValue());  // a cashier can only perform one checkout operation at any given point in time
    }

    // ADD ITEM
    @Test
    public void testAddItemsNoOffers() throws InvalidAttributeValueException{
        List<ChangeItemQuantityRequest> items = List.of(
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT",1),
                new ChangeItemQuantityRequest("TSHIRT", 1),  new ChangeItemQuantityRequest("PANTS", 2),
                new ChangeItemQuantityRequest("PANTS", 1)
        );
        List<IntermediateTransactionFeedback> expectedFeedback = List.of(
                new IntermediateTransactionFeedback(5, null),    new IntermediateTransactionFeedback(25, null),
                new IntermediateTransactionFeedback(45, null),   new IntermediateTransactionFeedback(60, null),
                new IntermediateTransactionFeedback(67.5, null)
        );

        // The input items list doesn't contain any offers, we check to ensure no offer has been triggered
        // Since we only add 1 type of item per scan, a numerical equivalence is enough here
        testSubtotalsSeries(items, expectedFeedback);
    }

    //END TODO
    @Test
    public void testTransactionEnd(){
        ResponseEntity<IntermediateTransactionFeedback> result = endTransaction();

//        assertTrue(result.getBody() != null && result.getBody() > 0);               // response contains the final amount to pay
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());           // with an OK status code
    }

    @Test  //TODO
    public void testTransactionEndBeforeStarting(){
        ResponseEntity<IntermediateTransactionFeedback> result = endTransaction();
        assertNull(result.getBody());                                               // transaction cannot be ended before it begins, hence price to pay is null
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCodeValue());  // is an invalid request
    }


}
