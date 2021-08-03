package shop.cashregister;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import shop.cashregister.control.CashierController;
import shop.cashregister.model.cashier.AuthorisationRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CashierControllerTest{
    @Autowired
    private CashierController cashierController;

    @Autowired
    private TestRestTemplate restTemplate;

    String url = "http://localhost:" + 8080 + "/cashier/login";

    @Test
    public void testLoginCorrectUsernameCorrectPassword(){
        AuthorisationRequest credentials = new AuthorisationRequest("Caroline", "pass");
        HttpEntity<AuthorisationRequest> request = new HttpEntity<>(credentials);

        ResponseEntity<List> result = restTemplate.postForEntity(url, request, List.class);
        List<String> offers = result.getBody();
        Assertions.assertTrue(offers.size() > 0);
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
    }
    
    @Test
    public void testLoginCorrectUsernameWrongPassword(){
        AuthorisationRequest credentials = new AuthorisationRequest("Caroline", "wrong password");
        HttpEntity<AuthorisationRequest> request = new HttpEntity<>(credentials);

        ResponseEntity<List> result = restTemplate.postForEntity(url, request, List.class);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getStatusCodeValue());
    }

    @Test
    public void testLoginWrongUsernameWrongPassword(){
        AuthorisationRequest credentials = new AuthorisationRequest("Michael", "abc");
        HttpEntity<AuthorisationRequest> request = new HttpEntity<>(credentials);
        ResponseEntity<List> result = restTemplate.postForEntity(url, request, List.class);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getStatusCodeValue());
    }
}
