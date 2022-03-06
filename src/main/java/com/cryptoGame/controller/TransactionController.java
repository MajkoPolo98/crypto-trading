package com.cryptoGame.controller;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.domain.Transaction;
import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.domain.dtos.TransactionDto;
import com.cryptoGame.exceptions.CoinNotFoundException;
import com.cryptoGame.exceptions.NotEnoughFundsException;
import com.cryptoGame.exceptions.UserNotFoundException;
import com.cryptoGame.externalApis.cryptoStock.CryptoStockClient;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsClient;
import com.cryptoGame.mapper.TransactionMapper;
import com.cryptoGame.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/v1")
public class TransactionController {

    private final TransactionService service;
    private final TransactionMapper mapper;
    private final NomicsClient client;

    @PostMapping("/transactions/buy")
    private TransactionDto buyCrypto(@RequestBody final TransactionDto dto) throws UserNotFoundException, NotEnoughFundsException {
        return mapper.mapToTransactionDto(service.buyCrypto(mapper.mapToTransaction(dto)));

    }

    @PostMapping("/transactions/sell")
    private TransactionDto sellCrypto(@RequestBody final TransactionDto dto) throws UserNotFoundException, NotEnoughFundsException{
        return mapper.mapToTransactionDto(service.sellCrypto(mapper.mapToTransaction(dto)));
    }

    @GetMapping("/transactions")
    private List<TransactionDto> getAllTransactions(){
        List<Transaction> transactions = service.getAllTransactions();
        List<String> cryptoSymbols = transactions.stream().map(transaction -> transaction.getCryptoSymbol()).collect(Collectors.toList());
        List<CoinDto> coinDtos =  client.getCoins(String.join(",", cryptoSymbols));
        transactions.stream().forEach(transaction -> transaction
                .setWorthNow(coinDtos.stream()
                        .filter(coinDto -> coinDto.getSymbol() == transaction.getCryptoSymbol())
                        .findFirst().get().getPrice()));
        transactions.forEach(transaction -> service.saveTransaction(transaction));
        return mapper.mapToTransactionDtoList(transactions);
    }

    @GetMapping("/transactions/{id}")
    private ResponseEntity getTransaction(@PathVariable("id") Long id){
        return new ResponseEntity(new Transaction(), HttpStatus.OK);
    }

}
