package com.sofram.auth.application;

import com.sofram.auditoria.application.AuditoriaService;
import com.sofram.auth.domain.Rol;
import com.sofram.auth.domain.Usuario;
import com.sofram.auth.infrastructure.persistence.RolRepository;
import com.sofram.auth.infrastructure.persistence.UsuarioRepository;
import com.sofram.auth.web.dto.ActualizarUsuarioRequest;
import com.sofram.auth.web.dto.CambiarPasswordUsuarioRequest;
import com.sofram.auth.web.dto.CrearUsuarioRequest;
import com.sofram.auth.web.dto.RolResponse;
import com.sofram.auth.web.dto.UsuarioResponse;
import com.sofram.personal.domain.Empleado;
import com.sofram.personal.infrastructure.persistence.EmpleadoRepository;
import com.sofram.shared.exception.BusinessRuleException;
import com.sofram.shared.exception.DuplicateResourceException;
import com.sofram.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class UsuarioAdminService {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_ROL = "ADMINISTRADOR";
    private static final Set<String> ROLES_PERMITIDOS = Set.of(
            "ADMINISTRADOR",
            "ADMINISTRATIVO",
            "MEDICO",
            "PSICOLOGO",
            "LIC_ENFERMERIA",
            "ENFERMERO",
            "TERAPISTA_OCUPACIONAL"
    );

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;

    public UsuarioAdminService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            EmpleadoRepository empleadoRepository,
            PasswordEncoder passwordEncoder,
            AuditoriaService auditoriaService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.empleadoRepository = empleadoRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditoriaService = auditoriaService;
    }

    @Transactional
    public UsuarioResponse crear(CrearUsuarioRequest request) {

        Empleado empleado = empleadoRepository
                .findById(request.empleadoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Empleado no encontrado"
                        )
                );

        if (!empleado.isActivo()) {
            throw new BusinessRuleException(
                    "No se puede crear una cuenta para un empleado dado de baja"
            );
        }

        if (usuarioRepository.existsByEmpleadoId(empleado.getId())) {
            throw new DuplicateResourceException(
                    "El empleado ya tiene una cuenta de acceso"
            );
        }

        String username = normalizarUsername(request.username());
        validarUsernameDisponible(username, null);

        Rol rol = buscarRolPermitido(request.rol());
        String passwordHash =
                passwordEncoder.encode(request.password());

        Usuario usuario = usuarioRepository.save(
                new Usuario(
                        empleado.getId(),
                        rol,
                        username,
                        passwordHash
                )
        );

        auditoriaService.registrar(
                "CREAR_USUARIO",
                "SEGURIDAD",
                "Usuario",
                usuario.getId(),
                "Cuenta creada para empleado " + empleado.getId()
        );

        return toResponse(usuario, empleado);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {

        List<Usuario> usuarios = usuarioRepository.findAll(
                Sort.by("username").ascending()
        );

        Map<Long, Empleado> empleadosPorId = empleadoRepository
                .findAllById(
                        usuarios.stream()
                                .map(Usuario::getEmpleadoId)
                                .toList()
                )
                .stream()
                .collect(Collectors.toMap(
                        Empleado::getId,
                        Function.identity()
                ));

        return usuarios.stream()
                .map(usuario -> toResponse(
                        usuario,
                        empleadosPorId.get(usuario.getEmpleadoId())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorEmpleado(Long empleadoId) {

        empleadoRepository.findById(empleadoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Empleado no encontrado"
                        )
                );

        Usuario usuario = usuarioRepository
                .findByEmpleadoId(empleadoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "El empleado no tiene cuenta de acceso"
                        )
                );

        Empleado empleado = empleadoRepository
                .findById(usuario.getEmpleadoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Empleado no encontrado"
                        )
                );

        return toResponse(usuario, empleado);
    }

    @Transactional
    public UsuarioResponse actualizar(
            Long id,
            ActualizarUsuarioRequest request
    ) {

        Usuario usuario = buscarUsuario(id);

        String username = normalizarUsername(request.username());
        Rol rol = buscarRolPermitido(request.rol());

        validarProteccionAdminBase(usuario, username, rol);
        validarUsernameDisponible(username, usuario.getId());

        usuario.actualizarDatosAcceso(username, rol);

        auditoriaService.registrar(
                "ACTUALIZAR_USUARIO",
                "SEGURIDAD",
                "Usuario",
                usuario.getId(),
                "Cuenta actualizada para empleado " + usuario.getEmpleadoId()
        );

        Empleado empleado = empleadoRepository
                .findById(usuario.getEmpleadoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Empleado no encontrado"
                        )
                );

        return toResponse(usuario, empleado);
    }

    @Transactional
    public UsuarioResponse cambiarPassword(
            Long id,
            CambiarPasswordUsuarioRequest request
    ) {

        Usuario usuario = buscarUsuario(id);

        usuario.cambiarPassword(
                passwordEncoder.encode(request.password())
        );

        auditoriaService.registrar(
                "RESTABLECER_PASSWORD",
                "SEGURIDAD",
                "Usuario",
                usuario.getId(),
                "Password restablecida para usuario " + usuario.getId()
        );

        Empleado empleado = empleadoRepository
                .findById(usuario.getEmpleadoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Empleado no encontrado"
                        )
                );

        return toResponse(usuario, empleado);
    }

    @Transactional(readOnly = true)
    public List<RolResponse> listarRoles() {

        return rolRepository.findAll(Sort.by("nombre").ascending())
                .stream()
                .filter(rol -> ROLES_PERMITIDOS.contains(rol.getNombre()))
                .map(rol -> new RolResponse(
                        rol.getId(),
                        rol.getNombre(),
                        rol.getDescripcion()
                ))
                .toList();
    }

    private Usuario buscarUsuario(Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );
    }

    private Rol buscarRolPermitido(String nombreRol) {

        String nombreNormalizado =
                nombreRol == null ? null : nombreRol.trim().toUpperCase();

        if (!ROLES_PERMITIDOS.contains(nombreNormalizado)) {
            throw new BusinessRuleException(
                    "Rol no permitido"
            );
        }

        return rolRepository.findByNombre(nombreNormalizado)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Rol no encontrado"
                        )
                );
    }

    private String normalizarUsername(String username) {

        return username.trim();
    }

    private void validarUsernameDisponible(
            String username,
            Long usuarioIdActual
    ) {

        usuarioRepository.findByUsername(username)
                .filter(usuario ->
                        !usuario.getId().equals(usuarioIdActual)
                )
                .ifPresent(usuario -> {
                    throw new DuplicateResourceException(
                            "Ya existe una cuenta con ese username"
                    );
                });
    }

    private void validarProteccionAdminBase(
            Usuario usuario,
            String username,
            Rol rol
    ) {

        if (!ADMIN_USERNAME.equals(usuario.getUsername())) {
            return;
        }

        if (!ADMIN_USERNAME.equals(username)
                || !ADMIN_ROL.equals(rol.getNombre())) {
            throw new BusinessRuleException(
                    "No se puede modificar username ni rol de la cuenta admin base"
            );
        }
    }

    private UsuarioResponse toResponse(
            Usuario usuario,
            Empleado empleado
    ) {

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRol().getNombre(),
                usuario.getEmpleadoId(),
                empleado != null ? empleado.getNombre() : null,
                empleado != null ? empleado.getApellido() : null,
                usuario.isActivo()
        );
    }
}
