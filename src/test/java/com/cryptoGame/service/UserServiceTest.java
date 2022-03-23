package com.cryptoGame.service;

import com.cryptoGame.domain.User;
import com.cryptoGame.domain.User;
import com.cryptoGame.exceptions.UserNotFoundException;
import com.cryptoGame.repository.UserRepository;
import com.cryptoGame.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService service;

    @Mock
    UserRepository repository;

    User user;

    @BeforeEach
    void createData() {
        this.user = User.builder()
                .userName("name")
                .adminStatus(true)
                .email("email")
                .password("pass")
                .money(new BigDecimal(10000))
                .id(1L)
                .build();
    }

    @Test
    void saveUser() {
        //Given
        when(repository.save(user)).thenReturn(user);
        //When
        User userToSave = service.saveUser(user);
        //Then
        assertEquals(user.getUserName(), userToSave.getUserName());
        assertEquals(user.getId(), userToSave.getId());
        assertEquals(user.getMoney(), userToSave.getMoney());
        assertEquals(user.isAdminStatus(), userToSave.isAdminStatus());
        assertEquals(user.getEmail(), userToSave.getEmail());
        assertEquals(user.getPassword(), userToSave.getPassword());
    }

    @Test
    void findUser() throws UserNotFoundException {
        //Given
        Long id = user.getId();
        when(repository.findById(id)).thenReturn(Optional.of(user));
        //When
        User searchedUser = service.findUser(id);
        //Then
        assertEquals(user.getId(), searchedUser.getId());
    }

    @Test
    void findUserByName() throws UserNotFoundException {
        //Given
        String name = user.getUserName();
        when(repository.findByUserName(name)).thenReturn(Optional.of(user));
        //When
        User searchedUser = service.findUserByName(name);
        //Then
        assertEquals(user.getId(), searchedUser.getId());
    }

    @Test
    void getAllUsers() throws UserNotFoundException {
        //Given
        List<User> allUsers = List.of(user);
        when(repository.findAll()).thenReturn(allUsers);
        //When
        List<User> searchedUserList = service.getAllUsers();
        //Then
        assertEquals(allUsers.get(0).getId(), searchedUserList.get(0).getId());
    }
}
