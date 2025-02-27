package com.app.service;

import com.app.dao.DataBaseRepository;
import com.app.entity.User;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class UserService {

    DataBaseRepository repository=new DataBaseRepository();


    public void registerUser(JSONObject jsonBody) {

        User user=new User();
        user.setUserName( jsonBody.getString("userName"));
        user.setPassWord(jsonBody.getString("password"));
        user.setRole(jsonBody.getString("role"));
        user.setName(jsonBody.getString("name"));

        repository.saveUser(user);


    }

    public User getUser(String userName) {
        User user= repository.getUser(userName);
        String decodedPassword= new String(Base64.getDecoder().decode(user.getPassWord()));
        user.setPassWord(decodedPassword);
        return user;
    }
}
