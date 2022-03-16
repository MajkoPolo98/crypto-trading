package com.cryptoGame.repository;

import com.cryptoGame.domain.OrganisationTransaction;
import com.cryptoGame.domain.UserTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface OrganisationTransactionRepository extends CrudRepository<OrganisationTransaction, Long> {

    @Override
    Optional<OrganisationTransaction> findById(Long id);

    @Override
    List<OrganisationTransaction> findAll();

    List<OrganisationTransaction> findAllByCryptoSymbol(String symbol);
}
