package com.cryptoGame.domain;

import com.cryptoGame.exceptions.NotEnoughFundsException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "admin_status", nullable = false)
    private boolean adminStatus;

    @Column(name = "money")
    private BigDecimal money;

    @Column(name = "user_value")
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @ElementCollection
    @CollectionTable(
            name = "crypto_wallet",
            joinColumns = {@JoinColumn(name = "user_id")}
    )
    @MapKeyColumn(name = "cryptocurrency")
    @Column(name = "amount", precision = 8, scale = 8)
    private Map<String, BigDecimal> crypto;

    public void addCrypto(String symbol, BigDecimal value){
        crypto.put(symbol, crypto.get(symbol).add(value));
    }

    public void sendMoneyToOrganisation(BigDecimal amount) throws NotEnoughFundsException{
        if(amount.compareTo(money) == 1){
            throw new NotEnoughFundsException();
        }
        money = money.subtract(amount);
        organisation.setMoney(organisation.getMoney().add(amount));
    }


}
