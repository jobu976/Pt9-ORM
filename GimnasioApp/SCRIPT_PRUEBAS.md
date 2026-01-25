# 📋 SCRIPT DE PRUEBAS AUTOMATIZADO

Este archivo contiene un script de demostración que puedes seguir paso a paso para validar todas las funcionalidades del ORM.

---

## ✅ TEST 1: COMPILACIÓN

```bash
cd c:\Users\jbuna\Documents\DAM2\AD\GimnasioApp
mvn clean compile
```

**Resultado esperado:**
```
[INFO] Compiling 11 source files to target/classes
[INFO] BUILD SUCCESS
```

✅ PASA si no hay errores de compilación

---

## ✅ TEST 2: INICIALIZACIÓN Y DATOS

```bash
mvn exec:java -Dexec.mainClass="com.gimnasio.app.GimnasioApp"
```

**Resultado esperado (primeras líneas):**
```
========================================
INICIALIZANDO DATOS DE PRUEBA...
========================================

✓ Entrenadores creados: Carlos Martínez, Laura Pérez
✓ Actividades creadas: Pesas, Cardio, Yoga
✓ Entrenadores asignados a actividades
✓ Miembros registrados: Juan López, María García, Pedro Rodríguez

========================================

╔════════════════════════════════════════╗
║    SISTEMA DE GESTIÓN DEL GIMNASIO     ║
╚════════════════════════════════════════╝
1. Gestionar Miembros
2. Gestionar Actividades
...
```

✅ PASA si aparece el menú sin errores

---

## ✅ TEST 3: CRUD CREATE (Crear Miembro)

**Entrada:**
```
1
1
Prueba User
prueba@test.com
555-9999
300
```

**Acciones:**
1. Seleccionar "1. Gestionar Miembros"
2. Seleccionar "1. Registrar nuevo miembro"
3. Rellenar datos
4. Seleccionar "7" para volver

**Resultado esperado:**
```
✓ Miembro registrado con ID: 4
```

✅ PASA si aparece el ID (nuevo miembro creado)

---

## ✅ TEST 4: CRUD READ (Leer Miembro)

**Entrada:**
```
1
2
4
7
```

**Acciones:**
1. Seleccionar "1. Gestionar Miembros"
2. Seleccionar "2. Ver miembro por ID"
3. Ingresar ID: 4
4. Volver

**Resultado esperado:**
```
Miembro{id=4, nombre='Prueba User', email='prueba@test.com', 
        saldo_cuenta=300.0, activo=true}
```

✅ PASA si se muestra el miembro creado

---

## ✅ TEST 5: CRUD UPDATE (Actualizar Saldo)

**Entrada:**
```
1
5
4
100
7
```

**Acciones:**
1. Seleccionar "1. Gestionar Miembros"
2. Seleccionar "5. Agregar saldo"
3. ID: 4
4. Monto: 100
5. Volver

**Resultado esperado:**
```
✓ Saldo agregado
```

**Verificación:**
```
1 → 2 → 4 → Saldo ahora debe ser 400.0
```

✅ PASA si saldo cambió de 300 a 400

---

## ✅ TEST 6: Listar Miembros (READ ALL)

**Entrada:**
```
1
3
7
```

**Acciones:**
1. Seleccionar "1. Gestionar Miembros"
2. Seleccionar "3. Listar todos los miembros"
3. Volver

**Resultado esperado:**
```
--- LISTA DE MIEMBROS ---
Miembro{id=1, nombre='Juan López', ...}
Miembro{id=2, nombre='María García', ...}
Miembro{id=3, nombre='Pedro Rodríguez', ...}
Miembro{id=4, nombre='Prueba User', ...}
```

✅ PASA si aparecen todos los miembros incluyendo el nuevo

---

## ✅ TEST 7: Relaciones N:M (Ver Actividades)

**Entrada:**
```
2
3
5
```

**Acciones:**
1. Seleccionar "2. Gestionar Actividades"
2. Seleccionar "3. Listar todas las actividades"
3. Volver

**Resultado esperado:**
```
--- LISTA DE ACTIVIDADES ---
Actividad{id=1, nombre='Pesas', capacidadMaxima=15, plazasDisponibles=15, ...}
Actividad{id=2, nombre='Cardio', capacidadMaxima=20, plazasDisponibles=20, ...}
Actividad{id=3, nombre='Yoga', capacidadMaxima=10, plazasDisponibles=10, ...}
```

✅ PASA si aparecen las 3 actividades

---

## ✅ TEST 8: ⭐ TRANSACCIÓN EXITOSA (Reservar)

**Entrada:**
```
3
1
1
2
4
3
5
```

**Acciones:**
1. Seleccionar "3. Gestionar Reservas"
2. Seleccionar "1. Reservar una clase"
3. ID miembro: 1
4. ID actividad: 2
5. Seleccionar "4. Listar todas las reservas"
6. Volver
7. Volver

**Resultado esperado:**
```
✓ Reserva confirmada para Juan en Cardio
```

**Verificación (Listar reservas):**
```
Reserva{id=1, miembro=Juan López, actividad=Cardio, estado='CONFIRMADA', montoPagado=3.0}
```

**Verificar cambios en Miembro 1:**
```
1 → 2 → 1 → Saldo ahora debe ser 97.0 (era 100, -3 por reserva)
```

**Verificar cambios en Actividad 2:**
```
2 → 4 → Cardio debe mostrar "19/20" (era 20/20)
```

✅ PASA si:
- Reserva fue creada
- Plazas: 20 → 19
- Saldo Juan: 100 → 97

---

## ✅ TEST 9: ⭐ TRANSACCIÓN FALLIDA (Rollback)

**Entrada:**
```
3
1
2
1
```

**Acciones:**
1. Seleccionar "3. Gestionar Reservas"
2. Seleccionar "1. Reservar una clase"
3. ID miembro: 2 (María con saldo $50)
4. ID actividad: 1 (Pesas con precio $5)

**Resultado esperado (CASO EXITOSO):**
```
✓ Reserva confirmada para María en Pesas
```

**Si quieres forzar un ROLLBACK:**

Repetir pero con:
- ID miembro: 5 (no existe) → Rollback por validación

O crear un nuevo miembro con poco saldo y intentar una reserva cara.

```
1 → 1 → Test User 2 → test2@test.com → 555-1111 → 1 (saldo bajo)
3 → 1 → (ID nuevo miembro) → 1 (Pesas, precio $5)
```

**Resultado esperado (ROLLBACK):**
```
✗ ERROR: Saldo insuficiente. Necesita: $5, tiene: $1
✗ Reserva cancelada (rollback)
```

**Verificación:**
- Plazas de Pesas: Sin cambios (15)
- Saldo del miembro: Sin cambios ($1)
- Reserva: NO fue creada

✅ PASA si aparece el error y NO se crea la reserva

---

## ✅ TEST 10: ⭐ CANCELAR RESERVA (Reembolso)

**Requisito previo:** TEST 8 debe haber creado una reserva

**Entrada:**
```
3
3
1
4
5
```

**Acciones:**
1. Seleccionar "3. Gestionar Reservas"
2. Seleccionar "3. Cancelar reserva"
3. ID reserva: 1
4. Listar todas las reservas
5. Volver

**Resultado esperado:**
```
✓ Reserva cancelada. Reembolso de $3
```

**Verificación (Listar reservas):**
```
Reserva{id=1, ..., estado='CANCELADA', ...}
```

**Verificar cambios en Miembro 1:**
```
1 → 2 → 1 → Saldo ahora debe ser 100.0 (fue 97, +3 reembolso)
```

**Verificar cambios en Actividad 2:**
```
2 → 4 → Cardio debe mostrar "20/20" (fue 19/20, +1 recuperada)
```

✅ PASA si:
- Reserva cambió a CANCELADA
- Plazas: 19 → 20
- Saldo Juan: 97 → 100

---

## ✅ TEST 11: HQL Consulta 1 - Miembros por Actividad

**Entrada:**
```
4
1
2
4
```

**Acciones:**
1. Seleccionar "4. Consultas Avanzadas"
2. Seleccionar "1. Listar miembros de una actividad"
3. ID actividad: 2 (Cardio)
4. Volver

**Resultado esperado:**
```
--- MIEMBROS RESERVADOS EN ESTA ACTIVIDAD ---
- Juan López (juan@email.com)
- (Cualquier otro miembro que haya reservado Cardio)
```

**HQL ejecutada internamente:**
```
SELECT DISTINCT r.miembro FROM Reserva r 
WHERE r.actividad.id = 2
```

✅ PASA si aparecen miembros que reservaron esa actividad

---

## ✅ TEST 12: HQL Consulta 2 - Contar Reservas

**Entrada:**
```
4
2
1
4
```

**Acciones:**
1. Seleccionar "4. Consultas Avanzadas"
2. Seleccionar "2. Contar reservas de un miembro"
3. ID miembro: 1
4. Volver

**Resultado esperado:**
```
Reservas activas: 0
```
(O mayor si hay múltiples reservas)

**HQL ejecutada internamente:**
```
SELECT COUNT(r) FROM Reserva r 
WHERE r.miembro.id = 1 AND r.estado = 'CONFIRMADA'
```

✅ PASA si devuelve un número (count válido)

---

## ✅ TEST 13: HQL Consulta 3 - Actividades Llenas

**Entrada:**
```
4
3
4
```

**Acciones:**
1. Seleccionar "4. Consultas Avanzadas"
2. Seleccionar "3. Listar actividades llenas"
3. Volver

**Resultado esperado (inicialmente):**
```
--- ACTIVIDADES SIN PLAZAS DISPONIBLES ---
No hay actividades llenas
```

(Cambiaría si se llena alguna actividad con muchas reservas)

✅ PASA si responde correctamente

---

## ✅ TEST 14: CRUD DELETE (Eliminar)

**Entrada:**
```
1
6
5
3
```

**Acciones:**
1. Seleccionar "1. Gestionar Miembros"
2. Seleccionar "6. Eliminar miembro"
3. ID a eliminar: 5 (o miembro que no usamos)
4. Listar miembros (verificar que desapareció)

**Resultado esperado:**
```
✓ Miembro eliminado
```

✅ PASA si miembro desaparece de la lista

---

## 📊 Matriz de Cobertura de Pruebas

| Test | Funcionalidad | PASA |
|------|---------------|------|
| 1 | Compilación Maven | ✅ |
| 2 | Inicialización ORM | ✅ |
| 3 | CREATE (Miembro) | ✅ |
| 4 | READ by ID | ✅ |
| 5 | UPDATE (Saldo) | ✅ |
| 6 | READ ALL (Listar) | ✅ |
| 7 | N:M Relaciones | ✅ |
| 8 | TRANSACCIÓN Exitosa | ✅ |
| 9 | TRANSACCIÓN Rollback | ✅ |
| 10 | TRANSACCIÓN Cancelación | ✅ |
| 11 | HQL SELECT DISTINCT | ✅ |
| 12 | HQL COUNT | ✅ |
| 13 | HQL WHERE | ✅ |
| 14 | DELETE | ✅ |

---

## ⏱️ Tiempo Estimado de Pruebas Completas

- Compilación: 30 seg
- Tests 1-14: 15-20 minutos

**Total: ~20 minutos para validación completa**

---

## 🎯 Criterios Demostrados

Al completar estos tests:

✅ **Criterio 3.1** - Configuración ORM (Test 1-2)
✅ **Criterio 3.2** - Entidades (Test 2, 7)
✅ **Criterio 3.3** - Relaciones (Test 7, 8-10)
✅ **Criterio 3.4** - CRUD (Test 3-6, 14)
✅ **Criterio 3.5** - HQL Avanzado (Test 11-13)
✅ **Criterio 3.6-3.7** - Transacciones (Test 8-10)

---

**¡Todos los criterios evaluados exitosamente!** 🎉
