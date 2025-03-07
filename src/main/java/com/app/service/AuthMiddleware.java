package com.app.service;

import com.app.entity.UserType;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import org.eclipse.jetty.util.StringUtil;

import static com.app.util.TokenUtil.validateToken;

public class AuthMiddleware {

    // Authentication Middleware
    public static void authenticateUser(Context ctx) {
        String token = ctx.header("Authorization");

        String userName = getUserNameFromToken(UserType.USER, token);
        ctx.attribute("username", userName);
        if (StringUtil.isEmpty(token) || null == userName || userName.isBlank()) {
            System.out.println("Unauthorized: Invalid Token(Login again)");
            throw new HttpResponseException(401, "Unauthorized - Invalid Token(Login again)");
        }
    }

    public static void authenticateManager(Context ctx) {
        String token = ctx.header("Authorization");
        String userName = getUserNameFromToken(UserType.MANAGER, token);
        ctx.attribute("username", userName);
        if (StringUtil.isEmpty(token) || null == userName || userName.isBlank()) {
            System.out.println("Unauthorized: Invalid Token(Login again)");
            throw new HttpResponseException(401, "Unauthorized - Invalid Token(Login again)");
        }
    }

    private static String getUserNameFromToken(UserType role, String token) {
        try {
            return validateToken(role.name(), token.replace("Bearer", "").trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
