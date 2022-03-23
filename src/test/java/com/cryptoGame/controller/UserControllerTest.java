package com.cryptoGame.controller;

import com.cryptoGame.domain.User;
import com.cryptoGame.domain.dtos.UserDto;
import com.cryptoGame.mapper.OrganisationMapper;
import com.cryptoGame.mapper.UserMapper;
import com.cryptoGame.service.OrganisationService;
import com.cryptoGame.service.UserService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    OrganisationService organisationService;

    @MockBean
    OrganisationMapper organisationMapper;

    @MockBean
    UserService service;

    @MockBean
    UserMapper mapper;


    UserDto userDto;
    User user;

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
    }

    @Test
    void testGetUsers() throws Exception{

        //Given
        List<User> users = new ArrayList<>();
        users.add(user);
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(userDto);

        when(service.getAllUsers()).thenReturn(users);
        when(mapper.mapToUserDtoList(users)).thenReturn(userDtos);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/user/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is("email")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].admin", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].money", Matchers.is(10000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].value", Matchers.is(10000)));
    }

    @Test
    void getUserById() throws Exception {
        //Given
        when(mapper.mapToUserDto(user)).thenReturn(userDto);
        when(service.findUser(user.getId())).thenReturn(user);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/user/"+1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("email")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.admin", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.money", Matchers.is(10000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.value", Matchers.is(10000)));
    }

    @Test
    void getUserByName() throws Exception {
        //Given
        when(mapper.mapToUserDto(user)).thenReturn(userDto);
        when(service.findUserByName(user.getUserName())).thenReturn(user);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/user/find/"+"name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("email")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.admin", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.money", Matchers.is(10000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.value", Matchers.is(10000)));
    }


    @Test
    void testCreateUser() throws Exception{
        //Given
        when(mapper.mapToUser(any(UserDto.class))).thenReturn(user);
        when(service.saveUser(any(User.class))).thenReturn(user);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(userDto);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void testDeleteUser() throws Exception{
        //Given
        when(service.findUser(userDto.getId())).thenReturn(user);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/user/"+1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}