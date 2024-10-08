package com.payment.service.config.security;

import com.payment.service.exceptions.PasswordStrengthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.regex.Pattern;

@Component
public class PasswordValidator {

    private final int minimumLength;

    private final boolean uppercase;

    private final boolean numeric;

    private final boolean specialCharacters;

    Pattern letter = Pattern.compile("[a-zA-z]");
    Pattern digit = Pattern.compile("[0-9]");
    Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");


    public PasswordValidator(@Value("${password.validation.min-length}") int minimumLength,
                             @Value("${password.validation.uppercase}") boolean uppercase,
                             @Value("${password.validation.numeric}") boolean numeric,
                             @Value("${password.validation.special-characters}") boolean specialCharacters) {
        this.minimumLength = minimumLength;
        this.uppercase = uppercase;
        this.numeric = numeric;
        this.specialCharacters = specialCharacters;
    }

    public void verifyPassword(String password) {

        var errors = new ArrayList<String>();

        if (password.length() < minimumLength)
            errors.add("Password can not be less than 8 characters");

        if (uppercase && !letter.matcher(password).find())
            errors.add("Password must contain uppercase and lowercase characters");

        if (numeric && !digit.matcher(password).find())
            errors.add("Password must contain at least one number");

        if (specialCharacters && !special.matcher(password).find())
            errors.add("Password must contain at least one special character");

        if (!errors.isEmpty())
            throw new PasswordStrengthException("Password doesn't meet minimum strength requirements", errors);
    }

}
