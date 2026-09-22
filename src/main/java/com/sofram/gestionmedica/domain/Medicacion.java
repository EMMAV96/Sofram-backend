package com.sofram.gestionmedica.domain;

import com.sofram.historiaclinica.domain.DetalleHistoriaClinica;
import jakarta.persistence.*;

@Entity
@Table(name = "medicacion")
public class Medicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "detalle_historia_clinica_id", nullable = false)
    private DetalleHistoriaClinica detalleHistoriaClinica;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String dosis;

    @Column(nullable = false, length = 150)
    private String frecuencia;

    protected Medicacion() {
    }

    public Medicacion(
            DetalleHistoriaClinica detalleHistoriaClinica,
            String nombre,
            String dosis,
            String frecuencia
    ) {
        this.detalleHistoriaClinica = detalleHistoriaClinica;
        this.nombre = nombre;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
    }

    public Long getId() {
        return id;
    }

    public DetalleHistoriaClinica getDetalleHistoriaClinica() {
        return detalleHistoriaClinica;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDosis() {
        return dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }
}