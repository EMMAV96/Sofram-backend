package com.sofram.gestionmedica.application;

import com.sofram.gestionmedica.domain.AtencionMedica;
import com.sofram.gestionmedica.domain.Evaluacion;
import com.sofram.gestionmedica.infrastructure.persistence.AtencionMedicaRepository;
import com.sofram.gestionmedica.infrastructure.persistence.EvaluacionRepository;
import com.sofram.gestionmedica.web.dto.EvaluacionRequest;
import com.sofram.gestionmedica.web.dto.EvaluacionResponse;
import com.sofram.historiaclinica.domain.DetalleHistoriaClinica;
import com.sofram.historiaclinica.application.HistoriaClinicaService;
import com.sofram.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;
    private final AtencionMedicaRepository atencionMedicaRepository;
    private final HistoriaClinicaService historiaClinicaService;
    private final GestionMedicaMapper mapper;

    public EvaluacionService(
            EvaluacionRepository evaluacionRepository,
            AtencionMedicaRepository atencionMedicaRepository,
            HistoriaClinicaService historiaClinicaService,
            GestionMedicaMapper mapper
    ) {
        this.evaluacionRepository = evaluacionRepository;
        this.atencionMedicaRepository = atencionMedicaRepository;
        this.historiaClinicaService = historiaClinicaService;
        this.mapper = mapper;
    }

    public EvaluacionResponse crear(EvaluacionRequest request) {

        AtencionMedica atencionMedica =
                atencionMedicaRepository.findById(
                        request.atencionMedicaId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Atención médica no encontrada"
                        )
                );

        DetalleHistoriaClinica detalle =
                historiaClinicaService.buscarDetalleEntidadPorId(
                        request.detalleHistoriaClinicaId()
                );

        Evaluacion evaluacion = new Evaluacion();

        evaluacion.registrar(
                atencionMedica,
                detalle,
                request.tipoEvaluacion(),
                request.descripcion(),
                request.planIntervencion()
        );

        return mapper.toEvaluacionResponse(
                evaluacionRepository.save(evaluacion)
        );
    }

    @Transactional(readOnly = true)
    public EvaluacionResponse buscarPorId(Long id) {

        Evaluacion evaluacion = evaluacionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Evaluación no encontrada")
                );

        return mapper.toEvaluacionResponse(evaluacion);
    }

    @Transactional(readOnly = true)
    public EvaluacionResponse buscarPorAtencion(Long atencionMedicaId) {

        Evaluacion evaluacion = evaluacionRepository
                .findByAtencionMedicaId(atencionMedicaId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No existe una evaluación para esta atención médica"
                        )
                );

        return mapper.toEvaluacionResponse(evaluacion);
    }

    @Transactional(readOnly = true)
    public List<EvaluacionResponse> listarPorDetalleHistoria(
            Long detalleHistoriaClinicaId
    ) {

        return evaluacionRepository
                .findByDetalleHistoriaClinicaIdOrderByIdDesc(
                        detalleHistoriaClinicaId
                )
                .stream()
                .map(mapper::toEvaluacionResponse)
                .toList();
    }
}