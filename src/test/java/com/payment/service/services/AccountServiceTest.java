package com.payment.service.services;

import com.payment.service.models.Account;
import com.payment.service.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(100.00));
        userId = UUID.randomUUID();
    }

    @Test
    void testAddMoneyToAccount_Success() throws Exception {
        BigDecimal depositAmount = BigDecimal.valueOf(50.00);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.addMoneyToAccount(1L, depositAmount);

        assertEquals(BigDecimal.valueOf(150.00), account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testAddMoneyToAccount_AccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> accountService.addMoneyToAccount(1L, BigDecimal.valueOf(50.00)));
        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testAddMoneyToAccount_InvalidAmount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.addMoneyToAccount(1L, BigDecimal.valueOf(-10.00)));
        assertEquals("Deposit amount must be greater than zero", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testGetAccountsByUserId_Success() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        when(accountRepository.findByUserPublicId(userId)).thenReturn(accounts);

        List<Account> result = accountService.getAccountsByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(account, result.get(0));
    }

    @Test
    void testGetAccountsByUserId_NoAccounts() {
        when(accountRepository.findByUserPublicId(userId)).thenReturn(new ArrayList<>());

        List<Account> result = accountService.getAccountsByUserId(userId);

        assertTrue(result.isEmpty());
    }
}

