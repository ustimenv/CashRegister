package shop.cashregister.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorisationRequest{
    private String username;
    private String password;
}
