package com.gimnasio.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Reserva - Representa la reserva de una clase por un miembro
 * Relaciones N:1 con Miembro y Actividad
 */
@Entity
@Table(name = "reservas")
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "miembro_id", nullable = false)
    private Miembro miembro;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;
    
    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;
    
    @Column(name = "fecha_clase")
    private LocalDateTime fechaClase;
    
    @Column(name = "estado", length = 20)
    private String estado; // CONFIRMADA, CANCELADA, ASISTIO
    
    @Column(name = "monto_pagado")
    private Double montoPagado;

    // Constructores
    public Reserva() {}

    public Reserva(Miembro miembro, Actividad actividad, LocalDateTime fechaClase) {
        this.miembro = miembro;
        this.actividad = actividad;
        this.fechaReserva = LocalDateTime.now();
        this.fechaClase = fechaClase;
        this.estado = "CONFIRMADA";
        this.montoPagado = actividad.getPrecioClase();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Miembro getMiembro() {
        return miembro;
    }

    public void setMiembro(Miembro miembro) {
        this.miembro = miembro;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public LocalDateTime getFechaClase() {
        return fechaClase;
    }

    public void setFechaClase(LocalDateTime fechaClase) {
        this.fechaClase = fechaClase;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(Double montoPagado) {
        this.montoPagado = montoPagado;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", miembro=" + miembro.getNombre() +
                ", actividad=" + actividad.getNombre() +
                ", estado='" + estado + '\'' +
                ", montoPagado=" + montoPagado +
                '}';
    }
}
