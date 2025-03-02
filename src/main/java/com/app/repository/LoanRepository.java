package com.app.repository;

import com.app.entity.Loan;
import com.app.entity.LoanType;
import com.app.entity.User;
import com.app.util.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LoanRepository {
    public boolean saveLoan(Loan loan) {
        try {
            Connection connection= DataBaseConnection.getDbConnection();

            PreparedStatement loanStatement = connection.prepareStatement("INSERT INTO loans (user_id, amount, loan_type, status, created_date) VALUES (?, ?, ?, ?, ?)");

            loanStatement.setString(1, loan.getUserName());
            loanStatement.setLong(2, loan.getAmount());
            loanStatement.setString(3, loan.getLoanType().name());
            loanStatement.setString(4, loan.getStatus());
            loanStatement.setTimestamp(5, loan.getCreatedDate());


            int loanAdded = loanStatement.executeUpdate();
            if (loanAdded > 0) {
                System.out.println("Loan registered successfully!");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Loan> getLoanListByUserName(String userName) {
        List<Loan> loanList=new ArrayList<>();

        try {
            Connection connection= DataBaseConnection.getDbConnection();

            PreparedStatement loanStatement = connection.prepareStatement("select * from loans where user_id= ?");

            loanStatement.setString(1, userName);

            ResultSet rs = loanStatement.executeQuery();
            while(rs.next()){
                Loan loan = new Loan();
                loan.setUserName(rs.getString("user_id"));
                loan.setAmount(rs.getLong("amount"));
                loan.setStatus(rs.getString("status"));
                loan.setLoanType(LoanType.valueOf(rs.getString("loan_type")));
                loan.setCreatedDate(rs.getTimestamp("created_date"));
                loanList.add(loan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loanList;
    }
}
