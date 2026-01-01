package com.rupesh.DevSpace.DTO;

import com.rupesh.DevSpace.Type.AuthProvider;
import com.rupesh.DevSpace.Type.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String email;
    private String password;
    private Role role;
    private AuthProvider provider;
    private String providerId;
}
