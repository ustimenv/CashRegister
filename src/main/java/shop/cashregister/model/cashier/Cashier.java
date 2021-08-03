package shop.cashregister.model.cashier;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.cashregister.model.transactions.CashRegisterTransaction;

import javax.persistence.*;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@NoArgsConstructor
public class Cashier{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="username", unique=true)
    private @Getter String username;

    @Column(name="first_name", nullable=false)
    private @Getter String firstName;

    @Column(name="last_name", nullable=false)
    private @Getter String lastName;

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
}
