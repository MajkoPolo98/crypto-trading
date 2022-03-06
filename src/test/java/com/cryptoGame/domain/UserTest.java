package com.cryptoGame.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void addCrypto() {
        HashMap<String, BigDecimal> wallet = new HashMap<>();
        wallet.put("BTC", new BigDecimal(100));
        User user = new User(1L,"user", "mail","pass",true,new BigDecimal(1000), wallet);

        user.addCrypto("BTC", new BigDecimal(400));

        assertEquals(new BigDecimal(500), user.getCrypto().get("BTC"));

    }
}