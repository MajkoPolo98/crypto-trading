package com.cryptoGame.controller;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.domain.User;
import com.cryptoGame.domain.UserTransaction;
import com.cryptoGame.domain.dtos.UserDto;
import com.cryptoGame.domain.dtos.UserTransactionDto;
import com.cryptoGame.mapper.UserTransactionMapper;
import com.cryptoGame.repository.CoinRepository;
import com.cryptoGame.service.UserTransactionService;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(UserTransactionController.class)
class UserTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserTransactionService service;

    @MockBean
    UserTransactionMapper mapper;

    @MockBean
    CoinRepository coinRepository;

    User user;
    UserDto userDto;
    UserTransaction userTransaction;
    UserTransactionDto userTransactionDto;

    @BeforeEach
    void createData(){
        this.user = User.builder()
                .userName("name")
                .adminStatus(true)
                .email("email")
                .password("pass")
                .money(new BigDecimal(10000))
                .value(new BigDecimal(10000))
                .id(1L)
                .build();


        this.userDto = new UserDto(
                1L,
                "name",
                "email",
                "pass",
                true,
                new BigDecimal(10000),
                new BigDecimal(10000),
                null,
                null);

        this.userTransaction = UserTransaction.builder()
                .id(1L)
                .user(user)
                .transactionDate(LocalDate.of(1990,10,10))
                .cryptoSymbol("BTC")
                .cryptoAmount(new BigDecimal(1))
                .money(new BigDecimal(1000))
                .worthNow(new BigDecimal(1000))
                .build();

        this.userTransactionDto = new UserTransactionDto(
                1L,
                1L,
                LocalDate.of(1990,10,10),
                "BTC",
                new BigDecimal(1),
                new BigDecimal(1000),
                new BigDecimal(1000));

    }

    @Test
    void testGetUserTransactions() throws Exception{

        //Given
        List<UserTransaction> userTransactions = new ArrayList<>();
        userTransactions.add(userTransaction);
        List<UserTransactionDto> userTransactionDtos = new ArrayList<>();
        userTransactionDtos.add(userTransactionDto);

        List<Coin> coins = List.of(new Coin("BTC", "bitcoin", new BigDecimal(1000), "http"));

        when(service.getAllTransactions()).thenReturn(userTransactions);
        when(mapper.mapToTransactionDtoList(userTransactions)).thenReturn(userTransactionDtos);
        when(coinRepository.findAll()).thenReturn(coins);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/user/transactions/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].transaction_date", Matchers.is("1990-10-10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].crypto_symbol", Matchers.is("BTC")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].crypto_amount", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].money", Matchers.is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].worth_now", Matchers.is(1000)));
    }

    @Test
    void getUserTransactionById() throws Exception {
        //Given
        List<Coin> coins = List.of(new Coin("BTC", "bitcoin", new BigDecimal(1000), "http"));

        when(mapper.mapToTransactionDto(userTransaction)).thenReturn(userTransactionDto);
        when(service.findTransaction(userTransaction.getId())).thenReturn(userTransaction);
        when(coinRepository.findAll()).thenReturn(coins);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/user/transactions/"+1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaction_date", Matchers.is("1990-10-10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.crypto_symbol", Matchers.is("BTC")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.crypto_amount", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.money", Matchers.is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.worth_now", Matchers.is(1000)));
    }



    @Test
    void testBuyCrypto() throws Exception{
        //Given
        UserTransaction buyTransaction = UserTransaction.builder()
                .user(user)
                .cryptoSymbol("Symbol")
                .money(new BigDecimal(200))
                .build();
        UserTransactionDto buyTransactionDto = new UserTransactionDto(null, user.getId(), null, "Symbol", null, new BigDecimal(200), null);

        when(mapper.mapToTransaction(any(UserTransactionDto.class))).thenReturn(buyTransaction);
        when(service.buyCrypto(any(UserTransaction.class))).thenReturn(buyTransaction);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(buyTransactionDto);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/user/transactions/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void testSellCrypto() throws Exception{
        //Given
        UserTransaction sellTransaction = UserTransaction.builder()
                .user(user)
                .cryptoSymbol("Symbol")
                .cryptoAmount(new BigDecimal(1))
                .build();
        UserTransactionDto sellTransactionDto = new UserTransactionDto(null, user.getId(), null, "Symbol", new BigDecimal(1), null, null);

        when(mapper.mapToTransaction(any(UserTransactionDto.class))).thenReturn(sellTransaction);
        when(service.sellCrypto(any(UserTransaction.class))).thenReturn(sellTransaction);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(sellTransactionDto);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/user/transactions/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void testDeleteUserTransaction() throws Exception{
        //Given
        when(service.findTransaction(userTransactionDto.getId())).thenReturn(userTransaction);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/user/transactions/"+1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}