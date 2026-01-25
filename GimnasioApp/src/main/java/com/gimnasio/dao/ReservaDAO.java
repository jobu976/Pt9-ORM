package com.gimnasio.dao;

import com.gimnasio.entity.Reserva;
import com.gimnasio.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

/**
 * DAO para operaciones CRUD de Reserva
 */
public class ReservaDAO {

    /**
     * CREATE - Guardar una nueva reserva
     */
    public Reserva save(Reserva reserva) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(reserva);
            transaction.commit();
            return reserva;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error guardando reserva: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    /**
     * READ - Obtener reserva por ID
     */
    public Reserva findById(Long id) {
        Session session = HibernateUtil.openSession();
        try {
            return session.get(Reserva.class, id);
        } finally {
            session.close();
        }
    }

    /**
     * READ - Obtener todas las reservas
     */
    public List<Reserva> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Reserva", Reserva.class).getResultList();
        } finally {
            session.close();
        }
    }

    /**
     * READ - Obtener reservas por miembro
     */
    public List<Reserva> findByMiembroId(Long miembroId) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                    "FROM Reserva WHERE miembro.id = :miembroId", Reserva.class)
                    .setParameter("miembroId", miembroId)
                    .getResultList();
        } finally {
            session.close();
        }
    }

    /**
     * READ - Obtener reservas por actividad
     */
    public List<Reserva> findByActividadId(Long actividadId) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                    "FROM Reserva WHERE actividad.id = :actividadId", Reserva.class)
                    .setParameter("actividadId", actividadId)
                    .getResultList();
        } finally {
            session.close();
        }
    }

    /**
     * UPDATE - Actualizar reserva
     */
    public Reserva update(Reserva reserva) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(reserva);
            transaction.commit();
            return reserva;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error actualizando reserva: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    /**
     * DELETE - Eliminar reserva
     */
    public void delete(Long id) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Reserva reserva = session.get(Reserva.class, id);
            if (reserva != null) {
                session.delete(reserva);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error eliminando reserva: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
}
