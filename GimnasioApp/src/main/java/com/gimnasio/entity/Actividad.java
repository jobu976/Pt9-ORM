package com.gimnasio.entity;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad Actividad - Representa una clase/actividad del gimnasio
 * Relación 1:N con Reserva
 * Relación N:M con Entrenador (muchos entrenadores pueden dirigir muchas actividades)
 */
@Entity
@Table(name = "actividades")
public class Actividad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;
    
    @Column(name = "plazas_disponibles", nullable = false)
    private Integer plazasDisponibles;
    
    @Column(name = "precio_clase")
    private Double precioClase;
    
    @Column(name = "hora_inicio")
    private LocalTime horaInicio;
    
    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
        name = "actividad_entrenador",
        joinColumns = @JoinColumn(name = "actividad_id"),
        inverseJoinColumns = @JoinColumn(name = "entrenador_id")
    )
    private Set<Entrenador> entrenadores = new HashSet<>();

    // Constructores
    public Actividad() {}

    public Actividad(String nombre, String descripcion, Integer capacidadMaxima, 
                    Double precioClase, LocalTime horaInicio, Integer duracionMinutos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.capacidadMaxima = capacidadMaxima;
        this.plazasDisponibles = capacidadMaxima;
        this.precioClase = precioClase;
        this.horaInicio = horaInicio;
        this.duracionMinutos = duracionMinutos;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public Integer getPlazasDisponibles() {
        return plazasDisponibles;
    }

    public void setPlazasDisponibles(Integer plazasDisponibles) {
        this.plazasDisponibles = plazasDisponibles;
    }

    public Double getPrecioClase() {
        return precioClase;
    }

    public void setPrecioClase(Double precioClase) {
        this.precioClase = precioClase;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Set<Entrenador> getEntrenadores() {
        return entrenadores;
    }

    public void setEntrenadores(Set<Entrenador> entrenadores) {
        this.entrenadores = entrenadores;
    }

    public void addEntrenador(Entrenador entrenador) {
        this.entrenadores.add(entrenador);
        entrenador.getActividades().add(this);
    }

    @Override
    public String toString() {
        return "Actividad{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", capacidadMaxima=" + capacidadMaxima +
                ", plazasDisponibles=" + plazasDisponibles +
                ", precioClase=" + precioClase +
                ", horaInicio=" + horaInicio +
                '}';
    }
}
