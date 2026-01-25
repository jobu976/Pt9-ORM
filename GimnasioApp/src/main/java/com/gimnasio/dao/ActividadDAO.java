package com.gimnasio.dao;

import com.gimnasio.entity.Actividad;
import com.gimnasio.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

/**
 * DAO para operaciones CRUD de Actividad
 */
public class ActividadDAO {

    /**
     * CREATE - Guardar una nueva actividad
     */
    public Actividad save(Actividad actividad) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(actividad);
            transaction.commit();
            return actividad;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error guardando actividad: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    /**
     * READ - Obtener actividad por ID
     */
    public Actividad findById(Long id) {
        Session session = HibernateUtil.openSession();
        try {
            return session.get(Actividad.class, id);
        } finally {
            session.close();
        }
    }

    /**
     * READ - Obtener todas las actividades
     */
    public List<Actividad> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Actividad", Actividad.class).getResultList();
        } finally {
            session.close();
        }
    }

    /**
     * READ - Obtener actividades con plazas disponibles
     */
    public List<Actividad> findWithAvailableSpaces() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                    "FROM Actividad WHERE plazasDisponibles > 0", Actividad.class)
                    .getResultList();
        } finally {
            session.close();
        }
    }

    /**
     * UPDATE - Actualizar actividad
     */
    public Actividad update(Actividad actividad) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(actividad);
            transaction.commit();
            return actividad;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error actualizando actividad: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    /**
     * DELETE - Eliminar actividad
     */
    public void delete(Long id) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Actividad actividad = session.get(Actividad.class, id);
            if (actividad != null) {
                session.delete(actividad);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error eliminando actividad: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
}
