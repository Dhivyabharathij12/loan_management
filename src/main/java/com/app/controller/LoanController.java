package com.app.controller;

import com.app.entity.Loan;
import com.app.entity.LoanType;
import com.app.service.LoanService;
import io.javalin.http.Context;
import org.eclipse.jetty.util.StringUtil;
import org.json.JSONObject;
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
        String userName= ctx.queryParam("username");
        List<JSONObject> loanList = loanService.getLoansListForUser(userName);
        ctx.status(200).json(loanList.toString());
    }

    public void approveLoan(Context ctx) {
        String body= ctx.body();
        JSONObject jsonBody = new JSONObject(body);
        boolean isApproved=loanService.approveLoan(jsonBody);
        if (isApproved) {
            ctx.status(200).json(new JSONObject().put("message", "Loan Approved"));
        } else {
            ctx.status(404).json(new JSONObject().put("message", "Loan not Approved"));
        }
    }

    public void rejectLoan(Context ctx) {
        String body= ctx.body();
        JSONObject jsonBody = new JSONObject(body);
        boolean isRejected=loanService.approveLoan(jsonBody);
        if (isRejected) {
            ctx.status(200).json(new JSONObject().put("message", "Loan Rejected"));
        } else {
            ctx.status(404).json(new JSONObject().put("message", "Loan not Rejected"));
        }
    }
}

