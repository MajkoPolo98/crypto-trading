package com.cryptoGame.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@Table(name = "organisations")
@NoArgsConstructor
@AllArgsConstructor
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="organisation_id", unique = true)
    private Long id;

    @Column(name = "organisation_name", nullable = false, unique = true)
    private String groupName;

    @Column(name = "organisation_funds")
    private BigDecimal money;

    @ElementCollection
    @CollectionTable(
            name = "organisation_wallet",
            joinColumns = {@JoinColumn(name = "organisation_id")}
    )
    @MapKeyColumn(name = "cryptocurrency")
    @Column(name = "amount")
    private Map<String, BigDecimal> crypto;

    @OneToMany(
            targetEntity = User.class,
            mappedBy = "organisation",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<User> users;

    public void addCrypto(String symbol, BigDecimal value){
        crypto.put(symbol, crypto.get(symbol).add(value));
    }

    public Organisation(Long id, String groupName, BigDecimal money, Map<String, BigDecimal> crypto) {
        this.id = id;
        this.groupName = groupName;
        this.money = money;
        this.crypto = crypto;
    }

    public void addUserToOrganisation(User user){
        users.add(user);
    }
}
