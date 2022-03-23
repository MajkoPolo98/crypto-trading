package com.cryptoGame.controller;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.domain.OrganisationTransaction;
import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.domain.dtos.OrganisationTransactionDto;
import com.cryptoGame.exceptions.CoinNotFoundException;
import com.cryptoGame.exceptions.NotEnoughFundsException;
import com.cryptoGame.exceptions.TransactionNotFoundException;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsClient;
import com.cryptoGame.mapper.CoinMapper;
import com.cryptoGame.mapper.OrganisationTransactionMapper;
import com.cryptoGame.repository.CoinRepository;
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
    private final CoinRepository coinRepository;

    @PostMapping("/organisation/transactions/buy")
    public OrganisationTransactionDto buyCrypto(@RequestBody final OrganisationTransactionDto dto) throws NotEnoughFundsException {
        return mapper.mapToTransactionDto(service.buyCrypto(mapper.mapToTransaction(dto)));

    }

    @PostMapping("/organisation/transactions/sell")
    public OrganisationTransactionDto sellCrypto(@RequestBody final OrganisationTransactionDto dto) throws NotEnoughFundsException{
        return mapper.mapToTransactionDto(service.sellCrypto(mapper.mapToTransaction(dto)));
    }

    @GetMapping("/organisation/transactions")
    public List<OrganisationTransactionDto> getAllTransactions(){
        List<OrganisationTransaction> transactions = service.getAllTransactions();
        List<Coin> coins = coinRepository.findAll();
        transactions.forEach(transaction -> transaction
                .setWorthNow(coins.stream()
                        .filter(coinDto -> Objects.equals(coinDto.getSymbol(), transaction.getCryptoSymbol()))
                        .findFirst().get().getPrice().multiply(transaction.getCryptoAmount())));
        transactions.forEach(service::saveTransaction);
        return mapper.mapToTransactionDtoList(transactions);
    }

    @GetMapping("/organisation/{organisationId}/transactions")
    public List<OrganisationTransactionDto> getAllOrganisationTransactions(@PathVariable("organisationId") Long organisationId){
        List<OrganisationTransaction> transactions = service.getAllTransactions().stream()
                .filter(organisationTransaction -> organisationTransaction.getOrganisation().getId().equals(organisationId)).collect(Collectors.toList());
        List<Coin> coins = coinRepository.findAll();
        transactions.forEach(transaction -> transaction
                .setWorthNow(coins.stream()
                        .filter(coinDto -> Objects.equals(coinDto.getSymbol(), transaction.getCryptoSymbol()))
                        .findFirst().get().getPrice().multiply(transaction.getCryptoAmount())));
        transactions.forEach(service::saveTransaction);
        return mapper.mapToTransactionDtoList(transactions);
    }

    @GetMapping("/organisation/transactions/{id}")
    public OrganisationTransactionDto getTransaction(@PathVariable("id") Long id) throws TransactionNotFoundException {
        return mapper.mapToTransactionDto(service.findTransaction(id));
    }

    @DeleteMapping(value = "/organisation/transactions/{id}")
    public void removeTransaction(@PathVariable("id") Long id)  {
        service.removeTransaction(id);
    }

}
