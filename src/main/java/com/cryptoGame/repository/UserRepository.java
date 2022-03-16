package com.cryptoGame.repository;

import com.cryptoGame.domain.User;
import com.cryptoGame.exceptions.UserNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    Optional<User> findById(Long userId);

    Optional<User> findByUserName(String name);

    @Override
    List<User> findAll();
}
