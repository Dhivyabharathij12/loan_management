package com.app.test.repository;

import com.app.entity.Loan;
import com.app.entity.LoanType;
import com.app.entity.User;
import com.app.repository.LoanRepository;
import com.app.repository.UserRepository;
import com.app.util.DataBaseConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LoanRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private LoanRepository loanRepository;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize mocks before each test
        // No need for initMocks as MockitoExtension handles that
    }

    @Test
    void testGetLoanListByUserName_Success() throws Exception {
        // Given: Prepare mock data
        String userName = "john_doe";
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUserName(userName);

        Loan mockLoan = new Loan();
        mockLoan.setId(1);
        mockLoan.setUserName(userName);
        mockLoan.setAmount(10000L);
        mockLoan.setLoanType(LoanType.PERSONAL);
        mockLoan.setStatus("pending");

        // Mock behavior of UserRepository
        when(userRepository.getUser(userName)).thenReturn(mockUser);

        try (MockedStatic<DataBaseConnection> mockedStatic = mockStatic(DataBaseConnection.class)) {
            mockedStatic.when(DataBaseConnection::getDbConnection).thenReturn(connection);
        }
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Mock ResultSet behavior
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(mockLoan.getId());
        when(resultSet.getString("userName")).thenReturn(mockLoan.getUserName());
        when(resultSet.getLong("amount")).thenReturn(mockLoan.getAmount());
        when(resultSet.getString("loan_type")).thenReturn(mockLoan.getLoanType().name());
        when(resultSet.getString("status")).thenReturn(mockLoan.getStatus());
        when(resultSet.getTimestamp("created_date")).thenReturn(mockLoan.getCreatedDate());

        // When: Call the method under test
        List<Loan> loans = loanRepository.getLoanListByUserName(userName);

        // Then: Verify the result
        assertNotNull(loans);
        assertEquals(1, loans.size());
        assertEquals(mockLoan.getId(), loans.get(0).getId());
        assertEquals(mockLoan.getUserName(), loans.get(0).getUserName());
        assertEquals(mockLoan.getAmount(), loans.get(0).getAmount());
        assertEquals(mockLoan.getLoanType(), loans.get(0).getLoanType());
        assertEquals(mockLoan.getStatus(), loans.get(0).getStatus());

        // Verify interactions
        verify(userRepository).getUser(userName);
        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
    }

    @Test
    void testGetLoanListByUserName_UserNotFound() throws Exception {
        // Given: User does not exist
        String userName = "non_existent_user";

        when(userRepository.getUser(userName)).thenReturn(null);

        // When: Call the method under test
        List<Loan> loans = loanRepository.getLoanListByUserName(userName);

        // Then: Verify the result
        assertNotNull(loans);
        assertTrue(loans.isEmpty());

        // Verify interactions
        verify(userRepository).getUser(userName);
        verifyNoMoreInteractions(connection, preparedStatement);
    }

    @Test
    void testGetLoanListByUserName_SqlException() throws Exception {
        // Given: Simulate a SQLException
        String userName = "john_doe";
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUserName(userName);

        when(userRepository.getUser(userName)).thenReturn(mockUser);
        try (MockedStatic<DataBaseConnection> mockedStatic = mockStatic(DataBaseConnection.class)) {
            mockedStatic.when(DataBaseConnection::getDbConnection).thenReturn(connection);
        }

        // When: Call the method under test
        List<Loan> loans = loanRepository.getLoanListByUserName(userName);

        // Then: Verify the result
        assertNotNull(loans);
        assertTrue(loans.isEmpty());

        // Verify interactions
        verify(userRepository).getUser(userName);
    }
}
