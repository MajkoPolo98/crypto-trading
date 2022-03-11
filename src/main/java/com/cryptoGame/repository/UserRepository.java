package com.cryptoGame.repository;

import com.cryptoGame.domain.User;
import com.cryptoGame.exceptions.UserNotFoundException;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    Optional<User> findById(Long userId);

    @Override
    List<User> findAll();
}
