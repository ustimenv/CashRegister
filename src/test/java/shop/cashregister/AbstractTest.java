package shop.cashregister;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import shop.cashregister.control.CheckOutController;
import shop.cashregister.model.cashier.AuthorisationRequest;
import shop.cashregister.model.items.SellableItemService;
import shop.cashregister.model.offers.OfferService;
import shop.cashregister.model.transactions.CashRegisterTransactionService;
import shop.cashregister.model.transactions.ChangeItemQuantityRequest;
import shop.cashregister.model.transactions.HistoricalItemsSoldService;
import shop.cashregister.model.transactions.IntermediateTransactionFeedback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractTest{
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private CheckOutController checkOutController;

    @Autowired
    private SellableItemService itemService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private CashRegisterTransactionService transactionsService;

    @Autowired
    private HistoricalItemsSoldService historicalItemsSoldService;


    String validCashierUsername = "Caroline";
    String validCashierPassword = "pass";

    // URLS & endpoints
    String baseUrl = "http://localhost:" + 8080;

    String cashierUrl = baseUrl + "/cashier";
    String loginEndpoint = cashierUrl + "/login";

    String checkoutUrl = baseUrl + "/" + validCashierUsername + "/checkout";
    String beginTransactionEndpoint =           checkoutUrl +   "/begin";
    String endTransactionEndpoint =             checkoutUrl +   "/end";
    String addItemTransactionEndpoint =         checkoutUrl +   "/add_item";
    String removeItemTransactionEndpoint =      checkoutUrl +   "/remove_item";



    void authenticateValidUser(){
        AuthorisationRequest credentials = new AuthorisationRequest(validCashierUsername, validCashierPassword);
        HttpEntity<AuthorisationRequest> request = new HttpEntity<>(credentials);
        ResponseEntity<List> result = restTemplate.postForEntity(loginEndpoint, request, List.class);
    }

    ResponseEntity<String> beginTransaction(){
        return restTemplate.postForEntity(beginTransactionEndpoint, null, String.class);
    }

    ResponseEntity<IntermediateTransactionFeedback> addItem(ChangeItemQuantityRequest itemDesc){
        return restTemplate.postForEntity(addItemTransactionEndpoint, itemDesc, IntermediateTransactionFeedback.class);
    }

    ResponseEntity<IntermediateTransactionFeedback> removeItem(ChangeItemQuantityRequest itemDesc){
        return restTemplate.postForEntity(removeItemTransactionEndpoint, itemDesc, IntermediateTransactionFeedback.class);
    }

    ResponseEntity<IntermediateTransactionFeedback> endTransaction(){
        return restTemplate.postForEntity(endTransactionEndpoint, null, IntermediateTransactionFeedback.class);
    }

    @Test
    void testSubtotalsSeries(List<ChangeItemQuantityRequest> items, List<IntermediateTransactionFeedback> expectedFeedback){
        for(int i=0; i<items.size(); i++){
            ResponseEntity<IntermediateTransactionFeedback> result = addItem(items.get(i));
            assertEquals(result.getBody().getSubtotal(), expectedFeedback.get(i).getSubtotal());
            assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        }
    }

}
