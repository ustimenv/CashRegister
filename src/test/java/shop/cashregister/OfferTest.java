package shop.cashregister;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import shop.cashregister.model.transactions.ChangeItemQuantityRequest;
import shop.cashregister.model.transactions.IntermediateTransactionFeedback;

import javax.management.InvalidAttributeValueException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OfferTest extends AbstractTest{

    //TODO
    @Test
    public void testAddItemsVoucherOffer() throws InvalidAttributeValueException{
        List<ChangeItemQuantityRequest> items = List.of(
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT", 1),
                new ChangeItemQuantityRequest("VOUCHER", 1)
        );
        List<IntermediateTransactionFeedback> expectedFeedback = List.of(
                new IntermediateTransactionFeedback(7.5, null),  new IntermediateTransactionFeedback(27.5, null),
                new IntermediateTransactionFeedback(27.5, null)
        );
        testSubtotalsSeries(items, expectedFeedback);
    }

    @Test
    public void testAddItemsTshirtOffer() throws InvalidAttributeValueException{
        List<ChangeItemQuantityRequest> items = List.of(
                new ChangeItemQuantityRequest("TSHIRT", 1), new ChangeItemQuantityRequest("TSHIRT", 1),
                new ChangeItemQuantityRequest("TSHIRT", 1), new ChangeItemQuantityRequest("VOUCHER", 1),
                new ChangeItemQuantityRequest("TSHIRT", 1)
        );
        List<IntermediateTransactionFeedback> expectedFeedback = List.of(
                new IntermediateTransactionFeedback(20, null),  new IntermediateTransactionFeedback(40, null),
                new IntermediateTransactionFeedback(57, null),  new IntermediateTransactionFeedback(62, null),
                new IntermediateTransactionFeedback(81, null)
        );
        testSubtotalsSeries(items, expectedFeedback);
    }

    @Test
    public void testAddItemsMultipleOffersWithoutRemoving() throws InvalidAttributeValueException{
        List<ChangeItemQuantityRequest> items = List.of(
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT", 1),
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("VOUCHER", 1),
                new ChangeItemQuantityRequest("PANTS", 1), new ChangeItemQuantityRequest("TSHIRT", 1),
                new ChangeItemQuantityRequest("TSHIRT", 1)
        );
        List<IntermediateTransactionFeedback> expectedFeedback = List.of(
                new IntermediateTransactionFeedback(5, null),  new IntermediateTransactionFeedback(25, null),
                new IntermediateTransactionFeedback(25, null),  new IntermediateTransactionFeedback(30, null),
                new IntermediateTransactionFeedback(37.5, null),  new IntermediateTransactionFeedback(57.5, null),
                new IntermediateTransactionFeedback(74.5, null)
        );
        testSubtotalsSeries(items, expectedFeedback);
    }

    @Test
    public void testAddItemsMultipleOffersWithRemoving() throws InvalidAttributeValueException{
        List<ChangeItemQuantityRequest> items = List.of(
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT", 1),
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT", 1),
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT", 1),
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT", -1)
        );
        List<IntermediateTransactionFeedback> expectedFeedback = List.of(
                new IntermediateTransactionFeedback(5, null),  new IntermediateTransactionFeedback(25, null),
                new IntermediateTransactionFeedback(25, null), new IntermediateTransactionFeedback(45, null),
                new IntermediateTransactionFeedback(50, null), new IntermediateTransactionFeedback(67, null),
                new IntermediateTransactionFeedback(67, null), new IntermediateTransactionFeedback(50, null)
        );
        testSubtotalsSeries(items, expectedFeedback);
    }
}
