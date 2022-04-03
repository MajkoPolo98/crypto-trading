package com.cryptoGame.scheduler;

import com.cryptoGame.controller.StockController;
import com.cryptoGame.domain.*;
import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.mapper.CoinMapper;
import com.cryptoGame.repository.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.Modifying;
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
@Setter
@Getter
public class RateUpdater {

    private final StockController stock;
    private final UserRepository userRepository;
    private final CoinRepository coinRepository;
    private final CoinMapper coinMapper;
    private final UserTransactionRepository userTransactionRepository;
    private final OrganisationTransactionRepository organisationTransactionRepository;
    private List<CoinDto> coins;
    private final String cron = "*/50 * * * * *";

    @Transactional
    @Modifying
    @Scheduled(cron = cron)
    public void updateRates() {
        List<String> usedSymbols = stock.getAllUsedCoins().stream().map(CoinDto::getSymbol).collect(Collectors.toList());
        this.coins = stock.getSelectedCurrencies(usedSymbols);
        coins.forEach(coinDto -> coinRepository.save(coinMapper.mapToCoin(coinDto)));
        updateOrganisationTransactions();
        updateUserTransactions();
        updateUserValue();
    }

    public void updateUserTransactions(){
        coins.forEach(coin -> userTransactionRepository.findAllByCryptoSymbol(coin.getSymbol())
                .forEach(transaction -> {
                    transaction.setWorthNow(transaction.getCryptoAmount().multiply(coin.getPrice()));
                    userTransactionRepository.save(transaction);
                }));
    }

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

    public void updateOrganisationTransactions(){
        coins.forEach(coin -> organisationTransactionRepository.findAllByCryptoSymbol(coin.getSymbol())
                .forEach(transaction -> {
                    transaction.setWorthNow(transaction.getCryptoAmount().multiply(coin.getPrice()));
                    organisationTransactionRepository.save(transaction);
                }));
    }

}
