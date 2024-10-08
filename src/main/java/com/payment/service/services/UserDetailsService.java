package com.payment.service.services;

import com.payment.service.models.AppUser;

import java.util.UUID;

public interface UserDetailsService {

    AppUser getUserDetailsById(UUID userId);

}
