package com.cryptoGame.mapper;

import com.cryptoGame.domain.Transaction;
import com.cryptoGame.domain.dtos.TransactionDto;
import com.cryptoGame.externalApis.cryptoStock.CryptoStockClient;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionMapper {

    @Autowired
    CryptoStockClient client;

    public Transaction mapToTransaction(TransactionDto dto){
        return new Transaction(dto.getId(),
                dto.getTransactionDate(),
                dto.getCryptoName(),
                dto.getCryptoAmount(),
                dto.getBoughtFor(),
                client.getPrice(dto.getCryptoName()));
    }
}
