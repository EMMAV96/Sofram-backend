package com.sofram.gestionmedica.domain;

import com.sofram.historiaclinica.domain.DetalleHistoriaClinica;
import jakarta.persistence.*;

@Entity
@Table(name = "evaluacion")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "atencion_medica_id", nullable = false, unique = true)
    private AtencionMedica atencionMedica;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "detalle_historia_clinica_id", nullable = false)
    private DetalleHistoriaClinica detalleHistoriaClinica;

    @Column(name = "tipo_evaluacion", nullable = false, length = 100)
    private String tipoEvaluacion;

    @Column(nullable = false, length = 2000)
    private String descripcion;

    @Column(name = "plan_intervencion", length = 2000)
    private String planIntervencion;

    public Evaluacion() {
    }

    public Long getId() {
        return id;
    }

    public AtencionMedica getAtencionMedica() {
        return atencionMedica;
    }

    public DetalleHistoriaClinica getDetalleHistoriaClinica() {
        return detalleHistoriaClinica;
    }

    public String getTipoEvaluacion() {
        return tipoEvaluacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPlanIntervencion() {
        return planIntervencion;
    }

    public void registrar(
            AtencionMedica atencionMedica,
            DetalleHistoriaClinica detalleHistoriaClinica,
            String tipoEvaluacion,
            String descripcion,
            String planIntervencion
    ) {
        this.atencionMedica = atencionMedica;
        this.detalleHistoriaClinica = detalleHistoriaClinica;
        this.tipoEvaluacion = tipoEvaluacion;
        this.descripcion = descripcion;
        this.planIntervencion = planIntervencion;
    }
}