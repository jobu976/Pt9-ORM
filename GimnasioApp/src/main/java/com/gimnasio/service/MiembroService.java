package com.gimnasio.service;

import com.gimnasio.dao.MiembroDAO;
import com.gimnasio.entity.Miembro;
import java.util.List;

/**
 * Servicio de Miembros - Gestiona miembros del gimnasio
 */
public class MiembroService {
    
    private MiembroDAO miembroDAO = new MiembroDAO();

    /**
     * Registrar nuevo miembro
     */
    public Miembro registrarMiembro(Miembro miembro) {
        return miembroDAO.save(miembro);
    }

    /**
     * Obtener miembro por ID
     */
    public Miembro obtenerMiembro(Long id) {
        return miembroDAO.findById(id);
    }

    /**
     * Obtener miembro por email
     */
    public Miembro obtenerMiembroPorEmail(String email) {
        return miembroDAO.findByEmail(email);
    }

    /**
     * Listar todos los miembros
     */
    public List<Miembro> listarMiembros() {
        return miembroDAO.findAll();
    }

    /**
     * Actualizar miembro
     */
    public Miembro actualizarMiembro(Miembro miembro) {
        return miembroDAO.update(miembro);
    }

    /**
     * Eliminar miembro
     */
    public void eliminarMiembro(Long id) {
        miembroDAO.delete(id);
    }

    /**
     * Agregar saldo a un miembro (recarga de cuenta)
     */
    public void agregarSaldo(Long miembroId, Double monto) {
        Miembro miembro = miembroDAO.findById(miembroId);
        if (miembro != null) {
            miembro.setSaldoCuenta(miembro.getSaldoCuenta() + monto);
            miembroDAO.update(miembro);
        }
    }
}
