package com.cryptoGame.repository;

import com.cryptoGame.domain.Organisation;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrganisationRepository extends CrudRepository<Organisation, Long> {
    Optional<Organisation> findByGroupName(String name);
}
