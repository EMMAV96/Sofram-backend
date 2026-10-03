package com.sofram.actividad.application;

import com.sofram.actividad.domain.Actividad;
import com.sofram.actividad.domain.Calendario;
import com.sofram.actividad.domain.DetalleCalendario;
import com.sofram.actividad.infrastructure.persistence.ActividadRepository;
import com.sofram.actividad.infrastructure.persistence.CalendarioRepository;
import com.sofram.actividad.infrastructure.persistence.DetalleCalendarioRepository;
import com.sofram.actividad.web.dto.*;
import com.sofram.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalendarioService {

    private final CalendarioRepository calendarioRepository;
    private final DetalleCalendarioRepository detalleRepository;
    private final ActividadRepository actividadRepository;

    public CalendarioService(
            CalendarioRepository calendarioRepository,
            DetalleCalendarioRepository detalleRepository,
            ActividadRepository actividadRepository
    ) {
        this.calendarioRepository = calendarioRepository;
        this.detalleRepository = detalleRepository;
        this.actividadRepository = actividadRepository;
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
                detalleRepository.save(detalle),
                null
        );
    }

    @Transactional(readOnly = true)
    public List<DetalleCalendarioResponse> listarDetalles(
            Long calendarioId
    ) {

        buscarEntidadPorId(calendarioId);

        List<DetalleCalendario> detalles = detalleRepository
                .findByCalendarioIdOrderByFechaAscHoraInicioAsc(
                        calendarioId
                );

        Map<Long, Actividad> actividadesPorDetalle =
                buscarActividadesPorDetalle(detalles);

        return detalles
                .stream()
                .map(detalle ->
                        toDetalleResponse(
                                detalle,
                                actividadesPorDetalle.get(detalle.getId())
                        )
                )
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

        return toDetalleResponse(
                detalle,
                actividadRepository
                        .findFirstByDetalleCalendarioIdOrderByIdAsc(id)
                        .orElse(null)
        );
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

    private Map<Long, Actividad> buscarActividadesPorDetalle(
            List<DetalleCalendario> detalles
    ) {
        List<Long> detalleIds = detalles.stream()
                .map(DetalleCalendario::getId)
                .toList();

        Map<Long, Actividad> actividadesPorDetalle = new HashMap<>();

        if (detalleIds.isEmpty()) {
            return actividadesPorDetalle;
        }

        actividadRepository
                .findByDetalleCalendarioIdInOrderByIdAsc(detalleIds)
                .forEach(actividad ->
                        actividadesPorDetalle.putIfAbsent(
                                actividad.getDetalleCalendario().getId(),
                                actividad
                        )
                );

        return actividadesPorDetalle;
    }

    private DetalleCalendarioResponse toDetalleResponse(
            DetalleCalendario detalle,
            Actividad actividad
    ) {
        return new DetalleCalendarioResponse(
                detalle.getId(),
                detalle.getCalendario().getId(),
                detalle.getFecha(),
                detalle.getHoraInicio(),
                detalle.getHoraFin(),
                detalle.getEstado(),
                actividad != null ? actividad.getId() : null,
                actividad != null ? actividad.getNombre() : null,
                actividad != null ? actividad.getTaller() : null
        );
    }
}
