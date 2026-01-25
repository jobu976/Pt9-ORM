package com.gimnasio.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Miembro - Representa un socio del gimnasio
 * Relación 1:N con Reserva (Un miembro puede tener múltiples reservas)
 */
@Entity
@Table(name = "miembros")
public class Miembro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDateTime fechaInscripcion;
    
    @Column(name = "saldo_cuenta")
    private Double saldoCuenta;
    
    @Column(name = "activo")
    private Boolean activo;

    // Constructores
    public Miembro() {}

    public Miembro(String nombre, String email, String telefono, Double saldoCuenta) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.fechaInscripcion = LocalDateTime.now();
        this.saldoCuenta = saldoCuenta;
        this.activo = true;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Double getSaldoCuenta() {
        return saldoCuenta;
    }

    public void setSaldoCuenta(Double saldoCuenta) {
        this.saldoCuenta = saldoCuenta;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Miembro{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", saldoCuenta=" + saldoCuenta +
                ", activo=" + activo +
                '}';
    }
}
