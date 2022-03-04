package com.cryptoGame.mapper;

import com.cryptoGame.domain.Transaction;
import com.cryptoGame.domain.dtos.TransactionDto;
import com.cryptoGame.externalApis.cryptoStock.CryptoStockClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionMapper {

    @Autowired
    CryptoStockClient client;

    public Transaction mapToTransaction(TransactionDto dto){
        return new Transaction(dto.getId(),
                dto.getTransactionDate(),
                dto.getCryptoSymbol(),
                dto.getCryptoAmount(),
                dto.getBoughtFor(),
                client.getPrice(dto.getCryptoSymbol()));
    }

    public TransactionDto mapToTransactionDto(Transaction transaction){
        return new TransactionDto(transaction.getId(),
                transaction.getTransactionDate(),
                transaction.getCryptoSymbol(),
                transaction.getCryptoAmount(),
                transaction.getBoughtFor(),
                client.getPrice(transaction.getCryptoSymbol()));
    }

    public List<TransactionDto> mapToTransactionDtoList(final List<Transaction> transactions){
        return transactions.stream().map(this::mapToTransactionDto).collect(Collectors.toList());
    }
}
