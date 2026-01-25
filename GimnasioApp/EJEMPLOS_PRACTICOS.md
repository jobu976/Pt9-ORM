# Ejemplos Prácticos - Pruebas de Funcionalidad

## 📋 Escenarios de Demostración

Este documento muestra cómo ejecutar y verificar cada funcionalidad del ORM.

---

## 1️⃣ CRUD: CREATE (Crear)

### Crear un nuevo miembro

**Entrada en menú:**
```
1. Gestionar Miembros
   → 1. Registrar nuevo miembro
   Nombre: Carlos Gómez
   Email: carlos@example.com
   Teléfono: 555-1234
   Saldo inicial: $150
```

**Código que se ejecuta:**
```java
Miembro miembro = new Miembro("Carlos Gómez", "carlos@example.com", "555-1234", 150.0);
Miembro creado = miembroService.registrarMiembro(miembro);
System.out.println("✓ Miembro registrado con ID: " + creado.getId());
```

**SQL generado por Hibernate:**
```sql
INSERT INTO miembros (nombre, email, telefono, fecha_inscripcion, saldo_cuenta, activo) 
VALUES ('Carlos Gómez', 'carlos@example.com', '555-1234', CURRENT_TIMESTAMP, 150.0, 1)
```

**Base de datos:**
```
ID │ nombre       │ email              │ saldo_cuenta
─────────────────────────────────────────────────────
1  │ Carlos Gómez │ carlos@example.com │ 150.0
```

---

## 2️⃣ CRUD: READ (Leer)

### Obtener miembro por ID

**Entrada en menú:**
```
1. Gestionar Miembros
   → 2. Ver miembro por ID
   ID del miembro: 1
```

**Código:**
```java
Miembro miembro = miembroService.obtenerMiembro(1L);
System.out.println(miembro);
// Output: Miembro{id=1, nombre='Carlos Gómez', email='carlos@example.com', 
//                   saldo_cuenta=150.0, activo=true}
```

**SQL:**
```sql
SELECT * FROM miembros WHERE id = 1
```

### Listar todos los miembros

**Entrada en menú:**
```
1. Gestionar Miembros
   → 3. Listar todos los miembros
```

**Código:**
```java
List<Miembro> miembros = miembroService.listarMiembros();
for (Miembro m : miembros) {
    System.out.println(m);
}
```

**SQL (HQL traducido):**
```sql
SELECT * FROM miembros
```

---

## 3️⃣ CRUD: UPDATE (Actualizar)

### Modificar saldo de un miembro

**Entrada en menú:**
```
1. Gestionar Miembros
   → 5. Agregar saldo
   ID del miembro: 1
   Monto a agregar: $50
```

**Código:**
```java
Miembro miembro = miembroService.obtenerMiembro(1L);
miembro.setSaldoCuenta(150.0 + 50.0);  // 200.0
miembroService.actualizarMiembro(miembro);
System.out.println("✓ Saldo actualizado a: $200.0");
```

**SQL:**
```sql
UPDATE miembros SET saldo_cuenta = 200.0 WHERE id = 1
```

**Base de datos (antes → después):**
```
saldo_cuenta: 150.0 → 200.0
```

---

## 4️⃣ CRUD: DELETE (Eliminar)

### Eliminar un miembro

**Entrada en menú:**
```
1. Gestionar Miembros
   → 6. Eliminar miembro
   ID del miembro a eliminar: 5
```

**Código:**
```java
miembroService.eliminarMiembro(5L);
System.out.println("✓ Miembro eliminado");
```

**SQL:**
```sql
DELETE FROM miembros WHERE id = 5
```

---

## 5️⃣ RELACIONES: 1:N (One-to-Many)

### Crear Miembro → Crear Reservas

**Estructura:**
```
Miembro (ID: 1) "Juan"
    ├── Reserva (ID: 1) → Pesas
    ├── Reserva (ID: 2) → Cardio
    └── Reserva (ID: 3) → Yoga
```

**Creación:**
```java
// 1. Crear miembro
Miembro miembro = new Miembro("Juan López", "juan@email.com", "555-0001", 100.0);
Miembro m = miembroService.registrarMiembro(miembro);  // ID: 1

// 2. Crear actividades
Actividad act1 = new Actividad("Pesas", ..., 15, 5.0, LocalTime.of(9,0), 60);
Actividad a1 = actividadService.crearActividad(act1);  // ID: 1

// 3. Crear reservas (vinculadas al miembro)
Reserva r1 = new Reserva(m, a1, LocalDateTime.now().plusDays(7));
reservaService.reservarClase(1L, 1L, ...);  // Miembro 1 → Actividad 1
```

**Base de datos:**
```
MIEMBROS:                    RESERVAS:
id │ nombre              id │ miembro_id │ actividad_id
──────────────────────────────────────────────────────
1  │ Juan López          1  │ 1          │ 1
                         2  │ 1          │ 2
                         3  │ 1          │ 3
```

**SQL generado:**
```sql
-- Insertar miembro
INSERT INTO miembros (...) VALUES (1, 'Juan López', ...)

-- Insertar reservas (con FK)
INSERT INTO reservas (miembro_id, actividad_id, ...) 
VALUES (1, 1, ...)
```

---

## 6️⃣ RELACIONES: N:M (Many-to-Many)

### Asignar múltiples Entrenadores a una Actividad

**Estructura:**
```
Entrenador 1: "Carlos" → [Pesas, Cardio]
Entrenador 2: "Laura"  → [Cardio, Yoga]
```

**Creación:**
```java
// 1. Crear entrenadores
Entrenador e1 = new Entrenador("Carlos Martínez", "Musculación", "carlos@gym.com", 8);
Entrenador e2 = new Entrenador("Laura Pérez", "Cardio", "laura@gym.com", 5);

Entrenador saved1 = entrenadorService.crearEntrenador(e1);  // ID: 1
Entrenador saved2 = entrenadorService.crearEntrenador(e2);  // ID: 2

// 2. Crear actividades
Actividad a1 = new Actividad("Pesas", ..., 15, 5.0, ..., 60);
Actividad a2 = new Actividad("Cardio", ..., 20, 3.0, ..., 45);

Actividad act1 = actividadService.crearActividad(a1);  // ID: 1
Actividad act2 = actividadService.crearActividad(a2);  // ID: 2

// 3. Asignar entrenadores (N:M)
actividadService.asignarEntrenador(1L, 1L);  // Pesas ← Carlos
actividadService.asignarEntrenador(2L, 1L);  // Cardio ← Carlos
actividadService.asignarEntrenador(2L, 2L);  // Cardio ← Laura
```

**Base de datos (Tabla intermedia):**
```
ACTIVIDAD_ENTRENADOR:
actividad_id │ entrenador_id
─────────────┼──────────────
1            │ 1           (Pesas - Carlos)
2            │ 1           (Cardio - Carlos)
2            │ 2           (Cardio - Laura)
```

**SQL generado:**
```sql
-- Tabla intermedia (auto-creada por JPA)
CREATE TABLE actividad_entrenador (
    actividad_id BIGINT REFERENCES actividades(id),
    entrenador_id BIGINT REFERENCES entrenadores(id),
    PRIMARY KEY (actividad_id, entrenador_id)
);

-- Insertar relaciones
INSERT INTO actividad_entrenador (actividad_id, entrenador_id) VALUES (1, 1);
INSERT INTO actividad_entrenador (actividad_id, entrenador_id) VALUES (2, 1);
INSERT INTO actividad_entrenador (actividad_id, entrenador_id) VALUES (2, 2);
```

---

## 7️⃣ CONSULTAS HQL AVANZADAS

### Consulta 1: Listar miembros de una actividad específica

**Entrada en menú:**
```
4. Consultas Avanzadas
   → 1. Listar miembros de una actividad específica
   ID de la actividad: 2
```

**Código:**
```java
List<Miembro> miembros = reservaService.obtenerMiembrosPorActividad(2L);
```

**HQL:**
```sql
SELECT DISTINCT r.miembro FROM Reserva r 
WHERE r.actividad.id = 2
```

**SQL nativo (traducido por Hibernate):**
```sql
SELECT DISTINCT m.* FROM miembros m 
INNER JOIN reservas r ON m.id = r.miembro_id 
WHERE r.actividad_id = 2
```

**Resultado ejemplo:**
```
--- MIEMBROS RESERVADOS EN ESTA ACTIVIDAD ---
- Juan López (juan@email.com)
- María García (maria@email.com)
- Pedro Rodríguez (pedro@email.com)
```

---

### Consulta 2: Contar reservas confirmadas de un miembro

**Entrada en menú:**
```
4. Consultas Avanzadas
   → 2. Contar reservas de un miembro
   ID del miembro: 1
```

**Código:**
```java
Long cantidad = reservaService.contarReservasPorMiembro(1L);
System.out.println("Reservas activas: " + cantidad);
```

**HQL:**
```sql
SELECT COUNT(r) FROM Reserva r 
WHERE r.miembro.id = 1 AND r.estado = 'CONFIRMADA'
```

**SQL nativo:**
```sql
SELECT COUNT(*) FROM reservas 
WHERE miembro_id = 1 AND estado = 'CONFIRMADA'
```

**Resultado:** 
```
Reservas activas: 3
```

---

### Consulta 3: Actividades sin plazas disponibles

**Código:**
```java
List<Actividad> llenas = reservaService.obtenerActividadesLlenas();
```

**HQL:**
```sql
FROM Actividad WHERE plazasDisponibles = 0
```

**SQL nativo:**
```sql
SELECT * FROM actividades WHERE plazas_disponibles = 0
```

**Resultado:**
```
--- ACTIVIDADES SIN PLAZAS DISPONIBLES ---
- Pesas (Capacidad: 15)
- Yoga (Capacidad: 10)
```

---

## ⭐ 8️⃣ TRANSACCIONES COMPLEJAS

### Caso 1: Reservar clase (Operación exitosa)

**Entrada en menú:**
```
3. Gestionar Reservas
   → 1. Reservar una clase
   ID del miembro: 1
   ID de la actividad: 2
```

**Estado inicial:**
```
Miembro: Juan (Saldo: $100)
Actividad: Cardio (Plazas: 20/20, Precio: $3)
```

**Código (ReservaService.reservarClase):**
```java
Session session = HibernateUtil.openSession();
Transaction transaction = session.beginTransaction();

try {
    // Paso 1: Obtener objetos
    Miembro miembro = session.get(Miembro.class, 1L);    // Juan
    Actividad actividad = session.get(Actividad.class, 2L);  // Cardio
    
    // Paso 2: Validaciones
    if (actividad.getPlazasDisponibles() <= 0) 
        throw new Exception("Sin plazas");  // ✓ PASA
    
    if (miembro.getSaldoCuenta() < actividad.getPrecioClase())
        throw new Exception("Saldo insuficiente");  // ✓ PASA ($100 > $3)
    
    // Paso 3: CREAR Reserva
    Reserva reserva = new Reserva(miembro, actividad, LocalDateTime.now().plusDays(7));
    session.save(reserva);
    
    // Paso 4: RESTAR plaza
    actividad.setPlazasDisponibles(20 - 1);  // 20 → 19
    session.update(actividad);
    
    // Paso 5: RESTAR dinero
    miembro.setSaldoCuenta(100.0 - 3.0);  // 100 → 97
    session.update(miembro);
    
    // Paso 6: COMMIT (éxito)
    transaction.commit();
    System.out.println("✓ Reserva confirmada para Juan en Cardio");
    
} catch (Exception e) {
    // Si hay error → ROLLBACK
    transaction.rollback();
    System.out.println("✗ Reserva cancelada (rollback)");
}
```

**SQL ejecutado (en orden):**
```sql
-- 1. Obtener miembro
SELECT * FROM miembros WHERE id = 1;

-- 2. Obtener actividad
SELECT * FROM actividades WHERE id = 2;

-- 3. Insertar reserva
INSERT INTO reservas (miembro_id, actividad_id, fecha_reserva, fecha_clase, estado, monto_pagado)
VALUES (1, 2, CURRENT_TIMESTAMP, '2026-02-01 10:00:00', 'CONFIRMADA', 3.0);

-- 4. Actualizar plazas
UPDATE actividades SET plazas_disponibles = 19 WHERE id = 2;

-- 5. Actualizar saldo
UPDATE miembros SET saldo_cuenta = 97.0 WHERE id = 1;

-- 6. COMMIT (todo OK)
COMMIT;
```

**Estado final:**
```
✓ Miembro: Juan (Saldo: $97)
✓ Actividad: Cardio (Plazas: 19/20)
✓ Reserva: Confirmada
```

---

### Caso 2: Reservar clase (Rollback por saldo insuficiente)

**Entrada en menú:**
```
3. Gestionar Reservas
   → 1. Reservar una clase
   ID del miembro: 2
   ID de la actividad: 1
```

**Estado inicial:**
```
Miembro: María (Saldo: $2)
Actividad: Pesas (Plazas: 10/15, Precio: $5)
```

**Ejecución:**
```
Validaciones:
  ✓ Miembro existe
  ✓ Actividad existe
  ✓ Plazas disponibles > 0 (10 > 0)
  ✗ FALLA: Saldo insuficiente ($2 < $5)
  
→ ROLLBACK automático
→ No se inserta nada
→ No se actualiza nada
```

**SQL intentado (rollback automático):**
```sql
BEGIN TRANSACTION;

-- Intenta insertar reserva
INSERT INTO reservas (...) VALUES (...);

-- Intenta actualizar plazas
UPDATE actividades SET plazas_disponibles = 9 WHERE id = 1;

-- Intenta actualizar saldo
UPDATE miembros SET saldo_cuenta = -3.0 WHERE id = 2;

-- ✗ ERROR: Saldo insuficiente
-- → ROLLBACK (deshace todos los cambios)
ROLLBACK;
```

**Estado final (sin cambios):**
```
✗ Miembro: María (Saldo: $2 - sin cambios)
✗ Actividad: Pesas (Plazas: 10 - sin cambios)
✗ Reserva: NO creada
✗ Error: "Saldo insuficiente. Necesita: $5, tiene: $2"
```

---

### Caso 3: Cancelar reserva (Reembolso transaccional)

**Entrada en menú:**
```
3. Gestionar Reservas
   → 3. Cancelar reserva
   ID de la reserva a cancelar: 5
```

**Estado inicial:**
```
Reserva 5: Juan en Cardio, $3, CONFIRMADA
Miembro: Juan (Saldo: $97)
Actividad: Cardio (Plazas: 19/20)
```

**Código (cancelarReserva):**
```java
try {
    transaction.beginTransaction();
    
    // Paso 1: Obtener reserva
    Reserva reserva = session.get(Reserva.class, 5L);
    
    // Paso 2: Validar estado
    if ("CANCELADA".equals(reserva.getEstado()))
        throw new Exception("Ya estaba cancelada");  // ✓ PASA
    
    // Paso 3: CAMBIAR estado
    reserva.setEstado("CANCELADA");
    session.update(reserva);
    
    // Paso 4: SUMAR plaza
    Actividad actividad = reserva.getActividad();
    actividad.setPlazasDisponibles(19 + 1);  // 19 → 20
    session.update(actividad);
    
    // Paso 5: DEVOLVER dinero
    Miembro miembro = reserva.getMiembro();
    miembro.setSaldoCuenta(97.0 + 3.0);  // 97 → 100
    session.update(miembro);
    
    transaction.commit();
    System.out.println("✓ Reserva cancelada. Reembolso de $3");
    
} catch (Exception e) {
    transaction.rollback();
    System.out.println("✗ Error cancelando");
}
```

**SQL ejecutado:**
```sql
-- 1. Cambiar estado
UPDATE reservas SET estado = 'CANCELADA' WHERE id = 5;

-- 2. Recuperar plaza
UPDATE actividades SET plazas_disponibles = 20 WHERE id = 2;

-- 3. Devolver dinero
UPDATE miembros SET saldo_cuenta = 100.0 WHERE id = 1;

-- 4. COMMIT
COMMIT;
```

**Estado final:**
```
✓ Reserva: CANCELADA
✓ Miembro: Juan (Saldo: $100 - reembolsado)
✓ Actividad: Cardio (Plazas: 20/20 - recuperada)
✓ Integridad garantizada
```

---

## 📊 Comparativa: Transacción exitosa vs Rollback

| Operación | Exitosa | Con Rollback |
|-----------|---------|-------------|
| Crear Reserva | ✓ Insertar | ✗ No insertar |
| Restar plazas | ✓ Actualizar a 19 | ✗ Mantener 20 |
| Restar dinero | ✓ Actualizar a 97 | ✗ Mantener 100 |
| Base de datos | ✓ Consistente | ✓ Consistente |
| Resultado | ✓ COMMIT | ✓ ROLLBACK |

---

## 🎯 Resumen de Funcionalidades Demostrables

✅ **CRUD**
- [x] CREATE: Crear Miembro, Actividad, Reserva, Entrenador
- [x] READ: Obtener por ID, listar todos
- [x] UPDATE: Modificar atributos, agregar saldo
- [x] DELETE: Eliminar entidades

✅ **Relaciones ORM**
- [x] 1:N: Miembro → Reservas
- [x] N:1: Reserva → Miembro/Actividad
- [x] N:M: Actividad ↔ Entrenador (con @JoinTable)

✅ **Consultas HQL**
- [x] SELECT DISTINCT con JOIN
- [x] COUNT con WHERE y AND
- [x] WHERE con condiciones múltiples

✅ **Transacciones ACID**
- [x] BEGIN → validaciones → múltiples UPDATEs → COMMIT
- [x] BEGIN → validación FALLA → ROLLBACK automático
- [x] Garantía de integridad referencial
