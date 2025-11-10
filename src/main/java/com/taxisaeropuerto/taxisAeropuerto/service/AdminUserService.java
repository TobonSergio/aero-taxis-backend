package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.dto.*;
import com.taxisaeropuerto.taxisAeropuerto.entity.Chofer;
import com.taxisaeropuerto.taxisAeropuerto.entity.Rol;
import com.taxisaeropuerto.taxisAeropuerto.entity.Staff;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.ChoferRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.RolRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.StaffRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final ChoferRepository choferRepository;
    private final EmailService emailService; // 🔹 aquí

    public Staff createStaffByAdmin(AdminCreateStaffRequest request) {
        // Validar rol permitido: ADMIN (1) o STAFF (2)
        if (request.getRolId() != 1L && request.getRolId() != 2L) {
            throw new RuntimeException("Solo se puede crear usuario con rol ADMIN (1) o STAFF (2)");
        }

        // Verificar que no exista el correo
        if (userRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // Crear usuario
        User user = new User();
        user.setUsername(request.getUsername());
        user.setCorreo(request.getCorreo());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true); // 🔹 la cuenta queda inactiva hasta verificar

        // Asignar rol
        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        user.setRol(rol);

        // 🔹 Generar token de verificación
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);

        // Guardar usuario
        userRepository.save(user);

        // Crear registro Staff (ADMIN o STAFF van en la misma tabla)
        Staff staff = new Staff();
        staff.setNombre(request.getNombre());
        staff.setApellido(request.getApellido());
        staff.setCorreo(request.getCorreo());
        staff.setTelefono(request.getTelefono());
        staff.setCargo(request.getCargo()); // Por ejemplo: "Administrador" o "Staff"
        staff.setUsuario(user);
        staffRepository.save(staff);

        // 🔹 Enviar correo de verificación
        //emailService.sendUsuarioVerificationEmail(user.getCorreo(), token);

        return staff;
    }
    // 🔹 Obtener todos los staff
    // 🔹 Obtener todos los staff como DTO limpio
    public List<StaffResponse> StaffResponse() {
        List<Staff> staffList = staffRepository.findAll();
        return staffList.stream()
                .map(s -> new StaffResponse(
                        s.getIdStaff(),
                        s.getNombre(),
                        s.getApellido(),
                        s.getCorreo(),
                        s.getTelefono(),
                        s.getCargo(),
                        s.getUsuario().getUsername(),
                        s.getUsuario().getRol().getNombre()
                ))
                .collect(Collectors.toList());
    }


    // 🔹 Obtener staff por ID
    public Staff getStaffById(Integer id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff no encontrado"));
    }

    // 🔹 Actualizar staff (y su usuario asociado)
    public Staff updateStaffByAdmin(Integer id, AdminCreateStaffRequest request) {
        Staff staff = getStaffById(id);
        User user = staff.getUsuario();

        user.setUsername(request.getUsername());
        user.setCorreo(request.getCorreo());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        user.setRol(rol);

        userRepository.save(user);

        staff.setNombre(request.getNombre());
        staff.setApellido(request.getApellido());
        staff.setCorreo(request.getCorreo());
        staff.setTelefono(request.getTelefono());
        staff.setCargo(request.getCargo());
        staffRepository.save(staff);

        return staff;
    }

    // 🔹 Eliminar staff (y su usuario)
    public void deleteStaffByAdmin(Integer id) {
        Staff staff = getStaffById(id);
        User user = staff.getUsuario();

        staffRepository.delete(staff);
        if (user != null) {
            userRepository.delete(user);
        }
    }


    // 🔹 Obtener perfil del staff autenticado
    public StaffProfileResponse getProfileByCorreo(String correo) {
        Staff staff = staffRepository.findByUsuario_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Staff no encontrado"));

        StaffProfileResponse response = new StaffProfileResponse();
        response.setIdStaff(staff.getIdStaff());
        response.setNombre(staff.getNombre());
        response.setApellido(staff.getApellido());
        response.setCorreo(staff.getCorreo());
        response.setTelefono(staff.getTelefono());
        response.setCargo(staff.getCargo());
        response.setIdUsuario(staff.getUsuario().getId());
        response.setUsername(staff.getUsuario().getUsername());
        response.setRolName(
                staff.getUsuario().getRol() != null
                        ? staff.getUsuario().getRol().getNombre()
                        : "Sin rol"
        );

        return response;
    }


}
