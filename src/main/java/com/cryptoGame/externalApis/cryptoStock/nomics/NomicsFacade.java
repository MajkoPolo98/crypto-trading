package com.cryptoGame.externalApis.cryptoStock.nomics;

import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.mapper.CoinMapper;
import com.cryptoGame.repository.CoinRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class NomicsFacade {
    private final CoinMapper coinMapper;
    private final CoinRepository repository;

    public List<CoinDto> getCoins() {
        return coinMapper.mapToCoinDtoList(repository.findAll());
    }
}
