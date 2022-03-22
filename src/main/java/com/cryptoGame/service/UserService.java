package com.cryptoGame.service;

import com.cryptoGame.domain.User;
import com.cryptoGame.exceptions.NotEnoughFundsException;
import com.cryptoGame.exceptions.UserNotFoundException;
import com.cryptoGame.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository repository;

    public User saveUser(User user){
        return repository.save(user);
    }

    public User findUser(Long id) throws UserNotFoundException{
        return repository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User findUserByName(String name) throws UserNotFoundException{
        return repository.findByUserName(name).orElseThrow(UserNotFoundException::new);
    }

    public void removeUser(Long id) {
        repository.deleteById(id);
    }

    public List<User> getAllUsers(){
        return repository.findAll();
    }

    public void sendMoneyToOrganisation(Long userId, BigDecimal amount) throws UserNotFoundException, NotEnoughFundsException{
        findUser(userId).sendMoneyToOrganisation(amount);
    }

}
