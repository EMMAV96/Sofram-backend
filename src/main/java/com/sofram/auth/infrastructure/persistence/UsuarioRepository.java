package com.sofram.auth.infrastructure.persistence;

import com.sofram.auth.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmpleadoId(Long empleadoId);

    boolean existsByUsername(String username);

    boolean existsByEmpleadoId(Long empleadoId);

    @Query("""
            select count(usuario) > 0
            from Usuario usuario
            where usuario.rol.nombre = :nombre
            """)
    boolean existsByRolNombre(@Param("nombre") String nombre);

    @Query("""
            select count(usuario)
            from Usuario usuario
            where usuario.rol.nombre = :nombre
            """)
    long countByRolNombre(@Param("nombre") String nombre);
}
