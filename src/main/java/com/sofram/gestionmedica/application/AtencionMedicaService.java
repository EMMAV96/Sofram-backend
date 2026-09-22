package com.sofram.gestionmedica.application;

import com.sofram.gestionmedica.domain.AtencionMedica;
import com.sofram.gestionmedica.infrastructure.persistence.AtencionMedicaRepository;
import com.sofram.gestionmedica.web.dto.AtencionMedicaRequest;
import com.sofram.gestionmedica.web.dto.AtencionMedicaResponse;

import com.sofram.personal.application.PersonalService;
import com.sofram.personal.domain.Empleado;
import com.sofram.residente.application.ResidenteService;
import com.sofram.residente.domain.Residente;
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

    public AtencionMedicaService(
            AtencionMedicaRepository atencionMedicaRepository,
            ResidenteService residenteService,
            PersonalService personalService,
            GestionMedicaMapper mapper
    ) {
        this.atencionMedicaRepository = atencionMedicaRepository;
        this.residenteService = residenteService;
        this.personalService = personalService;
        this.mapper = mapper;
    }

    public AtencionMedicaResponse crear(AtencionMedicaRequest request) {

        Residente residente = residenteService.buscarEntidadPorId(
                request.residenteId()
        );

        Empleado empleado = personalService.buscarEmpleadoPorId(request.empleadoId());

        AtencionMedica atencion = new AtencionMedica();

        atencion.registrar(
                residente,
                empleado,
                request.fecha(),
                request.motivo(),
                request.tipoIntervencion(),
                request.observaciones()
        );

        return mapper.toAtencionResponse(
                atencionMedicaRepository.save(atencion)
        );
    }

    @Transactional(readOnly = true)
    public AtencionMedicaResponse buscarPorId(Long id) {

        AtencionMedica atencion = atencionMedicaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Atención médica no encontrada")
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
}