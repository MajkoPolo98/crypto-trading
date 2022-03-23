package com.cryptoGame.mapper;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.domain.dtos.CoinDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CoinMapperTest {

    @Autowired
    CoinMapper coinMapper;

    @Test
    void mapToCoin() {
        //Given
        CoinDto dto = new CoinDto("BTC", "Bitcoin", new BigDecimal(1), "logo_url");
        //When
        Coin coin = coinMapper.mapToCoin(dto);
        //Then
        assertEquals(dto.getSymbol(), coin.getSymbol());
    }

    @Test
    void mapToCoinDto() {
        //Given
        Coin coin = new Coin("BTC", "Bitcoin", new BigDecimal(1), "logo_url");
        //When
        CoinDto dto = coinMapper.mapToCoinDto(coin);
        //Then
        assertEquals(coin.getSymbol(), dto.getSymbol());
    }

    @Test
    void mapToCoinDtoList() {
        //Given
        List<Coin> coinList = new ArrayList<>();
        Coin coin = new Coin("BTC", "Bitcoin", new BigDecimal(1), "logo_url");
        coinList.add(coin);
        //When
        List<CoinDto> dtos = coinMapper.mapToCoinDtoList(coinList);
        //Then
        assertEquals(coinList.get(0).getSymbol(), dtos.get(0).getSymbol());
    }
}