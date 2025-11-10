package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.AdminCreateStaffRequest;
import com.taxisaeropuerto.taxisAeropuerto.dto.StaffProfileResponse;
import com.taxisaeropuerto.taxisAeropuerto.entity.Staff;
import com.taxisaeropuerto.taxisAeropuerto.repository.StaffRepository;
import com.taxisaeropuerto.taxisAeropuerto.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffProfileController {

    private final StaffRepository staffRepository;
    private final AdminUserService adminUserService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<StaffProfileResponse> getProfile(Authentication authentication) {
        String correo = authentication.getName(); // correo del JWT
        StaffProfileResponse response = adminUserService.getProfileByCorreo(correo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<?> updateProfile(Authentication authentication,
                                           @RequestBody AdminCreateStaffRequest request) {
        String correo = authentication.getName(); // correo del JWT
        Staff staff = staffRepository.findByUsuario_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Staff no encontrado"));

        // Actualizar solo los campos de Staff
        staff.setNombre(request.getNombre());
        staff.setApellido(request.getApellido());
        staff.setTelefono(request.getTelefono());
        staff.setCargo(request.getCargo());

        staffRepository.save(staff);

        return ResponseEntity.ok(staff);
    }
}
