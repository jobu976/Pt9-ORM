# 📚 ÍNDICE DE DOCUMENTACIÓN

Bienvenido al Proyecto **GimnasioApp - Sistema de Gestión ORM con Hibernate**.

Este índice te ayuda a navegar toda la documentación disponible.

---

## 🚀 EMPEZAR AQUÍ

### 1️⃣ Para ejecutar rápidamente
👉 [GUIA_RAPIDA.md](GUIA_RAPIDA.md)
- Instrucciones paso a paso
- Compilación y ejecución
- Demostración interactiva recomendada
- Troubleshooting

### 2️⃣ Para entender qué hace el proyecto
👉 [README.md](README.md)
- Descripción general
- Modelo de datos
- Funcionalidades principales
- Tecnologías usadas

### 3️⃣ Para ver si cubre los criterios
👉 [CRITERIOS_CUBIERTOS.md](CRITERIOS_CUBIERTOS.md)
- Mapeo a criterios 3.1-3.7
- Código de cada funcionalidad
- Archivos relevantes
- Matriz de cobertura

---

## 📖 DOCUMENTACIÓN TÉCNICA

### Para aprender sobre arquitectura
👉 [ARQUITECTURA.md](ARQUITECTURA.md)
- Diagramas de capas
- Flujos de transacciones
- Diseño OOP
- Jerarquía de clases
- Patrones usados

### Para ver ejemplos prácticos
👉 [EJEMPLOS_PRACTICOS.md](EJEMPLOS_PRACTICOS.md)
- Casos de uso con SQL nativo
- Operaciones CRUD paso a paso
- Relaciones 1:N y N:M
- Transacciones exitosas y con rollback
- Consultas HQL explicadas

### Para validar con pruebas
👉 [SCRIPT_PRUEBAS.md](SCRIPT_PRUEBAS.md)
- 14 tests automáticos
- Resultados esperados para cada test
- Matriz de cobertura
- Tiempo estimado (20 minutos)

### Para confirmar completitud
👉 [PROYECTO_COMPLETADO.md](PROYECTO_COMPLETADO.md)
- Estado final del proyecto
- Checklist de funcionalidades
- Resumen de cobertura
- Puntos fuertes

---

## 💻 CÓDIGO FUENTE

### Entidades (Entity classes)
```
src/main/java/com/gimnasio/entity/
├── Miembro.java          (Soci del gimnasio)
├── Actividad.java        (Clase/actividad)
├── Reserva.java          (Reserva de clase - CRÍTICA)
└── Entrenador.java       (Entrenador)
```

**Leer primero:** Estas clases definen la estructura de datos con anotaciones JPA.

### Data Access Objects (DAO)
```
src/main/java/com/gimnasio/dao/
├── MiembroDAO.java       (CRUD: create, read, update, delete)
├── ActividadDAO.java     (CRUD + queries custom)
├── ReservaDAO.java       (CRUD + queries custom)
└── EntrenadorDAO.java    (CRUD básico)
```

**Leer segundo:** Implementan CRUD y HQL queries.

### Services (Lógica de negocio)
```
src/main/java/com/gimnasio/service/
├── MiembroService.java       (Operaciones de miembro)
├── ActividadService.java     (Operaciones de actividad)
├── ReservaService.java       (⭐ TRANSACCIONES AQUÍ)
└── EntrenadorService.java    (Operaciones de entrenador)

ReservaService es CRÍTICO:
  ├── reservarClase()          ← Transacción compleja
  ├── cancelarReserva()        ← Transacción compleja
  └── Consultas HQL avanzadas
```

**Leer tercero:** Aquí está la lógica y las transacciones.

### Utilidades
```
src/main/java/com/gimnasio/util/
└── HibernateUtil.java    (Gestión SessionFactory)
```

**Referencia:** Proporciona acceso centralizado a sesiones.

### Aplicación Console
```
src/main/java/com/gimnasio/app/
└── GimnasioApp.java      (Menú principal e interactivo)
```

**Punto de entrada:** Main method aquí.

### Configuración
```
src/main/resources/
└── hibernate.cfg.xml     (Configuración Hibernate + Base de datos)
```

**Referencia:** Propiedades de conexión y mapeos.

---

## 🔍 BUSCAR POR TEMA

### CRUD (Crear, Leer, Actualizar, Eliminar)

**Dónde buscar:**
- [MiembroDAO.java](src/main/java/com/gimnasio/dao/MiembroDAO.java) - Ejemplo completo
- [ActividadDAO.java](src/main/java/com/gimnasio/dao/ActividadDAO.java)
- [ReservaDAO.java](src/main/java/com/gimnasio/dao/ReservaDAO.java)

**Métodos clave:**
- `save(T)` → CREATE
- `findById(id)` → READ
- `findAll()` → READ ALL
- `update(T)` → UPDATE
- `delete(id)` → DELETE

**Documentación:** [EJEMPLOS_PRACTICOS.md#crud](EJEMPLOS_PRACTICOS.md) (Tests 3-6, 14)

---

### Relaciones ORM (1:N y N:M)

**Dónde buscar:**
- [Miembro.java](src/main/java/com/gimnasio/entity/Miembro.java) - Lado 1 de 1:N
- [Reserva.java](src/main/java/com/gimnasio/entity/Reserva.java) - Lado N de 1:N
- [Actividad.java](src/main/java/com/gimnasio/entity/Actividad.java) - Lado N:M
- [Entrenador.java](src/main/java/com/gimnasio/entity/Entrenador.java) - Lado N:M

**Anotaciones clave:**
- `@ManyToOne` - Relación N:1
- `@ManyToMany` - Relación N:M
- `@JoinTable` - Tabla intermedia

**Documentación:** [EJEMPLOS_PRACTICOS.md#relaciones](EJEMPLOS_PRACTICOS.md) (Tests 7)

---

### Consultas HQL (Avanzadas)

**Dónde buscar:**
- [ReservaService.java](src/main/java/com/gimnasio/service/ReservaService.java) - Métodos:
  - `obtenerMiembrosPorActividad()` - SELECT DISTINCT
  - `contarReservasPorMiembro()` - COUNT
  - `obtenerActividadesLlenas()` - WHERE
- [ActividadDAO.java](src/main/java/com/gimnasio/dao/ActividadDAO.java) - `findWithAvailableSpaces()`

**Ejemplos:**
```java
// SELECT DISTINCT con JOIN
SELECT DISTINCT r.miembro FROM Reserva r 
WHERE r.actividad.id = :actividadId

// COUNT con múltiples condiciones
SELECT COUNT(r) FROM Reserva r 
WHERE r.miembro.id = :miembroId AND r.estado = 'CONFIRMADA'

// WHERE simple
FROM Actividad WHERE plazasDisponibles = 0
```

**Documentación:** [EJEMPLOS_PRACTICOS.md#hql](EJEMPLOS_PRACTICOS.md) (Tests 11-13)

---

### Transacciones ACID (Críticas) ⭐

**Dónde buscar:**
- [ReservaService.java](src/main/java/com/gimnasio/service/ReservaService.java) - Métodos:
  - `reservarClase()` ← **OPERACIÓN TRANSACCIONAL COMPLEJA**
  - `cancelarReserva()` ← **OPERACIÓN TRANSACCIONAL CON REEMBOLSO**

**Estructura genérica:**
```java
try {
    transaction = session.beginTransaction();
    
    // Validaciones
    // Operaciones múltiples (CREATE, UPDATE, UPDATE)
    
    transaction.commit();  // Si todo OK
} catch (Exception e) {
    transaction.rollback();  // Si hay error
}
```

**Garantías:**
- ✅ Atomicidad: Todo o nada
- ✅ Consistencia: Datos válidos siempre
- ✅ Aislamiento: Transacciones independientes
- ✅ Durabilidad: COMMIT persiste en BD

**Documentación:** [EJEMPLOS_PRACTICOS.md#transacciones](EJEMPLOS_PRACTICOS.md) (Tests 8-10)

---

### Configuración Hibernate

**Dónde buscar:**
- [hibernate.cfg.xml](src/main/resources/hibernate.cfg.xml) - Configuración central
- [HibernateUtil.java](src/main/java/com/gimnasio/util/HibernateUtil.java) - Gestión SessionFactory

**Propiedades importantes:**
```xml
<property name="connection.driver_class">org.h2.Driver</property>
<property name="connection.url">jdbc:h2:mem:gimnasio</property>
<property name="dialect">org.hibernate.dialect.H2Dialect</property>
<property name="show_sql">true</property>  ← Ver SQL generado
<property name="hbm2ddl.auto">create</property>  ← Crear tablas
```

**Documentación:** [README.md#configuracion](README.md)

---

## 🎯 CUÁNDO LEER CADA DOCUMENTO

### Antes de compilar/ejecutar
1. [GUIA_RAPIDA.md](GUIA_RAPIDA.md) - 5 minutos
2. [README.md](README.md) - 10 minutos

### Mientras se ejecuta la demostración
1. [SCRIPT_PRUEBAS.md](SCRIPT_PRUEBAS.md) - Seguir paso a paso
2. [EJEMPLOS_PRACTICOS.md](EJEMPLOS_PRACTICOS.md) - Comparar con output

### Después de la demostración
1. [CRITERIOS_CUBIERTOS.md](CRITERIOS_CUBIERTOS.md) - Verificar criterios
2. [ARQUITECTURA.md](ARQUITECTURA.md) - Entender diseño
3. [PROYECTO_COMPLETADO.md](PROYECTO_COMPLETADO.md) - Resumen final

### Para estudiar en profundidad
1. [README.md](README.md) - Conceptos generales
2. [ARQUITECTURA.md](ARQUITECTURA.md) - Diseño
3. [EJEMPLOS_PRACTICOS.md](EJEMPLOS_PRACTICOS.md) - Casos de uso
4. Código fuente - Implementación

---

## 📊 TAMAÑO DE DOCUMENTACIÓN

| Documento | Tamaño | Tiempo Lectura |
|-----------|--------|----------------|
| GUIA_RAPIDA.md | ~4 KB | 5 min |
| README.md | ~20 KB | 15 min |
| CRITERIOS_CUBIERTOS.md | ~25 KB | 20 min |
| EJEMPLOS_PRACTICOS.md | ~30 KB | 25 min |
| SCRIPT_PRUEBAS.md | ~15 KB | 10 min |
| ARQUITECTURA.md | ~20 KB | 15 min |
| PROYECTO_COMPLETADO.md | ~8 KB | 5 min |

**Total:** ~122 KB documentación
**Tiempo total lectura:** ~95 minutos (si lees todo)
**Tiempo recomendado:** 20-30 minutos (lo esencial)

---

## 🔗 NAVEGACIÓN RÁPIDA

### Por Rol

**Si eres ESTUDIANTE (quiero entender):**
1. [GUIA_RAPIDA.md](GUIA_RAPIDA.md) - Cómo ejecutar
2. [README.md](README.md) - Qué hace
3. [ARQUITECTURA.md](ARQUITECTURA.md) - Cómo está hecho
4. Código fuente - Ver implementación

**Si eres PROFESOR (quiero evaluar):**
1. [CRITERIOS_CUBIERTOS.md](CRITERIOS_CUBIERTOS.md) - Criterios
2. [SCRIPT_PRUEBAS.md](SCRIPT_PRUEBAS.md) - Validación
3. [PROYECTO_COMPLETADO.md](PROYECTO_COMPLETADO.md) - Resumen
4. Código fuente - Revisar implementación

**Si eres EVALUADOR (quiero verificar):**
1. [PROYECTO_COMPLETADO.md](PROYECTO_COMPLETADO.md) - Estado
2. [GUIA_RAPIDA.md](GUIA_RAPIDA.md) - Ejecutar
3. [SCRIPT_PRUEBAS.md](SCRIPT_PRUEBAS.md) - Pruebas
4. [CRITERIOS_CUBIERTOS.md](CRITERIOS_CUBIERTOS.md) - Criterios

---

## 🎓 ESTRUCTURA EDUCATIVA

El proyecto está diseñado para ser entendido en niveles:

### Nivel 1: Usuario Final
- Lee: [GUIA_RAPIDA.md](GUIA_RAPIDA.md)
- Hace: Ejecuta menú y prueba
- Tiempo: 20 minutos

### Nivel 2: Desarrollador Junior
- Lee: [README.md](README.md) + [EJEMPLOS_PRACTICOS.md](EJEMPLOS_PRACTICOS.md)
- Entiende: Qué hace cada parte
- Tiempo: 45 minutos

### Nivel 3: Desarrollador Senior
- Lee: Todo
- Entiende: Diseño, patrones, decisiones
- Modifica: Agrega nuevas entidades
- Tiempo: 2 horas

---

## ✅ CHECKLIST DE LECTURA

### Mínimo (20 minutos)
- [ ] GUIA_RAPIDA.md
- [ ] README.md (primera mitad)
- [ ] Ejecutar proyecto

### Recomendado (45 minutos)
- [ ] Todo lo anterior
- [ ] EJEMPLOS_PRACTICOS.md
- [ ] SCRIPT_PRUEBAS.md
- [ ] CRITERIOS_CUBIERTOS.md

### Completo (95 minutos)
- [ ] Todo lo anterior
- [ ] ARQUITECTURA.md
- [ ] PROYECTO_COMPLETADO.md
- [ ] Revisar código fuente

---

## 🆘 AYUDA RÁPIDA

### "¿Cómo ejecuto?"
→ [GUIA_RAPIDA.md](GUIA_RAPIDA.md#pasos-para-ejecutar-la-aplicación)

### "¿Qué hace?"
→ [README.md](README.md#descripción-del-proyecto)

### "¿Cubre los criterios?"
→ [CRITERIOS_CUBIERTOS.md](CRITERIOS_CUBIERTOS.md#resumen-de-cobertura)

### "¿Cómo pruebo?"
→ [SCRIPT_PRUEBAS.md](SCRIPT_PRUEBAS.md#test-1-compilación)

### "¿Cuál es la arquitectura?"
→ [ARQUITECTURA.md](ARQUITECTURA.md#capas-de-la-aplicación)

### "¿Qué falla al compilar?"
→ [GUIA_RAPIDA.md#troubleshooting](GUIA_RAPIDA.md#-troubleshooting)

### "¿Dónde está el CRUD?"
→ [EJEMPLOS_PRACTICOS.md#crud-create](EJEMPLOS_PRACTICOS.md) (Tests 3-6)

### "¿Dónde están las transacciones?"
→ [ReservaService.java](src/main/java/com/gimnasio/service/ReservaService.java)

---

## 📌 RESUMEN EJECUTIVO

```
PROYECTO: Sistema de Gestión de Gimnasio ORM
TECNOLOGÍA: Hibernate/JPA + H2
CRITERIOS: 3.1-3.7 (Acceso a Datos)

FUNCIONALIDADES:
✅ CRUD completo (create, read, update, delete)
✅ Relaciones 1:N y N:M
✅ Consultas HQL avanzadas
✅ Transacciones ACID con rollback
✅ Menú console interactivo

TIEMPO DE LECTURA:
- Mínimo: 20 min
- Recomendado: 45 min
- Completo: 95 min

TIEMPO DE EJECUCIÓN:
- Compilación: 30 seg
- Demostración: 15 min
- Pruebas completas: 20 min

ESTADO: 100% Completo ✅
```

---

**¡Bienvenido! Comienza por [GUIA_RAPIDA.md](GUIA_RAPIDA.md)** 🚀
