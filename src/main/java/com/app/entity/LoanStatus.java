package com.app.entity;

public enum LoanStatus {
    PENDING("Loan application submitted, awaiting approval"),
    APPROVED("Loan application approved, funds disbursed soon"),
    REJECTED("Loan application was not approved");

    private final String description;

    LoanStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
