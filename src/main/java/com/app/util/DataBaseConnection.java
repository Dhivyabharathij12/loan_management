package com.app.util;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseConnection
{

    private static final String URL ="jdbc:postgresql://localhost:5432/mydatabase?currentSchema=public";
    private static final String USER = "postgres";
    private static final String PASSWORD = "dhivya93";
    private static Connection conn=null;
    private Statement stmt;

    public static Connection getDbConnection(){
        if(conn == null){
            try {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return conn;
    }

}