package com.cryptoGame.mapper;

import com.cryptoGame.domain.User;
import com.cryptoGame.domain.dtos.UserDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {



    public User mapToUser(UserDto dto){
        return new User(dto.getId(),
                dto.getUserName(),
                dto.getEmail(),
                dto.getEmail(),
                dto.isAdminStatus(),
                new BigDecimal(dto.getMoney()),
                dto.getCrypto());
    }

    public UserDto mapToUserDto(User user){
        return new UserDto(user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                user.isAdminStatus(),
                String.valueOf(user.getMoney()),
                user.getCrypto());
    }

    public List<UserDto> mapToUserDtoList(final List<User> users){
       return users.stream().map(this::mapToUserDto).collect(Collectors.toList());
    }
}
