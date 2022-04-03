package com.cryptoGame.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "organisation_transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id", nullable = false, unique = true)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "organisation_name", referencedColumnName = "organisation_name")
    private Organisation organisation;

    @Column(name = "transaction_date")
    private LocalDate transactionDate = LocalDate.now();

    @Column(name = "crypto_symbol", nullable = false)
    private String cryptoSymbol;

    @Column(name = "crypto_amount", scale = 8)
    private BigDecimal cryptoAmount;

    @Column(name = "money")
    private BigDecimal money;

    @Column(name = "worth_now")
    private BigDecimal worthNow;

}
