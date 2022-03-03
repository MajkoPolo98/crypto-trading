package com.cryptoGame.externalApis.cryptoStock.nomics;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NomicsConfigTest {


/*    @Test
    void getNomicsApiEndpoint() {
        NomicsConfig config = new NomicsConfig();
        String apiEndpoint = config.getNomicsApiEndpoint();

        assertEquals("https://api.nomics.com/v1", apiEndpoint);
    }*/

    @Test
    void getNomicsApiKey() {
    }

    @Test
    void getDefaultCurrencies() {
    }
}