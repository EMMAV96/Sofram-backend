package com.sofram.actividad.application;

import com.sofram.actividad.domain.Calendario;
import com.sofram.actividad.domain.DetalleCalendario;
import com.sofram.actividad.infrastructure.persistence.CalendarioRepository;
import com.sofram.actividad.infrastructure.persistence.DetalleCalendarioRepository;
import com.sofram.actividad.web.dto.*;
import com.sofram.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CalendarioService {

    private final CalendarioRepository calendarioRepository;
    private final DetalleCalendarioRepository detalleRepository;

    public CalendarioService(
            CalendarioRepository calendarioRepository,
            DetalleCalendarioRepository detalleRepository
    ) {
        this.calendarioRepository = calendarioRepository;
        this.detalleRepository = detalleRepository;
    }

    @Transactional
    public CalendarioResponse crear(CalendarioRequest request) {

        Calendario calendario = new Calendario(
                request.nombre(),
                request.periodo(),
                request.anio(),
                request.estado()
        );

        return toResponse(
                calendarioRepository.save(calendario)
        );
    }

    @Transactional(readOnly = true)
    public List<CalendarioResponse> listar() {

        return calendarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CalendarioResponse buscarPorId(Long id) {

        return toResponse(buscarEntidadPorId(id));
    }

    @Transactional
    public DetalleCalendarioResponse agregarDetalle(
            Long calendarioId,
            DetalleCalendarioRequest request
    ) {

        Calendario calendario =
                buscarEntidadPorId(calendarioId);

        if (!request.horaFin().isAfter(request.horaInicio())) {
            throw new IllegalArgumentException(
                    "La hora de fin debe ser posterior a la hora de inicio"
            );
        }

        DetalleCalendario detalle =
                new DetalleCalendario(
                        calendario,
                        request.fecha(),
                        request.horaInicio(),
                        request.horaFin(),
                        request.estado()
                );

        return toDetalleResponse(
                detalleRepository.save(detalle)
        );
    }

    @Transactional(readOnly = true)
    public List<DetalleCalendarioResponse> listarDetalles(
            Long calendarioId
    ) {

        buscarEntidadPorId(calendarioId);

        return detalleRepository
                .findByCalendarioIdOrderByFechaAscHoraInicioAsc(
                        calendarioId
                )
                .stream()
                .map(this::toDetalleResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DetalleCalendarioResponse buscarDetallePorId(
            Long id
    ) {

        DetalleCalendario detalle =
                detalleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Detalle de calendario no encontrado con id: "
                                                + id
                                )
                        );

        return toDetalleResponse(detalle);
    }

    @Transactional(readOnly = true)
    public Calendario buscarEntidadPorId(Long id) {

        return calendarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Calendario no encontrado con id: " + id
                        )
                );
    }

    /*
     * Lo necesitaremos en V12 para asociar una Actividad
     * a un DetalleCalendario sin acceder directamente
     * al repository desde otro servicio.
     */
    @Transactional(readOnly = true)
    public DetalleCalendario buscarDetalleEntidadPorId(Long id) {

        return detalleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Detalle de calendario no encontrado con id: "
                                        + id
                        )
                );
    }

    private CalendarioResponse toResponse(
            Calendario calendario
    ) {
        return new CalendarioResponse(
                calendario.getId(),
                calendario.getNombre(),
                calendario.getPeriodo(),
                calendario.getAnio(),
                calendario.getEstado()
        );
    }

    private DetalleCalendarioResponse toDetalleResponse(
            DetalleCalendario detalle
    ) {
        return new DetalleCalendarioResponse(
                detalle.getId(),
                detalle.getCalendario().getId(),
                detalle.getFecha(),
                detalle.getHoraInicio(),
                detalle.getHoraFin(),
                detalle.getEstado()
        );
    }
}