package com.rupesh.DevSpace.Service;

import com.rupesh.DevSpace.DTO.LoginRequestDTO;
import com.rupesh.DevSpace.DTO.LoginResponseDTO;
import com.rupesh.DevSpace.DTO.SignupRequestDTO;
import com.rupesh.DevSpace.DTO.SignupResponseDTO;
import com.rupesh.DevSpace.Entity.User;
import com.rupesh.DevSpace.Repository.AuthRepository;
import com.rupesh.DevSpace.Security.AuthUtil;
import com.rupesh.DevSpace.Type.AuthProvider;
import com.rupesh.DevSpace.Type.Role;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static com.rupesh.DevSpace.Type.AuthProvider.LOCAL;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final AuthUtil authUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthRepository authRepository, AuthUtil authUtil, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.authUtil = authUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }


    public User signUpInternal(SignupRequestDTO signupRequestDto, AuthProvider authProvider, String providerId) {
        User user = authRepository.findByEmail(signupRequestDto.getEmail()).orElse(null);

        if(user != null) throw new IllegalArgumentException("User already exists");

        user = User.builder()
                .email(signupRequestDto.getEmail())
                .providerId(providerId)
                .provider(authProvider)
                .role(Role.USER)
                .build();

        if(authProvider == LOCAL) {
            user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        }
        return authRepository.save(user);
    }
    public SignupResponseDTO signup(SignupRequestDTO signupRequestDTO) {
        User user= signUpInternal(signupRequestDTO , LOCAL , null);

        return new SignupResponseDTO(user.getEmail());
    }



    @Transactional
    public ResponseEntity<LoginResponseDTO> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {

        AuthProvider providerType=authUtil.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User , registrationId);

        User user= authRepository.findByProviderAndProviderId(  providerType , providerId).orElse(null);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        User emailUser = authRepository.findByEmail(email).orElse(null);


        if(user ==null && emailUser == null)
        {
            String username = authUtil.determineProviderIdFromOAuth2User(oAuth2User , registrationId );
            user = signUpInternal(new SignupRequestDTO(username , null), providerType , providerId);
        } else if (user != null) {
            if (email != null && !email.isBlank() && !email.equals(user.getEmail())) {
                user.setEmail(email);
                authRepository.save(user);
            }
        }else {
            throw new BadCredentialsException("This email is already registered with provided " + email);
        }
        LoginResponseDTO loginResponseDTO=
                new LoginResponseDTO(authUtil.generateToken(user) /*, user.getId()*/, user.getRole().toString());
        return ResponseEntity.ok(loginResponseDTO);
    }



    public  LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        User user = authRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(user.getProvider() != null && user.getProvider() != LOCAL) {
            throw new RuntimeException("This email is registered via " + user.getProvider() + ". Use OAuth login.");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

        String token = authUtil.generateToken(user);

        return new LoginResponseDTO(token,/* user.getId(),*/ user.getRole().name());
    }

}
