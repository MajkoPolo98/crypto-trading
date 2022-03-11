package com.cryptoGame.repository;

import com.cryptoGame.domain.Organisation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrganisationRepository extends CrudRepository<Organisation, Long> {

    Optional<Organisation> findByGroupName(String name);

    @Override
    Optional<Organisation>findById(Long id);

    @Override
    List<Organisation> findAll();
}
