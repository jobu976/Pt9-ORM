# Resumen de Criterios Cubiertos - Proyecto Gimnasio

## ✅ Requisitos Técnicos Indispensables

### 1. Configuración ✓
- **Herramienta ORM:** Hibernate 5.6.15 + JPA 2.2
- **Base de datos:** H2 (en memoria para facilitar testing)
- **Archivo de configuración:** `src/main/resources/hibernate.cfg.xml`
- **Dependencias Maven:** Configuradas en `pom.xml`
- **SessionFactory:** Gestión centralizada en `HibernateUtil.java`

**Demostración:**
```
Cuando se ejecuta GimnasioApp → Hibernate inicializa automáticamente
→ Crea tablas en H2
→ Carga configuración de hibernate.cfg.xml
→ Establece conexión jdbc:h2:mem:gimnasio
```

---

## ✅ Modelo de Datos (Criterio 3.1-3.3)

### Mínimo 3 Entidades Relacionadas ✓

```
✓ MIEMBRO          (Soci)      → Entity con 8 propiedades
✓ ACTIVIDAD        (Activity)  → Entity con 8 propiedades + N:M
✓ RESERVA          (Booking)   → Entity crítica con 2 FK
✓ ENTRENADOR       (Trainer)   → Entity adicional con N:M
```

**Archivos de entidades:**
- [entity/Miembro.java](src/main/java/com/gimnasio/entity/Miembro.java)
- [entity/Actividad.java](src/main/java/com/gimnasio/entity/Actividad.java)
- [entity/Reserva.java](src/main/java/com/gimnasio/entity/Reserva.java)
- [entity/Entrenador.java](src/main/java/com/gimnasio/entity/Entrenador.java)

### Relaciones Obligatorias ✓

#### 1:N (Uno a Muchos) ✓
```
Miembro (1) ────────── (N) Reserva
   ↓
Un miembro puede tener múltiples reservas
   ↓
Implementación: Miembro-Reserva con @ManyToOne en Reserva
Código: 
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "miembro_id", nullable = false)
  private Miembro miembro;
```

#### N:M (Muchos a Muchos) ✓
```
Actividad (N) ────────── (M) Entrenador
   ↓
Una actividad puede tener múltiples entrenadores
Un entrenador puede dirigir múltiples actividades
   ↓
Implementación: @ManyToMany con @JoinTable
Código:
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
    name = "actividad_entrenador",
    joinColumns = @JoinColumn(name = "actividad_id"),
    inverseJoinColumns = @JoinColumn(name = "entrenador_id")
  )
  private Set<Entrenador> entrenadores;
```

### Anotaciones JPA/Hibernate ✓

| Anotación | Ubicación | Propósito |
|-----------|-----------|----------|
| `@Entity` | Miembro, Actividad, Reserva, Entrenador | Define clase persistente |
| `@Table` | Todas las entidades | Especifica nombre de tabla |
| `@Id` | Todas (campo id) | Clave primaria |
| `@GeneratedValue` | Todas (estrategia IDENTITY) | Auto-incremento |
| `@Column` | Todos los atributos | Restricciones: nullable, unique, length |
| `@ManyToOne` | Reserva.miembro, Reserva.actividad | Relación N:1 |
| `@JoinColumn` | Reserva | Define FK |
| `@ManyToMany` | Actividad.entrenadores | Relación N:M |
| `@JoinTable` | Actividad.entrenadores | Tabla intermedia |
| `@FetchType.EAGER` | Reserva | Carga inmediata de relaciones |

---

## ✅ CRUD Completo (Criterio 3.4)

### CREATE - Guardar nuevos objetos ✓

**DAO:** [MiembroDAO.java](src/main/java/com/gimnasio/dao/MiembroDAO.java) (línea `save`)

```java
public Miembro save(Miembro miembro) {
    Session session = HibernateUtil.openSession();
    Transaction transaction = null;
    try {
        transaction = session.beginTransaction();
        session.save(miembro);  // ← CREATE
        transaction.commit();
        return miembro;
    } catch (Exception e) {
        if (transaction != null) transaction.rollback();
        throw new RuntimeException("Error guardando miembro", e);
    } finally {
        session.close();
    }
}
```

**Ejemplos implementados:**
- ✓ Guardar Miembro
- ✓ Guardar Actividad
- ✓ Guardar Entrenador
- ✓ Guardar Reserva (con transacción)

**Menú:** `1. Gestionar Miembros → 1. Registrar nuevo miembro`

---

### READ - Recuperar objetos ✓

**DAO:** [MiembroDAO.java](src/main/java/com/gimnasio/dao/MiembroDAO.java)

**Por ID:**
```java
public Miembro findById(Long id) {
    Session session = HibernateUtil.openSession();
    try {
        return session.get(Miembro.class, id);  // ← READ by ID
    } finally {
        session.close();
    }
}
```

**Listar todos:**
```java
public List<Miembro> findAll() {
    Session session = HibernateUtil.openSession();
    try {
        return session.createQuery("FROM Miembro", Miembro.class)
                      .getResultList();  // ← READ all
    } finally {
        session.close();
    }
}
```

**Ejemplos implementados:**
- ✓ Obtener Miembro por ID
- ✓ Obtener Miembro por Email
- ✓ Listar todos los Miembros
- ✓ Listar Actividades disponibles
- ✓ Listar Reservas por Miembro
- ✓ Listar Reservas por Actividad

**Menú:** `1. Gestionar Miembros → 2. Ver miembro por ID` o `3. Listar todos`

---

### UPDATE - Modificar objetos ✓

**DAO:** [MiembroDAO.java](src/main/java/com/gimnasio/dao/MiembroDAO.java)

```java
public Miembro update(Miembro miembro) {
    Session session = HibernateUtil.openSession();
    Transaction transaction = null;
    try {
        transaction = session.beginTransaction();
        session.update(miembro);  // ← UPDATE
        transaction.commit();
        return miembro;
    } catch (Exception e) {
        if (transaction != null) transaction.rollback();
        throw new RuntimeException("Error actualizando miembro", e);
    } finally {
        session.close();
    }
}
```

**Ejemplos implementados:**
- ✓ Actualizar nombre de Miembro
- ✓ Agregar saldo (recarga)
- ✓ Actualizar Actividad
- ✓ Cambiar estado de Reserva

**Menú:** `1. Gestionar Miembros → 4. Actualizar miembro`

---

### DELETE - Eliminar objetos ✓

**DAO:** [MiembroDAO.java](src/main/java/com/gimnasio/dao/MiembroDAO.java)

```java
public void delete(Long id) {
    Session session = HibernateUtil.openSession();
    Transaction transaction = null;
    try {
        transaction = session.beginTransaction();
        Miembro miembro = session.get(Miembro.class, id);
        if (miembro != null) {
            session.delete(miembro);  // ← DELETE
        }
        transaction.commit();
    } catch (Exception e) {
        if (transaction != null) transaction.rollback();
        throw new RuntimeException("Error eliminando miembro", e);
    } finally {
        session.close();
    }
}
```

**Ejemplos implementados:**
- ✓ Eliminar Miembro
- ✓ Eliminar Actividad
- ✓ Eliminar Entrenador

**Menú:** `1. Gestionar Miembros → 6. Eliminar miembro`

---

## ✅ Consultas Avanzadas HQL (Criterio 3.5)

### Consulta 1: Listar Miembros por Actividad ✓

**Archivo:** [ReservaService.java](src/main/java/com/gimnasio/service/ReservaService.java) (línea `obtenerMiembrosPorActividad`)

```java
public List<Miembro> obtenerMiembrosPorActividad(Long actividadId) {
    Session session = HibernateUtil.openSession();
    try {
        return session.createQuery(
                "SELECT DISTINCT r.miembro FROM Reserva r " +
                "WHERE r.actividad.id = :actividadId", 
                Miembro.class)
                .setParameter("actividadId", actividadId)
                .getResultList();
    } finally {
        session.close();
    }
}
```

**Conceptos HQL usados:**
- ✓ SELECT DISTINCT (sin duplicados)
- ✓ JOIN implícito (r.miembro navegación)
- ✓ WHERE con parámetros
- ✓ Tipado fuerte (Miembro.class)

**Menú:** `4. Consultas Avanzadas → 1. Listar miembros de una actividad`

---

### Consulta 2: Contar Reservas Confirmadas ✓

**Archivo:** [ReservaService.java](src/main/java/com/gimnasio/service/ReservaService.java) (línea `contarReservasPorMiembro`)

```java
public Long contarReservasPorMiembro(Long miembroId) {
    Session session = HibernateUtil.openSession();
    try {
        return session.createQuery(
                "SELECT COUNT(r) FROM Reserva r " +
                "WHERE r.miembro.id = :miembroId AND r.estado = 'CONFIRMADA'", 
                Long.class)
                .setParameter("miembroId", miembroId)
                .uniqueResult();
    } finally {
        session.close();
    }
}
```

**Conceptos HQL usados:**
- ✓ COUNT() agregación
- ✓ WHERE con AND (múltiples condiciones)
- ✓ uniqueResult() para obtener escalar

**Menú:** `4. Consultas Avanzadas → 2. Contar reservas de un miembro`

---

### Consulta 3: Actividades Llenas ✓

**Archivo:** [ReservaService.java](src/main/java/com/gimnasio/service/ReservaService.java)

```java
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
```

**Menú:** `4. Consultas Avanzadas → 3. Listar actividades llenas`

---

### Consulta 4: Actividades Disponibles ✓

**Archivo:** [ActividadDAO.java](src/main/java/com/gimnasio/dao/ActividadDAO.java)

```java
public List<Actividad> findWithAvailableSpaces() {
    Session session = HibernateUtil.openSession();
    try {
        return session.createQuery(
                "FROM Actividad WHERE plazasDisponibles > 0", 
                Actividad.class)
                .getResultList();
    } finally {
        session.close();
    }
}
```

**Menú:** `2. Gestionar Actividades → 4. Listar actividades con plazas disponibles`

---

## ⭐ Transacciones ACID (Criterio 3.6-3.7)

### Transacción Crítica 1: Reservar Clase ⭐

**Archivo:** [ReservaService.java](src/main/java/com/gimnasio/service/ReservaService.java) (línea `reservarClase`)

**Garantía ACID:**
```
BEGIN TRANSACTION
├─ Validación 1: Miembro existe → FALLA → ROLLBACK
├─ Validación 2: Actividad existe → FALLA → ROLLBACK
├─ Validación 3: Plazas disponibles > 0 → FALLA → ROLLBACK
├─ Validación 4: Saldo ≥ Precio → FALLA → ROLLBACK
├─ Paso 1: CREATE Reserva
├─ Paso 2: UPDATE Actividad (plazasDisponibles - 1)
├─ Paso 3: UPDATE Miembro (saldoCuenta - precio)
└─ COMMIT → TODO OK o ROLLBACK → NADA
```

**Código crítico:**
```java
try {
    transaction = session.beginTransaction();
    
    // VALIDACIONES
    Miembro miembro = session.get(Miembro.class, miembroId);
    if (miembro == null) throw new IllegalArgumentException("Miembro no existe");
    
    Actividad actividad = session.get(Actividad.class, actividadId);
    if (actividad == null) throw new IllegalArgumentException("Actividad no existe");
    
    if (actividad.getPlazasDisponibles() <= 0) 
        throw new IllegalStateException("Sin plazas");
    
    if (miembro.getSaldoCuenta() < actividad.getPrecioClase()) 
        throw new IllegalStateException("Saldo insuficiente");
    
    // OPERACIONES MÚLTIPLES (TRANSACCIÓN)
    Reserva reserva = new Reserva(miembro, actividad, fechaClase);
    session.save(reserva);                    // Crear
    
    actividad.setPlazasDisponibles(
        actividad.getPlazasDisponibles() - 1  // Restar plaza
    );
    session.update(actividad);
    
    miembro.setSaldoCuenta(
        miembro.getSaldoCuenta() - actividad.getPrecioClase()  // Restar dinero
    );
    session.update(miembro);
    
    // COMMIT SI TODO OK
    transaction.commit();
    
} catch (Exception e) {
    // ROLLBACK SI HAY ERROR
    if (transaction != null && transaction.isActive()) {
        transaction.rollback();
    }
    throw new RuntimeException("Error en transacción: " + e.getMessage(), e);
} finally {
    session.close();
}
```

**Propiedades ACID garantizadas:**
| Propiedad | Garantía |
|-----------|----------|
| **Atomicidad** | Todo se hace o nada. No hay estado intermedio |
| **Consistencia** | Total = Plazas + Reservas coinciden. Saldos válidos |
| **Aislamiento** | Una transacción no interfiere con otras |
| **Durabilidad** | COMMIT persiste en DB. ROLLBACK deshace todo |

**Menú:** `3. Gestionar Reservas → 1. Reservar una clase`

**Casos de uso demostrados:**

**Caso A: Exitoso**
```
Entrada: Miembro=1 ($100), Actividad=2 ($3, 20 plazas)
Validaciones: ✓ Todas pasan
Operaciones:
  ✓ CREATE Reserva
  ✓ UPDATE Plazas: 20 → 19
  ✓ UPDATE Saldo: 100 → 97
Resultado: ✓ COMMIT exitoso
```

**Caso B: Falla por saldo insuficiente**
```
Entrada: Miembro=2 ($2), Actividad=1 ($5, 15 plazas)
Validaciones: ✗ Saldo insuficiente ($2 < $5)
Operaciones: Ninguna ejecutada
Resultado: ✗ ROLLBACK automático
Base de datos: Sin cambios (plazas=15, saldo=$2)
```

---

### Transacción Crítica 2: Cancelar Reserva ⭐

**Archivo:** [ReservaService.java](src/main/java/com/gimnasio/service/ReservaService.java) (línea `cancelarReserva`)

**Operaciones múltiples:**
```
BEGIN TRANSACTION
├─ UPDATE Reserva: estado = "CANCELADA"
├─ UPDATE Actividad: plazasDisponibles + 1
├─ UPDATE Miembro: saldoCuenta + montoPagado
└─ COMMIT o ROLLBACK
```

**Código:**
```java
try {
    transaction = session.beginTransaction();
    
    Reserva reserva = session.get(Reserva.class, reservaId);
    if (reserva == null) throw new IllegalArgumentException("Reserva no existe");
    
    if ("CANCELADA".equals(reserva.getEstado())) 
        throw new IllegalStateException("Ya estaba cancelada");
    
    // OPERACIÓN 1: Cambiar estado
    reserva.setEstado("CANCELADA");
    session.update(reserva);
    
    // OPERACIÓN 2: Recuperar plaza
    Actividad actividad = reserva.getActividad();
    actividad.setPlazasDisponibles(actividad.getPlazasDisponibles() + 1);
    session.update(actividad);
    
    // OPERACIÓN 3: Devolver dinero
    Miembro miembro = reserva.getMiembro();
    miembro.setSaldoCuenta(miembro.getSaldoCuenta() + reserva.getMontoPagado());
    session.update(miembro);
    
    transaction.commit();
    
} catch (Exception e) {
    if (transaction != null && transaction.isActive()) {
        transaction.rollback();
    }
    throw new RuntimeException("Error cancelando reserva", e);
}
```

**Menú:** `3. Gestionar Reservas → 3. Cancelar reserva`

---

## 📊 Resumen de Cobertura

| Criterio | Código | Archivo | Línea |
|----------|--------|---------|-------|
| **3.1** Configuración ORM | ✓ | `pom.xml`, `hibernate.cfg.xml` | - |
| **3.2** Entidades (3+) | ✓ | `entity/*.java` | Todas |
| **3.3** Relaciones (1:N, N:M) | ✓ | `entity/Reserva.java`, `entity/Actividad.java` | @ManyToOne, @ManyToMany |
| **3.4** CRUD (Create) | ✓ | `dao/*.java` | `save()` |
| **3.4** CRUD (Read) | ✓ | `dao/*.java` | `findById()`, `findAll()` |
| **3.4** CRUD (Update) | ✓ | `dao/*.java` | `update()` |
| **3.4** CRUD (Delete) | ✓ | `dao/*.java` | `delete()` |
| **3.5** Consultas HQL | ✓ | `service/ReservaService.java` | Múltiples queries |
| **3.6** Transacciones | ✓ | `service/ReservaService.java` | `reservarClase()`, `cancelarReserva()` |
| **3.7** ACID garantizado | ✓ | `service/ReservaService.java` | try-catch-finally con rollback |

---

## 🎯 Cómo Ejecutar para Demostración

```bash
# 1. Compilar
cd c:\Users\jbuna\Documents\DAM2\AD\GimnasioApp
mvn clean compile

# 2. Ejecutar
mvn exec:java -Dexec.mainClass="com.gimnasio.app.GimnasioApp"

# 3. Menú interactivo aparecerá
# Demostración sugerida:
#   - Miembros: Registrar, ver, actualizar saldo
#   - Actividades: Ver disponibles
#   - Reservas: Reservar clase (exitosa y fallida)
#   - Consultas: Listar miembros por actividad
```

---

## 📁 Estructura de Archivos

```
GimnasioApp/
├── pom.xml                                    [Dependencias Maven]
├── README.md                                  [Documentación completa]
├── EJEMPLOS_PRACTICOS.md                      [Casos de uso detallados]
├── CRITERIOS_CUBIERTOS.md                     [Este archivo]
│
└── src/main/
    ├── java/com/gimnasio/
    │   ├── entity/
    │   │   ├── Miembro.java                   [@Entity 1:N con Reserva]
    │   │   ├── Actividad.java                 [@Entity N:M con Entrenador]
    │   │   ├── Reserva.java                   [@Entity N:1 hacia Miembro/Actividad]
    │   │   └── Entrenador.java                [@Entity N:M con Actividad]
    │   │
    │   ├── dao/
    │   │   ├── MiembroDAO.java                [CRUD: save, findById, findAll, update, delete]
    │   │   ├── ActividadDAO.java              [CRUD + findWithAvailableSpaces]
    │   │   ├── ReservaDAO.java                [CRUD + findByMiembroId, findByActividadId]
    │   │   └── EntrenadorDAO.java             [CRUD básico]
    │   │
    │   ├── service/
    │   │   ├── ReservaService.java            [⭐ TRANSACCIONES: reservarClase, cancelarReserva]
    │   │   │                                   [⭐ HQL: obtenerMiembrosPorActividad, etc]
    │   │   ├── ActividadService.java          [Lógica actividades]
    │   │   ├── MiembroService.java            [Lógica miembros]
    │   │   └── EntrenadorService.java         [Lógica entrenadores]
    │   │
    │   ├── util/
    │   │   └── HibernateUtil.java             [SessionFactory centralizada]
    │   │
    │   └── app/
    │       └── GimnasioApp.java               [Menú Console interactivo]
    │
    └── resources/
        └── hibernate.cfg.xml                   [Configuración Hibernate + H2]
```

---

## ✅ Checklist Final

- ✅ Proyecto Maven compilable
- ✅ Dependencias correctas (Hibernate, JPA, H2)
- ✅ 4 entidades con anotaciones JPA
- ✅ Relación 1:N (Miembro → Reserva)
- ✅ Relación N:M (Actividad ↔ Entrenador)
- ✅ CRUD completo en DAOs
- ✅ HQL avanzado (SELECT DISTINCT, COUNT, WHERE múltiples)
- ✅ Transacciones ACID con rollback automático
- ✅ Menú Console funcional
- ✅ Documentación completa

**Estado:** 🟢 COMPLETO - Listo para evaluación

