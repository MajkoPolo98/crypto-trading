package com.cryptoGame.controller;

import com.cryptoGame.domain.Organisation;
import com.cryptoGame.domain.User;
import com.cryptoGame.domain.dtos.OrganisationDto;
import com.cryptoGame.domain.dtos.UserDto;
import com.cryptoGame.exceptions.NotEnoughFundsException;
import com.cryptoGame.exceptions.OrganisationNotFoundException;
import com.cryptoGame.exceptions.UserNotFoundException;
import com.cryptoGame.mapper.OrganisationMapper;
import com.cryptoGame.mapper.UserMapper;
import com.cryptoGame.service.OrganisationService;
import com.cryptoGame.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

    private final UserMapper mapper;
    private final UserService service;
    private final OrganisationService organisationService;
    private final OrganisationMapper organisationMapper;

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addUser(@RequestBody final UserDto userDto){
        service.saveUser(mapper.mapToUser(userDto));
    }

    @PutMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto changeData(@RequestBody final UserDto userDto){
        User user = mapper.mapToUser(userDto);
        User savedUser = service.saveUser(user);
        return mapper.mapToUserDto(savedUser);
    }

    @DeleteMapping(value = "/user/{userId}")
    public void removeUser(@PathVariable("userId") Long userId) throws UserNotFoundException {
        service.removeUser(userId);
    }

    @PutMapping(value = "/user/{userId}/{moneyAmount}")
    public UserDto addMoney(@PathVariable("userId") Long userId, @PathVariable("moneyAmount") BigDecimal moneyAmount) throws UserNotFoundException{
        User user = service.findUser(userId);
        user.setMoney(user.getMoney().add(moneyAmount));
        User savedUser = service.saveUser(user);
        return mapper.mapToUserDto(savedUser);
    }

    @GetMapping(value = "/user")
    public List<UserDto> getAllUsers(){
        return mapper.mapToUserDtoList(service.getAllUsers());
    }

    @GetMapping(value = "/user/find/{name}")
    public UserDto getUserByName(@PathVariable("name") String name) throws UserNotFoundException{
        return mapper.mapToUserDto(service.findUserByName(name));
    }

    @GetMapping(value = "/user/{userId}")
    public UserDto getUser(@PathVariable("userId") Long userId) throws UserNotFoundException{
        return mapper.mapToUserDto(service.findUser(userId));
    }

    @PutMapping(value = "/user/organisation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrganisationDto giveMoneyToOrganisation(@RequestBody Map<String, BigDecimal> json) throws NotEnoughFundsException, UserNotFoundException {
        Long userId = json.get("user_id").longValue();
        BigDecimal amount = json.get("amount");
        service.sendMoneyToOrganisation(userId, amount);
        service.saveUser(service.findUser(userId));
        return organisationMapper.mapToOrganisationDto(organisationService.saveOrganisation(service.findUser(userId).getOrganisation()));
    }

    @PutMapping(value = "/user/{userId}/organisation/{organisationId}")
    public OrganisationDto joinOrganisation(@PathVariable("organisationId") Long organisationId, @PathVariable("userId") Long userId) throws
            UserNotFoundException, OrganisationNotFoundException {
        User user = service.findUser(userId);
        Organisation organisation = organisationService.findOrganisation(organisationId);
        user.setOrganisation(organisation);
        service.saveUser(user);
        return organisationMapper.mapToOrganisationDto(organisationService.saveOrganisation(organisation));
    }
}
