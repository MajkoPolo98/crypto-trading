package com.cryptoGame.controller;

import com.cryptoGame.domain.Organisation;
import com.cryptoGame.domain.User;
import com.cryptoGame.domain.dtos.OrganisationDto;
import com.cryptoGame.mapper.OrganisationMapper;
import com.cryptoGame.service.OrganisationService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(OrganisationController.class)
class OrganisationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    OrganisationService service;

    @MockBean
    OrganisationMapper mapper;


    OrganisationDto organisationDto;
    Organisation organisation;


    @BeforeEach
    void createData(){
        Map<String,BigDecimal> crypto = Map.of("BTC", new BigDecimal(1));
        User user = User.builder()
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
    }

    @Test
    void testGetOrganisations() throws Exception{

        //Given
        List<Organisation> organisations = new ArrayList<>();
        organisations.add(organisation);
        List<OrganisationDto> organisationDtos = new ArrayList<>();
        organisationDtos.add(organisationDto);

        when(service.getAllOrganisations()).thenReturn(organisations);
        when(mapper.mapToOrganisationDtoList(organisations)).thenReturn(organisationDtos);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/organisation")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].organisation_name", Matchers.is("name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].organisation_funds", Matchers.is(5000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].users[0]", Matchers.is(1)));
    }

    @Test
    void getOrganisationById() throws Exception {
        //Given
        when(mapper.mapToOrganisationDto(organisation)).thenReturn(organisationDto);
        when(service.findOrganisation(organisation.getId())).thenReturn(organisation);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/organisation/"+1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.organisation_name", Matchers.is("name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.organisation_funds", Matchers.is(5000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.users[0]", Matchers.is(1)));
    }

    @Test
    void getOrganisationByName() throws Exception {
        //Given
        when(mapper.mapToOrganisationDto(organisation)).thenReturn(organisationDto);
        when(service.findOrganisationByName(organisation.getGroupName())).thenReturn(organisation);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/organisation/find/"+"name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.organisation_name", Matchers.is("name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.organisation_funds", Matchers.is(5000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.users[0]", Matchers.is(1)));
    }


    @Test
    void testCreateOrganisation() throws Exception{
        //Given
        when(mapper.mapToOrganisation(any(OrganisationDto.class))).thenReturn(organisation);
        when(service.saveOrganisation(any(Organisation.class))).thenReturn(organisation);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(organisationDto);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/organisation")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void testDeleteOrganisation() throws Exception{
        //Given
        when(service.findOrganisation(organisationDto.getId())).thenReturn(organisation);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/organisation/"+1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}