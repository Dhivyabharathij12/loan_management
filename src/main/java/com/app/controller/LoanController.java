package com.app.controller;

import com.app.entity.Loan;
import com.app.entity.LoanType;
import com.app.service.LoanService;
import io.javalin.http.Context;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoanController {
    private static final Map<Integer, JSONObject> loans = new ConcurrentHashMap<>();
    LoanService loanService=new LoanService();
    public void applyLoan(Context ctx) {
        String body = ctx.body();
        JSONObject json = new JSONObject(body);

        boolean isLoanAdded=loanService.saveLoanDetails(json);
        if(isLoanAdded){
            ctx.status(200).json(new JSONObject().put("message", "Loan registered successful").toString());
        } else {
            ctx.status(401).json(new JSONObject().put("message", "Error happened, Loan not registered").toString());
        }
    }

    public void getLoans(Context ctx) {
        String body = ctx.body();
        JSONObject json = new JSONObject(body);
        List<JSONObject> loanList = loanService.getLoansListForUser(json);
        ctx.status(200).json(loanList);
    }

    public void approveLoan(Context ctx) {
        int loanId = Integer.parseInt(ctx.pathParam("id"));
        if (loans.containsKey(loanId)) {
            loans.get(loanId).put("status", "approved");
            ctx.status(200).json(loans.get(loanId));
        } else {
            ctx.status(404).json(new JSONObject().put("message", "Loan not found"));
        }
    }

    public void rejectLoan(Context ctx) {
        int loanId = Integer.parseInt(ctx.pathParam("id"));
        if (loans.containsKey(loanId)) {
            loans.get(loanId).put("status", "rejected");
            ctx.status(200).json(loans.get(loanId));
        } else {
            ctx.status(404).json(new JSONObject().put("message", "Loan not found"));
        }
    }
}

