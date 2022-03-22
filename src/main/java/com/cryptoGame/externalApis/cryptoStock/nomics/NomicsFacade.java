package com.cryptoGame.externalApis.cryptoStock.nomics;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.mapper.CoinMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class NomicsFacade {
    private final NomicsClient nomicsClient;
    private final CoinMapper coinMapper;

    public List<Coin> getCoins(String... coinSymbols) {
        return coinMapper.mapToCoinList(nomicsClient.getCoins(coinSymbols));
    }
}
