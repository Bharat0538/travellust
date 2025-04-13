package com.travellust.config;

import com.travellust.model.AuthProvider;
import com.travellust.model.Role;
import com.travellust.model.RoleType;
import com.travellust.model.User;
import com.travellust.repository.RoleRepository;
import com.travellust.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void initUsers() {
        if (userRepository.count() == 0) {
            createDefaultRoles();
            createDefaultUser();
        }
    }

    private void createDefaultRoles() {
        if (roleRepository.findByName(RoleType.ROLE_USER).isEmpty()) {
            Role userRole = new Role();
            userRole.setName(RoleType.ROLE_USER);
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName(RoleType.ROLE_ADMIN).isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName(RoleType.ROLE_ADMIN);
            roleRepository.save(adminRole);
        }
    }

    private void createDefaultUser() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setProvider(AuthProvider.LOCAL);
        user.setEnabled(true);
        
        // Set user role
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
        roles.add(userRole);
        
        // Also add admin role for test user
        Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found."));
        roles.add(adminRole);
        
        user.setRoles(roles);
        
        userRepository.save(user);
        
        // Create a second user
        User user2 = new User();
        user2.setFirstName("Guest");
        user2.setLastName("User");
        user2.setEmail("guest@example.com");
        user2.setPassword(passwordEncoder.encode("guest123"));
        user2.setProvider(AuthProvider.LOCAL);
        user2.setEnabled(true);
        
        // Set user role only
        Set<Role> guestRoles = new HashSet<>();
        guestRoles.add(userRole);
        user2.setRoles(guestRoles);
        
        userRepository.save(user2);
    }
} 