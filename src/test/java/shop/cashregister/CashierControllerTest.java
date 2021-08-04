package shop.cashregister;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import shop.cashregister.security.AuthorisationRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CashierControllerTest extends AbstractTest{

    @Test
    public void testLoginCorrectUsernameCorrectPassword(){
        AuthorisationRequest credentials = new AuthorisationRequest(validCashierUsername, validCashierPassword);
        HttpEntity<AuthorisationRequest> request = new HttpEntity<>(credentials);
        ResponseEntity<List> result = restTemplate.postForEntity(loginEndpoint, request, List.class);
        List<String> singleItemOffers = result.getBody();
        Assertions.assertTrue(singleItemOffers.size() > 0);
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
    }

    @Test
    public void testLoginCorrectUsernameWrongPassword(){
        AuthorisationRequest credentials = new AuthorisationRequest(validCashierUsername, "some very clearly wrong password");
        HttpEntity<AuthorisationRequest> request = new HttpEntity<>(credentials);
        ResponseEntity<List> result = restTemplate.postForEntity(loginEndpoint, request, List.class);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getStatusCodeValue());
    }

    @Test
    public void testLoginWrongUsernameWrongPassword(){
        AuthorisationRequest credentials = new AuthorisationRequest("Michael", "abc");
        HttpEntity<AuthorisationRequest> request = new HttpEntity<>(credentials);
        ResponseEntity<List> result = restTemplate.postForEntity(loginEndpoint, request, List.class);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getStatusCodeValue());
    }
}
