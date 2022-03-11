package com.cryptoGame.mapper;

import com.cryptoGame.domain.Organisation;
import com.cryptoGame.domain.User;
import com.cryptoGame.domain.dtos.OrganisationDto;
import com.cryptoGame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganisationMapper {

    @Autowired
    private UserRepository repository;

    public Organisation mapToOrganisation(OrganisationDto dto){
        return new Organisation(dto.getId(),
                dto.getGroupName(),
                dto.getGroupFunds(),
                dto.getCrypto(),
                dto.getUsers().stream().map(id -> repository.findById(id).get()).collect(Collectors.toList()));
    }

    public OrganisationDto mapToOrganisationDto(Organisation organisation){
        OrganisationDto dto = new OrganisationDto(organisation.getId(),
                organisation.getGroupName(),
                organisation.getMoney(),
                organisation.getCrypto(),
                organisation.getUsers().stream().map(User::getId).collect(Collectors.toList()));
        return dto;
    }

    public List<OrganisationDto> mapToOrganisationDtoList(final List<Organisation> users){
        return users.stream().map(this::mapToOrganisationDto).collect(Collectors.toList());
    }
}
