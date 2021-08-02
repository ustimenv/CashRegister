package shop.cashregister.model.items;

import lombok.Getter;

import javax.persistence.*;

@Entity
public class Offer{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private @Getter String description;

    @Transient
    private Class<?> interceptor;
}
