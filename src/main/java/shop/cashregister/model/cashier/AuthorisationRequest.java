package shop.cashregister.model.cashier;

import lombok.Data;

@Data
public class AuthorisationRequest{
    private String username;
    private String password;
}
