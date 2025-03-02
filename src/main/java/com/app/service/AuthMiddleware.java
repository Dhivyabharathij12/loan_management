package com.app.service;

import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import org.eclipse.jetty.util.StringUtil;
import org.json.JSONObject;

import java.util.Objects;

import static com.app.util.TokenUtil.validateToken;

public class AuthMiddleware {

    // Authentication Middleware
    public static void authenticate(Context ctx) {
        String token = ctx.header("Authorization");
        String role = ctx.header("Role");

        String userName = getUserName(ctx);

        if (StringUtil.isEmpty(token) || !isValidUser(userName, role, token)) {
            System.out.println("Unauthorized: Invalid Token(Login again)");
            throw new HttpResponseException(401, "Unauthorized - Invalid Token(Login again)");
        }
    }

    private static boolean isValidUser(String userName, String role, String token){
        try {
            String tokenUserName = validateToken(role, token.replace("Bearer", "").trim());
            return Objects.equals(tokenUserName, userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getUserName(Context ctx){
        String body= ctx.body();
        if(!StringUtil.isEmpty(body)) {
            JSONObject jsonBody = new JSONObject(body);
            return jsonBody.getString("username");
        }
        return ctx.queryParam("username");
    }

}
