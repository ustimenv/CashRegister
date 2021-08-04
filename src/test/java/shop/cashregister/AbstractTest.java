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

    // URLS & endpoints
    String baseUrl = "http://localhost:" + 8080;

    String cashierUrl = baseUrl + "/cashier";
    String loginEndpoint = cashierUrl + "/login";

    String checkoutUrl = baseUrl + "/checkout/";


    protected ResponseEntity<String> authenticateUser(String username, String password){
        AuthorisationRequest credentials = new AuthorisationRequest(username, password);
        HttpEntity<AuthorisationRequest> request = new HttpEntity<>(credentials);
        return restTemplate.postForEntity(loginEndpoint, request, String.class);
    }

    protected ResponseEntity<String> beginTransaction(String username, String token){
        String beginTransactionEndpoint = checkoutUrl + username+  "/begin";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);
        return restTemplate.postForEntity(beginTransactionEndpoint, request, String.class);
    }

    protected ResponseEntity<TransactionFeedback> addItem(String username, String token, ChangeItemQuantityRequest itemDesc){
        String addItemTransactionEndpoint = checkoutUrl + username+  "/add_item";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Bearer " + token);
        HttpEntity<ChangeItemQuantityRequest> request = new HttpEntity<>(itemDesc, headers);
        return restTemplate.postForEntity(addItemTransactionEndpoint, request, TransactionFeedback.class);
    }

    protected ResponseEntity<TransactionFeedback> removeItem(String username, String token, ChangeItemQuantityRequest itemDesc){
        String removeItemTransactionEndpoint = checkoutUrl + username+ "/remove_item";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Bearer " + token);
        HttpEntity<ChangeItemQuantityRequest> request = new HttpEntity<>(itemDesc, headers);
        return restTemplate.postForEntity(removeItemTransactionEndpoint, request, TransactionFeedback.class);
    }

    protected ResponseEntity<String> endTransaction(String username, String token){
        String endTransactionEndpoint = checkoutUrl + username+ "/end";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Bearer " + token);
        return restTemplate.postForEntity(endTransactionEndpoint, new HttpEntity<>(headers), String.class);
    }

    protected void testTransactionFeedbackEvolution(List<ChangeItemQuantityRequest> items, List<TransactionFeedback> expectedFeedback,
                                                    String username, String password){
        String token = authenticateUser(username, password).getBody();
        beginTransaction(username, token);
        for(int i=0; i<items.size(); i++){
            ResponseEntity<TransactionFeedback> result;
            if(items.get(i).getChangeBy() > 0){
                result = addItem(username, token, items.get(i));
            } else{
                result = removeItem(username, token, items.get(i));
            }
            assertEquals(expectedFeedback.get(i).getAmountToPay(), result.getBody().getAmountToPay());
            assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        }
        endTransaction(username, token);
    }

}
