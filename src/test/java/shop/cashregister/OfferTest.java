package shop.cashregister;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import shop.cashregister.model.transactions.ChangeItemQuantityRequest;
import shop.cashregister.model.transactions.TransactionFeedback;

import javax.management.InvalidAttributeValueException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OfferTest extends AbstractTest{

    @Test
    public void testAddItemsVoucherOffer() throws InvalidAttributeValueException{
        List<ChangeItemQuantityRequest> items = List.of(
                new ChangeItemQuantityRequest("VOUCHER", 1), new ChangeItemQuantityRequest("TSHIRT", 1),
                new ChangeItemQuantityRequest("VOUCHER", 1)
        );
        List<TransactionFeedback> expectedFeedback = List.of(
                new TransactionFeedback(7.5, null, null),  new TransactionFeedback(27.5, null, null),
                new TransactionFeedback(27.5, null, null)
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
        List<TransactionFeedback> expectedFeedback = List.of(
                new TransactionFeedback(20, null, null),  new TransactionFeedback(40, null, null),
                new TransactionFeedback(57, null, null),  new TransactionFeedback(62, null, null),
                new TransactionFeedback(81, null, null)
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
        List<TransactionFeedback> expectedFeedback = List.of(
                new TransactionFeedback(5, null, null),  new TransactionFeedback(25, null, null),
                new TransactionFeedback(25, null, null),  new TransactionFeedback(30, null, null),
                new TransactionFeedback(37.5, null, null),  new TransactionFeedback(57.5, null, null),
                new TransactionFeedback(74.5, null, null)
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
        List<TransactionFeedback> expectedFeedback = List.of(
                new TransactionFeedback(5, null, null),  new TransactionFeedback(25, null, null),
                new TransactionFeedback(25, null, null), new TransactionFeedback(45, null, null),
                new TransactionFeedback(50, null, null), new TransactionFeedback(67, null, null),
                new TransactionFeedback(67, null, null), new TransactionFeedback(50, null, null)
        );
        testSubtotalsSeries(items, expectedFeedback);
    }
}
