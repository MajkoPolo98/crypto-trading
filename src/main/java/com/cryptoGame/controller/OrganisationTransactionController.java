package com.cryptoGame.controller;

import com.cryptoGame.domain.OrganisationTransaction;
import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.domain.dtos.OrganisationTransactionDto;
import com.cryptoGame.exceptions.NotEnoughFundsException;
import com.cryptoGame.exceptions.TransactionNotFoundException;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsClient;
import com.cryptoGame.mapper.OrganisationTransactionMapper;
import com.cryptoGame.service.OrganisationTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/v1")
public class OrganisationTransactionController {

    private final OrganisationTransactionService service;
    private final OrganisationTransactionMapper mapper;
    private final NomicsClient client;

    @PostMapping("/organisation/transactions/buy")
    private OrganisationTransactionDto buyCrypto(@RequestBody final OrganisationTransactionDto dto) throws NotEnoughFundsException {
        return mapper.mapToTransactionDto(service.buyCrypto(mapper.mapToTransaction(dto)));

    }

    @PostMapping("/organisation/transactions/sell")
    private OrganisationTransactionDto sellCrypto(@RequestBody final OrganisationTransactionDto dto) throws NotEnoughFundsException{
        return mapper.mapToTransactionDto(service.sellCrypto(mapper.mapToTransaction(dto)));
    }

    @GetMapping("/organisation/transactions")
    private List<OrganisationTransactionDto> getAllTransactions(){
        List<OrganisationTransaction> transactions = service.getAllTransactions();
        List<String> cryptoSymbols = transactions.stream().map(transaction -> transaction.getCryptoSymbol()).collect(Collectors.toList());
        List<CoinDto> coinDtos =  client.getCoins(String.join(",", cryptoSymbols));
        transactions.forEach(transaction -> transaction
                .setWorthNow(coinDtos.stream()
                        .filter(coinDto -> Objects.equals(coinDto.getSymbol(), transaction.getCryptoSymbol()))
                        .findFirst().get().getPrice()));
        transactions.forEach(transaction -> service.saveTransaction(transaction));
        return mapper.mapToTransactionDtoList(transactions);
    }

    @GetMapping("/organisation/transactions/{id}")
    private OrganisationTransactionDto getTransaction(@PathVariable("id") Long id) throws TransactionNotFoundException {
        return mapper.mapToTransactionDto(service.findTransaction(id));
    }

    @DeleteMapping(value = "/organisation/transactions/{id}")
    private void removeTransaction(@PathVariable("id") Long id)  {
        service.removeTransaction(id);
    }
}
