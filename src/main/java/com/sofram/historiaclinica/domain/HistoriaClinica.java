package com.sofram.historiaclinica.domain;

import com.sofram.residente.domain.Residente;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "historia_clinica",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_historia_clinica_residente",
                        columnNames = "residente_id"
                )
        }
)
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "residente_id",
            nullable = false,
            unique = true
    )
    private Residente residente;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Column(length = 1000)
    private String observaciones;

    public HistoriaClinica() {
    }

    public Long getId() {
        return id;
    }

    public Residente getResidente() {
        return residente;
    }

    public void setResidente(Residente residente) {
        this.residente = residente;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}