package com.payment.service.api;

import com.payment.service.dto.AppUserBasicProjectionDto;
import com.payment.service.dto.request.LoginRequestDto;
import com.payment.service.dto.request.UserSignupRequest;
import com.payment.service.models.AppUser;
import com.payment.service.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/signup")
    public ResponseEntity<AppUserBasicProjectionDto> registerUser(@Valid @RequestBody UserSignupRequest userSignupRequest) {
        try {
            AppUserBasicProjectionDto registeredUser = userService.registerUser(userSignupRequest);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<AppUserBasicProjectionDto> getUserByEmail(@PathVariable String email) {
        Optional<AppUserBasicProjectionDto> userDto = userService.getUserByEmail(email);
        return userDto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
