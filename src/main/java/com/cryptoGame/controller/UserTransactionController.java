package com.cryptoGame.controller;

import com.cryptoGame.domain.UserTransaction;
import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.domain.dtos.UserTransactionDto;
import com.cryptoGame.exceptions.NotEnoughFundsException;
import com.cryptoGame.exceptions.TransactionNotFoundException;
import com.cryptoGame.exceptions.UserNotFoundException;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsClient;
import com.cryptoGame.mapper.UserTransactionMapper;
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
    private final NomicsClient client;

    @PostMapping("/transactions/buy")
    private UserTransactionDto buyCrypto(@RequestBody final UserTransactionDto dto) throws UserNotFoundException, NotEnoughFundsException {
        return mapper.mapToTransactionDto(service.buyCrypto(mapper.mapToTransaction(dto)));

    }

    @PostMapping("/transactions/sell")
    private UserTransactionDto sellCrypto(@RequestBody final UserTransactionDto dto) throws UserNotFoundException, NotEnoughFundsException{
        return mapper.mapToTransactionDto(service.sellCrypto(mapper.mapToTransaction(dto)));
    }

    @GetMapping("/transactions")
    private List<UserTransactionDto> getAllTransactions(){
        List<UserTransaction> transactions = service.getAllTransactions();
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
    private UserTransactionDto getTransaction(@PathVariable("id") Long id) throws TransactionNotFoundException {
        return mapper.mapToTransactionDto(service.findTransaction(id));
    }

    @DeleteMapping(value = "/transactions/{id}")
    private void removeTransaction(@PathVariable("id") Long id) throws TransactionNotFoundException {
        service.removeTransaction(id);
    }
}
