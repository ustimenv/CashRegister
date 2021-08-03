package shop.cashregister;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.junit4.SpringRunner;
import shop.cashregister.control.CheckOutController;
import shop.cashregister.model.cashier.AuthorisationRequest;
import shop.cashregister.model.items.ItemsOfferService;
import shop.cashregister.model.items.SellableItemService;
import shop.cashregister.model.transactions.CashRegisterTransactionService;
import shop.cashregister.model.transactions.HistoricalItemsSoldService;

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

    @BeforeTestExecution
    public void authenticate(){
        AuthorisationRequest credentials = new AuthorisationRequest(cashierUsername, cashierPassword);
    }

    // BEGIN
    @Test
    public void testTransactionBegin(){
        String endpoint = url + "/begin";
        ResponseEntity<String> result = restTemplate.postForEntity(endpoint, null, String.class);

        assertTrue(result.getBody() == null || result.getBody().length() < 1);      // empty response
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());           // with an OK status code
    }

    @Test
    public void testTransactionConcurrentBegin(){
        testTransactionBegin();
        String endpoint = url + "/begin";
        ResponseEntity<String> result = restTemplate.postForEntity(endpoint, null, String.class);
        assertTrue(result.getBody() == null || result.getBody().length() < 1);      // empty response
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCodeValue());  // a cashier can only perform one checkout operation at any given point in time
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
