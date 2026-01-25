package com.gimnasio.dao;

import com.gimnasio.entity.Miembro;
import com.gimnasio.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

/**
 * DAO para operaciones CRUD de Miembro
 */
public class MiembroDAO {

    /**
     * CREATE - Guardar un nuevo miembro
     */
    public Miembro save(Miembro miembro) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(miembro);
            transaction.commit();
            return miembro;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error guardando miembro: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    /**
     * READ - Obtener miembro por ID
     */
    public Miembro findById(Long id) {
        Session session = HibernateUtil.openSession();
        try {
            return session.get(Miembro.class, id);
        } finally {
            session.close();
        }
    }

    /**
     * READ - Obtener todos los miembros
     */
    public List<Miembro> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Miembro", Miembro.class).getResultList();
        } finally {
            session.close();
        }
    }

    /**
     * READ - Obtener miembro por email
     */
    public Miembro findByEmail(String email) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Miembro WHERE email = :email", Miembro.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    /**
     * UPDATE - Actualizar miembro
     */
    public Miembro update(Miembro miembro) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(miembro);
            transaction.commit();
            return miembro;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error actualizando miembro: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    /**
     * DELETE - Eliminar miembro
     */
    public void delete(Long id) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Miembro miembro = session.get(Miembro.class, id);
            if (miembro != null) {
                session.delete(miembro);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error eliminando miembro: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
}
