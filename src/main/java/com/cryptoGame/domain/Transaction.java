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

    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "transaction_date")
    private LocalDate transactionDate = LocalDate.now();

    @Column(name = "crypto_symbol", nullable = false)
    private String cryptoSymbol;

    @Column(name = "crypto_amount")
    private BigDecimal cryptoAmount;

    @Column(name = "money")
    private BigDecimal money;


    @Column(name = "worth_now")
    private BigDecimal worthNow;

    public Transaction(User user, LocalDate transactionDate, String cryptoSymbol, BigDecimal cryptoAmount, BigDecimal money) {
        this.user = user;
        this.transactionDate = transactionDate;
        this.cryptoSymbol = cryptoSymbol;
        this.cryptoAmount = cryptoAmount;
        this.money = money;
    }
}
