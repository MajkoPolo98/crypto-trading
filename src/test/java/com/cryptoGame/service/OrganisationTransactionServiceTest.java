package com.cryptoGame.service;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.domain.Organisation;
import com.cryptoGame.domain.OrganisationTransaction;
import com.cryptoGame.domain.User;
import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.exceptions.NotEnoughFundsException;
import com.cryptoGame.exceptions.OrganisationNotFoundException;
import com.cryptoGame.exceptions.TransactionNotFoundException;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsClient;
import com.cryptoGame.mapper.CoinMapper;
import com.cryptoGame.repository.CoinRepository;
import com.cryptoGame.repository.OrganisationRepository;
import com.cryptoGame.repository.OrganisationTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrganisationTransactionServiceTest {
    @InjectMocks
    OrganisationTransactionService service;

    @Mock
    OrganisationTransactionRepository organisationTransactionRepository;

    @Mock
    OrganisationRepository organisationRepository;

    @Mock
    NomicsClient client;

    @Mock
    CoinRepository coinRepository;

    @Mock
    CoinMapper coinMapper;

    OrganisationTransaction organisationTransaction;
    Organisation organisation;
    User user;
    Coin coin;
    CoinDto coinDto;

    @BeforeEach
    void createData() {
        Map<String, BigDecimal> crypto = new HashMap<>();
        crypto.put("BTC", new BigDecimal(1));
        this.user = User.builder()
                .userName("name")
                .adminStatus(true)
                .email("email")
                .password("pass")
                .money(new BigDecimal(10000))
                .id(1L)
                .build();

        this.organisation = Organisation.builder()
                .money(new BigDecimal(5000))
                .crypto(crypto)
                .id(1L)
                .groupName("name")
                .users(List.of(user))
                .build();

        this.organisationTransaction = OrganisationTransaction.builder()
                .id(1L)
                .user(user)
                .organisation(organisation)
                .transactionDate(LocalDate.of(1990,10,10))
                .cryptoSymbol("BTC")
                .cryptoAmount(new BigDecimal(1))
                .money(new BigDecimal(100))
                .worthNow(new BigDecimal(100))
                .build();

        this.coin = new Coin("BTC", "Bitcoin", new BigDecimal(100), "URL");
        this.coinDto = new CoinDto("BTC", "Bitcoin", new BigDecimal(100), "URL");
    }

    @Test
    void testBuyCrypto() throws NotEnoughFundsException {
        //Given
        OrganisationTransaction buyTransaction = OrganisationTransaction.builder()
                .user(user)
                .organisation(organisation)
                .cryptoSymbol(coin.getSymbol())
                .money(new BigDecimal(100))
                .build();

        when(coinMapper.mapToCoin(coinDto)).thenReturn(coin);
        when(client.getCoins(coin.getSymbol())).thenReturn(List.of(coinDto));
        when(coinRepository.save(coin)).thenReturn(coin);
        when(organisationRepository.save(organisation)).thenReturn(organisation);
        when(organisationTransactionRepository.save(any(OrganisationTransaction.class))).thenReturn(organisationTransaction);

        //When
        service.buyCrypto(buyTransaction);
        BigDecimal moneyAfterTransaction = organisation.getMoney();
        BigDecimal btcAmountAfter = organisation.getCrypto().get("BTC");

        //Then
        assertEquals(new BigDecimal(4900), moneyAfterTransaction);
        assertEquals(new BigDecimal(2).setScale(8), btcAmountAfter);
    }

    @Test
    void testSellCrypto() throws NotEnoughFundsException{
        //Given
        OrganisationTransaction sellTransaction = OrganisationTransaction.builder()
                .user(user)
                .organisation(organisation)
                .cryptoSymbol(coin.getSymbol())
                .cryptoAmount(new BigDecimal(0.5))
                .build();

        when(client.getCoins(coin.getSymbol())).thenReturn(List.of(coinDto));
        when(organisationRepository.save(organisation)).thenReturn(organisation);
        when(organisationTransactionRepository.save(any(OrganisationTransaction.class))).thenReturn(organisationTransaction);

        //When
        service.sellCrypto(sellTransaction);
        BigDecimal moneyAfterTransaction = organisation.getMoney();
        BigDecimal btcAmountAfter = organisation.getCrypto().get("BTC");

        //Then
        assertEquals(new BigDecimal(5050).setScale(1), moneyAfterTransaction);
        assertEquals(new BigDecimal(0.5), btcAmountAfter);
    }

    @Test
    void testSaveTransaction(){
        //Given
        when(organisationTransactionRepository.save(organisationTransaction)).thenReturn(organisationTransaction);
        //When
        OrganisationTransaction savedOrganisation = service.saveTransaction(organisationTransaction);
        //Then
        assertEquals(organisation.getId(), savedOrganisation.getId());
    }

    @Test
    void testFindTransaction() throws TransactionNotFoundException {
        //Given
        when(organisationTransactionRepository.findById(organisationTransaction.getId())).thenReturn(Optional.of(organisationTransaction));
        //When
        OrganisationTransaction searchedTransaction = service.findTransaction(organisation.getId());
        //Then
        assertEquals(organisation.getId(), searchedTransaction.getId());
    }

    @Test
    void testGetAllTransactions(){
        //Given
        List<OrganisationTransaction> allTransactions = List.of(organisationTransaction);
        when(organisationTransactionRepository.findAll()).thenReturn(allTransactions);
        //When
        List<OrganisationTransaction> searchedTransactionsList = service.getAllTransactions();
        //Then
        assertEquals(allTransactions.get(0).getId(), searchedTransactionsList.get(0).getId());
    }
}
