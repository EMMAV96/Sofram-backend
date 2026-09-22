package com.sofram.personal.application;

import com.sofram.personal.domain.AsignacionTurno;
import com.sofram.personal.domain.Cargo;
import com.sofram.personal.domain.Empleado;
import com.sofram.personal.domain.Turno;
import com.sofram.personal.infrastructure.persistence.AsignacionTurnoRepository;
import com.sofram.personal.infrastructure.persistence.CargoRepository;
import com.sofram.personal.infrastructure.persistence.EmpleadoRepository;
import com.sofram.personal.infrastructure.persistence.TurnoRepository;
import com.sofram.personal.web.dto.AsignacionTurnoRequest;
import com.sofram.personal.web.dto.AsignacionTurnoResponse;
import com.sofram.personal.web.dto.BajaEmpleadoRequest;
import com.sofram.personal.web.dto.CargoRequest;
import com.sofram.personal.web.dto.CargoResponse;
import com.sofram.personal.web.dto.EmpleadoRequest;
import com.sofram.personal.web.dto.EmpleadoResponse;
import com.sofram.personal.web.dto.TurnoRequest;
import com.sofram.personal.web.dto.TurnoResponse;
import com.sofram.shared.exception.BusinessRuleException;
import com.sofram.shared.exception.DuplicateResourceException;
import com.sofram.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonalService {

    private final CargoRepository cargoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final TurnoRepository turnoRepository;
    private final AsignacionTurnoRepository asignacionTurnoRepository;

    public PersonalService(
            CargoRepository cargoRepository,
            EmpleadoRepository empleadoRepository,
            TurnoRepository turnoRepository,
            AsignacionTurnoRepository asignacionTurnoRepository
    ) {
        this.cargoRepository = cargoRepository;
        this.empleadoRepository = empleadoRepository;
        this.turnoRepository = turnoRepository;
        this.asignacionTurnoRepository = asignacionTurnoRepository;
    }

    @Transactional(readOnly = true)
    public List<CargoResponse> listarCargos() {
        return cargoRepository.findAll().stream()
                .map(PersonalMapper::toResponse)
                .toList();
    }

    @Transactional
    public CargoResponse crearCargo(CargoRequest request) {
        Cargo cargo = cargoRepository.save(new Cargo(
                request.nombre(),
                request.sector(),
                request.matricula(),
                request.especialidad()
        ));
        return PersonalMapper.toResponse(cargo);
    }

    @Transactional(readOnly = true)
    public List<EmpleadoResponse> listarEmpleados() {
        return empleadoRepository.findAll().stream()
                .map(PersonalMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmpleadoResponse obtenerEmpleado(Long id) {
        return PersonalMapper.toResponse(buscarEmpleado(id));
    }

    @Transactional
    public EmpleadoResponse crearEmpleado(EmpleadoRequest request) {
        if (empleadoRepository.existsByDni(request.dni())) {
            throw new DuplicateResourceException("Ya existe un empleado con ese DNI");
        }

        Cargo cargo = buscarCargo(request.cargoId());
        Empleado empleado = empleadoRepository.save(new Empleado(
                cargo,
                request.apellido(),
                request.nombre(),
                request.dni(),
                request.fechaNacimiento(),
                request.direccion(),
                request.telefono(),
                request.email()
        ));
        return PersonalMapper.toResponse(empleado);
    }

    @Transactional
    public EmpleadoResponse actualizarEmpleado(Long id, EmpleadoRequest request) {
        Empleado empleado = buscarEmpleado(id);
        empleadoRepository.findByDni(request.dni())
                .filter(existente -> !existente.getId().equals(id))
                .ifPresent(existente -> {
                    throw new DuplicateResourceException("Ya existe un empleado con ese DNI");
                });
        Cargo cargo = buscarCargo(request.cargoId());
        empleado.actualizar(
                cargo,
                request.apellido(),
                request.nombre(),
                request.fechaNacimiento(),
                request.direccion(),
                request.telefono(),
                request.email()
        );
        return PersonalMapper.toResponse(empleado);
    }

    @Transactional
    public EmpleadoResponse darDeBajaEmpleado(Long id, BajaEmpleadoRequest request) {
        Empleado empleado = buscarEmpleado(id);
        if (!empleado.isActivo()) {
            throw new BusinessRuleException("El empleado ya se encuentra dado de baja");
        }
        empleado.darDeBaja(request.fechaBaja());
        asignacionTurnoRepository.findByEmpleadoIdAndFechaHastaIsNull(id)
                .ifPresent(asignacion -> asignacion.cerrar(request.fechaBaja()));
        return PersonalMapper.toResponse(empleado);
    }

    @Transactional(readOnly = true)
    public List<TurnoResponse> listarTurnos() {
        return turnoRepository.findAll().stream()
                .map(PersonalMapper::toResponse)
                .toList();
    }

    @Transactional
    public TurnoResponse crearTurno(TurnoRequest request) {
        Turno turno = turnoRepository.save(new Turno(
                request.descripcion(),
                request.horaInicio(),
                request.horaFin()
        ));
        return PersonalMapper.toResponse(turno);
    }

    @Transactional(readOnly = true)
    public List<AsignacionTurnoResponse> listarAsignaciones(Long empleadoId) {
        buscarEmpleado(empleadoId);
        return asignacionTurnoRepository.findByEmpleadoIdOrderByFechaDesdeDesc(empleadoId).stream()
                .map(PersonalMapper::toResponse)
                .toList();
    }

    @Transactional
    public AsignacionTurnoResponse asignarTurno(Long empleadoId, AsignacionTurnoRequest request) {
        Empleado empleado = buscarEmpleado(empleadoId);
        if (!empleado.isActivo()) {
            throw new BusinessRuleException("No se puede asignar turno a un empleado dado de baja");
        }

        Turno turno = buscarTurno(request.turnoId());
        asignacionTurnoRepository.findByEmpleadoIdAndFechaHastaIsNull(empleadoId)
                .ifPresent(asignacionActiva -> {
                    if (!request.fechaDesde().isAfter(asignacionActiva.getFechaDesde())) {
                        throw new BusinessRuleException("La nueva asignacion debe comenzar despues de la asignacion vigente");
                    }
                    asignacionActiva.cerrar(request.fechaDesde().minusDays(1));
                });

        AsignacionTurno asignacion = asignacionTurnoRepository.save(new AsignacionTurno(
                empleado,
                turno,
                request.fechaDesde(),
                request.motivoCambio()
        ));
        return PersonalMapper.toResponse(asignacion);
    }

    private Cargo buscarCargo(Long id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo no encontrado"));
    }

    private Empleado buscarEmpleado(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));
    }

    private Turno buscarTurno(Long id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado"));
    }
    @Transactional(readOnly = true)
    public Empleado buscarEmpleadoPorId(Long id) {
        return buscarEmpleado(id);
    }
}
