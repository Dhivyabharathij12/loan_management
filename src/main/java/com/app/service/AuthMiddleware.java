package com.app.service;

import io.javalin.http.Context;
import org.json.JSONObject;

import java.util.Objects;

import static com.app.util.TokenUtil.validateToken;

public class AuthMiddleware {

    // Authentication Middleware
    public static void authenticate(Context ctx) {
        String token = ctx.header("Authorization");
        String body= ctx.body();
        JSONObject jsonBody=new JSONObject(body);
        String userName= jsonBody.getString("username");

        // Check if token is null or invalid
        if (token == null || !isValidUser(userName, token)) {
            // Send Unauthorized response
            ctx.status(401).result(new JSONObject().put("message", "Unauthorized").toString());
            return;  // Stop further execution here
        }
    }

    // Authorization Middleware for Manager role
    public static void authorizeManager(Context ctx) {
        String role = ctx.header("Role");
        String token = ctx.header("Authorization");
        String body= ctx.body();
        JSONObject jsonBody=new JSONObject(body);
        String userName= jsonBody.getString("username");

        // Check if the role is not "manager"
        if (role != null && Objects.equals(role, "manager") && isValidUser(userName, token)) {
            // Send Forbidden response
            ctx.status(403).json(new JSONObject().put("message", "Forbidden: Manager role required"));
            return;  // Stop further execution here
        }
    }


    private static boolean isValidUser(String userName, String token){
        try {
            String tokenUserName = validateToken(userName, token.replace("Bearer", "").trim());
            return Objects.equals(tokenUserName, userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
