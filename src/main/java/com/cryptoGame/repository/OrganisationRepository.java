package com.cryptoGame.repository;

import com.cryptoGame.domain.Organisation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface OrganisationRepository extends CrudRepository<Organisation, Long> {

    Optional<Organisation> findByGroupName(String name);

    @Override
    Optional<Organisation>findById(Long id);

    @Override
    List<Organisation> findAll();
}
