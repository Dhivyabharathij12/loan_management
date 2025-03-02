package com.app.repository;

import com.app.entity.User;
import com.app.util.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository {


    public boolean saveUser(User user) {
        try {
            Connection connection= DataBaseConnection.getDbConnection();

            PreparedStatement userStatement = connection.prepareStatement("INSERT INTO users (name, username, password, role) VALUES (?, ?, ?, ?)");

            userStatement.setString(1, user.getName());
            userStatement.setString(2, user.getUserName());
            userStatement.setString(3, user.getPassWord());
            userStatement.setString(4, user.getRole());

            int userAdded = userStatement.executeUpdate();
            if (userAdded > 0) {
                System.out.println("User registered successfully!");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUser(String userName) {
        try {
            Connection connection= DataBaseConnection.getDbConnection();

            PreparedStatement userStatement = connection.prepareStatement("select * from users where username= ?");

            userStatement.setString(1, userName);

            ResultSet rs = userStatement.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setUserName(rs.getString("username"));
                user.setPassWord(rs.getString("password"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
