package com.app.test.controller;

import com.app.controller.LoanController;
import com.app.service.LoanService;
import io.javalin.http.Context;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class LoanControllerTest {
    private LoanService loanService;
    private LoanController loanController;
    private Context ctx;

    @BeforeEach
    void setUp() {
        loanService = mock(LoanService.class);
        loanController = new LoanController();
        ctx = mock(Context.class);

        // Mock the status and json methods to prevent NullPointerException
        when(ctx.status(anyInt())).thenReturn(ctx);  // return ctx itself for method chaining
        when(ctx.json(any())).thenReturn(ctx);      // return ctx itself for method chaining
    }

    @Test
    void testApplyLoan_Failure() {
        String requestBody = """
            {
                "user_id": 1,
                "amount": 5000,
                "loanType": "PERSONAL",
                "username":"test_username"
            }
        """;
        when(ctx.body()).thenReturn(requestBody);
        when(loanService.saveLoanDetails(any(JSONObject.class))).thenReturn(false);

        loanController.applyLoan(ctx);

        verify(ctx).status(401);
        verify(ctx).json("{\"message\":\"Error happened, Loan not registered\"}");
    }

    @Test
    void testGetLoans() {
        when(ctx.queryParam("username")).thenReturn("johndoe");
        List<JSONObject> mockLoans = List.of(
                new JSONObject().put("id", 1).put("amount", 5000.00),
                new JSONObject().put("id", 2).put("amount", 7000.00)
        );
        when(loanService.getLoansListForUser("johndoe")).thenReturn(mockLoans);

        loanController.getLoans(ctx);

        verify(ctx).status(200);
    }

    @Test
    void testApproveLoan_Failure() {
        String requestBody = """
            {
                "loanId": 1,
                "status": "approved",
                "username":"test_username"
            }
        """;
        when(ctx.body()).thenReturn(requestBody);
        when(loanService.approveLoan(any())).thenReturn(false);

        loanController.approveLoan(ctx);

        verify(ctx).status(404);
        verify(ctx).json("{\"message\":\"Loan not Approved\"}");
    }
}