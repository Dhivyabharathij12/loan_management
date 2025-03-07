package com.app.controller;

import com.app.entity.User;
import com.app.service.UserService;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import static com.app.util.HashUtil.verifyPassword;
import static com.app.util.TokenUtil.generateToken;

public class UserController {
    private final UserService userService;

    // Constructor for dependency injection
    public UserController(UserService userService) {
        this.userService = userService;
    }
    public void login(@NotNull Context context) {

        String body= context.body();
        JSONObject jsonBody=new JSONObject(body);
        String userName= jsonBody.getString("username");
        String password= jsonBody.getString("password");
        User user=userService.getUser(userName);

        if(null!=user && verifyPassword(password, user.getPassWord())){
            context.status(200).json(new JSONObject().put("message", "Login successfully").put("token", generateToken(user.getUserName(), user.getRole())).toString());
        } else {
            context.status(401).json(new JSONObject().put("message", "Invalid credentials").toString());
        }

}

    public void register(@NotNull Context context) {
        String body= context.body();
        JSONObject jsonBody=new JSONObject(body);
        boolean isUserSaved=userService.registerUser(jsonBody);

        if(isUserSaved){
            context.status(200).json(new JSONObject().put("message", "User registered successful").toString());
        } else {
            context.status(401).json(new JSONObject().put("message", "Error happened, User not registered").toString());
        }
    }
    }
