package com.travellust.service;

import com.travellust.dto.SignUpRequest;
import com.travellust.model.User;

public interface UserService {
    User registerNewUser(SignUpRequest signUpRequest);
    
    boolean existsByEmail(String email);
    
    User findByEmail(String email);
} 