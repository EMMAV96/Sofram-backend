package com.sofram.gestionmedica.application;

import com.sofram.auditoria.application.AuditoriaService;
import com.sofram.gestionmedica.domain.Medicacion;
import com.sofram.gestionmedica.infrastructure.persistence.MedicacionRepository;
import com.sofram.gestionmedica.web.dto.MedicacionRequest;
import com.sofram.gestionmedica.web.dto.MedicacionResponse;
import com.sofram.historiaclinica.application.HistoriaClinicaService;
import com.sofram.historiaclinica.domain.DetalleHistoriaClinica;
import com.sofram.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MedicacionService {

    private final MedicacionRepository medicacionRepository;
    private final HistoriaClinicaService historiaClinicaService;
    private final GestionMedicaMapper mapper;
    private final AuditoriaService auditoriaService;

    public MedicacionService(
            MedicacionRepository medicacionRepository,
            HistoriaClinicaService historiaClinicaService,
            GestionMedicaMapper mapper,
            AuditoriaService auditoriaService
    ) {
        this.medicacionRepository = medicacionRepository;
        this.historiaClinicaService = historiaClinicaService;
        this.mapper = mapper;
        this.auditoriaService = auditoriaService;
    }

    public MedicacionResponse crear(MedicacionRequest request) {

        DetalleHistoriaClinica detalle =
                historiaClinicaService.buscarDetalleEntidadPorId(
                        request.detalleHistoriaClinicaId()
                );

        Medicacion medicacion = new Medicacion(
                detalle,
                request.nombre(),
                request.dosis(),
                request.frecuencia()
        );

        Medicacion guardada =
                medicacionRepository.save(medicacion);

        auditoriaService.registrar(
                "CREAR",
                "GESTION_MEDICA",
                "Medicacion",
                guardada.getId(),
                "Registro de medicación"
        );

        return mapper.toMedicacionResponse(guardada);
    }

    @Transactional(readOnly = true)
    public MedicacionResponse buscarPorId(Long id) {

        Medicacion medicacion = medicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicación no encontrada con id: " + id
                ));

        return mapper.toMedicacionResponse(medicacion);
    }

    @Transactional(readOnly = true)
    public List<MedicacionResponse> listarPorDetalleHistoria(
            Long detalleHistoriaClinicaId
    ) {
        return medicacionRepository
                .findByDetalleHistoriaClinicaIdOrderByIdDesc(
                        detalleHistoriaClinicaId
                )
                .stream()
                .map(mapper::toMedicacionResponse)
                .toList();
    }
}
