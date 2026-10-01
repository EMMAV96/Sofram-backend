package com.sofram.auth.bootstrap;

import com.sofram.auth.domain.Rol;
import com.sofram.auth.domain.Usuario;
import com.sofram.auth.infrastructure.persistence.RolRepository;
import com.sofram.auth.infrastructure.persistence.UsuarioRepository;
import com.sofram.personal.domain.Cargo;
import com.sofram.personal.domain.Empleado;
import com.sofram.personal.infrastructure.persistence.CargoRepository;
import com.sofram.personal.infrastructure.persistence.EmpleadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class AdminBootstrapRunner implements ApplicationRunner {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(AdminBootstrapRunner.class);
    private static final String ADMIN_ROLE = "ADMINISTRADOR";

    private final AdminBootstrapProperties properties;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EmpleadoRepository empleadoRepository;
    private final CargoRepository cargoRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminBootstrapRunner(
            AdminBootstrapProperties properties,
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            EmpleadoRepository empleadoRepository,
            CargoRepository cargoRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.properties = properties;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.empleadoRepository = empleadoRepository;
        this.cargoRepository = cargoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!properties.isConfigured()) {
            if (properties.hasPartialCredentials()) {
                throw new IllegalStateException(
                        "Bootstrap de administrador incompleto"
                );
            }

            LOGGER.info(
                    "Bootstrap de administrador omitido: credenciales iniciales no configuradas"
            );
            return;
        }

        String username = normalizeRequired(
                properties.getUsername(),
                "username"
        );

        if (usuarioRepository.existsByUsername(username)) {
            LOGGER.info(
                    "Bootstrap de administrador omitido: la cuenta configurada ya existe"
            );
            return;
        }

        if (usuarioRepository.existsByRolNombre(ADMIN_ROLE)) {
            LOGGER.info(
                    "Bootstrap de administrador omitido: ya existe una cuenta administradora"
            );
            return;
        }

        Rol adminRole = rolRepository.findByNombre(ADMIN_ROLE)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No existe el rol ADMINISTRADOR"
                        )
                );

        Empleado empleado = findOrCreateEmpleado();

        usuarioRepository.save(
                new Usuario(
                        empleado.getId(),
                        adminRole,
                        username,
                        passwordEncoder.encode(properties.getPassword())
                )
        );

        LOGGER.info("Cuenta administradora inicial creada");
    }

    private Empleado findOrCreateEmpleado() {
        String dni = normalizeRequired(properties.getDni(), "dni");

        return empleadoRepository.findByDni(dni)
                .map(this::validateEmpleadoDisponible)
                .orElseGet(() -> empleadoRepository.save(
                        new Empleado(
                                findOrCreateCargo(),
                                normalizeRequired(
                                        properties.getApellido(),
                                        "apellido"
                                ),
                                normalizeRequired(
                                properties.getNombre(),
                                        "nombre"
                                ),
                                dni,
                                requiredFechaNacimiento(),
                                null,
                                null,
                                normalizeOptional(properties.getEmail())
                        )
                ));
    }

    private Empleado validateEmpleadoDisponible(Empleado empleado) {
        if (usuarioRepository.existsByEmpleadoId(empleado.getId())) {
            throw new IllegalStateException(
                    "El empleado configurado para bootstrap ya tiene usuario"
            );
        }

        if (!empleado.isActivo()) {
            throw new IllegalStateException(
                    "El empleado configurado para bootstrap esta dado de baja"
            );
        }

        return empleado;
    }

    private Cargo findOrCreateCargo() {
        String nombre = normalizeRequired(
                properties.getCargoNombre(),
                "cargoNombre"
        );
        String sector = normalizeOptional(properties.getCargoSector());
        String especialidad = normalizeOptional(
                properties.getCargoEspecialidad()
        );

        return cargoRepository
                .findFirstByNombreAndSectorAndEspecialidad(
                        nombre,
                        sector,
                        especialidad
                )
                .orElseGet(() -> cargoRepository.save(
                        new Cargo(
                                nombre,
                                sector,
                                null,
                                especialidad
                        )
                ));
    }

    private String normalizeRequired(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalStateException(
                    "Configuracion invalida de bootstrap: " + fieldName
            );
        }

        return value.trim();
    }

    private String normalizeOptional(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private java.time.LocalDate requiredFechaNacimiento() {
        if (properties.getFechaNacimiento() == null) {
            throw new IllegalStateException(
                    "Configuracion invalida de bootstrap: fechaNacimiento"
            );
        }

        return properties.getFechaNacimiento();
    }
}
