package com.rupesh.DevSpace.Repository;

import com.rupesh.DevSpace.Entity.User;
import com.rupesh.DevSpace.Type.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByProviderAndProviderId(AuthProvider authProvider , String providerId);

    boolean existsByEmail(String adminEmail);
}
