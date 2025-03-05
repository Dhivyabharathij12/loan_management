package com.app;

import com.app.controller.LoanController;
import com.app.controller.UserController;
import com.app.service.AuthMiddleware;
import com.app.util.DataBaseConnection;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoanApp {
    public static void main(String[] args) {
        startApp(7777);
    }
    public static Javalin startApp(int port) {

        Javalin app = Javalin.create().start(port);
        dataBaseInit();

        UserController userController=new UserController();
        LoanController loanController=new LoanController();

        app.routes(() -> {
            ApiBuilder.path("/register", () -> {
                ApiBuilder.post("/", userController::register);
            });
            ApiBuilder.path("/auth", () -> {
                ApiBuilder.post("/login", userController::login);
            });

            ApiBuilder.path("user/loans", () -> {
                ApiBuilder.before(AuthMiddleware::authenticate);
                ApiBuilder.post("/apply",loanController::applyLoan);
                ApiBuilder.get("/all", loanController::getLoans);
            });
            ApiBuilder.path("manager/loans", () -> {
                ApiBuilder.before(AuthMiddleware::authenticate);
                ApiBuilder.get("/all", loanController::getAllLoans);
                ApiBuilder.put("/approve", loanController::approveLoan);
                ApiBuilder.put("/reject", loanController::rejectLoan);
            });

        });
        return app;
    }

    public static void dataBaseInit() {

        try  {
            Connection connection = DataBaseConnection.getDbConnection();
            String userTable = "CREATE TABLE IF NOT EXISTS users ( id SERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL,username VARCHAR(50) UNIQUE NOT NULL, password VARCHAR(255) NOT NULL, role VARCHAR(20) NOT NULL);";

            String loanTable = "CREATE TABLE IF NOT EXISTS loans (id SERIAL PRIMARY KEY, user_id INT REFERENCES users(id), amount DECIMAL(10,2) NOT NULL, loan_type VARCHAR(50) NOT NULL, status VARCHAR(20) DEFAULT 'pending', created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

            PreparedStatement userTableStatement = connection.prepareStatement(userTable);
            PreparedStatement loanTableStatement = connection.prepareStatement(loanTable);

            userTableStatement.executeUpdate();
            loanTableStatement.executeUpdate();
            System.out.println("Table created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
