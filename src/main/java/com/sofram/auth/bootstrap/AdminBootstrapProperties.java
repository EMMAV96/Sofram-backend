package com.sofram.auth.bootstrap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Component
@ConfigurationProperties(prefix = "sofram.bootstrap.admin")
public class AdminBootstrapProperties {

    private String username;
    private String password;
    private String nombre = "Administrador";
    private String apellido = "Sistema";
    private String dni = "00000000";
    private LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);
    private String email = "admin@sofram.local";
    private String cargoNombre = "Administrador del sistema";
    private String cargoSector = "Administracion";
    private String cargoEspecialidad = "SOFRAM";

    public boolean isConfigured() {
        return StringUtils.hasText(username) && StringUtils.hasText(password);
    }

    public boolean hasPartialCredentials() {
        return StringUtils.hasText(username) || StringUtils.hasText(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargoNombre() {
        return cargoNombre;
    }

    public void setCargoNombre(String cargoNombre) {
        this.cargoNombre = cargoNombre;
    }

    public String getCargoSector() {
        return cargoSector;
    }

    public void setCargoSector(String cargoSector) {
        this.cargoSector = cargoSector;
    }

    public String getCargoEspecialidad() {
        return cargoEspecialidad;
    }

    public void setCargoEspecialidad(String cargoEspecialidad) {
        this.cargoEspecialidad = cargoEspecialidad;
    }
}
