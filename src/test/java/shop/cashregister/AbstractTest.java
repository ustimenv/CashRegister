package shop.cashregister;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import shop.cashregister.control.RootController;
import shop.cashregister.security.AuthorisationRequest;
import shop.cashregister.model.items.SellableItemService;
import shop.cashregister.model.offers.OfferService;
import shop.cashregister.model.transactions.CashRegisterTransactionService;
import shop.cashregister.model.transactions.ChangeItemQuantityRequest;
import shop.cashregister.model.transactions.TransactionItemsService;
import shop.cashregister.model.transactions.TransactionFeedback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractTest{
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private RootController rootController;

    @Autowired
    private SellableItemService itemService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private CashRegisterTransactionService transactionsService;

    @Autowired
    private TransactionItemsService transactionItemsService;


    String validCashierUsername = "Caroline";
    String validCashierPassword = "pass";

    // URLS & endpoints
    String baseUrl = "http://localhost:" + 8080;

    String cashierUrl = baseUrl + "/cashier";
    String loginEndpoint = cashierUrl + "/login";

    String checkoutUrl = baseUrl + "/checkout/" + validCashierUsername;
    String beginTransactionEndpoint =           checkoutUrl +   "/begin";
    String endTransactionEndpoint =             checkoutUrl +   "/end";
    String addItemTransactionEndpoint =         checkoutUrl +   "/add_item";
    String removeItemTransactionEndpoint =      checkoutUrl +   "/remove_item";



    ResponseEntity<String> authenticateUser(String username, String password){
        AuthorisationRequest credentials = new AuthorisationRequest(username, password);
        HttpEntity<AuthorisationRequest> request = new HttpEntity<>(credentials);
        return restTemplate.postForEntity(loginEndpoint, request, String.class);
    }


    ResponseEntity<String> beginTransaction(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Bearer "+token);
//        headers.set("Authorization", "Bearer "+token);
        return restTemplate.postForEntity(beginTransactionEndpoint, headers, String.class);
    }

    ResponseEntity<TransactionFeedback> addItem(ChangeItemQuantityRequest itemDesc){
        return restTemplate.postForEntity(addItemTransactionEndpoint, itemDesc, TransactionFeedback.class);
    }

    ResponseEntity<TransactionFeedback> removeItem(ChangeItemQuantityRequest itemDesc){
        return restTemplate.postForEntity(removeItemTransactionEndpoint, itemDesc, TransactionFeedback.class);
    }

    ResponseEntity<TransactionFeedback> endTransaction(){
        return restTemplate.postForEntity(endTransactionEndpoint, null, TransactionFeedback.class);
    }

    void testSubtotalsSeries(List<ChangeItemQuantityRequest> items, List<TransactionFeedback> expectedFeedback){
        for(int i=0; i<items.size(); i++){
            ResponseEntity<TransactionFeedback> result = addItem(items.get(i));
            assertEquals(result.getBody().getAmountToPay(), expectedFeedback.get(i).getAmountToPay());
            assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        }
    }

}
