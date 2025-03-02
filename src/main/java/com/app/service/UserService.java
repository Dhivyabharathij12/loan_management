package com.app.service;

import com.app.repository.UserRepository;
import com.app.entity.User;
import org.json.JSONObject;

import static com.app.util.HashUtil.hashPassword;

public class UserService {

    UserRepository repository=new UserRepository();


    public boolean registerUser(JSONObject jsonBody) {

        String hashPassword =hashPassword(jsonBody.getString("password"));

        User user=new User();
        user.setUserName( jsonBody.getString("userName"));
        user.setPassWord(hashPassword);
        user.setRole(jsonBody.getString("role"));
        user.setName(jsonBody.getString("name"));

        return repository.saveUser(user);


    }

    public User getUser(String userName) {
        return repository.getUser(userName);
    }
}
