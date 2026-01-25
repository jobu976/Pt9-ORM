package com.gimnasio.service;

import com.gimnasio.dao.EntrenadorDAO;
import com.gimnasio.entity.Entrenador;
import java.util.List;

/**
 * Servicio de Entrenadores
 */
public class EntrenadorService {
    
    private EntrenadorDAO entrenadorDAO = new EntrenadorDAO();

    /**
     * Crear entrenador
     */
    public Entrenador crearEntrenador(Entrenador entrenador) {
        return entrenadorDAO.save(entrenador);
    }

    /**
     * Obtener entrenador por ID
     */
    public Entrenador obtenerEntrenador(Long id) {
        return entrenadorDAO.findById(id);
    }

    /**
     * Listar todos los entrenadores
     */
    public List<Entrenador> listarEntrenadores() {
        return entrenadorDAO.findAll();
    }

    /**
     * Actualizar entrenador
     */
    public Entrenador actualizarEntrenador(Entrenador entrenador) {
        return entrenadorDAO.update(entrenador);
    }

    /**
     * Eliminar entrenador
     */
    public void eliminarEntrenador(Long id) {
        entrenadorDAO.delete(id);
    }
}
