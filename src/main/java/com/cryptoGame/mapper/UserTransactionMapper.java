package com.cryptoGame.mapper;

import com.cryptoGame.domain.UserTransaction;
import com.cryptoGame.domain.dtos.UserTransactionDto;
import com.cryptoGame.exceptions.UserNotFoundException;
import com.cryptoGame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTransactionMapper {

    @Autowired
    private UserRepository userRepository;

    public UserTransaction mapToTransaction(UserTransactionDto dto) throws UserNotFoundException{
        UserTransaction transaction = new UserTransaction();
        transaction.setId(dto.getId());
        transaction.setUser(userRepository.findById(dto.getUser_id()).orElseThrow(UserNotFoundException::new));
        transaction.setTransactionDate(dto.getTransactionDate());
        transaction.setCryptoSymbol(dto.getCryptoSymbol());
        String cryptoAmount = dto.getCryptoAmount();
        if(cryptoAmount==null){
            transaction.setMoney(new BigDecimal(dto.getMoney()));
        } else {
            transaction.setCryptoAmount(new BigDecimal(cryptoAmount));
        }
        return transaction;

    }

    public UserTransactionDto mapToTransactionDto(UserTransaction transaction){
        System.out.println(transaction.getCryptoSymbol());
        return new UserTransactionDto(transaction.getId(),
                transaction.getUser().getId(),
                transaction.getTransactionDate(),
                transaction.getCryptoSymbol(),
                String.valueOf(transaction.getCryptoAmount()),
                String.valueOf(transaction.getMoney()),
                String.valueOf(transaction.getWorthNow()));
    }

    public List<UserTransactionDto> mapToTransactionDtoList(final List<UserTransaction> transactions){
        return transactions.stream().map(this::mapToTransactionDto).collect(Collectors.toList());
    }
}
