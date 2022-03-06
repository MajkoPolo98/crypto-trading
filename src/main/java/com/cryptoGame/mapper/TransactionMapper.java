package com.cryptoGame.mapper;

import com.cryptoGame.domain.Transaction;
import com.cryptoGame.domain.dtos.TransactionDto;
import com.cryptoGame.exceptions.UserNotFoundException;
import com.cryptoGame.externalApis.cryptoStock.CryptoStockClient;
import com.cryptoGame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionMapper {

    @Autowired
    private CryptoStockClient client;
    @Autowired
    private UserRepository userRepository;

    public Transaction mapToTransaction(TransactionDto dto) throws UserNotFoundException{
        Transaction transaction = new Transaction();
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

    public TransactionDto mapToTransactionDto(Transaction transaction){
        System.out.println(transaction.getCryptoSymbol());
        return new TransactionDto(transaction.getId(),
                transaction.getUser().getId(),
                transaction.getTransactionDate(),
                transaction.getCryptoSymbol(),
                String.valueOf(transaction.getCryptoAmount()),
                String.valueOf(transaction.getMoney()),
                String.valueOf(transaction.getWorthNow()));
    }

    public List<TransactionDto> mapToTransactionDtoList(final List<Transaction> transactions){
        return transactions.stream().map(this::mapToTransactionDto).collect(Collectors.toList());
    }
}
