package com.cryptoGame.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue
    @Column(name = "transaction_id", nullable = false)
    private Long id;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate = LocalDate.now();

    @Column(name = "crypto_name", nullable = false)
    private String cryptoName;

    @Column(name = "crypto_amount", nullable = false)
    private BigDecimal cryptoAmount;

    @Column(name = "bought_for", nullable = false)
    private BigDecimal boughtFor;

    @Column(name = "worth_now")
    private BigDecimal worthNow;
}
