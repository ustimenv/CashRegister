package shop.cashregister.authentication;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import shop.cashregister.AbstractTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthenticationTest extends AbstractTest{

    @Test
    public void testLoginCorrectUsernameCorrectPassword(){
        ResponseEntity<String> result = authenticateUser(validCashierUsername, validCashierPassword);
        String token = result.getBody();
        Assertions.assertTrue(token != null && token.length() > 0);
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
    }

    @Test
    public void testLoginCorrectUsernameWrongPassword(){
        ResponseEntity<String> result = authenticateUser(validCashierUsername, "some very clearly wrong password");
        String token = result.getBody();
        Assertions.assertNull(token);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getStatusCodeValue());
    }

    @Test
    public void testLoginWrongUsernameWrongPassword(){
        ResponseEntity<String> result = authenticateUser("Michael", "abc");
        String token = result.getBody();
        Assertions.assertNull(token);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getStatusCodeValue());
    }


    @Test
    public void testTransactionBeginEnd(){
        String token = authenticateUser(validCashierUsername, validCashierPassword).getBody();
        ResponseEntity<String> result = beginTransaction(token);
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());           // with an OK status code

        result = endTransaction(token);
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());           // with an OK status code
    }
}
