package com.cryptoGame.controller;

import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsClient;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/v1")
@RequiredArgsConstructor
public class StockController {

    private final NomicsClient nomicsClient;
    private final NomicsConfig config;

    @GetMapping("/currencies")
    public void getCurrencies(){
        List<CoinDto> coinsList = nomicsClient.getCoins(config.getNomicsMainCurrencies());

        coinsList.forEach(coinDto -> System.out.println(coinDto.getSymbol()+ " " + coinDto.getPrice()));
        //coinsList.forEach(coinDto -> System.out.println(coinDto.getSymbol()));
    }
}
