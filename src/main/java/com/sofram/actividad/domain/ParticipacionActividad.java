package com.sofram.actividad.domain;

import com.sofram.residente.domain.Residente;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "participacion_actividad")
public class ParticipacionActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "residente_id", nullable = false)
    private Residente residente;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private Boolean asistencia;

    @Column(nullable = false, length = 50)
    private String estado;

    @Column(length = 1000)
    private String observaciones;

    protected ParticipacionActividad() {
    }

    public ParticipacionActividad(
            Actividad actividad,
            Residente residente,
            LocalDate fecha,
            Boolean asistencia,
            String estado,
            String observaciones
    ) {
        this.actividad = actividad;
        this.residente = residente;
        this.fecha = fecha;
        this.asistencia = asistencia;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public Residente getResidente() {
        return residente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Boolean getAsistencia() {
        return asistencia;
    }

    public String getEstado() {
        return estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void actualizarAsistencia(
            Boolean asistencia,
            String estado,
            String observaciones
    ) {
        this.asistencia = asistencia;
        this.estado = estado;
        this.observaciones = observaciones;
    }
}