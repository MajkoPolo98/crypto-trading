package com.cryptoGame.service;

import com.cryptoGame.domain.Organisation;
import com.cryptoGame.domain.OrganisationTransaction;
import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.exceptions.NotEnoughFundsException;
import com.cryptoGame.exceptions.TransactionNotFoundException;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsClient;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsFacade;
import com.cryptoGame.repository.OrganisationRepository;
import com.cryptoGame.repository.OrganisationTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganisationTransactionService {

    private final OrganisationTransactionRepository transactionRepository;
    private final OrganisationRepository organisationRepository;
    private final NomicsClient client;

    public OrganisationTransaction buyCrypto (OrganisationTransaction transaction) throws
            NotEnoughFundsException {
        String symbol = transaction.getCryptoSymbol();
        BigDecimal amountPLN = transaction.getMoney();
        Organisation organisation = transaction.getOrganisation();

        BigDecimal cryptoOwned = organisation.getCrypto().get(symbol);
        if(amountPLN.compareTo(organisation.getMoney())==1){ throw new NotEnoughFundsException();}
        CoinDto coinDto = client.getCoins(symbol).get(0);
        BigDecimal price = coinDto.getPrice();
        BigDecimal cryptoToAdd = amountPLN.divide(price, 8, RoundingMode.HALF_EVEN);
        if(cryptoOwned != null){
            organisation.addCrypto(symbol, cryptoToAdd);
        } else {
            organisation.getCrypto().put(symbol, cryptoToAdd);
        }
        organisation.setMoney(organisation.getMoney().subtract(amountPLN));

        OrganisationTransaction transactionToSave = OrganisationTransaction.builder()
                .user(transaction.getUser())
                .organisation(transaction.getUser().getOrganisation()) //Added
                .transactionDate(LocalDate.now())
                .cryptoSymbol(symbol)
                .cryptoAmount(cryptoToAdd)
                .money(amountPLN.negate()).build();

        organisationRepository.save(organisation);
        transactionRepository.save(transactionToSave);

        return transactionToSave;

    }

    public OrganisationTransaction sellCrypto(OrganisationTransaction transaction) throws
            NotEnoughFundsException {

        Organisation organisation = transaction.getOrganisation();
        String symbol = transaction.getCryptoSymbol();
        BigDecimal amountCrypto = transaction.getCryptoAmount();

        BigDecimal cryptoOwned = organisation.getCrypto().get(symbol);
        if(amountCrypto.compareTo(cryptoOwned)==1){ throw new NotEnoughFundsException();}

        BigDecimal price = client.getCoins(symbol).get(0).getPrice();
        BigDecimal moneyToAdd = amountCrypto.multiply(price);
        organisation.setMoney(organisation.getMoney().add(moneyToAdd));
        organisation.addCrypto(symbol, amountCrypto.negate());

        OrganisationTransaction transactionToSave = OrganisationTransaction.builder()
                .user(transaction.getUser())
                .organisation(transaction.getUser().getOrganisation())
                .transactionDate(LocalDate.now())
                .cryptoSymbol(symbol)
                .cryptoAmount(amountCrypto.negate())
                .money(moneyToAdd).build();

        transactionRepository.save(transactionToSave);
        organisationRepository.save(organisation);

        return transactionToSave;
    }

    public OrganisationTransaction saveTransaction(OrganisationTransaction transaction){
        return transactionRepository.save(transaction);
    }

    public OrganisationTransaction findTransaction(Long id) throws TransactionNotFoundException {
        return transactionRepository.findById(id).orElseThrow(TransactionNotFoundException::new);
    }

    public void removeTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<OrganisationTransaction> getAllTransactions(){
        return transactionRepository.findAll();
    }
}
