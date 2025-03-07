package com.app.test.service;

import com.app.entity.User;
import com.app.repository.UserRepository;
import com.app.service.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void testRegisterUser_Success() {
        JSONObject json = new JSONObject();
        json.put("username", "dhivya_93");
        json.put("password", "password123");
        json.put("role", "USER");
        json.put("name", "Dhivya");

        when(userRepository.saveUser(any(User.class))).thenReturn(true);

        boolean result = userService.registerUser(json);
        assertTrue(result);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).saveUser(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("dhivya_93", savedUser.getUserName());
        assertNotEquals("password123", savedUser.getPassWord());
        assertEquals("USER", savedUser.getRole());
        assertEquals("Dhivya", savedUser.getName());
    }

    @Test
    void testRegisterUser_Failure() {
        JSONObject json = new JSONObject();
        json.put("username", "dhivya_93");
        json.put("password", "password123");
        json.put("role", "USER");
        json.put("name", "Dhivya");

        when(userRepository.saveUser(any(User.class))).thenReturn(false);

        boolean result = userService.registerUser(json);
        assertFalse(result);

        verify(userRepository).saveUser(any(User.class));
    }

    @Test
    void testGetUser_Success() {
        User mockUser = new User();
        mockUser.setUserName("dhivya_93");
        mockUser.setPassWord("hashed_password");
        mockUser.setRole("USER");
        mockUser.setName("Dhivya");

        when(userRepository.getUser("dhivya_93")).thenReturn(mockUser);

        User result = userService.getUser("dhivya_93");

        assertNotNull(result);
        assertEquals("dhivya_93", result.getUserName());
        assertEquals("hashed_password", result.getPassWord());
        assertEquals("USER", result.getRole());
        assertEquals("Dhivya", result.getName());
    }

    @Test
    void testGetUser_NotFound() {
        when(userRepository.getUser("unknown_user")).thenReturn(null);

        User result = userService.getUser("unknown_user");

        assertNull(result);
    }
}
