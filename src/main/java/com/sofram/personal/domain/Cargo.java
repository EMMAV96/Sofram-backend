package com.sofram.personal.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "cargo")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String sector;

    @Column(length = 50)
    private String matricula;

    @Column(length = 100)
    private String especialidad;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    protected Cargo() {
    }

    public Cargo(String nombre, String sector, String matricula, String especialidad) {
        this.nombre = nombre;
        this.sector = sector;
        this.matricula = matricula;
        this.especialidad = especialidad;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSector() {
        return sector;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void actualizar(String nombre, String sector, String matricula, String especialidad) {
        this.nombre = nombre;
        this.sector = sector;
        this.matricula = matricula;
        this.especialidad = especialidad;
    }
}
