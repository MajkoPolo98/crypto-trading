package com.cryptoGame.controller;

import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsClient;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsConfig;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/v1")
@RequiredArgsConstructor
public class StockController {

    private final NomicsClient nomicsClient;
    private final NomicsConfig config;
    private final NomicsFacade facade;


    @GetMapping("/stock")
    public List<CoinDto> getCurrencies(){
        return nomicsClient.getCoins(config.getNomicsMainCurrencies());
    }

    @GetMapping("/stock/{symbol}")
    public CoinDto getCurrency(@PathVariable("symbol") String symbols){
        return nomicsClient.getCoins(symbols).get(0);
    }

    @PostMapping("/stock/select")
    public List<CoinDto> getSelectedCurrencies(@RequestBody List<String> symbols){
        return nomicsClient.getCoins(String.join(",", symbols));
    }

    @GetMapping("/stock/all")
    public List<CoinDto> getAllUsedCoins(){
        return facade.getCoins();
    }

}
