package com.sofram.gestionmedica.domain;

import com.sofram.historiaclinica.domain.DetalleHistoriaClinica;
import jakarta.persistence.*;

@Entity
@Table(name = "diagnostico")
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "detalle_historia_clinica_id", nullable = false)
    private DetalleHistoriaClinica detalleHistoriaClinica;

    @Column(nullable = false, length = 2000)
    private String descripcion;

    protected Diagnostico() {
    }

    public Diagnostico(
            DetalleHistoriaClinica detalleHistoriaClinica,
            String descripcion
    ) {
        this.detalleHistoriaClinica = detalleHistoriaClinica;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public DetalleHistoriaClinica getDetalleHistoriaClinica() {
        return detalleHistoriaClinica;
    }

    public String getDescripcion() {
        return descripcion;
    }
}