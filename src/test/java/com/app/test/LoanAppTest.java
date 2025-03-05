package com.app.test;

import com.app.LoanApp;
import io.javalin.Javalin;
import okhttp3.*;
import org.junit.jupiter.api.*;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoanAppTest {
    private Javalin app;
    private OkHttpClient client;
    private String baseUrl;

    @BeforeAll
    void setUp() {
        app = LoanApp.startApp(0); // Start on a dynamic port
        int port = app.port(); // Get the assigned port
        baseUrl = "http://localhost:" + port;
        client = new OkHttpClient();
    }

    @AfterAll
    void tearDown() {
        app.stop();
    }

    @Test
    void testRegisterUser() throws IOException {
        RequestBody body = RequestBody.create("""
            {
                "name": "John Doe",
                "username": "johndoe",
                "password": "password123",
                "role": "user"
            }
        """, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(baseUrl + "/register/")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(401, response.code(), "User registration should return 401 Created");
    }

    @Test
    void testLoginUser() throws IOException {
        RequestBody body = RequestBody.create("""
            {
                "username": "johndoe",
                "password": "password123"
            }
        """, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(baseUrl + "/auth/login")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(200, response.code(), "Login should return 200 OK");
    }

    @Test
    void testApplyLoanWithoutAuth() throws IOException {
        RequestBody body = RequestBody.create("""
            {
                "amount": 5000.00,
                "loan_type": "personal",
                "username":"test_username"
            }
        """, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(baseUrl + "/user/loans/apply")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(401, response.code(), "Applying for a loan without authentication should return 401 Unauthorized");
    }
}
