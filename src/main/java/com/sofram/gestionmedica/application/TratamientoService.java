package com.sofram.gestionmedica.application;

import com.sofram.auditoria.application.AuditoriaService;
import com.sofram.gestionmedica.domain.Tratamiento;
import com.sofram.gestionmedica.infrastructure.persistence.TratamientoRepository;
import com.sofram.gestionmedica.web.dto.TratamientoRequest;
import com.sofram.gestionmedica.web.dto.TratamientoResponse;
import com.sofram.historiaclinica.application.HistoriaClinicaService;
import com.sofram.historiaclinica.domain.DetalleHistoriaClinica;
import com.sofram.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TratamientoService {

    private final TratamientoRepository tratamientoRepository;
    private final HistoriaClinicaService historiaClinicaService;
    private final GestionMedicaMapper mapper;
    private final AuditoriaService auditoriaService;

    public TratamientoService(
            TratamientoRepository tratamientoRepository,
            HistoriaClinicaService historiaClinicaService,
            GestionMedicaMapper mapper,
            AuditoriaService auditoriaService
    ) {
        this.tratamientoRepository = tratamientoRepository;
        this.historiaClinicaService = historiaClinicaService;
        this.mapper = mapper;
        this.auditoriaService = auditoriaService;
    }

    public TratamientoResponse crear(TratamientoRequest request) {

        DetalleHistoriaClinica detalle =
                historiaClinicaService.buscarDetalleEntidadPorId(
                        request.detalleHistoriaClinicaId()
                );

        Tratamiento tratamiento = new Tratamiento(
                detalle,
                request.nombre(),
                request.descripcion()
        );

        Tratamiento guardado =
                tratamientoRepository.save(tratamiento);

        auditoriaService.registrar(
                "CREAR",
                "GESTION_MEDICA",
                "Tratamiento",
                guardado.getId(),
                "Registro de tratamiento"
        );

        return mapper.toTratamientoResponse(guardado);
    }

    @Transactional(readOnly = true)
    public TratamientoResponse buscarPorId(Long id) {

        Tratamiento tratamiento = tratamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tratamiento no encontrado con id: " + id
                ));

        return mapper.toTratamientoResponse(tratamiento);
    }

    @Transactional(readOnly = true)
    public List<TratamientoResponse> listarPorDetalleHistoria(
            Long detalleHistoriaClinicaId
    ) {
        return tratamientoRepository
                .findByDetalleHistoriaClinicaIdOrderByIdDesc(
                        detalleHistoriaClinicaId
                )
                .stream()
                .map(mapper::toTratamientoResponse)
                .toList();
    }
}
