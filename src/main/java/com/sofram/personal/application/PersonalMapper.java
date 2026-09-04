package com.sofram.personal.application;

import com.sofram.personal.domain.AsignacionTurno;
import com.sofram.personal.domain.Cargo;
import com.sofram.personal.domain.Empleado;
import com.sofram.personal.domain.Turno;
import com.sofram.personal.web.dto.AsignacionTurnoResponse;
import com.sofram.personal.web.dto.CargoResponse;
import com.sofram.personal.web.dto.EmpleadoResponse;
import com.sofram.personal.web.dto.TurnoResponse;

final class PersonalMapper {

    private PersonalMapper() {
    }

    static CargoResponse toResponse(Cargo cargo) {
        return new CargoResponse(
                cargo.getId(),
                cargo.getNombre(),
                cargo.getSector(),
                cargo.getMatricula(),
                cargo.getEspecialidad()
        );
    }

    static EmpleadoResponse toResponse(Empleado empleado) {
        return new EmpleadoResponse(
                empleado.getId(),
                empleado.getCargo().getId(),
                empleado.getCargo().getNombre(),
                empleado.getApellido(),
                empleado.getNombre(),
                empleado.getDni(),
                empleado.getFechaNacimiento(),
                empleado.getDireccion(),
                empleado.getTelefono(),
                empleado.getEmail(),
                empleado.getFechaBaja(),
                empleado.isActivo()
        );
    }

    static TurnoResponse toResponse(Turno turno) {
        return new TurnoResponse(
                turno.getId(),
                turno.getDescripcion(),
                turno.getHoraInicio(),
                turno.getHoraFin()
        );
    }

    static AsignacionTurnoResponse toResponse(AsignacionTurno asignacion) {
        return new AsignacionTurnoResponse(
                asignacion.getId(),
                asignacion.getEmpleado().getId(),
                asignacion.getTurno().getId(),
                asignacion.getTurno().getDescripcion(),
                asignacion.getTurno().getHoraInicio(),
                asignacion.getTurno().getHoraFin(),
                asignacion.getFechaDesde(),
                asignacion.getFechaHasta(),
                asignacion.getMotivoCambio()
        );
    }
}
