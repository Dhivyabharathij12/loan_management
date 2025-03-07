package com.app.entity;

public enum LoanType {
    PERSONAL("Personal Loan", "Unsecured loan for personal use"),
    HOME("Home Loan", "Loan for purchasing a house"),
    AUTO("Auto Loan", "Loan for buying a vehicle"),
    BUSINESS("Business Loan", "Loan for business needs"),
    STUDENT("Student Loan", "Loan for educational expenses"),
    AGRICULTURAL("Agricultural Loan", "Loan for farmers and agricultural purposes");

    private final String displayName;
    private final String description;

    LoanType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static LoanType getLoanName(String name){
        try {
            LoanType loanName = LoanType.valueOf(name);
            return loanName;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}

