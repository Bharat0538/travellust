package com.travellust.service;

import com.travellust.dto.SignUpRequest;
import com.travellust.model.User;

public interface UserService {
    User registerNewUser(SignUpRequest signUpRequest);
    
    boolean existsByEmail(String email);
    
    User findByEmail(String email);
    
    /**
     * Get a default user for demo/testing purposes
     * @return A default user from the database
     */
    User getDefaultUser();
} 