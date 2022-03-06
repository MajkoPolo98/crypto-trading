package com.cryptoGame.repository;

import com.cryptoGame.domain.Coin;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CoinRepository extends CrudRepository<Coin, String> {

    Optional<Coin> findBySymbol(String symbol);

    void deleteBySymbol(String symbol);

    @Override
    List<Coin> findAll();
}
