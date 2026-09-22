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

    @Column(name = "antecedentes_personales", length = 2000)
    private String antecedentesPersonales;

    @Column(name = "antecedentes_familiares", length = 2000)
    private String antecedentesFamiliares;

    @Column(length = 2000)
    private String alergias;

    public HistoriaClinica() {
    }

    public void actualizarAntecedentesYAlergias(
            String antecedentesPersonales,
            String antecedentesFamiliares,
            String alergias
    ) {
        this.antecedentesPersonales = antecedentesPersonales;
        this.antecedentesFamiliares = antecedentesFamiliares;
        this.alergias = alergias;
    }

    public Long getId() {
        return id;
    }

    public Residente getResidente() {
        return residente;
    }

    public String getAntecedentesPersonales() {
        return antecedentesPersonales;
    }

    public String getAntecedentesFamiliares() {
        return antecedentesFamiliares;
    }

    public String getAlergias() {
        return alergias;
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