package com.app.test.service;

import com.app.entity.Loan;
import com.app.entity.LoanStatus;
import com.app.entity.LoanType;
import com.app.repository.LoanRepository;
import com.app.service.LoanService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    private LoanRepository loanRepository;
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        loanRepository = Mockito.mock(LoanRepository.class);
        loanService = new LoanService(loanRepository);
    }

    @Test
    void testSaveLoanDetails_Success() {
        JSONObject json = new JSONObject();
        json.put("username", "john_doe");
        json.put("amount", 5000);
        json.put("loanType", "PERSONAL");

        when(loanRepository.saveLoan(any(Loan.class))).thenReturn(true);

        boolean result = loanService.saveLoanDetails(json);
        assertTrue(result);

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).saveLoan(loanCaptor.capture());

        Loan savedLoan = loanCaptor.getValue();
        assertEquals("john_doe", savedLoan.getUserName());
        assertEquals(5000, savedLoan.getAmount());
        assertEquals(LoanType.PERSONAL, savedLoan.getLoanType());
        assertEquals(LoanStatus.PENDING.name(), savedLoan.getStatus());
    }

    @Test
    void testSaveLoanDetails_InvalidData() {
        JSONObject json = new JSONObject();
        json.put("username", "john_doe");
        json.put("amount", -5000); // Invalid amount
        json.put("loanType", "INVALID_LOAN_TYPE");

        boolean result = loanService.saveLoanDetails(json);
        assertFalse(result);

    }

    @Test
    void testGetLoansListForUser_Success() {
        Loan loan1 = new Loan(1, "john_doe", 5000L, LoanType.PERSONAL, LoanStatus.PENDING.name(), new Timestamp(System.currentTimeMillis()));
        Loan loan2 = new Loan(2, "john_doe", 10000L, LoanType.BUSINESS, LoanStatus.APPROVED.name(), new Timestamp(System.currentTimeMillis()));

        when(loanRepository.getLoanListByUserName("john_doe")).thenReturn(Arrays.asList(loan1, loan2));

        List<JSONObject> loans = loanService.getLoansListForUser("john_doe");

        assertEquals(2, loans.size());
        assertEquals(5000, loans.get(0).getLong("amount"));
        assertEquals("PENDING", loans.get(0).getString("status"));
        assertEquals(10000, loans.get(1).getLong("amount"));
        assertEquals("APPROVED", loans.get(1).getString("status"));
    }

    @Test
    void testGetLoansList_Success() {
        Loan loan1 = new Loan(1, "john_doe", 5000L, LoanType.PERSONAL, LoanStatus.PENDING.name(), new Timestamp(System.currentTimeMillis()));
        Loan loan2 = new Loan(2, "jane_doe", 8000L, LoanType.BUSINESS, LoanStatus.REJECTED.name(), new Timestamp(System.currentTimeMillis()));

        when(loanRepository.getLoanList()).thenReturn(Arrays.asList(loan1, loan2));

        List<JSONObject> loans = loanService.getLoansList();

        assertEquals(2, loans.size());
        assertEquals(5000, loans.get(0).getLong("amount"));
        assertEquals("PENDING", loans.get(0).getString("status"));
        assertEquals(8000, loans.get(1).getLong("amount"));
        assertEquals("REJECTED", loans.get(1).getString("status"));
    }

    @Test
    void testApproveLoan_Success() {
        JSONObject json = new JSONObject();
        json.put("loanId", 1);

        when(loanRepository.updateLoanStatus(1, LoanStatus.APPROVED)).thenReturn(true);

        boolean result = loanService.approveLoan(json);
        assertTrue(result);

        verify(loanRepository).updateLoanStatus(1, LoanStatus.APPROVED);
    }

    @Test
    void testApproveLoan_InvalidLoanId() {
        JSONObject json = new JSONObject();
        json.put("loanId", -1);

        boolean result = loanService.approveLoan(json);
        assertFalse(result);

    }

    @Test
    void testRejectLoan_Success() {
        JSONObject json = new JSONObject();
        json.put("loanId", 2);

        when(loanRepository.updateLoanStatus(2, LoanStatus.REJECTED)).thenReturn(true);

        boolean result = loanService.rejectLoan(json);
        assertTrue(result);

        verify(loanRepository).updateLoanStatus(2, LoanStatus.REJECTED);
    }

    @Test
    void testRejectLoan_InvalidLoanId() {
        JSONObject json = new JSONObject();
        json.put("loanId", 0); // Invalid ID

        boolean result = loanService.rejectLoan(json);
        assertFalse(result);

    }
}
