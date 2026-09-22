package com.sofram.gestionmedica.domain;

import com.sofram.historiaclinica.domain.DetalleHistoriaClinica;
import jakarta.persistence.*;

@Entity
@Table(name = "tratamiento")
public class Tratamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "detalle_historia_clinica_id", nullable = false)
    private DetalleHistoriaClinica detalleHistoriaClinica;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 2000)
    private String descripcion;

    protected Tratamiento() {
    }

    public Tratamiento(
            DetalleHistoriaClinica detalleHistoriaClinica,
            String nombre,
            String descripcion
    ) {
        this.detalleHistoriaClinica = detalleHistoriaClinica;
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }
}