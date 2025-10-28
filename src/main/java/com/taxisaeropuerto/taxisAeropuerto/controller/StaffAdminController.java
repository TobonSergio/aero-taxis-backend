package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.AdminCreateStaffRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.Staff;
import com.taxisaeropuerto.taxisAeropuerto.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/staff")
@RequiredArgsConstructor
public class StaffAdminController {

    private final AdminUserService adminUserService;

    // ✅ Crear un nuevo staff o admin
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createStaff(@Valid @RequestBody AdminCreateStaffRequest request) {
        try {
            Staff staff = adminUserService.createStaffByAdmin(request);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Usuario creado correctamente. Se ha enviado un correo de verificación.",
                    "staffId", staff.getIdStaff(),
                    "userId", staff.getUsuario().getId()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // ✅ Listar todos los staff (incluye admins y staff)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staffList = adminUserService.getAllStaff();
        return ResponseEntity.ok(staffList);
    }

    // ✅ Obtener un staff por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getStaffById(@PathVariable Integer id) {
        try {
            Staff staff = adminUserService.getStaffById(id);
            return ResponseEntity.ok(staff);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // ✅ Actualizar datos de un staff
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStaff(@PathVariable Integer id, @Valid @RequestBody AdminCreateStaffRequest request) {
        try {
            Staff updated = adminUserService.updateStaffByAdmin(id, request);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Usuario actualizado correctamente.",
                    "staffId", updated.getIdStaff()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // ✅ Eliminar un staff
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteStaff(@PathVariable Integer id) {
        try {
            adminUserService.deleteStaffByAdmin(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Usuario eliminado correctamente."
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}
