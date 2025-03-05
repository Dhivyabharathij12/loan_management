package com.app.controller;

import com.app.service.LoanService;
import io.javalin.http.Context;
import org.json.JSONObject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoanController {
    private static final Map<Integer, JSONObject> loans = new ConcurrentHashMap<>();
    LoanService loanService;
    public LoanController(LoanService loanService){
        this.loanService=loanService;
    }
    public void applyLoan(Context ctx) {
        String body = ctx.body();
        JSONObject json = new JSONObject(body);

        boolean isLoanAdded=loanService.saveLoanDetails(json);
        if(isLoanAdded){
            ctx.status(200).json(new JSONObject().put("message", "Loan registered successfully").toString());
        } else {
            ctx.status(401).json(new JSONObject().put("message", "Error happened, Loan not registered").toString());
        }
    }

    public void getLoans(Context ctx) {
        String userName= ctx.queryParam("username");
        List<JSONObject> loanList = loanService.getLoansListForUser(userName);
        ctx.status(200).json(loanList.toString());
    }

    public void getAllLoans(Context ctx) {
        List<JSONObject> loanList = loanService.getLoansList();
        ctx.status(200).json(loanList.toString());
    }

    public void approveLoan(Context ctx) {
        String body= ctx.body();
        JSONObject jsonBody = new JSONObject(body);
        boolean isApproved=loanService.approveLoan(jsonBody);
        if (isApproved) {
            ctx.status(200).json(new JSONObject().put("message", "Loan Approved").toString());
        } else {
            ctx.status(404).json(new JSONObject().put("message", "Loan not Approved").toString());
        }
    }

    public void rejectLoan(Context ctx) {
        String body= ctx.body();
        JSONObject jsonBody = new JSONObject(body);
        boolean isRejected=loanService.rejectLoan(jsonBody);
        if (isRejected) {
            ctx.status(200).json(new JSONObject().put("message", "Loan Rejected").toString());
        } else {
            ctx.status(404).json(new JSONObject().put("message", "Loan not Rejected").toString());
        }
    }
}

