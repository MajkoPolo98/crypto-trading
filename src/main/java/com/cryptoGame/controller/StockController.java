package com.cryptoGame.controller;

import com.cryptoGame.domain.OrganisationTransaction;
import com.cryptoGame.domain.UserTransaction;
import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsClient;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsConfig;
import com.cryptoGame.mapper.CoinMapper;
import com.cryptoGame.repository.CoinRepository;
import com.cryptoGame.repository.OrganisationTransactionRepository;
import com.cryptoGame.repository.UserTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/v1")
@RequiredArgsConstructor
public class StockController {

    private final NomicsClient nomicsClient;
    private final NomicsConfig config;
    private final CoinMapper coinMapper;
    private final CoinRepository coinRepository;
    private final OrganisationTransactionRepository organisationTransactionRepository;

    @GetMapping("/stock")
    public List<CoinDto> getCurrencies(){
        List<CoinDto> coins =  coinMapper.mapToCoinDtoList(nomicsClient.getCoins(config.getNomicsMainCurrencies()));
        return coins;
    }

    @GetMapping("/stock/{symbol}")
    public CoinDto getCurrency(@PathVariable("symbol") String symbols){
        return coinMapper.mapToCoinDto(nomicsClient.getCoins(symbols).get(0));
    }

    @PostMapping("/stock/select")
    public List<CoinDto> getSelectedCurrencies(@RequestBody List<String> symbols){
        return coinMapper.mapToCoinDtoList(nomicsClient.getCoins(symbols.stream().collect(Collectors.joining(","))));
    }

    @GetMapping("/stock/all")
    public List<CoinDto> getAllUsedCoins(){
        return coinMapper.mapToCoinDtoList(coinRepository.findAll());
    }

}
