package com.cryptoGame.service;

import com.cryptoGame.domain.User;
import com.cryptoGame.exceptions.CoinNotFoundException;
import com.cryptoGame.exceptions.NotEnoughFundsException;
import com.cryptoGame.exceptions.UserNotFoundException;
import com.cryptoGame.externalApis.cryptoStock.CryptoStockClient;
import com.cryptoGame.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    CryptoStockClient cryptoStockClient;

    @Autowired
    private UserRepository repository;

    public void buyCrypto (Long userId, String symbol, BigDecimal amountPLN) throws
            UserNotFoundException, CoinNotFoundException, NotEnoughFundsException {
        User user = repository.findById(userId).orElseThrow(UserNotFoundException::new);
        BigDecimal cryptoOwned = user.getCrypto().get(symbol);
        if(amountPLN.compareTo(user.getMoney())==1){ throw new NotEnoughFundsException();}
        try {
            BigDecimal price = cryptoStockClient.getCoins(symbol).get(0).getPrice();
            BigDecimal cryptoToAdd = amountPLN.divide(price, 6);
            if(cryptoOwned != null){
                cryptoOwned.add(cryptoToAdd);
            } else {
                user.getCrypto().put(symbol, cryptoToAdd);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new CoinNotFoundException();
        }
        user.getMoney().subtract(amountPLN);
    }

    public void sellCrypto(Long userId, String symbol, BigDecimal amountCrypto) throws
            UserNotFoundException, CoinNotFoundException, NotEnoughFundsException {
        User user = findUser(userId);
        BigDecimal cryptoOwned = user.getCrypto().get(symbol);
        if(amountCrypto.compareTo(cryptoOwned)==1){ throw new NotEnoughFundsException();}
        try {
            BigDecimal price = cryptoStockClient.getCoins(symbol).get(0).getPrice();
            BigDecimal moneyToAdd = amountCrypto.multiply(price);
            user.getMoney().add(moneyToAdd);
            cryptoOwned.subtract(amountCrypto);

        } catch (IndexOutOfBoundsException e) {
            throw new CoinNotFoundException();
        }
        saveUser(user);
    }

    public User saveUser(User user){
        return repository.save(user);
    }

    public User findUser(Long id) throws UserNotFoundException{
        return repository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public void removeUser(Long id) {
        repository.deleteById(id);
    }

    public List<User> getAllUsers(){
        return repository.findAll();
    }

}
