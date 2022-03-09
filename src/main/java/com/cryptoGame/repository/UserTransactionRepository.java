package com.cryptoGame.repository;


import com.cryptoGame.domain.UserTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface UserTransactionRepository extends CrudRepository<UserTransaction, Long> {

    @Override
    Optional<UserTransaction> findById(Long id);

    @Override
    List<UserTransaction> findAll();

    List<UserTransaction> findAllByCryptoSymbol(String symbol);
}
