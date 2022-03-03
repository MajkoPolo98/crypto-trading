package com.cryptoGame.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @Column(name="user_id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "admin_status", nullable = false)
    private boolean adminStatus;

    @Column(name = "money")
    private BigDecimal money;

    @ElementCollection
    @CollectionTable(
            name = "crypto_wallet",
            joinColumns = {@JoinColumn(name = "user_id")}
    )
    @MapKeyColumn(name = "cryptocurrency")
    @Column(name = "amount")
    private Map<String, BigDecimal> crypto;
}
