package com.cryptoGame.controller;

import com.cryptoGame.domain.Organisation;
import com.cryptoGame.domain.dtos.OrganisationDto;
import com.cryptoGame.exceptions.OrganisationNotFoundException;
import com.cryptoGame.mapper.OrganisationMapper;
import com.cryptoGame.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/v1")
public class OrganisationController {

    private final OrganisationMapper mapper;
    private final OrganisationService service;

    @PostMapping(value = "/organisation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addOrganisation(@RequestBody final OrganisationDto organisationDto){
        service.saveOrganisation(mapper.mapToOrganisation(organisationDto));
    }

    @PutMapping(value = "/organisation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrganisationDto changeData(@RequestBody final OrganisationDto organisationDto){
        Organisation organisation = mapper.mapToOrganisation(organisationDto);
        Organisation savedOrganisation = service.saveOrganisation(organisation);
        return mapper.mapToOrganisationDto(savedOrganisation);
    }

    @DeleteMapping(value = "/organisation/{organisationId}")
    public void removeOrganisation(@PathVariable("organisationId") Long organisationId) {
        service.removeOrganisation(organisationId);
    }

    @PutMapping(value = "/organisation/{organisationId}/{moneyAmount}")
    public OrganisationDto addMoney(@PathVariable("organisationId") Long organisationId, @PathVariable("moneyAmount") BigDecimal moneyAmount) throws OrganisationNotFoundException{
        Organisation organisation = service.findOrganisation(organisationId);
        organisation.setMoney(organisation.getMoney().add(moneyAmount));
        Organisation savedOrganisation = service.saveOrganisation(organisation);
        return mapper.mapToOrganisationDto(savedOrganisation);
    }

    @GetMapping(value = "/organisation")
    public List<OrganisationDto> getAllOrganisations(){
        return mapper.mapToOrganisationDtoList(service.getAllOrganisations());
    }

    @GetMapping(value = "/organisation/{organisationId}")
    public OrganisationDto getOrganisation(@PathVariable("organisationId") Long organisationId) throws OrganisationNotFoundException{
        return mapper.mapToOrganisationDto(service.findOrganisation(organisationId));
    }

    @GetMapping(value = "/organisation/find/{organisationName}")
    public OrganisationDto getOrganisationByName(@PathVariable("organisationName") String organisationName) throws OrganisationNotFoundException{
        return mapper.mapToOrganisationDto(service.findOrganisationByName(organisationName));
    }


}
