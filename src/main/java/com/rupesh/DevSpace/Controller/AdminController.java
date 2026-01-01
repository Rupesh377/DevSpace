package com.rupesh.DevSpace.Controller;

import com.rupesh.DevSpace.DTO.UserDTO;
import com.rupesh.DevSpace.Entity.User;
import com.rupesh.DevSpace.Service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getUser(@AuthenticationPrincipal UserDetails user)
    {
        return ResponseEntity.ok(adminService.getAllUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetails user , @PathVariable Long id)
    {
        return ResponseEntity.ok(adminService.deleteUserById(user , id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@AuthenticationPrincipal UserDetails user , @PathVariable Long id)
    {
        return ResponseEntity.ok(adminService.getUserById(user , id));
    }
}
