package com.app.test.controller;


import com.app.controller.UserController;
import com.app.entity.User;
import com.app.service.UserService;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private UserController userController;
    private UserService userService;
    private Context context;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        context = mock(Context.class);
        userController = new UserController(userService);
        when(context.status(anyInt())).thenReturn(context);

    }

    @Test
    void testRegister_Success() {
        when(context.body()).thenReturn(new JSONObject()
                .put("username", "john_doe")
                .put("password", "securePassword").toString());
        when(userService.registerUser(any())).thenReturn(true);

        userController.register(context);

        verify(context).status(200);
    }

    @Test
    void testRegister_Failure() {
        when(context.body()).thenReturn(new JSONObject()
                .put("username", "john_doe")
                .put("password", "securePassword").toString());
        when(userService.registerUser(any())).thenReturn(false);

        userController.register(context);

        verify(context).status(401);
    }

    @Test
    void testRegister_InvalidJson() {
        when(context.body()).thenReturn(new JSONObject().put("message","invalid_json").toString());

        userController.register(context);

        verify(context).status(401);
    }
}

