package com.app.entity;

import java.sql.Timestamp;

public class Loan {


    private Integer id;
    private String userName;
    private Long amount;
    private LoanType loanType;
    private String status;

    public Loan() {
    }

    private Timestamp createdDate;



    public Loan(Integer id, String userName, Long amount, LoanType loanType, String status, Timestamp createdDate) {
        this.id = id;
        this.userName = userName;
        this.amount = amount;
        this.loanType = loanType;
        this.status = status;
        this.createdDate = createdDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
