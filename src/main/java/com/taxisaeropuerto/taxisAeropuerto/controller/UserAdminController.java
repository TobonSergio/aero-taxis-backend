package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.AdminCreateUserRequest;
import com.taxisaeropuerto.taxisAeropuerto.dto.AdminUpdateUserRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody AdminCreateUserRequest request) {
        User user = userService.createUserByAdmin(request);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody AdminUpdateUserRequest request) {
        User updatedUser = userService.updateUserByAdmin(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserByAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
