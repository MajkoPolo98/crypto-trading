package com.cryptoGame.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Coin {

    private String symbol;

    private String name;

    private BigDecimal price;

    private transient String logoUrl;

}
