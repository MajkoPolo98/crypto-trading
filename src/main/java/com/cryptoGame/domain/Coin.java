package com.cryptoGame.domain;

import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Coin {

    @Id
    @Column(name="symbol", unique = true)
    private String symbol;

    @Column(name="name")
    private String name;

    @Column(name="price")
    private BigDecimal price;

    @Transient
    private String logo_url;

}
