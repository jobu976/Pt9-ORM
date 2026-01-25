package com.gimnasio.app;

import com.gimnasio.entity.*;
import com.gimnasio.service.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

/**
 * Aplicación Console - Menú principal para demostrar funcionalidad CRUD y transacciones
 */
public class GimnasioApp {
    
    private MiembroService miembroService = new MiembroService();
    private ActividadService actividadService = new ActividadService();
    private ReservaService reservaService = new ReservaService();
    private EntrenadorService entrenadorService = new EntrenadorService();
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        GimnasioApp app = new GimnasioApp();
        app.inicializarDatos();
        app.mostrarMenuPrincipal();
    }

    /**
     * Inicializar datos de prueba
     */
    private void inicializarDatos() {
        System.out.println("\n========================================");
        System.out.println("INICIALIZANDO DATOS DE PRUEBA...");
        System.out.println("========================================\n");
        
        // Crear entrenadores
        Entrenador e1 = entrenadorService.crearEntrenador(
            new Entrenador("Carlos Martínez", "Musculación", "carlos@gym.com", 8)
        );
        Entrenador e2 = entrenadorService.crearEntrenador(
            new Entrenador("Laura Pérez", "Cardio", "laura@gym.com", 5)
        );
        
        System.out.println("✓ Entrenadores creados: " + e1.getNombre() + ", " + e2.getNombre());
        
        // Crear actividades
        Actividad a1 = actividadService.crearActividad(
            new Actividad("Pesas", "Entrenamiento de fuerza", 15, 5.0, LocalTime.of(9, 0), 60)
        );
        Actividad a2 = actividadService.crearActividad(
            new Actividad("Cardio", "Máquinas cardiovasculares", 20, 3.0, LocalTime.of(10, 0), 45)
        );
        Actividad a3 = actividadService.crearActividad(
            new Actividad("Yoga", "Yoga y flexibilidad", 10, 4.0, LocalTime.of(18, 0), 60)
        );
        
        System.out.println("✓ Actividades creadas: " + a1.getNombre() + ", " + a2.getNombre() + ", " + a3.getNombre());
        
        // Asignar entrenadores a actividades
        actividadService.asignarEntrenador(a1.getId(), e1.getId());
        actividadService.asignarEntrenador(a2.getId(), e2.getId());
        System.out.println("✓ Entrenadores asignados a actividades");
        
        // Crear miembros
        Miembro m1 = miembroService.registrarMiembro(
            new Miembro("Juan López", "juan@email.com", "555-0001", 100.0)
        );
        Miembro m2 = miembroService.registrarMiembro(
            new Miembro("María García", "maria@email.com", "555-0002", 50.0)
        );
        Miembro m3 = miembroService.registrarMiembro(
            new Miembro("Pedro Rodríguez", "pedro@email.com", "555-0003", 200.0)
        );
        
        System.out.println("✓ Miembros registrados: " + m1.getNombre() + ", " + m2.getNombre() + ", " + m3.getNombre());
        System.out.println("\n========================================\n");
    }

    /**
     * Menú principal
     */
    private void mostrarMenuPrincipal() {
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║    SISTEMA DE GESTIÓN DEL GIMNASIO     ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Gestionar Miembros");
            System.out.println("2. Gestionar Actividades");
            System.out.println("3. Gestionar Reservas");
            System.out.println("4. Consultas Avanzadas");
            System.out.println("5. Salir");
            System.out.print("\nSeleccione opción: ");
            
            int opcion = leerEntero();
            
            switch (opcion) {
                case 1:
                    menuMiembros();
                    break;
                case 2:
                    menuActividades();
                    break;
                case 3:
                    menuReservas();
                    break;
                case 4:
                    menuConsultasAvanzadas();
                    break;
                case 5:
                    System.out.println("\n¡Hasta luego!");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    /**
     * Menú de Miembros
     */
    private void menuMiembros() {
        System.out.println("\n--- GESTIÓN DE MIEMBROS ---");
        System.out.println("1. Registrar nuevo miembro");
        System.out.println("2. Ver miembro por ID");
        System.out.println("3. Listar todos los miembros");
        System.out.println("4. Actualizar miembro");
        System.out.println("5. Agregar saldo");
        System.out.println("6. Eliminar miembro");
        System.out.println("7. Volver");
        System.out.print("Seleccione opción: ");
        
        int opcion = leerEntero();
        
        switch (opcion) {
            case 1:
                registrarMiembro();
                break;
            case 2:
                verMiembro();
                break;
            case 3:
                listarMiembros();
                break;
            case 4:
                actualizarMiembro();
                break;
            case 5:
                agregarSaldo();
                break;
            case 6:
                eliminarMiembro();
                break;
            case 7:
                break;
            default:
                System.out.println("Opción inválida");
        }
    }

    private void registrarMiembro() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();
        System.out.print("Saldo inicial: $");
        double saldo = leerDouble();
        
        Miembro miembro = new Miembro(nombre, email, telefono, saldo);
        Miembro creado = miembroService.registrarMiembro(miembro);
        System.out.println("✓ Miembro registrado con ID: " + creado.getId());
    }

    private void verMiembro() {
        System.out.print("ID del miembro: ");
        Long id = leerLong();
        Miembro miembro = miembroService.obtenerMiembro(id);
        
        if (miembro != null) {
            System.out.println("\n" + miembro);
        } else {
            System.out.println("Miembro no encontrado");
        }
    }

    private void listarMiembros() {
        List<Miembro> miembros = miembroService.listarMiembros();
        System.out.println("\n--- LISTA DE MIEMBROS ---");
        if (miembros.isEmpty()) {
            System.out.println("No hay miembros registrados");
        } else {
            for (Miembro m : miembros) {
                System.out.println(m);
            }
        }
    }

    private void actualizarMiembro() {
        System.out.print("ID del miembro a actualizar: ");
        Long id = leerLong();
        Miembro miembro = miembroService.obtenerMiembro(id);
        
        if (miembro != null) {
            System.out.print("Nuevo nombre (actual: " + miembro.getNombre() + "): ");
            String nombre = scanner.nextLine();
            if (!nombre.isEmpty()) miembro.setNombre(nombre);
            
            Miembro actualizado = miembroService.actualizarMiembro(miembro);
            System.out.println("✓ Miembro actualizado");
        } else {
            System.out.println("Miembro no encontrado");
        }
    }

    private void agregarSaldo() {
        System.out.print("ID del miembro: ");
        Long id = leerLong();
        System.out.print("Monto a agregar: $");
        double monto = leerDouble();
        
        try {
            miembroService.agregarSaldo(id, monto);
            System.out.println("✓ Saldo agregado");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarMiembro() {
        System.out.print("ID del miembro a eliminar: ");
        Long id = leerLong();
        
        try {
            miembroService.eliminarMiembro(id);
            System.out.println("✓ Miembro eliminado");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Menú de Actividades
     */
    private void menuActividades() {
        System.out.println("\n--- GESTIÓN DE ACTIVIDADES ---");
        System.out.println("1. Crear nueva actividad");
        System.out.println("2. Ver actividad por ID");
        System.out.println("3. Listar todas las actividades");
        System.out.println("4. Listar actividades con plazas disponibles");
        System.out.println("5. Volver");
        System.out.print("Seleccione opción: ");
        
        int opcion = leerEntero();
        
        switch (opcion) {
            case 1:
                crearActividad();
                break;
            case 2:
                verActividad();
                break;
            case 3:
                listarActividades();
                break;
            case 4:
                listarActividadesDisponibles();
                break;
            case 5:
                break;
            default:
                System.out.println("Opción inválida");
        }
    }

    private void crearActividad() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        System.out.print("Capacidad máxima: ");
        int capacidad = leerEntero();
        System.out.print("Precio de la clase: $");
        double precio = leerDouble();
        System.out.print("Hora de inicio (HH): ");
        int hora = leerEntero();
        System.out.print("Minuto: ");
        int minuto = leerEntero();
        System.out.print("Duración en minutos: ");
        int duracion = leerEntero();
        
        Actividad actividad = new Actividad(nombre, descripcion, capacidad, precio, 
                LocalTime.of(hora, minuto), duracion);
        Actividad creada = actividadService.crearActividad(actividad);
        System.out.println("✓ Actividad creada con ID: " + creada.getId());
    }

    private void verActividad() {
        System.out.print("ID de la actividad: ");
        Long id = leerLong();
        Actividad actividad = actividadService.obtenerActividad(id);
        
        if (actividad != null) {
            System.out.println("\n" + actividad);
            System.out.println("Entrenadores: " + actividad.getEntrenadores().size());
        } else {
            System.out.println("Actividad no encontrada");
        }
    }

    private void listarActividades() {
        List<Actividad> actividades = actividadService.listarActividades();
        System.out.println("\n--- LISTA DE ACTIVIDADES ---");
        if (actividades.isEmpty()) {
            System.out.println("No hay actividades");
        } else {
            for (Actividad a : actividades) {
                System.out.println(a);
            }
        }
    }

    private void listarActividadesDisponibles() {
        List<Actividad> actividades = actividadService.listarActividadesDisponibles();
        System.out.println("\n--- ACTIVIDADES CON PLAZAS DISPONIBLES ---");
        if (actividades.isEmpty()) {
            System.out.println("No hay actividades disponibles");
        } else {
            for (Actividad a : actividades) {
                System.out.println(a + " (Plazas: " + a.getPlazasDisponibles() + "/" + a.getCapacidadMaxima() + ")");
            }
        }
    }

    /**
     * Menú de Reservas - OPERACIONES TRANSACCIONALES
     */
    private void menuReservas() {
        System.out.println("\n--- GESTIÓN DE RESERVAS ---");
        System.out.println("1. Reservar una clase (OPERACIÓN TRANSACCIONAL)");
        System.out.println("2. Ver reserva por ID");
        System.out.println("3. Cancelar reserva (OPERACIÓN TRANSACCIONAL)");
        System.out.println("4. Listar todas las reservas");
        System.out.println("5. Volver");
        System.out.print("Seleccione opción: ");
        
        int opcion = leerEntero();
        
        switch (opcion) {
            case 1:
                reservarClase();
                break;
            case 2:
                verReserva();
                break;
            case 3:
                cancelarReserva();
                break;
            case 4:
                listarReservas();
                break;
            case 5:
                break;
            default:
                System.out.println("Opción inválida");
        }
    }

    private void reservarClase() {
        System.out.print("ID del miembro: ");
        Long miembroId = leerLong();
        System.out.print("ID de la actividad: ");
        Long actividadId = leerLong();
        
        try {
            reservaService.reservarClase(miembroId, actividadId, LocalDateTime.now().plusDays(7));
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void verReserva() {
        System.out.print("ID de la reserva: ");
        Long id = leerLong();
        
        // Obtener reservas para demostrar
        List<Reserva> reservas = reservaService.obtenerTodasLasReservas();
        Reserva encontrada = reservas.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
        
        if (encontrada != null) {
            System.out.println("\n" + encontrada);
        } else {
            System.out.println("Reserva no encontrada");
        }
    }

    private void cancelarReserva() {
        System.out.print("ID de la reserva a cancelar: ");
        Long id = leerLong();
        
        try {
            reservaService.cancelarReserva(id);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void listarReservas() {
        List<Reserva> reservas = reservaService.obtenerTodasLasReservas();
        System.out.println("\n--- LISTA DE RESERVAS ---");
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas");
        } else {
            for (Reserva r : reservas) {
                System.out.println(r);
            }
        }
    }

    /**
     * Menú de Consultas Avanzadas (HQL)
     */
    private void menuConsultasAvanzadas() {
        System.out.println("\n--- CONSULTAS AVANZADAS (HQL) ---");
        System.out.println("1. Listar miembros de una actividad específica");
        System.out.println("2. Contar reservas de un miembro");
        System.out.println("3. Listar actividades llenas (sin plazas)");
        System.out.println("4. Volver");
        System.out.print("Seleccione opción: ");
        
        int opcion = leerEntero();
        
        switch (opcion) {
            case 1:
                listarMiembrosPorActividad();
                break;
            case 2:
                contarReservasMiembro();
                break;
            case 3:
                listarActividadesLlenas();
                break;
            case 4:
                break;
            default:
                System.out.println("Opción inválida");
        }
    }

    private void listarMiembrosPorActividad() {
        System.out.print("ID de la actividad: ");
        Long actividadId = leerLong();
        
        List<Miembro> miembros = reservaService.obtenerMiembrosPorActividad(actividadId);
        System.out.println("\n--- MIEMBROS RESERVADOS EN ESTA ACTIVIDAD ---");
        if (miembros.isEmpty()) {
            System.out.println("No hay miembros registrados");
        } else {
            for (Miembro m : miembros) {
                System.out.println("- " + m.getNombre() + " (" + m.getEmail() + ")");
            }
        }
    }

    private void contarReservasMiembro() {
        System.out.print("ID del miembro: ");
        Long miembroId = leerLong();
        
        Long cantidad = reservaService.contarReservasPorMiembro(miembroId);
        System.out.println("\nReservas activas: " + cantidad);
    }

    private void listarActividadesLlenas() {
        List<Actividad> actividades = reservaService.obtenerActividadesLlenas();
        System.out.println("\n--- ACTIVIDADES SIN PLAZAS DISPONIBLES ---");
        if (actividades.isEmpty()) {
            System.out.println("No hay actividades llenas");
        } else {
            for (Actividad a : actividades) {
                System.out.println("- " + a.getNombre() + " (Capacidad: " + a.getCapacidadMaxima() + ")");
            }
        }
    }

    // Utilidades para leer entrada
    private int leerEntero() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private Long leerLong() {
        try {
            return Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    private double leerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
