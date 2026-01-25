package com.gimnasio.service;

import com.gimnasio.dao.ActividadDAO;
import com.gimnasio.dao.EntrenadorDAO;
import com.gimnasio.entity.Actividad;
import com.gimnasio.entity.Entrenador;
import java.util.List;

/**
 * Servicio de Actividades - Gestiona actividades y entrenadores
 */
public class ActividadService {
    
    private ActividadDAO actividadDAO = new ActividadDAO();
    private EntrenadorDAO entrenadorDAO = new EntrenadorDAO();

    /**
     * Crear actividad
     */
    public Actividad crearActividad(Actividad actividad) {
        return actividadDAO.save(actividad);
    }

    /**
     * Obtener actividad por ID
     */
    public Actividad obtenerActividad(Long id) {
        return actividadDAO.findById(id);
    }

    /**
     * Listar todas las actividades
     */
    public List<Actividad> listarActividades() {
        return actividadDAO.findAll();
    }

    /**
     * Listar actividades con plazas disponibles
     */
    public List<Actividad> listarActividadesDisponibles() {
        return actividadDAO.findWithAvailableSpaces();
    }

    /**
     * Actualizar actividad
     */
    public Actividad actualizarActividad(Actividad actividad) {
        return actividadDAO.update(actividad);
    }

    /**
     * Eliminar actividad
     */
    public void eliminarActividad(Long id) {
        actividadDAO.delete(id);
    }

    /**
     * Asignar entrenador a actividad
     */
    public void asignarEntrenador(Long actividadId, Long entrenadorId) {
        Actividad actividad = actividadDAO.findById(actividadId);
        Entrenador entrenador = entrenadorDAO.findById(entrenadorId);
        
        if (actividad != null && entrenador != null) {
            actividad.addEntrenador(entrenador);
            actividadDAO.update(actividad);
        }
    }
}
