package com.app.test.service;

import com.app.service.AuthMiddleware;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthMiddlewareTest {

    private Context ctx;

    @BeforeEach
    void setUp() {
        ctx = Mockito.mock(Context.class);
    }

    @Test
    void testAuthenticate_ValidToken() {
        // Mock a valid token and user
        when(ctx.header("Authorization")).thenReturn("Bearer valid_token");
        when(ctx.header("Role")).thenReturn("manager");
        when(ctx.body()).thenReturn(new JSONObject().put("username", "john_doe").toString());

        // Mock token validation (assume "valid_token" returns "john_doe")
        try (var tokenUtilMock = mockStatic(com.app.util.TokenUtil.class)) {
            tokenUtilMock.when(() -> com.app.util.TokenUtil.validateToken("manager", "valid_token"))
                    .thenReturn("john_doe");

            assertDoesNotThrow(() -> AuthMiddleware.authenticateUser(ctx));
        }
    }

    @Test
    void testAuthenticate_InvalidToken() {
        // Mock an invalid token
        when(ctx.header("Authorization")).thenReturn("Bearer invalid_token");
        when(ctx.body()).thenReturn(new JSONObject().put("username", "john_doe").toString());
        when(ctx.header("Role")).thenReturn("manager");

        // Mock token validation failure
        try (var tokenUtilMock = mockStatic(com.app.util.TokenUtil.class)) {
            tokenUtilMock.when(() -> com.app.util.TokenUtil.validateToken("manager", "invalid_token"))
                    .thenThrow(new RuntimeException("Invalid Token"));

            HttpResponseException ex = assertThrows(HttpResponseException.class, () -> AuthMiddleware.authenticateManager(ctx));
            assertEquals(401, ex.getStatus());
            assertTrue(ex.getMessage().contains("Unauthorized"));
        }
    }

    @Test
    void testAuthenticate_InvalidUsernameInToken() {
        // Mock valid token but username mismatch
        when(ctx.header("Authorization")).thenReturn("Bearer valid_token");
        when(ctx.body()).thenReturn(new JSONObject().put("username", "wrong_user").toString());
        when(ctx.header("Role")).thenReturn("manager");

        // Mock token validation returning "john_doe"
        try (var tokenUtilMock = mockStatic(com.app.util.TokenUtil.class)) {
            tokenUtilMock.when(() -> com.app.util.TokenUtil.validateToken("manager", "valid_token"))
                    .thenReturn("john_doe");

            HttpResponseException ex = assertThrows(HttpResponseException.class, () -> AuthMiddleware.authenticateManager(ctx));
            assertEquals(401, ex.getStatus());
            assertTrue(ex.getMessage().contains("Unauthorized"));
        }
    }

}
