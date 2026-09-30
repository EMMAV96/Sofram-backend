package com.sofram.historiaclinica.application;

import com.sofram.auditoria.application.AuditoriaService;
import com.sofram.auth.application.UsuarioPrincipal;
import com.sofram.historiaclinica.domain.DetalleHistoriaClinica;
import com.sofram.historiaclinica.domain.HistoriaClinica;
import com.sofram.historiaclinica.infrastructure.persistence.DetalleHistoriaClinicaRepository;
import com.sofram.historiaclinica.infrastructure.persistence.HistoriaClinicaRepository;
import com.sofram.historiaclinica.web.dto.*;
import com.sofram.personal.application.PersonalService;
import com.sofram.personal.domain.Empleado;
import com.sofram.residente.application.ResidenteService;
import com.sofram.residente.domain.Residente;
import com.sofram.shared.exception.DuplicateResourceException;
import com.sofram.shared.exception.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoriaClinicaService {

    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final DetalleHistoriaClinicaRepository detalleRepository;
    private final ResidenteService residenteService;
    private final PersonalService personalService;
    private final AuditoriaService auditoriaService;

    public HistoriaClinicaService(
            HistoriaClinicaRepository historiaClinicaRepository,
            DetalleHistoriaClinicaRepository detalleRepository,
            ResidenteService residenteService,
            PersonalService personalService,
            AuditoriaService auditoriaService
    ) {
        this.historiaClinicaRepository = historiaClinicaRepository;
        this.detalleRepository = detalleRepository;
        this.residenteService = residenteService;
        this.personalService = personalService;
        this.auditoriaService = auditoriaService;
    }

    @Transactional
    public HistoriaClinicaResponse crear(HistoriaClinicaRequest request) {

        if (historiaClinicaRepository.existsByResidenteId(request.residenteId())) {
            throw new DuplicateResourceException(
                    "El residente ya tiene una historia clínica"
            );
        }

        Residente residente =
                residenteService.buscarEntidadPorId(request.residenteId());

        HistoriaClinica historiaClinica = new HistoriaClinica();

        historiaClinica.setResidente(residente);
        historiaClinica.setFechaCreacion(request.fechaCreacion());
        historiaClinica.setObservaciones(request.observaciones());

        HistoriaClinica guardada =
                historiaClinicaRepository.save(historiaClinica);

        auditoriaService.registrar(
                "CREAR",
                "HISTORIA_CLINICA",
                "HistoriaClinica",
                guardada.getId(),
                "Creación de historia clínica"
        );

        return toResponse(guardada);
    }

    @Transactional(readOnly = true)
    public HistoriaClinicaResponse buscarPorId(Long id) {

        HistoriaClinica historiaClinica =
                historiaClinicaRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Historia clínica no encontrada con id: " + id
                                )
                        );

        return toResponse(historiaClinica);
    }

    @Transactional(readOnly = true)
    public HistoriaClinicaResponse buscarPorResidente(Long residenteId) {

        HistoriaClinica historiaClinica =
                historiaClinicaRepository.findByResidenteId(residenteId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "El residente no tiene historia clínica"
                                )
                        );

        return toResponse(historiaClinica);
    }

    @Transactional
    public DetalleHistoriaClinicaResponse agregarDetalle(
            Long historiaClinicaId,
            DetalleHistoriaClinicaRequest request
    ) {

        HistoriaClinica historiaClinica =
                historiaClinicaRepository.findById(historiaClinicaId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Historia clínica no encontrada con id: "
                                                + historiaClinicaId
                                )
                        );

        DetalleHistoriaClinica detalle =
                new DetalleHistoriaClinica();

        Empleado profesional = obtenerProfesionalAutenticado();

        detalle.setHistoriaClinica(historiaClinica);
        detalle.setFecha(request.fecha());
        detalle.setObservaciones(request.observaciones());
        detalle.setProfesional(profesional);

        DetalleHistoriaClinica guardado =
                detalleRepository.save(detalle);

        auditoriaService.registrar(
                "AGREGAR_DETALLE",
                "HISTORIA_CLINICA",
                "DetalleHistoriaClinica",
                guardado.getId(),
                "Nuevo detalle de historia clínica"
        );

        return toDetalleResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<DetalleHistoriaClinicaResponse> listarDetalles(
            Long historiaClinicaId
    ) {

        if (!historiaClinicaRepository.existsById(historiaClinicaId)) {
            throw new ResourceNotFoundException(
                    "Historia clínica no encontrada con id: "
                            + historiaClinicaId
            );
        }

        return detalleRepository
                .findByHistoriaClinicaIdOrderByFechaDesc(historiaClinicaId)
                .stream()
                .map(this::toDetalleResponse)
                .toList();
    }

    private HistoriaClinicaResponse toResponse(
            HistoriaClinica historiaClinica
    ) {
        return new HistoriaClinicaResponse(
                historiaClinica.getId(),
                historiaClinica.getResidente().getId(),
                historiaClinica.getFechaCreacion(),
                historiaClinica.getObservaciones(),
                historiaClinica.getAntecedentesPersonales(),
                historiaClinica.getAntecedentesFamiliares(),
                historiaClinica.getAlergias()
        );
    }

    private DetalleHistoriaClinicaResponse toDetalleResponse(
            DetalleHistoriaClinica detalle
    ) {
        Empleado profesional = detalle.getProfesional();

        return new DetalleHistoriaClinicaResponse(
                detalle.getId(),
                detalle.getHistoriaClinica().getId(),
                detalle.getFecha(),
                detalle.getObservaciones(),
                profesional != null ? profesional.getId() : null,
                profesional != null ? profesional.getNombre() : null,
                profesional != null ? profesional.getApellido() : null,
                profesional != null ? profesional.getCargo().getNombre() : null
        );
    }

    private Empleado obtenerProfesionalAutenticado() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal()
                instanceof UsuarioPrincipal principal)) {
            throw new AccessDeniedException("Acceso denegado");
        }

        if (principal.getEmpleadoId() == null) {
            throw new ResourceNotFoundException(
                    "El usuario autenticado no tiene empleado asociado"
            );
        }

        return personalService.buscarEmpleadoPorId(principal.getEmpleadoId());
    }

    @Transactional(readOnly = true)
    public DetalleHistoriaClinica buscarDetalleEntidadPorId(Long id) {

        return detalleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Detalle de historia clínica no encontrado con id: " + id
                        )
                );
    }

    public HistoriaClinicaResponse actualizarAntecedentesYAlergias(
            Long historiaClinicaId,
            AntecedentesHistoriaClinicaRequest request
    ) {

        HistoriaClinica historiaClinica = historiaClinicaRepository
                .findById(historiaClinicaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Historia clínica no encontrada con id: "
                                + historiaClinicaId
                ));

        historiaClinica.actualizarAntecedentesYAlergias(
                request.antecedentesPersonales(),
                request.antecedentesFamiliares(),
                request.alergias()
        );

        HistoriaClinica actualizada =
                historiaClinicaRepository.save(historiaClinica);

        auditoriaService.registrar(
                "ACTUALIZAR_ANTECEDENTES",
                "HISTORIA_CLINICA",
                "HistoriaClinica",
                actualizada.getId(),
                "Actualización de antecedentes y alergias"
        );

        return toResponse(actualizada);
    }
}
