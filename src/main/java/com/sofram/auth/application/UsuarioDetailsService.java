package com.sofram.auth.application;

import com.sofram.auth.infrastructure.persistence.UsuarioRepository;
import com.sofram.personal.infrastructure.persistence.EmpleadoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final EmpleadoRepository empleadoRepository;

    public UsuarioDetailsService(
            UsuarioRepository usuarioRepository,
            EmpleadoRepository empleadoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(usuario -> {
                    boolean empleadoActivo = empleadoRepository
                            .findById(usuario.getEmpleadoId())
                            .map(empleado -> empleado.isActivo())
                            .orElse(false);

                    return new UsuarioPrincipal(
                            usuario,
                            empleadoActivo
                    );
                })
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario no encontrado"
                        )
                );
    }
}
