package com.cryptoGame.service;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.exceptions.CoinNotFoundException;
import com.cryptoGame.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CoinService {

    @Autowired
    private CoinRepository repository;

    public Coin saveTransaction(Coin transaction){
        return repository.save(transaction);
    }

    public Coin findTransaction(String symbol) throws CoinNotFoundException {
        return repository.findBySymbol(symbol).orElseThrow(CoinNotFoundException::new);
    }

    public void removeTransaction(String symbol) {
        repository.deleteBySymbol(symbol);
    }

    public List<Coin> getAllTransactions(){
        return repository.findAll();
    }
}
