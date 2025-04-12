package com.travellust.repository;

import com.travellust.model.AuthProvider;
import com.travellust.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    Boolean existsByEmail(String email);
    
    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);
} 