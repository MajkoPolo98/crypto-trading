package com.cryptoGame.controller;

import com.cryptoGame.domain.User;
import com.cryptoGame.domain.dtos.UserDto;
import com.cryptoGame.exceptions.UserNotFoundException;
import com.cryptoGame.mapper.UserMapper;
import com.cryptoGame.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

    private final UserMapper mapper;
    private final UserService service;

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    private void addUser(@RequestBody final UserDto userDto){
        service.saveUser(mapper.mapToUser(userDto));
    }

    @PutMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    private UserDto changeData(@RequestBody final UserDto userDto){
        User user = mapper.mapToUser(userDto);
        User savedUser = service.saveUser(user);
        return mapper.mapToUserDto(savedUser);
    }

    @DeleteMapping(value = "/user/{userId}")
    private void removeUser(@PathVariable("userId") Long userId) throws UserNotFoundException {
        service.removeUser(userId);
    }

    @PutMapping(value = "/user/{userId}/{moneyAmount}")
    private UserDto addMoney(@PathVariable("userId") Long userId, @PathVariable("moneyAmount") BigDecimal moneyAmount) throws UserNotFoundException{
        User user = service.findUser(userId);
        user.setMoney(user.getMoney().add(moneyAmount));
        User savedUser = service.saveUser(user);
        return mapper.mapToUserDto(savedUser);
    }

    @GetMapping(value = "/user")
    private List<UserDto> getAllUsers(){
        return mapper.mapToUserDtoList(service.getAllUsers());
    }

    @GetMapping(value = "/user/{userId}")
    private UserDto getUser(@PathVariable("userId") Long userId) throws UserNotFoundException{
        return mapper.mapToUserDto(service.findUser(userId));
    }

}
