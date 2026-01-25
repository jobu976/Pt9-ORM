package com.gimnasio.service;

import com.gimnasio.dao.ActividadDAO;
import com.gimnasio.dao.MiembroDAO;
import com.gimnasio.dao.ReservaDAO;
import com.gimnasio.entity.Actividad;
import com.gimnasio.entity.Miembro;
import com.gimnasio.entity.Reserva;
import com.gimnasio.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de Reservas - Gestiona reservas con transacciones
 * GARANTIZA integridad transaccional: ambas operaciones se hacen o ninguna
 */
public class ReservaService {
    
    private ReservaDAO reservaDAO = new ReservaDAO();
    private MiembroDAO miembroDAO = new MiembroDAO();
    private ActividadDAO actividadDAO = new ActividadDAO();

    /**
     * OPERACIÓN TRANSACCIONAL CRÍTICA
     * Reservar una clase: 
     * 1. Crear registro Reserva
     * 2. Restar 1 plaza disponible de la Actividad
     * 3. Restar monto del saldo del Miembro
     * 
     * Si cualquier paso falla, TODO el rollback
     */
    public Reserva reservarClase(Long miembroId, Long actividadId, LocalDateTime fechaClase) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            
            // Paso 1: Obtener miembro y actividad en el contexto de la sesión actual
            Miembro miembro = session.get(Miembro.class, miembroId);
            if (miembro == null) {
                throw new IllegalArgumentException("Miembro no encontrado: " + miembroId);
            }
            
            Actividad actividad = session.get(Actividad.class, actividadId);
            if (actividad == null) {
                throw new IllegalArgumentException("Actividad no encontrada: " + actividadId);
            }
            
            // Paso 2: Validar plazas disponibles
            if (actividad.getPlazasDisponibles() <= 0) {
                throw new IllegalStateException("No hay plazas disponibles en: " + actividad.getNombre());
            }
            
            // Paso 3: Validar saldo del miembro
            if (miembro.getSaldoCuenta() < actividad.getPrecioClase()) {
                throw new IllegalStateException("Saldo insuficiente. Necesita: $" + actividad.getPrecioClase() + 
                        ", tiene: $" + miembro.getSaldoCuenta());
            }
            
            // Paso 4: Crear la reserva
            Reserva reserva = new Reserva(miembro, actividad, fechaClase);
            session.save(reserva);
            
            // Paso 5: Decrementar plazas disponibles
            actividad.setPlazasDisponibles(actividad.getPlazasDisponibles() - 1);
            session.update(actividad);
            
            // Paso 6: Restar saldo del miembro
            miembro.setSaldoCuenta(miembro.getSaldoCuenta() - actividad.getPrecioClase());
            session.update(miembro);
            
            // Si todo va bien, commit
            transaction.commit();
            System.out.println("✓ Reserva confirmada para " + miembro.getNombre() + " en " + actividad.getNombre());
            return reserva;
            
        } catch (Exception e) {
            // Si hay error, ROLLBACK automático
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
                System.out.println("✗ Reserva cancelada (rollback). Razón: " + e.getMessage());
            }
            throw new RuntimeException("Error en la transacción de reserva: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    /**
     * Cancelar una reserva (TRANSACCIÓN): 
     * 1. Cambiar estado a CANCELADA
     * 2. Sumar 1 plaza de nuevo a la Actividad
     * 3. Devolver dinero al Miembro
     */
    public void cancelarReserva(Long reservaId) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            
            Reserva reserva = session.get(Reserva.class, reservaId);
            if (reserva == null) {
                throw new IllegalArgumentException("Reserva no encontrada: " + reservaId);
            }
            
            if ("CANCELADA".equals(reserva.getEstado())) {
                throw new IllegalStateException("La reserva ya estaba cancelada");
            }
            
            // Cambiar estado
            reserva.setEstado("CANCELADA");
            session.update(reserva);
            
            // Recuperar plaza
            Actividad actividad = reserva.getActividad();
            actividad.setPlazasDisponibles(actividad.getPlazasDisponibles() + 1);
            session.update(actividad);
            
            // Devolver dinero
            Miembro miembro = reserva.getMiembro();
            miembro.setSaldoCuenta(miembro.getSaldoCuenta() + reserva.getMontoPagado());
            session.update(miembro);
            
            transaction.commit();
            System.out.println("✓ Reserva cancelada. Reembolso de $" + reserva.getMontoPagado());
            
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error cancelando reserva: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    /**
     * CONSULTA AVANZADA HQL: Listar miembros que se han reservado en una actividad específica
     */
    public List<Miembro> obtenerMiembrosPorActividad(Long actividadId) {
        Session session = HibernateUtil.openSession();
        try {
            // Consulta HQL que obtiene miembros únicos que han reservado en una actividad
            return session.createQuery(
                    "SELECT DISTINCT r.miembro FROM Reserva r WHERE r.actividad.id = :actividadId", 
                    Miembro.class)
                    .setParameter("actividadId", actividadId)
                    .getResultList();
        } finally {
            session.close();
        }
    }

    /**
     * CONSULTA AVANZADA HQL: Contar cuántas reservas tiene un miembro
     */
    public Long contarReservasPorMiembro(Long miembroId) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                    "SELECT COUNT(r) FROM Reserva r WHERE r.miembro.id = :miembroId AND r.estado = 'CONFIRMADA'", 
                    Long.class)
                    .setParameter("miembroId", miembroId)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    /**
     * CONSULTA AVANZADA HQL: Listar actividades llenas (sin plazas)
     */
    public List<Actividad> obtenerActividadesLlenas() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                    "FROM Actividad WHERE plazasDisponibles = 0", 
                    Actividad.class)
                    .getResultList();
        } finally {
            session.close();
        }
    }

    /**
     * CONSULTA AVANZADA HQL: Listar todas las reservas con detalles
     */
    public List<Reserva> obtenerTodasLasReservas() {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery("FROM Reserva ORDER BY fechaReserva DESC", Reserva.class)
                    .getResultList();
        } finally {
            session.close();
        }
    }
}
