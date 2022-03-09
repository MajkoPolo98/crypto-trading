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
    @GeneratedValue
    @Column(name="organisation_id", unique = true)
    private Long id;

    @Column(name = "organisation_name", nullable = false)
    private String groupName;

    @Column(name = "organisation_funds")
    private BigDecimal groupFunds;

    @Column(name = "currency")
    private String currency;

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
}
