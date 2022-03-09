package com.cryptoGame.service;

import com.cryptoGame.domain.User;
import com.cryptoGame.exceptions.UserNotFoundException;
import com.cryptoGame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OrganisationService {

    @Autowired
    private UserRepository repository;

    public User saveUser(User user){
        return repository.save(user);
    }

    public User findUser(Long id) throws UserNotFoundException {
        return repository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public void removeUser(Long id) {
        repository.deleteById(id);
    }

    public List<User> getAllUsers(){
        return repository.findAll();
    }
}
