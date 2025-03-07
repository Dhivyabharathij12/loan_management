package com.app.util;

import static com.app.entity.UserType.MANAGER;

public class JwtUtil {

    // Secret keys for different roles
    public static final String USER_SECRET_KEY = "userSecretKey123";
    public static final String MANAGER_SECRET_KEY = "managerSecretKey123";

    static String getKey(String role){

        if(null != role && MANAGER.name().equals(role.toUpperCase()
        )){
            return MANAGER_SECRET_KEY;
        }
        return USER_SECRET_KEY;
    }
}
