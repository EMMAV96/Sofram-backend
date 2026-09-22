package com.sofram.gestionmedica.application;

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

    public TratamientoService(
            TratamientoRepository tratamientoRepository,
            HistoriaClinicaService historiaClinicaService,
            GestionMedicaMapper mapper
    ) {
        this.tratamientoRepository = tratamientoRepository;
        this.historiaClinicaService = historiaClinicaService;
        this.mapper = mapper;
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

        return mapper.toTratamientoResponse(
                tratamientoRepository.save(tratamiento)
        );
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