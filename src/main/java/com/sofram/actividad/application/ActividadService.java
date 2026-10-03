package com.sofram.actividad.application;

import com.sofram.auditoria.application.AuditoriaService;
import com.sofram.auth.application.UsuarioPrincipal;
import com.sofram.actividad.domain.Actividad;
import com.sofram.actividad.domain.DetalleCalendario;
import com.sofram.actividad.infrastructure.persistence.ActividadRepository;
import com.sofram.actividad.infrastructure.persistence.ParticipacionActividadRepository;
import com.sofram.actividad.web.dto.ActividadRequest;
import com.sofram.actividad.web.dto.ActividadResponse;
import com.sofram.personal.application.PersonalService;
import com.sofram.personal.domain.Empleado;
import com.sofram.shared.exception.BusinessRuleException;
import com.sofram.shared.exception.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActividadService {

    private static final String ESTADO_ELIMINADA = "ELIMINADA";

    private final ActividadRepository actividadRepository;
    private final ParticipacionActividadRepository participacionRepository;
    private final CalendarioService calendarioService;
    private final PersonalService personalService;
    private final AuditoriaService auditoriaService;

    public ActividadService(
            ActividadRepository actividadRepository,
            ParticipacionActividadRepository participacionRepository,
            CalendarioService calendarioService,
            PersonalService personalService,
            AuditoriaService auditoriaService
    ) {
        this.actividadRepository = actividadRepository;
        this.participacionRepository = participacionRepository;
        this.calendarioService = calendarioService;
        this.personalService = personalService;
        this.auditoriaService = auditoriaService;
    }

    @Transactional
    public ActividadResponse crear(ActividadRequest request) {

        DetalleCalendario detalle =
                calendarioService.buscarDetalleEntidadPorId(
                        request.detalleCalendarioId()
                );

        Empleado empleado =
                personalService.buscarEmpleadoPorId(
                        request.empleadoId()
                );
        validarEmpleadoAutenticado(empleado.getId());
        validarEmpleadoActivo(empleado);

        Actividad actividad = new Actividad(
                detalle,
                empleado,
                request.nombre(),
                request.taller(),
                request.descripcion(),
                request.tipo(),
                request.duracion(),
                request.cupoMaximo(),
                request.estado()
        );

        Actividad guardada = actividadRepository.save(actividad);

        auditoriaService.registrar(
                "CREAR",
                "ACTIVIDADES",
                "Actividad",
                guardada.getId(),
                "Creación de actividad"
        );

        return toResponse(guardada);
    }

    @Transactional
    public ActividadResponse actualizar(
            Long id,
            ActividadRequest request
    ) {

        Actividad actividad = buscarEntidadPorId(id);

        DetalleCalendario detalle =
                calendarioService.buscarDetalleEntidadPorId(
                        request.detalleCalendarioId()
                );

        Empleado empleado =
                personalService.buscarEmpleadoPorId(
                        request.empleadoId()
                );
        validarEmpleadoAutenticado(empleado.getId());
        validarEmpleadoActivo(empleado);
        validarCupoMaximoNoMenorAParticipantes(
                actividad.getId(),
                request.cupoMaximo()
        );

        actividad.actualizar(
                detalle,
                empleado,
                request.nombre(),
                request.taller(),
                request.descripcion(),
                request.tipo(),
                request.duracion(),
                request.cupoMaximo(),
                request.estado()
        );

        Actividad actualizada = actividadRepository.save(actividad);

        auditoriaService.registrar(
                "ACTUALIZAR",
                "ACTIVIDADES",
                "Actividad",
                actualizada.getId(),
                "Actualización de actividad"
        );

        return toResponse(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {

        Actividad actividad = buscarEntidadPorId(id);

        validarEmpleadoAutenticado(
                actividad.getEmpleado().getId()
        );

        if (participacionRepository.existsByActividadId(id)) {
            actividad.cambiarEstado(ESTADO_ELIMINADA);
            actividadRepository.save(actividad);

            auditoriaService.registrar(
                    "ELIMINAR_LOGICO",
                    "ACTIVIDADES",
                    "Actividad",
                    actividad.getId(),
                    "Baja lógica de actividad con participaciones"
            );
            return;
        }

        actividadRepository.delete(actividad);

        auditoriaService.registrar(
                "ELIMINAR",
                "ACTIVIDADES",
                "Actividad",
                id,
                "Eliminación física de actividad sin participaciones"
        );
    }

    @Transactional(readOnly = true)
    public ActividadResponse buscarPorId(Long id) {

        return toResponse(
                buscarEntidadPorId(id)
        );
    }

    @Transactional(readOnly = true)
    public List<ActividadResponse> listar() {

        return actividadRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ActividadResponse> listarPorDetalleCalendario(
            Long detalleCalendarioId
    ) {

        calendarioService.buscarDetalleEntidadPorId(
                detalleCalendarioId
        );

        return actividadRepository
                .findByDetalleCalendarioIdOrderByIdAsc(
                        detalleCalendarioId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ActividadResponse> listarPorEmpleado(
            Long empleadoId
    ) {

        personalService.buscarEmpleadoPorId(empleadoId);

        return actividadRepository
                .findByEmpleadoIdOrderByIdDesc(empleadoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Actividad buscarEntidadPorId(Long id) {

        return actividadRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Actividad no encontrada con id: " + id
                        )
                );
    }

    private ActividadResponse toResponse(
            Actividad actividad
    ) {

        return new ActividadResponse(
                actividad.getId(),
                actividad.getDetalleCalendario().getId(),
                actividad.getEmpleado().getId(),
                actividad.getNombre(),
                actividad.getTaller(),
                actividad.getDescripcion(),
                actividad.getTipo(),
                actividad.getDuracion(),
                actividad.getCupoMaximo(),
                actividad.getEstado()
        );
    }

    private void validarEmpleadoAutenticado(Long empleadoId) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal()
                instanceof UsuarioPrincipal principal)) {
            throw new AccessDeniedException("Acceso denegado");
        }

        if ("ADMINISTRADOR".equals(principal.getRol())) {
            return;
        }

        if (!empleadoId.equals(principal.getEmpleadoId())) {
            throw new AccessDeniedException("Acceso denegado");
        }
    }

    private void validarEmpleadoActivo(Empleado empleado) {

        if (!empleado.isActivo()) {
            throw new BusinessRuleException(
                    "El empleado se encuentra dado de baja"
            );
        }
    }

    private void validarCupoMaximoNoMenorAParticipantes(
            Long actividadId,
            Integer cupoMaximo
    ) {

        long cantidadParticipantes =
                participacionRepository.countByActividadId(actividadId);

        if (cupoMaximo < cantidadParticipantes) {
            throw new BusinessRuleException(
                    "El cupo máximo no puede ser menor a la cantidad de participantes registrados"
            );
        }
    }
}
