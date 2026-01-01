package com.rupesh.DevSpace.Security;

import com.rupesh.DevSpace.Entity.User;
import com.rupesh.DevSpace.Repository.AuthRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final AuthRepository authRepository;

    public CustomUserDetailService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user= authRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("User not found with email "+email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
