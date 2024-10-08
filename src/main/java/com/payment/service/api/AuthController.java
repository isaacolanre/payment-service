package com.payment.service.api;

import com.payment.service.dto.AppUserBasicProjectionDto;
import com.payment.service.dto.request.LoginRequestDto;
import com.payment.service.dto.request.UserSignupRequest;
import com.payment.service.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/customer/login")
    public ResponseEntity<Void> loginUser(@RequestBody LoginRequestDto loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
