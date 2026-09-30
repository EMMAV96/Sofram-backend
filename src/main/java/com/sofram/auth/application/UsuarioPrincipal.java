package com.sofram.auth.application;

import com.sofram.auth.domain.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioPrincipal implements UserDetails {

    private final Long id;
    private final Long empleadoId;
    private final String username;
    private final String password;
    private final boolean activo;
    private final String rol;

    public UsuarioPrincipal(Usuario usuario) {
        this.id = usuario.getId();
        this.empleadoId = usuario.getEmpleadoId();
        this.username = usuario.getUsername();
        this.password = usuario.getPasswordHash();
        this.activo = usuario.isActivo();
        this.rol = usuario.getRol().getNombre();
    }

    public Long getId() {
        return id;
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public String getRol() {
        return rol;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }
}
