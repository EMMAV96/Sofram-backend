package com.sofram.gestionmedica.application;

import com.sofram.auditoria.application.AuditoriaService;
import com.sofram.gestionmedica.domain.Diagnostico;
import com.sofram.gestionmedica.infrastructure.persistence.DiagnosticoRepository;
import com.sofram.gestionmedica.web.dto.DiagnosticoRequest;
import com.sofram.gestionmedica.web.dto.DiagnosticoResponse;


import com.sofram.historiaclinica.application.HistoriaClinicaService;
import com.sofram.historiaclinica.domain.DetalleHistoriaClinica;

import com.sofram.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final HistoriaClinicaService historiaClinicaService;
    private final GestionMedicaMapper mapper;
    private final AuditoriaService auditoriaService;

    public DiagnosticoService(
            DiagnosticoRepository diagnosticoRepository,
            HistoriaClinicaService historiaClinicaService,
            GestionMedicaMapper mapper,
            AuditoriaService auditoriaService
    ) {
        this.diagnosticoRepository = diagnosticoRepository;
        this.historiaClinicaService = historiaClinicaService;
        this.mapper = mapper;
        this.auditoriaService = auditoriaService;
    }

    public DiagnosticoResponse crear(DiagnosticoRequest request) {

        DetalleHistoriaClinica detalle =
                historiaClinicaService.buscarDetalleEntidadPorId(
                        request.detalleHistoriaClinicaId()
                );

        Diagnostico diagnostico = new Diagnostico(
                detalle,
                request.descripcion()
        );

        Diagnostico guardado =
                diagnosticoRepository.save(diagnostico);

        auditoriaService.registrar(
                "CREAR",
                "GESTION_MEDICA",
                "Diagnostico",
                guardado.getId(),
                "Registro de diagnóstico"
        );

        return mapper.toDiagnosticoResponse(guardado);
    }

    @Transactional(readOnly = true)
    public DiagnosticoResponse buscarPorId(Long id) {

        Diagnostico diagnostico = diagnosticoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Diagnóstico no encontrado con id: " + id
                ));

        return mapper.toDiagnosticoResponse(diagnostico);
    }

    @Transactional(readOnly = true)
    public List<DiagnosticoResponse> listarPorDetalleHistoria(
            Long detalleHistoriaClinicaId
    ) {
        return diagnosticoRepository
                .findByDetalleHistoriaClinicaIdOrderByIdDesc(
                        detalleHistoriaClinicaId
                )
                .stream()
                .map(mapper::toDiagnosticoResponse)
                .toList();
    }
}
