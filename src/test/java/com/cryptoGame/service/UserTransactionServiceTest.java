package com.cryptoGame.service;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.domain.User;
import com.cryptoGame.domain.UserTransaction;
import com.cryptoGame.domain.User;
import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.exceptions.NotEnoughFundsException;
import com.cryptoGame.exceptions.TransactionNotFoundException;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsClient;
import com.cryptoGame.mapper.CoinMapper;
import com.cryptoGame.repository.CoinRepository;
import com.cryptoGame.repository.UserRepository;
import com.cryptoGame.repository.UserTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserTransactionServiceTest {
    @InjectMocks
    UserTransactionService service;

    @Mock
    UserTransactionRepository userTransactionRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    NomicsClient client;

    @Mock
    CoinRepository coinRepository;

    @Mock
    CoinMapper coinMapper;

    UserTransaction userTransaction;
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
                .crypto(crypto)
                .password("pass")
                .money(new BigDecimal(10000))
                .id(1L)
                .build();

        this.userTransaction = UserTransaction.builder()
                .id(1L)
                .user(user)
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
        Map<String, BigDecimal> crypto = new HashMap<>();
        crypto.put("BTC", new BigDecimal(1));
        user.setCrypto(crypto);
        UserTransaction buyTransaction = UserTransaction.builder()
                .user(user)
                .cryptoSymbol(coin.getSymbol())
                .money(new BigDecimal(100))
                .build();

        when(coinMapper.mapToCoin(coinDto)).thenReturn(coin);
        when(client.getCoins(coin.getSymbol())).thenReturn(List.of(coinDto));
        when(coinRepository.save(coin)).thenReturn(coin);
        when(userRepository.save(user)).thenReturn(user);
        when(userTransactionRepository.save(any(UserTransaction.class))).thenReturn(userTransaction);

        //When
        service.buyCrypto(buyTransaction);
        BigDecimal moneyAfterTransaction = user.getMoney();
        BigDecimal btcAmountAfter = user.getCrypto().get("BTC");

        //Then
        assertEquals(new BigDecimal(9900), moneyAfterTransaction);
        assertEquals(new BigDecimal(2).setScale(8), btcAmountAfter);
    }

    @Test
    void testSellCrypto() throws NotEnoughFundsException{
        //Given
        UserTransaction sellTransaction = UserTransaction.builder()
                .user(user)
                .cryptoSymbol(coin.getSymbol())
                .cryptoAmount(new BigDecimal(0.5))
                .build();

        when(client.getCoins(coin.getSymbol())).thenReturn(List.of(coinDto));
        when(userRepository.save(user)).thenReturn(user);
        when(userTransactionRepository.save(any(UserTransaction.class))).thenReturn(userTransaction);

        //When
        service.sellCrypto(sellTransaction);
        BigDecimal moneyAfterTransaction = user.getMoney();
        BigDecimal btcAmountAfter = user.getCrypto().get("BTC");

        //Then
        assertEquals(new BigDecimal(10050).setScale(1), moneyAfterTransaction);
        assertEquals(new BigDecimal(0.5), btcAmountAfter);
    }

    @Test
    void testSaveTransaction(){
        //Given
        when(userTransactionRepository.save(userTransaction)).thenReturn(userTransaction);
        //When
        UserTransaction savedUser = service.saveTransaction(userTransaction);
        //Then
        assertEquals(user.getId(), savedUser.getId());
    }

    @Test
    void testFindTransaction() throws TransactionNotFoundException {
        //Given
        when(userTransactionRepository.findById(userTransaction.getId())).thenReturn(Optional.of(userTransaction));
        //When
        UserTransaction searchedTransaction = service.findTransaction(user.getId());
        //Then
        assertEquals(user.getId(), searchedTransaction.getId());
    }

    @Test
    void testGetAllTransactions(){
        //Given
        List<UserTransaction> allTransactions = List.of(userTransaction);
        when(userTransactionRepository.findAll()).thenReturn(allTransactions);
        //When
        List<UserTransaction> searchedTransactionsList = service.getAllTransactions();
        //Then
        assertEquals(allTransactions.get(0).getId(), searchedTransactionsList.get(0).getId());
    }
}
