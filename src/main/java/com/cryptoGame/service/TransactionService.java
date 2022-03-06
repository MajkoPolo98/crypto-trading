package com.cryptoGame.service;

import com.cryptoGame.domain.Transaction;
import com.cryptoGame.domain.User;
import com.cryptoGame.exceptions.NotEnoughFundsException;
import com.cryptoGame.exceptions.TransactionNotFoundException;
import com.cryptoGame.externalApis.cryptoStock.CryptoStockClient;
import com.cryptoGame.repository.TransactionRepository;
import com.cryptoGame.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private final CryptoStockClient cryptoStockClient;

    public Transaction buyCrypto (Transaction transaction) throws
            NotEnoughFundsException {
        String symbol = transaction.getCryptoSymbol();
        BigDecimal amountPLN = transaction.getMoney();
        User user = transaction.getUser();

        BigDecimal cryptoOwned = user.getCrypto().get(symbol);
        if(amountPLN.compareTo(user.getMoney())==1){ throw new NotEnoughFundsException();}
        BigDecimal price = cryptoStockClient.getCoins(symbol).get(0).getPrice();
        BigDecimal cryptoToAdd = amountPLN.divide(price, 8, RoundingMode.HALF_EVEN);
        if(cryptoOwned != null){
            user.addCrypto(symbol, cryptoToAdd);
            System.out.println("TUTUTU");
        } else {
            user.getCrypto().put(symbol, cryptoToAdd);
        }
        user.setMoney(user.getMoney().subtract(amountPLN));

        Transaction transactionToSave = new Transaction(transaction.getUser(), LocalDate.now(), symbol, cryptoToAdd, amountPLN.negate());
        userRepository.save(user);
        transactionRepository.save(transactionToSave);

        return transactionToSave;

    }

    public Transaction sellCrypto(Transaction transaction) throws
            NotEnoughFundsException {

        User user = transaction.getUser();
        String symbol = transaction.getCryptoSymbol();
        BigDecimal amountCrypto = transaction.getCryptoAmount();

        BigDecimal cryptoOwned = user.getCrypto().get(symbol);
        if(amountCrypto.compareTo(cryptoOwned)==1){ throw new NotEnoughFundsException();}

        BigDecimal price = cryptoStockClient.getCoins(symbol).get(0).getPrice();
        BigDecimal moneyToAdd = amountCrypto.multiply(price);
        user.setMoney(user.getMoney().add(moneyToAdd));
        user.addCrypto(symbol, amountCrypto.negate());

        Transaction transactionToSave = new Transaction(user, LocalDate.now(), symbol, amountCrypto.negate(), moneyToAdd);
        transactionRepository.save(transactionToSave);
        userRepository.save(user);

        return transactionToSave;
    }

    public Transaction saveTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public Transaction findTransaction(Long id) throws TransactionNotFoundException {
        return transactionRepository.findById(id).orElseThrow(TransactionNotFoundException::new);
    }

    public void removeTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }
}
