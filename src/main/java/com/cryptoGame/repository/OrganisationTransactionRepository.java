package com.cryptoGame.repository;

import com.cryptoGame.domain.OrganisationTransaction;
import com.cryptoGame.domain.UserTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrganisationTransactionRepository extends CrudRepository<OrganisationTransaction, Long> {

    @Override
    Optional<OrganisationTransaction> findById(Long id);

    @Override
    List<OrganisationTransaction> findAll();

    List<OrganisationTransaction> findAllByCryptoSymbol(String symbol);
}
