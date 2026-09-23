package com.sofram.actividad.domain;

import com.sofram.personal.domain.Empleado;
import jakarta.persistence.*;

@Entity
@Table(name = "actividad")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "detalle_calendario_id", nullable = false)
    private DetalleCalendario detalleCalendario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 2000)
    private String descripcion;

    @Column(nullable = false, length = 100)
    private String tipo;

    @Column(nullable = false)
    private Integer duracion;

    @Column(name = "cupo_maximo", nullable = false)
    private Integer cupoMaximo;

    @Column(nullable = false, length = 50)
    private String estado;

    protected Actividad() {
    }

    public Actividad(
            DetalleCalendario detalleCalendario,
            Empleado empleado,
            String nombre,
            String descripcion,
            String tipo,
            Integer duracion,
            Integer cupoMaximo,
            String estado
    ) {
        this.detalleCalendario = detalleCalendario;
        this.empleado = empleado;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.duracion = duracion;
        this.cupoMaximo = cupoMaximo;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public DetalleCalendario getDetalleCalendario() {
        return detalleCalendario;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    public String getEstado() {
        return estado;
    }
}