package com.gimnasio.dao;

import com.gimnasio.entity.Entrenador;
import com.gimnasio.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

/**
 * DAO para operaciones CRUD de Entrenador
 */
public class EntrenadorDAO {

    /**
     * CREATE - Guardar un nuevo entrenador
     */
    public Entrenador save(Entrenador entrenador) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(entrenador);
            transaction.commit();
            return entrenador;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error guardando entrenador: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    /**
     * READ - Obtener entrenador por ID
     */
    public Entrenador findById(Long id) {
        Session session = HibernateUtil.openSession();
        try {
            return session.get(Entrenador.class, id);
        } finally {
            session.close();
        }
    }

    /**
     * READ - Obtener todos los entrenadores
     */
    public List<Entrenador> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Entrenador", Entrenador.class).getResultList();
        } finally {
            session.close();
        }
    }

    /**
     * UPDATE - Actualizar entrenador
     */
    public Entrenador update(Entrenador entrenador) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(entrenador);
            transaction.commit();
            return entrenador;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error actualizando entrenador: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    /**
     * DELETE - Eliminar entrenador
     */
    public void delete(Long id) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Entrenador entrenador = session.get(Entrenador.class, id);
            if (entrenador != null) {
                session.delete(entrenador);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error eliminando entrenador: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
}
