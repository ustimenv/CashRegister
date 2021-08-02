package shop.cashregister.model.items;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
@Getter
@NoArgsConstructor
public class Item{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="full_name")
    private String fullName;

    @Column(name="code", unique = true)
    private String code;

    @Column(name="default_price", nullable = false)
    private double defaultPrice;

    // between 1 and 10 characters: capitals & digits
    private static final Pattern itemCodePattern = Pattern.compile("^[A-Z0-9]{1,10}$");

    // between 2 and 40 characters: letters, digits & space
    private static final Pattern fullNamePattern = Pattern.compile("^[^-\\s][A-Za-z0-9 ]{0,40}$");

    public static boolean isCodeValid(String itemCode){
        return itemCodePattern.matcher(itemCode).matches();
    }

    public static boolean isFullNameValid(String fullName){
        return fullNamePattern.matcher(fullName).matches();
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Item){
            return ((Item) other).code.equals(code);
        } else return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(code);
    }
}
