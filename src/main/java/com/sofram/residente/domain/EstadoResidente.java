package com.sofram.residente.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "estado_residente")
public class EstadoResidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    public EstadoResidente() {
    }

    public EstadoResidente(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}