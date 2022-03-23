package com.cryptoGame.mapper;

import com.cryptoGame.domain.User;
import com.cryptoGame.domain.dtos.UserDto;

import com.cryptoGame.repository.OrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    @Autowired
    private OrganisationRepository orgRepository;

    public User mapToUser(UserDto dto){

        return new User(dto.getId(),
                dto.getUserName(),
                dto.getEmail(),
                dto.getPassword(),
                dto.isAdminStatus(),
                dto.getMoney(),
                dto.getValue(),
                orgRepository.findByGroupName(dto.getGroup_name()).orElse(null),
                dto.getCrypto());
    }

    public UserDto mapToUserDto(User user){

        UserDto dto = new UserDto(user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                user.isAdminStatus(),
                user.getMoney(),
                user.getValue(),
                null,
                user.getCrypto());
        if(user.getOrganisation() != null){
            dto.setGroup_name(user.getOrganisation().getGroupName());
        }
        return dto;
    }

    public List<UserDto> mapToUserDtoList(final List<User> users){
       return users.stream().map(this::mapToUserDto).collect(Collectors.toList());
    }
}
