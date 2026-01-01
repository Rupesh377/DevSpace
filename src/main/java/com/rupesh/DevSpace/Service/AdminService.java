package com.rupesh.DevSpace.Service;

import com.rupesh.DevSpace.DTO.UserDTO;
import com.rupesh.DevSpace.Entity.User;
import com.rupesh.DevSpace.Repository.AuthRepository;
import com.rupesh.DevSpace.Type.Role;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AuthRepository authRepository;
    private final ModelMapper modelMapper;

    public AdminService(AuthRepository authRepository, ModelMapper modelMapper) {
        this.authRepository = authRepository;
        this.modelMapper = modelMapper;
    }

    public List<UserDTO> getAllUser(UserDetails user) {

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return authRepository.findAll().stream()
                    .map(u -> modelMapper.map(u, UserDTO.class))
                    .toList();
        }
        throw new IllegalArgumentException("You are not Admin");
    }


    public  String deleteUserById(UserDetails user, Long id ) {
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new IllegalArgumentException("You are not Admin");
        }

        User user1= authRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));

        if(isAdmin)
            return "Admin can't delete himself";
        authRepository.deleteById(id);
        return "Deleted user with user_id "+id;
    }


    public UserDTO getUserById(UserDetails user, Long id)
    {
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new IllegalArgumentException("You are not Admin");
        }
        User user1 = authRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return modelMapper.map(user1, UserDTO.class);
    }

}
