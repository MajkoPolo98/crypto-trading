package com.cryptoGame.service;

import com.cryptoGame.domain.Organisation;
import com.cryptoGame.domain.User;
import com.cryptoGame.exceptions.OrganisationNotFoundException;
import com.cryptoGame.repository.OrganisationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganisationServiceTest {

    @InjectMocks
    OrganisationService service;

    @Mock
    OrganisationRepository repository;

    Organisation organisation;

    @BeforeEach
    void createData() {
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
    }

    @Test
    void saveOrganisation() {
        //Given
        when(repository.save(organisation)).thenReturn(organisation);
        //When
        Organisation organisationToSave = service.saveOrganisation(organisation);
        //Then
        assertEquals(organisation.getGroupName(), organisationToSave.getGroupName());
        assertEquals(organisation.getId(), organisationToSave.getId());
        assertEquals(organisation.getMoney(), organisationToSave.getMoney());
        assertEquals(organisation.getCrypto(), organisationToSave.getCrypto());
        assertEquals(organisation.getUsers(), organisationToSave.getUsers());
    }

    @Test
    void findOrganisation() throws OrganisationNotFoundException {
        //Given
        Long id = organisation.getId();
        when(repository.findById(id)).thenReturn(Optional.of(organisation));
        //When
        Organisation searchedOrganisation = service.findOrganisation(id);
        //Then
        assertEquals(organisation.getId(), searchedOrganisation.getId());
    }

    @Test
    void findOrganisationByName() throws OrganisationNotFoundException {
        //Given
        String name = organisation.getGroupName();
        when(repository.findByGroupName(name)).thenReturn(Optional.of(organisation));
        //When
        Organisation searchedOrganisation = service.findOrganisationByName(name);
        //Then
        assertEquals(organisation.getId(), searchedOrganisation.getId());
    }

    @Test
    void getAllOrganisations() throws OrganisationNotFoundException {
        //Given
        List<Organisation> allOrganisations = List.of(organisation);
        when(repository.findAll()).thenReturn(allOrganisations);
        //When
        List<Organisation> searchedOrganisationList = service.getAllOrganisations();
        //Then
        assertEquals(allOrganisations.get(0).getId(), searchedOrganisationList.get(0).getId());
    }
}