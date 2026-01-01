package com.rupesh.DevSpace.Config;

import com.rupesh.DevSpace.Entity.User;
import com.rupesh.DevSpace.Repository.AuthRepository;
import com.rupesh.DevSpace.Type.AuthProvider;
import com.rupesh.DevSpace.Type.Role;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminCreating {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.pass}")
    private String adminPassword;

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createAdminIfNotExists() {

        if (authRepository.existsByEmail(adminEmail)) {
            return;
        }

        User admin = User.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .provider(AuthProvider.LOCAL)
                .build();

        authRepository.save(admin);
        System.out.println("✅ Admin user created");
    }
}
