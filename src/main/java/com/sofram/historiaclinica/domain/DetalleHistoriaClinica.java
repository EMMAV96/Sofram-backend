package com.sofram.historiaclinica.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "detalle_historia_clinica")
public class DetalleHistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "historia_clinica_id",
            nullable = false
    )
    private HistoriaClinica historiaClinica;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 1000)
    private String observaciones;

    public DetalleHistoriaClinica() {
    }

    public Long getId() {
        return id;
    }

    public HistoriaClinica getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}