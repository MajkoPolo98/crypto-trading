package com.cryptoGame.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "coins")
public class Coin {

    @Id
    @Column
    private String symbol;

    @Column
    private String name;

    @Column
    private BigDecimal price;

    @Transient
    private transient String logoUrl;

}
