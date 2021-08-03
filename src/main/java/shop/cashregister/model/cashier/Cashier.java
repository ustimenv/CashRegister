package shop.cashregister.model.cashier;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import shop.cashregister.model.transactions.CashRegisterTransaction;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@NoArgsConstructor
public class Cashier implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="username", unique=true, nullable=false)
    private String username;

    @Column(name="first_name", nullable=false)
    private String firstName;

    @Column(name="last_name", nullable=false)
    private String lastName;

    @Column(name="password", nullable=false)
    private String password;

    @OneToMany(mappedBy="transactionExecutor", fetch = FetchType.LAZY)
    private List<CashRegisterTransaction> transactions;

    // username is an alphanumeric (no whitespaces) string between 1 and 10 characters
    private static final Pattern usernamePattern = Pattern.compile("^[A-Za-z0-9]{1,10}$");

    public boolean isEqualToPassword(String password){
        return this.password.equals(password);
    }

    public static boolean isUsernameValid(String username){
        return usernamePattern.matcher(username).matches();
    }

    public String getUsername(){
        return username;
    }

    // UserDetails methods

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("Cashier"));
    }

    public String getPassword(){
        return password;
    }
}
