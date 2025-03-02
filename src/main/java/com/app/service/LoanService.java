package com.app.service;

import com.app.entity.Loan;
import com.app.entity.LoanStatus;
import com.app.entity.LoanType;
import com.app.repository.LoanRepository;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LoanService {
   private final LoanRepository loanRepository=new LoanRepository();


    public boolean saveLoanDetails(JSONObject json) {

        String userName= json.getString("username");
        Long amount= json.getLong("amount");
        LoanType loanType= LoanType.getLoanName(json.getString("loanType"));
        json.put("status", "pending");

        Loan loan=new Loan();
        loan.setUserName(userName);
        loan.setAmount(amount);
        loan.setStatus(LoanStatus.PENDING.name());
        loan.setLoanType(loanType);
        loan.setCreatedDate(new Timestamp(System.currentTimeMillis()));

       return loanRepository.saveLoan(loan);
    }

    public List<JSONObject> getLoansListForUser(JSONObject json) {
        String userName= json.getString("username");
        List<Loan> loanList= loanRepository.getLoanListByUserName(userName);
        List<JSONObject> loanObjects=new ArrayList<>();

        for(Loan loan:loanList){
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userName",loan.getUserName());
            jsonObj.put("amount",loan.getAmount());
            jsonObj.put("loanType",loan.getLoanType());
            jsonObj.put("status",loan.getStatus());
            jsonObj.put("createdDate",loan.getCreatedDate());

            loanObjects.add(jsonObj);
        }

        return loanObjects;
    }
}
