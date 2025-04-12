package com.travellust.config;

import com.travellust.model.Role;
import com.travellust.model.RoleType;
import com.travellust.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DatabaseInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        // Initialize roles
        initRoles();
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            // Create user roles
            Role userRole = new Role(RoleType.ROLE_USER);
            Role adminRole = new Role(RoleType.ROLE_ADMIN);

            roleRepository.save(userRole);
            roleRepository.save(adminRole);
        }
    }
} 