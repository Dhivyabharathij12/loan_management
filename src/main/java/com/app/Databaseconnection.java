package com.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Databaseconnection
{
    private static final String URL ="jdbc:postgresql://localhost:5432/mydatabase?currentSchema=public";
    private static final String USER = "postgres";
    private static final String PASSWORD = "dhivya93";

    public static void main(String[] args) throws SQLException
    {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement())
        {

            String userTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY, " +
                    "username VARCHAR(100) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "role VARCHAR(20) CHECK (role IN ('user', 'manager')) NOT NULL" +
                    ");";

            String loanTable = "CREATE TABLE IF NOT EXISTS loans (" +
                    "id SERIAL PRIMARY KEY, " +
                    "user_id INT REFERENCES users(id) ON DELETE CASCADE, " +
                    "amount DECIMAL(10,2) NOT NULL, " +
                    "status VARCHAR(20) CHECK (status IN ('pending', 'approved', 'rejected')) DEFAULT 'pending', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";

            stmt.executeUpdate(userTable);
            stmt.executeUpdate(loanTable);
            System.out.println("Table created successfully!");

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}