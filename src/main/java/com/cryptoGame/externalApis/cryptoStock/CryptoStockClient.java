package com.cryptoGame.externalApis.cryptoStock;

import com.cryptoGame.domain.dtos.CoinDto;

import java.math.BigDecimal;
import java.util.List;

public interface CryptoStockClient {
    List<CoinDto> getCoins(String... coinSymbols);
    BigDecimal getPrice(String coinSymbol);

}
