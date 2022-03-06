package com.cryptoGame.repository;


import com.cryptoGame.domain.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Override
    Optional<Transaction> findById(Long id);

    @Override
    List<Transaction> findAll();
}
