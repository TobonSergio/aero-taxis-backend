package com.taxisaeropuerto.taxisAeropuerto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "usuario", unique = true)
    private String username;

    @Column(name = "correo", unique = true)
    private String correo; // 👈 necesario solo para Google Login

    @Column(name = "contraseña")
    private String password;

    @Column(name = "enabled")
    private Boolean enabled = false;

    @Column(name = "verification_token")
    private String verificationToken;

    @ManyToOne
    @JoinColumn(name = "fk_id_rol", referencedColumnName = "id_rol")
    private Rol rol;

    @OneToOne(mappedBy = "usuario")
    private Cliente cliente;

    @OneToOne(mappedBy = "usuario")
    private Staff staff;

    @OneToOne(mappedBy = "usuario")
    private Chofer chofer;

    // 👇 Este método devuelve el perfil correspondiente según el rol
    public Object getPerfil() {
        if (rol != null) {
            switch (rol.getNombre().toUpperCase()) {
                case "CLIENTE":
                    return cliente;
                case "STAFF":
                    return staff;
                case "CHOFER":
                    return chofer;
                default:
                    return null;
            }
        }
        return null;
    }


    // Métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return this.enabled; }
}

