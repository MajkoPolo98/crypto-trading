package com.cryptoGame.controller;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.domain.Organisation;
import com.cryptoGame.domain.OrganisationTransaction;
import com.cryptoGame.domain.User;
import com.cryptoGame.domain.dtos.OrganisationDto;
import com.cryptoGame.domain.dtos.OrganisationTransactionDto;
import com.cryptoGame.externalApis.cryptoStock.nomics.NomicsClient;
import com.cryptoGame.mapper.OrganisationTransactionMapper;
import com.cryptoGame.repository.CoinRepository;
import com.cryptoGame.service.OrganisationTransactionService;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(OrganisationTransactionController.class)
class OrganisationTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    OrganisationTransactionService service;

    @MockBean
    OrganisationTransactionMapper mapper;

    @MockBean
    NomicsClient client;

    @MockBean
    CoinRepository coinRepository;

    Organisation organisation;
    OrganisationDto organisationDto;
    OrganisationTransaction organisationTransaction;
    OrganisationTransactionDto organisationTransactionDto;
    User user;

    @BeforeEach
    void createData(){
        Map<String,BigDecimal> crypto = Map.of("BTC", new BigDecimal(1));
        this.user = User.builder()
                .userName("name")
                .adminStatus(true)
                .email("email")
                .password("pass")
                .money(new BigDecimal(10000))
                .id(1L)
                .build();

        this.organisation = Organisation.builder()
                .money(new BigDecimal(5000))
                .crypto(crypto)
                .id(1L)
                .groupName("name")
                .users(List.of(user))
                .build();

        this.organisationDto = new OrganisationDto(
                1L,
                "name",
                new BigDecimal(5000),
                crypto,
                List.of(user.getId()));

        this.organisationTransaction = OrganisationTransaction.builder()
                .id(1L)
                .user(user)
                .organisation(organisation)
                .transactionDate(LocalDate.of(1990,10,10))
                .cryptoSymbol("BTC")
                .cryptoAmount(new BigDecimal(1))
                .money(new BigDecimal(100))
                .worthNow(new BigDecimal(100))
                .build();

        this.organisationTransactionDto = new OrganisationTransactionDto(
                1L,
                user.getId(),
                organisation.getGroupName(),
                LocalDate.of(1990,10,10),
                "BTC",
                new BigDecimal(1),
                new BigDecimal(100),
                new BigDecimal(100));

    }

    @Test
    void testGetOrganisationTransactions() throws Exception{

        //Given
        List<OrganisationTransaction> organisationTransactions = new ArrayList<>();
        organisationTransactions.add(organisationTransaction);
        List<OrganisationTransactionDto> organisationTransactionDtos = new ArrayList<>();
        organisationTransactionDtos.add(organisationTransactionDto);

        List<Coin> coins = List.of(new Coin("BTC", "bitcoin", new BigDecimal(1000), "http"));

        when(service.getAllTransactions()).thenReturn(organisationTransactions);
        when(mapper.mapToTransactionDtoList(organisationTransactions)).thenReturn(organisationTransactionDtos);
        when(coinRepository.findAll()).thenReturn(coins);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/organisation/transactions/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].organisation_id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].organisation_name", Matchers.is("name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].transaction_date", Matchers.is("1990-10-10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].crypto_symbol", Matchers.is("BTC")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].crypto_amount", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].money", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].worth_now", Matchers.is(100)));
    }

    @Test
    void getOrganisationTransactionById() throws Exception {
        //Given
        List<Coin> coins = List.of(new Coin("BTC", "bitcoin", new BigDecimal(1000), "http"));

        when(mapper.mapToTransactionDto(organisationTransaction)).thenReturn(organisationTransactionDto);
        when(service.findTransaction(organisationTransaction.getId())).thenReturn(organisationTransaction);
        when(coinRepository.findAll()).thenReturn(coins);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/organisation/transactions/"+1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.organisation_id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.organisation_name", Matchers.is("name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaction_date", Matchers.is("1990-10-10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.crypto_symbol", Matchers.is("BTC")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.crypto_amount", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.money", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.worth_now", Matchers.is(100)));
    }



    @Test
    void testBuyCrypto() throws Exception{
        //Given
        OrganisationTransaction sellTransaction = OrganisationTransaction.builder()
                .id(1L)
                .user(user)
                .organisation(organisation)
                .cryptoSymbol("BTC")
                .money(new BigDecimal(100))
                .build();

        OrganisationTransactionDto sellTransactionDto = new OrganisationTransactionDto(
                1L,
                user.getId(),
                organisation.getGroupName(),
                null,
                "BTC",
                null,
                new BigDecimal(100),
                null);

        when(mapper.mapToTransaction(any(OrganisationTransactionDto.class))).thenReturn(sellTransaction);
        when(service.sellCrypto(any(OrganisationTransaction.class))).thenReturn(sellTransaction);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(sellTransactionDto);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/organisation/transactions/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testSellCrypto() throws Exception{
        //Given
        OrganisationTransaction sellTransaction = OrganisationTransaction.builder()
                .id(1L)
                .user(user)
                .organisation(organisation)
                .cryptoSymbol("BTC")
                .cryptoAmount(new BigDecimal(1))
                .build();

        OrganisationTransactionDto sellTransactionDto = new OrganisationTransactionDto(
                1L,
                user.getId(),
                organisation.getGroupName(),
                null,
                "BTC",
                new BigDecimal(1),
                null,
                null);

        when(mapper.mapToTransaction(any(OrganisationTransactionDto.class))).thenReturn(sellTransaction);
        when(service.sellCrypto(any(OrganisationTransaction.class))).thenReturn(sellTransaction);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(sellTransactionDto);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/organisation/transactions/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void testDeleteOrganisationTransaction() throws Exception{
        //Given
        when(service.findTransaction(organisationTransactionDto.getId())).thenReturn(organisationTransaction);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/organisation/transactions/"+1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
