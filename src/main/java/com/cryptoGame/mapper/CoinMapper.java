package com.cryptoGame.mapper;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.domain.dtos.CoinDto;

import java.util.List;
import java.util.stream.Collectors;

public class CoinMapper {

    public Coin mapToCoin(CoinDto dto){
        return new Coin(dto.getSymbol(),
                dto.getName(),
                dto.getPrice(),
                dto.getLogoUrl());
    }

    public CoinDto mapToCoinDto(Coin coin){
        return new CoinDto(coin.getSymbol(),
                coin.getName(),
                coin.getPrice(),
                coin.getLogoUrl());
    }

    public List<CoinDto> mapToCoinDtoList(List<Coin> coins){
        return coins.stream().map(this::mapToCoinDto).collect(Collectors.toList());
    }
}
