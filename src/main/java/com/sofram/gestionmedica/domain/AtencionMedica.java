package com.sofram.gestionmedica.domain;

import com.sofram.personal.domain.Empleado;
import com.sofram.residente.domain.Residente;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "atencion_medica")
public class AtencionMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "residente_id", nullable = false)
    private Residente residente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 500)
    private String motivo;

    @Column(name = "tipo_intervencion", length = 100)
    private String tipoIntervencion;

    @Column(length = 1000)
    private String observaciones;

    public AtencionMedica() {
    }

    public Long getId() {
        return id;
    }

    public Residente getResidente() {
        return residente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getTipoIntervencion() {
        return tipoIntervencion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void registrar(
            Residente residente,
            Empleado empleado,
            LocalDate fecha,
            String motivo,
            String tipoIntervencion,
            String observaciones
    ) {
        this.residente = residente;
        this.empleado = empleado;
        this.fecha = fecha;
        this.motivo = motivo;
        this.tipoIntervencion = tipoIntervencion;
        this.observaciones = observaciones;
    }
}