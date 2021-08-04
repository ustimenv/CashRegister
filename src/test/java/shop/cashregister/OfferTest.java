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
                new TransactionFeedback(7.5),  new TransactionFeedback(27.5),
                new TransactionFeedback(27.5)
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
                new TransactionFeedback(20),  new TransactionFeedback(40),
                new TransactionFeedback(57),  new TransactionFeedback(62),
                new TransactionFeedback(81)
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
                new TransactionFeedback(5),  new TransactionFeedback(25),
                new TransactionFeedback(25),  new TransactionFeedback(30),
                new TransactionFeedback(37.5),  new TransactionFeedback(57.5),
                new TransactionFeedback(74.5)
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
                new TransactionFeedback(5),  new TransactionFeedback(25),
                new TransactionFeedback(25), new TransactionFeedback(45),
                new TransactionFeedback(50), new TransactionFeedback(67),
                new TransactionFeedback(67), new TransactionFeedback(50)
        );
        testSubtotalsSeries(items, expectedFeedback);
    }
}
