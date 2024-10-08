package com.payment.service.api;

import com.payment.service.dto.request.DepositRequest;
import com.payment.service.models.Account;
import com.payment.service.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<String> addMoneyToAccount(@PathVariable Long accountId, @RequestBody DepositRequest depositRequest) {
        try {
            accountService.addMoneyToAccount(accountId, depositRequest.getAmount());
            return ResponseEntity.ok("Money added successfully to account balance.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add money to the account.");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable UUID userId) {
        List<Account> accounts = accountService.getAccountsByUserId(userId);
        if (accounts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accounts);
    }
}
