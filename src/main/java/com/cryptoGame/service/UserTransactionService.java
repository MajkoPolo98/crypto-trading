package com.cryptoGame.service;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.domain.User;
import com.cryptoGame.domain.UserTransaction;
import com.cryptoGame.exceptions.NotEnoughFundsException;
import com.cryptoGame.exceptions.TransactionNotFoundException;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsClient;
import com.cryptoGame.mapper.CoinMapper;
import com.cryptoGame.repository.CoinRepository;
import com.cryptoGame.repository.UserRepository;
import com.cryptoGame.repository.UserTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTransactionService {

    private final UserTransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CoinRepository coinRepository;
    private final NomicsClient client;
    private final CoinMapper mapper;


    public UserTransaction buyCrypto (UserTransaction transaction) throws
            NotEnoughFundsException {
        String symbol = transaction.getCryptoSymbol();
        BigDecimal amountPLN = transaction.getMoney();
        User user = transaction.getUser();

        BigDecimal cryptoOwned = user.getCrypto().get(symbol);
        if(amountPLN.compareTo(user.getMoney())==1){ throw new NotEnoughFundsException();}
        Coin coin = mapper.mapToCoin(client.getCoins(symbol).get(0));
        coinRepository.save(coin);
        BigDecimal price = coin.getPrice();
        BigDecimal cryptoToAdd = amountPLN.divide(price, 8, RoundingMode.HALF_EVEN);
        if(cryptoOwned != null){
            user.addCrypto(symbol, cryptoToAdd);
        } else {
            user.getCrypto().put(symbol, cryptoToAdd);
        }
        user.setMoney(user.getMoney().subtract(amountPLN));

        UserTransaction transactionToSave = UserTransaction.builder()
                .user(transaction.getUser())
                .transactionDate(LocalDate.now())
                .cryptoSymbol(symbol)
                .cryptoAmount(cryptoToAdd)
                .money(amountPLN.negate()).build();

        userRepository.save(user);
        transactionRepository.save(transactionToSave);


        return transactionToSave;

    }

    public UserTransaction sellCrypto(UserTransaction transaction) throws
            NotEnoughFundsException {

        User user = transaction.getUser();
        String symbol = transaction.getCryptoSymbol();
        BigDecimal amountCrypto = transaction.getCryptoAmount();

        BigDecimal cryptoOwned = user.getCrypto().get(symbol);
        if(amountCrypto.compareTo(cryptoOwned)==1){ throw new NotEnoughFundsException();}

        BigDecimal price = client.getCoins(symbol).get(0).getPrice();
        BigDecimal moneyToAdd = amountCrypto.multiply(price);
        user.setMoney(user.getMoney().add(moneyToAdd));
        user.addCrypto(symbol, amountCrypto.negate());

        UserTransaction transactionToSave = UserTransaction.builder()
                .user(user)
                .transactionDate(LocalDate.now())
                .cryptoSymbol(symbol)
                .cryptoAmount(amountCrypto.negate())
                .money(moneyToAdd).build();

        transactionRepository.save(transactionToSave);
        userRepository.save(user);

        return transactionToSave;
    }

    public UserTransaction saveTransaction(UserTransaction transaction){
        return transactionRepository.save(transaction);
    }

    public UserTransaction findTransaction(Long id) throws TransactionNotFoundException {
        return transactionRepository.findById(id).orElseThrow(TransactionNotFoundException::new);
    }

    public void removeTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<UserTransaction> getAllTransactions(){
        return transactionRepository.findAll();
    }
}
