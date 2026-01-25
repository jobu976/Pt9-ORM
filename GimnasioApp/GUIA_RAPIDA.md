# 🚀 GUÍA RÁPIDA DE INSTALACIÓN Y EJECUCIÓN

## Requisitos Previos

- **Java 11 o superior** instalado
- **Maven 3.6+** instalado

Verificar instalación:
```bash
java -version
mvn -version
```

---

## 📦 Pasos para Ejecutar la Aplicación

### Paso 1: Abrir terminal en la carpeta del proyecto

```bash
cd c:\Users\jbuna\Documents\DAM2\AD\GimnasioApp
```

### Paso 2: Limpiar y compilar

```bash
mvn clean compile
```

**Qué sucede:**
- Maven descarga las dependencias
- Compila el código Java
- Genera carpeta `target/` con clases compiladas

**Tiempo estimado:** 30-60 segundos (primera ejecución)

### Paso 3: Ejecutar la aplicación

```bash
mvn exec:java -Dexec.mainClass="com.gimnasio.app.GimnasioApp"
```

**Qué sucede:**
- Se inicia la aplicación
- Hibernate crea la base de datos en memoria
- Se cargan datos iniciales de prueba
- Aparece el menú interactivo

---

## 🎮 Demostración Interactiva Recomendada

### Secuencia 1: CRUD Básico

```
════════════════════════════════════
    CREAR Y VER UN MIEMBRO
════════════════════════════════════

1. Seleccionar opción: 1 (Gestionar Miembros)
2. Seleccionar opción: 1 (Registrar nuevo miembro)

   Nombre: Carlos López
   Email: carlos@test.com
   Teléfono: 555-1234
   Saldo inicial: $250

   ✓ Miembro registrado con ID: 4

3. Seleccionar opción: 3 (Listar todos los miembros)
   ✓ Verá todos los miembros, incluyendo el nuevo
   
4. Seleccionar opción: 2 (Ver miembro por ID)
   ID del miembro: 4
   ✓ Verá los detalles del miembro creado
   
5. Seleccionar opción: 7 (Volver)
```

---

### Secuencia 2: Relaciones (1:N y N:M)

```
════════════════════════════════════
    VER ACTIVIDADES Y ENTRENADORES
════════════════════════════════════

1. Seleccionar opción: 2 (Gestionar Actividades)
2. Seleccionar opción: 3 (Listar todas las actividades)

   ✓ Verá las 3 actividades creadas inicialmente:
     - Pesas (Entrenador: Carlos Martínez)
     - Cardio (Entrenador: Laura Pérez)
     - Yoga (Sin entrenador)

   Notar:
   • Relación 1:N: Miembro → múltiples Reservas
   • Relación N:M: Actividad ← múltiples Entrenadores

3. Seleccionar opción: 4 (Listar actividades con plazas disponibles)
   ✓ Todas deberían tener plazas disponibles al inicio

4. Seleccionar opción: 5 (Volver)
```

---

### Secuencia 3: ⭐ TRANSACCIÓN EXITOSA

```
════════════════════════════════════
    RESERVAR UNA CLASE (EXITOSO)
════════════════════════════════════

Estado inicial:
• Miembro 1 (Juan): Saldo $100
• Actividad 2 (Cardio): 20 plazas, Precio $3

Acciones:

1. Seleccionar opción: 3 (Gestionar Reservas)
2. Seleccionar opción: 1 (Reservar una clase)
   ID del miembro: 1
   ID de la actividad: 2
   
   ✓ Reserva confirmada para Juan en Cardio
   
   Qué pasó internamente (TRANSACCIÓN):
   ├─ Validación: Miembro existe ✓
   ├─ Validación: Actividad existe ✓
   ├─ Validación: Plazas > 0 ✓ (20 plazas)
   ├─ Validación: Saldo ≥ Precio ✓ ($100 > $3)
   ├─ CREATE: Reserva
   ├─ UPDATE: Plazas 20 → 19
   ├─ UPDATE: Saldo $100 → $97
   └─ COMMIT ✓ (TODO guardado)

3. Seleccionar opción: 4 (Listar todas las reservas)
   ✓ Verá la reserva recién creada
   
4. Seleccionar opción: 1 (Miembro por ID)
   ID: 1 (Juan)
   ✓ Saldo ahora es $97 (se restó $3)
   
5. Seleccionar opción: 7 (Volver)
   
Volver a Miembros → opción 3 (Listar actividades con plazas)
✓ Cardio ahora muestra "19 plazas" (en lugar de 20)
```

---

### Secuencia 4: ⭐ TRANSACCIÓN FALLIDA (ROLLBACK)

```
════════════════════════════════════
    INTENTO DE RESERVA (ROLLBACK)
════════════════════════════════════

Estado inicial:
• Miembro 2 (María): Saldo $2
• Actividad 1 (Pesas): 15 plazas, Precio $5

Acciones:

1. Seleccionar opción: 3 (Gestionar Reservas)
2. Seleccionar opción: 1 (Reservar una clase)
   ID del miembro: 2
   ID de la actividad: 1
   
   ✗ ERROR: Saldo insuficiente. Necesita: $5, tiene: $2
   
   Qué pasó internamente (TRANSACCIÓN):
   ├─ Validación: Miembro existe ✓
   ├─ Validación: Actividad existe ✓
   ├─ Validación: Plazas > 0 ✓ (15 plazas)
   ├─ Validación: Saldo ≥ Precio ✗ ($2 < $5) ← FALLA AQUÍ
   ├─ ROLLBACK ✓ (se deshace todo)
   └─ Base de datos: Sin cambios

3. Verificar que nada cambió:
   • Plazas de Pesas: sigue siendo 15
   • Saldo de María: sigue siendo $2
   • Reserva: NO fue creada
```

---

### Secuencia 5: ⭐ CANCELAR RESERVA (REEMBOLSO)

```
════════════════════════════════════
    CANCELAR UNA RESERVA
════════════════════════════════════

Requisito previo: Tener al menos una reserva activa
(De la Secuencia 3, Juan debería tener una reserva en Cardio)

Acciones:

1. Seleccionar opción: 3 (Gestionar Reservas)
2. Seleccionar opción: 3 (Cancelar reserva)
   ID de la reserva a cancelar: 1
   
   ✓ Reserva cancelada. Reembolso de $3
   
   Qué pasó internamente (TRANSACCIÓN):
   ├─ UPDATE: Reserva estado "CONFIRMADA" → "CANCELADA"
   ├─ UPDATE: Plazas 19 → 20 (recuperada)
   ├─ UPDATE: Saldo $97 → $100 (reembolsada)
   └─ COMMIT ✓

3. Verificación:
   • Listar reservas: Verá estado "CANCELADA"
   • Ver miembro 1: Saldo volvió a $100
   • Actividades disponibles: Cardio volvió a tener 20/20 plazas
```

---

### Secuencia 6: Consultas HQL Avanzadas

```
════════════════════════════════════
    CONSULTAS AVANZADAS
════════════════════════════════════

1. Seleccionar opción: 4 (Consultas Avanzadas)

2. Seleccionar opción: 1 (Listar miembros de una actividad)
   ID de la actividad: 2
   
   Query HQL ejecutada:
   SELECT DISTINCT r.miembro FROM Reserva r 
   WHERE r.actividad.id = 2
   
   ✓ Resultado: Lista de miembros que reservaron Cardio

3. Seleccionar opción: 2 (Contar reservas de un miembro)
   ID del miembro: 1
   
   Query HQL ejecutada:
   SELECT COUNT(r) FROM Reserva r 
   WHERE r.miembro.id = 1 AND r.estado = 'CONFIRMADA'
   
   ✓ Resultado: Número de reservas activas

4. Seleccionar opción: 3 (Listar actividades llenas)
   
   Query HQL ejecutada:
   FROM Actividad WHERE plazasDisponibles = 0
   
   ✓ Resultado: Actividades sin plazas disponibles
   (Inicialmente: ninguna)
```

---

## 📊 Demostración Completa (~10-15 minutos)

### Timeline recomendado:

| Tiempo | Acción |
|--------|--------|
| 0:00   | Compilar y ejecutar |
| 0:30   | Menú aparece, mostrar datos iniciales |
| 1:00   | Crear nuevo miembro (CRUD: CREATE) |
| 1:30   | Ver miembro (CRUD: READ) |
| 2:00   | Listar miembros (CRUD: READ all) |
| 2:30   | Agregar saldo (CRUD: UPDATE) |
| 3:00   | Ver actividades disponibles |
| 3:30   | **Reservar clase exitosa (TRANSACCIÓN)** |
| 4:30   | Verificar cambios (plazas, saldo) |
| 5:00   | Intento fallido de reserva (ROLLBACK) |
| 6:00   | Cancelar reserva (ROLLBACK + REEMBOLSO) |
| 7:00   | Listar miembros por actividad (HQL) |
| 7:30   | Contar reservas (HQL COUNT) |
| 8:00   | Actividades llenas (HQL WHERE) |
| 8:30   | **FIN: Demostración completa** |

---

## 🔍 Ver SQL Generado

Para ver el SQL que Hibernate genera, el proyecto ya tiene activado:

```xml
<!-- En hibernate.cfg.xml -->
<property name="show_sql">true</property>
<property name="format_sql">true</property>
```

**En la consola verá:**
```
Hibernate: SELECT miembro0_.id as id1_1_, miembro0_.activo as activo2_1_, ...
Hibernate: INSERT INTO miembros (id, nombre, email, ...)
Hibernate: UPDATE actividades SET plazas_disponibles = ...
```

---

## ⚠️ Troubleshooting

### Error: "Command not found: mvn"
**Solución:** Maven no está en PATH
```bash
# Usar ruta completa
"C:\Program Files\Apache\maven\bin\mvn" clean compile
```

### Error: "Java version mismatch"
**Solución:** Actualizar pom.xml con tu versión de Java
```bash
java -version  # Ver tu versión
# Editar pom.xml: <maven.compiler.source>11</maven.compiler.source>
```

### Error: "ClassNotFoundException"
**Solución:** Asegurarse de compilar antes
```bash
mvn clean compile  # Siempre hacer esto primero
```

### La aplicación se cierra sin menú
**Solución:** Hay un error de compilación. Revisar output de `mvn compile`

---

## 📝 Notas

- La base de datos es en memoria (H2), se crea al ejecutar
- Todos los datos se pierden al cerrar la aplicación
- Para ejecutar nuevamente, volver a correr `mvn exec:java...`
- Presionar `Ctrl+C` para salir

---

## ✅ Verificación de Funcionalidad

Después de ejecutar, verificar que aparezcan los datos iniciales:

```
========================================
INICIALIZANDO DATOS DE PRUEBA...
========================================

✓ Entrenadores creados: Carlos Martínez, Laura Pérez
✓ Actividades creadas: Pesas, Cardio, Yoga
✓ Entrenadores asignados a actividades
✓ Miembros registrados: Juan López, María García, Pedro Rodríguez

========================================
```

Si aparece esto ✓, todo está funcionando correctamente.

---

**¡Aplicación lista para demostración!** 🎉
