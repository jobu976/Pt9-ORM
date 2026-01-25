package com.gimnasio.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad Entrenador - Representa un entrenador del gimnasio
 * Relación N:M con Actividad (muchos entrenadores pueden dirigir muchas actividades)
 */
@Entity
@Table(name = "entrenadores")
public class Entrenador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 50)
    private String especialidad;
    
    @Column(length = 100)
    private String email;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(name = "experiencia_anos")
    private Integer experienciaAnos;
    
    @ManyToMany(mappedBy = "entrenadores", fetch = FetchType.EAGER)
    private Set<Actividad> actividades = new HashSet<>();

    // Constructores
    public Entrenador() {}

    public Entrenador(String nombre, String especialidad, String email, Integer experienciaAnos) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.email = email;
        this.experienciaAnos = experienciaAnos;
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

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
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

    public Integer getExperienciaAnos() {
        return experienciaAnos;
    }

    public void setExperienciaAnos(Integer experienciaAnos) {
        this.experienciaAnos = experienciaAnos;
    }

    public Set<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(Set<Actividad> actividades) {
        this.actividades = actividades;
    }

    @Override
    public String toString() {
        return "Entrenador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", email='" + email + '\'' +
                ", experienciaAnos=" + experienciaAnos +
                '}';
    }
}
