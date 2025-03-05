package com.app.entity;

public class User {

    private Integer id;
    private String name;
    private String role;
    private String userName;
    private String passWord;

    public User() {}

    // Constructor with all fields
    public User(Integer id, String name, String role, String username, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.userName = username;
        this.passWord = password;
    }

    // Constructor for testing purposes (without id & name)
    public User(String username, String password, String role) {
        this.userName = username;
        this.passWord = password;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
