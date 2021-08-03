package shop.cashregister;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import shop.cashregister.control.CheckOutController;
import shop.cashregister.model.cashier.AuthorisationRequest;
import shop.cashregister.model.items.ItemsOfferService;
import shop.cashregister.model.items.SellableItemService;
import shop.cashregister.model.transactions.CashRegisterTransactionService;
import shop.cashregister.model.transactions.ChangeItemQuantityRequest;
import shop.cashregister.model.transactions.HistoricalItemsSoldService;
import shop.cashregister.model.transactions.IntermediateTransactionFeedback;

import javax.management.InvalidAttributeValueException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CheckoutControllerTest{
    @Autowired
    private CheckOutController checkOutController;

    @Autowired
    private SellableItemService itemsService;

    @Autowired
    private ItemsOfferService offersService;

    @Autowired
    private CashRegisterTransactionService transactionsService;

    @Autowired
    private HistoricalItemsSoldService historicalItemsSoldService;

    @Autowired
    private TestRestTemplate restTemplate;

    String cashierUsername="Caroline";
    String cashierPassword="pass";
    String url = "http://localhost:" + 8080 + "/" + cashierUsername + "/checkout";

    @BeforeEach
    private void authenticate(){
        AuthorisationRequest credentials = new AuthorisationRequest(cashierUsername, cashierPassword);
        HttpEntity<AuthorisationRequest> request = new HttpEntity<>(credentials);
        ResponseEntity<List> result = restTemplate.postForEntity(url, request, List.class);
    }

    private ResponseEntity<String> beginTransaction(){
        String endpoint = url + "/begin";
        return restTemplate.postForEntity(endpoint, null, String.class);
    }

    private ResponseEntity<IntermediateTransactionFeedback> addItem(ChangeItemQuantityRequest itemDesc){
        String endpoint = url + "/add_item";
        return restTemplate.postForEntity(endpoint, itemDesc, IntermediateTransactionFeedback.class);
    }

    private ResponseEntity<IntermediateTransactionFeedback> removeItem(ChangeItemQuantityRequest itemDesc){
        String endpoint = url + "/remove_item";
        return restTemplate.postForEntity(endpoint, itemDesc, IntermediateTransactionFeedback.class);
    }

    private ResponseEntity<?> endTransaction(){ //todo de-generify
        String endpoint = url + "/end";
        return restTemplate.postForEntity(endpoint, null, String.class);
    }

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
                new IntermediateTransactionFeedback(5, List.of()),    new IntermediateTransactionFeedback(25, List.of()),
                new IntermediateTransactionFeedback(45, List.of()),   new IntermediateTransactionFeedback(60, List.of()),
                new IntermediateTransactionFeedback(67.5, List.of()), new IntermediateTransactionFeedback(5, List.of())
        );

        for(int i=0; i<items.size(); i++){
            ResponseEntity<IntermediateTransactionFeedback> result = addItem(items.get(i));
            assertEquals(result.getBody(), expectedFeedback.get(i));
            assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());           // with an OK status code
        }
    }

    //TODO
    @Test
    public void testAddItemsVoucherOffer(){
    }

    @Test
    public void testAddItemsTshirtOffer() {
    }

    @Test
    public void testAddItemsMultipleOffersWithoutRemoving(){
    }

    @Test
    public void testAddItemsMultipleOffersWithRemoving(){
    }



    //END
    @Test
    public void testTransactionEnd(){
        //TODO populate the transactions
        String endpoint = url + "/end";
        ResponseEntity<Double> result = restTemplate.postForEntity(endpoint, null, Double.class);

        assertTrue(result.getBody() != null && result.getBody() > 0);               // response contains the final amount to pay
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());           // with an OK status code
    }

    @Test
    public void testTransactionEndBeforeStarting(){
        String endpoint = url + "/end";
        ResponseEntity<Double> result = restTemplate.postForEntity(endpoint, null, Double.class);

        assertNull(result.getBody());                                               // transaction cannot be ended before it begins, hence price to pay is null
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCodeValue());  // is an invalid request
    }


}
