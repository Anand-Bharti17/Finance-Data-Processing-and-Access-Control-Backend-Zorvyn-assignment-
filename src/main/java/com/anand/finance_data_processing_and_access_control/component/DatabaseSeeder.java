package com.anand.finance_data_processing_and_access_control.component;

import com.anand.finance_data_processing_and_access_control.model.entity.Role;
import com.anand.finance_data_processing_and_access_control.model.entity.User;
import com.anand.finance_data_processing_and_access_control.model.enums.RoleType;
import com.anand.finance_data_processing_and_access_control.repository.RoleRepository;
import com.anand.finance_data_processing_and_access_control.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 1. Seed Roles if they don't exist
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, RoleType.ROLE_VIEWER));
            roleRepository.save(new Role(null, RoleType.ROLE_ANALYST));
            roleRepository.save(new Role(null, RoleType.ROLE_ADMIN));
        }

        // 2. Seed an Admin User if it doesn't exist
        if (!userRepository.existsByUsername("admin")) {
            Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN).orElseThrow();

            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123")); // Securely hashes the password
            adminUser.setRoles(Set.of(adminRole));

            userRepository.save(adminUser);
            System.out.println("✅ Default Admin User created: username=admin, password=admin123");
        }
    }
}