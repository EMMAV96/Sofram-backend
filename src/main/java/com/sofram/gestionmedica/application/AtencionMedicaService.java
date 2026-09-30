package com.sofram.gestionmedica.application;

import com.sofram.auditoria.application.AuditoriaService;
import com.sofram.auth.application.UsuarioPrincipal;
import com.sofram.gestionmedica.domain.AtencionMedica;
import com.sofram.gestionmedica.infrastructure.persistence.AtencionMedicaRepository;
import com.sofram.gestionmedica.web.dto.AtencionMedicaRequest;
import com.sofram.gestionmedica.web.dto.AtencionMedicaResponse;

import com.sofram.personal.application.PersonalService;
import com.sofram.personal.domain.Empleado;
import com.sofram.residente.application.ResidenteService;
import com.sofram.residente.domain.Residente;
import com.sofram.shared.exception.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AtencionMedicaService {

    private final AtencionMedicaRepository atencionMedicaRepository;
    private final ResidenteService residenteService;
    private final PersonalService personalService;
    private final GestionMedicaMapper mapper;
    private final AuditoriaService auditoriaService;

    public AtencionMedicaService(
            AtencionMedicaRepository atencionMedicaRepository,
            ResidenteService residenteService,
            PersonalService personalService,
            GestionMedicaMapper mapper,
            AuditoriaService auditoriaService
    ) {
        this.atencionMedicaRepository = atencionMedicaRepository;
        this.residenteService = residenteService;
        this.personalService = personalService;
        this.mapper = mapper;
        this.auditoriaService = auditoriaService;
    }

    public AtencionMedicaResponse crear(AtencionMedicaRequest request) {

        Residente residente = residenteService.buscarEntidadPorId(
                request.residenteId()
        );

        Empleado empleado = personalService.buscarEmpleadoPorId(
                request.empleadoId()
        );
        validarEmpleadoAutenticado(empleado.getId());

        AtencionMedica atencion = new AtencionMedica();

        atencion.registrar(
                residente,
                empleado,
                request.fecha(),
                request.motivo(),
                request.tipoIntervencion(),
                request.observaciones()
        );

        AtencionMedica guardada =
                atencionMedicaRepository.save(atencion);

        auditoriaService.registrar(
                "CREAR",
                "GESTION_MEDICA",
                "AtencionMedica",
                guardada.getId(),
                "Registro de atención médica"
        );

        return mapper.toAtencionResponse(guardada);
    }

    @Transactional(readOnly = true)
    public AtencionMedicaResponse buscarPorId(Long id) {

        AtencionMedica atencion = atencionMedicaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Atención médica no encontrada"
                        )
                );

        return mapper.toAtencionResponse(atencion);
    }

    @Transactional(readOnly = true)
    public List<AtencionMedicaResponse> listarPorResidente(Long residenteId) {

        return atencionMedicaRepository
                .findByResidenteIdOrderByFechaDesc(residenteId)
                .stream()
                .map(mapper::toAtencionResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AtencionMedicaResponse> listarPorEmpleado(Long empleadoId) {

        return atencionMedicaRepository
                .findByEmpleadoIdOrderByFechaDesc(empleadoId)
                .stream()
                .map(mapper::toAtencionResponse)
                .toList();
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
}
