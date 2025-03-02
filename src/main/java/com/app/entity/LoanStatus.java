package com.app.entity;

public enum LoanStatus {
    PENDING("Loan application submitted, awaiting approval"),
    APPROVED("Loan application approved, funds disbursed soon"),
    REJECTED("Loan application was not approved"),
    ACTIVE("Loan is currently being repaid"),
    CLOSED("Loan has been fully repaid"),
    DEFAULTED("Loan payments have not been made as per schedule"),
    CANCELED("Loan application was withdrawn or canceled");

    private final String description;

    LoanStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
