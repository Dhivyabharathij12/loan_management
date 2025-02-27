package com.app.controller;

import com.app.entity.User;
import com.app.service.UserService;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class UserController {
    UserService userService =new UserService();

    public void login(@NotNull Context context) {

        String body= context.body();
        JSONObject jsonBody=new JSONObject(body);
        String userName= jsonBody.getString("username");
        String password= jsonBody.getString("password");
        User user=userService.getUser(userName);

        if(user.getPassWord().equals(password)){
            context.status(200).json(new JSONObject().put("message", "Login successful").put("token", "dummy-token").toString());
        } else {
            context.status(401).json(new JSONObject().put("message", "Invalid credentials").toString());
        }

}

    public void register(@NotNull Context context) {
        String body= context.body();
        JSONObject jsonBody=new JSONObject(body);
        userService.registerUser(jsonBody);
    }
    }
