package com.cryptoGame.scheduler;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.domain.User;
import com.cryptoGame.domain.UserTransaction;
import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.externalApis.cryptoStock.CryptoStockClient;
import com.cryptoGame.mapper.CoinMapper;
import com.cryptoGame.repository.CoinRepository;
import com.cryptoGame.repository.UserRepository;
import com.cryptoGame.repository.UserTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class RateUpdater {

    private final CryptoStockClient client;
    private final UserRepository userRepository;
    private final UserTransactionRepository transactionRepository;
    private List<CoinDto> coins;
    private final String cron = "*/20 * * * * *";

    @Scheduled(cron = cron)
    public void updateRates() {
        String symbols = String.join(",", transactionRepository.findAll().stream().map(UserTransaction::getCryptoSymbol).collect(Collectors.toSet()));
        this.coins = client.getCoins(symbols);
    }

    @Scheduled(cron = cron)
    public void updateTransactions(){
        coins.forEach(coin -> transactionRepository.findAllByCryptoSymbol(coin.getSymbol())
                .forEach(transaction -> {
                    transaction.setWorthNow(transaction.getCryptoAmount().multiply(coin.getPrice()).negate());
                    transactionRepository.save(transaction);
                }));
    }

    @Scheduled(cron = cron)
    public void updateUserValue(){
        List<User> users = userRepository.findAll();

        Map<String, CoinDto> coinMap = new HashMap<>();
        for(CoinDto coin: coins){
            coinMap.put(coin.getSymbol(), coin);
        }
        for(User user: users){
            BigDecimal value = user.getMoney();
            for(Map.Entry<String, BigDecimal> entry: user.getCrypto().entrySet()){
                value = value.add(coinMap.get(entry.getKey()).getPrice().multiply(entry.getValue()));
            }
            user.setValue(value);
            userRepository.save(user);
        }
    }
}
