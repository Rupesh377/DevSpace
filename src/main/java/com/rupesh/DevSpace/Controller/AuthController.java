package com.rupesh.DevSpace.Controller;

import com.rupesh.DevSpace.DTO.LoginRequestDTO;
import com.rupesh.DevSpace.DTO.LoginResponseDTO;
import com.rupesh.DevSpace.DTO.SignupRequestDTO;
import com.rupesh.DevSpace.DTO.SignupResponseDTO;
import com.rupesh.DevSpace.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> sign(@RequestBody SignupRequestDTO signupRequestDTO)
    {
        return ResponseEntity.ok(authService.signup(signupRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO)
    {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }
}
