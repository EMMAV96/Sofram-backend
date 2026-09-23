package com.sofram.actividad.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "calendario")
public class Calendario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String periodo;

    @Column(nullable = false)
    private Integer anio;

    @Column(nullable = false, length = 50)
    private String estado;

    protected Calendario() {
    }

    public Calendario(
            String nombre,
            String periodo,
            Integer anio,
            String estado
    ) {
        this.nombre = nombre;
        this.periodo = periodo;
        this.anio = anio;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPeriodo() {
        return periodo;
    }

    public Integer getAnio() {
        return anio;
    }

    public String getEstado() {
        return estado;
    }
}