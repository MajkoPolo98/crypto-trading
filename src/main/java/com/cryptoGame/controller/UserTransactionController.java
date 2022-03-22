package com.cryptoGame.controller;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.domain.UserTransaction;
import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.domain.dtos.UserTransactionDto;
import com.cryptoGame.exceptions.CoinNotFoundException;
import com.cryptoGame.exceptions.NotEnoughFundsException;
import com.cryptoGame.exceptions.TransactionNotFoundException;
import com.cryptoGame.exceptions.UserNotFoundException;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsClient;
import com.cryptoGame.mapper.CoinMapper;
import com.cryptoGame.mapper.UserTransactionMapper;
import com.cryptoGame.repository.CoinRepository;
import com.cryptoGame.service.UserTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserTransactionController {

    private final UserTransactionService service;
    private final UserTransactionMapper mapper;
    private final CoinRepository coinRepository;

    @PostMapping("/user/transactions/buy")
    private UserTransactionDto buyCrypto(@RequestBody final UserTransactionDto dto) throws UserNotFoundException, NotEnoughFundsException, CoinNotFoundException {
        return mapper.mapToTransactionDto(service.buyCrypto(mapper.mapToTransaction(dto)));
    }

    @PostMapping("/user/transactions/sell")
    private UserTransactionDto sellCrypto(@RequestBody final UserTransactionDto dto) throws UserNotFoundException, NotEnoughFundsException{
        return mapper.mapToTransactionDto(service.sellCrypto(mapper.mapToTransaction(dto)));
    }

    @GetMapping("/user/transactions")
    private List<UserTransactionDto> getAllTransactions(){
        List<UserTransaction> transactions = service.getAllTransactions();
        List<String> cryptoSymbols = transactions.stream().map(UserTransaction::getCryptoSymbol).collect(Collectors.toList());
        List<Coin> coins = coinRepository.findAll();
        transactions.forEach(transaction -> transaction
                .setWorthNow(coins.stream()
                        .filter(coin -> coin.getSymbol().equals(transaction.getCryptoSymbol()))
                        .findFirst().get().getPrice().multiply(transaction.getCryptoAmount())));
        transactions.forEach(service::saveTransaction);
        return mapper.mapToTransactionDtoList(transactions);
    }

    @GetMapping("/user/{userId}/transactions")
    private List<UserTransactionDto> getUserTransactions(@PathVariable("userId") Long userId){
        List<UserTransaction> transactions = service.getAllTransactions().stream().filter(transaction -> transaction.getUser().getId().equals(userId)).collect(Collectors.toList());
        List<String> cryptoSymbols = transactions.stream().map(UserTransaction::getCryptoSymbol).collect(Collectors.toList());
        List<Coin> coins = coinRepository.findAll();
        transactions.stream()
                .forEach(transaction -> transaction
                .setWorthNow(coins.stream()
                        .filter(coin -> coin.getSymbol().equals(transaction.getCryptoSymbol()))
                        .findFirst().get().getPrice().multiply(transaction.getCryptoAmount())));
        transactions.forEach(service::saveTransaction);
        return mapper.mapToTransactionDtoList(transactions);
    }

    @GetMapping("/user/transactions/{id}")
    private UserTransactionDto getTransaction(@PathVariable("id") Long id) throws TransactionNotFoundException {
        return mapper.mapToTransactionDto(service.findTransaction(id));
    }

    @DeleteMapping(value = "/user/transactions/{id}")
    private void removeTransaction(@PathVariable("id") Long id) throws TransactionNotFoundException {
        service.removeTransaction(id);
    }
}
