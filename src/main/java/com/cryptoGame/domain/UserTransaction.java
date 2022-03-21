package com.cryptoGame.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "user_transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "transaction_date")
    private LocalDate transactionDate = LocalDate.now();

    @Column(name = "crypto_symbol", nullable = false)
    private String cryptoSymbol;

    @Column(name = "crypto_amount", precision = 8, scale = 8)
    private BigDecimal cryptoAmount;

    @Column(name = "money")
    private BigDecimal money;

    @Column(name = "worth_now")
    private BigDecimal worthNow;

}
