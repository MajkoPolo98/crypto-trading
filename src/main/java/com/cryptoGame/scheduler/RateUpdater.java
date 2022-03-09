package com.cryptoGame.scheduler;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.domain.UserTransaction;
import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.externalApis.cryptoStock.CryptoStockClient;
import com.cryptoGame.mapper.CoinMapper;
import com.cryptoGame.repository.CoinRepository;
import com.cryptoGame.repository.UserTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RateUpdater {

    private final CryptoStockClient client;
    private final UserTransactionRepository transactionRepository;

    @Scheduled(cron = "* * * * */5 *")
    public void updateTransactions() {
        String symbols = String.join(",", transactionRepository.findAll().stream().map(UserTransaction::getCryptoSymbol).collect(Collectors.toSet()));
        List<CoinDto> coins = client.getCoins(symbols);

        coins.forEach(coin -> transactionRepository.findAllByCryptoSymbol(coin.getSymbol())
                .forEach(transaction -> {
                    transaction.setWorthNow(transaction.getCryptoAmount().multiply(coin.getPrice()).negate());
                            transactionRepository.save(transaction);
                }));

    }
}
