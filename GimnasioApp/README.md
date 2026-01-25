# Sistema de Gestión de Gimnasio - ORM con Hibernate

## 📋 Descripción del Proyecto

Aplicación completa que demuestra el uso de **Hibernate/JPA ORM** para gestionar un gimnasio. Implementa todas las operaciones CRUD, consultas HQL avanzadas y transacciones complejas.

## 🎯 Criterios Cubiertos

- ✅ **3.1-3.7**: Configuración y uso del ORM
- ✅ **3.6, 3.8-3.11**: Relaciones entre entidades, consultas y transacciones

## 🏗️ Arquitectura

```
GimnasioApp/
├── src/main/java/com/gimnasio/
│   ├── entity/          # Entidades JPA (@Entity)
│   │   ├── Miembro.java        # Socio del gimnasio (1:N con Reserva)
│   │   ├── Actividad.java      # Clase/actividad (1:N con Reserva, N:M con Entrenador)
│   │   ├── Reserva.java        # Reserva de clase (N:1 con Miembro y Actividad)
│   │   └── Entrenador.java     # Entrenador (N:M con Actividad)
│   ├── dao/             # Data Access Objects (CRUD base)
│   │   ├── MiembroDAO.java
│   │   ├── ActividadDAO.java
│   │   ├── ReservaDAO.java
│   │   └── EntrenadorDAO.java
│   ├── service/         # Lógica de negocio y transacciones
│   │   ├── MiembroService.java
│   │   ├── ActividadService.java
│   │   ├── ReservaService.java      # ⭐ TRANSACCIONES CRÍTICAS
│   │   └── EntrenadorService.java
│   ├── util/            # Utilidades
│   │   └── HibernateUtil.java       # Gestión de SessionFactory
│   └── app/
│       └── GimnasioApp.java         # Aplicación Console con menú
├── src/main/resources/
│   └── hibernate.cfg.xml             # Configuración de Hibernate
└── pom.xml                           # Dependencias Maven
```

## 📊 Modelo de Datos

### Relaciones

```
┌─────────────┐         ┌──────────────┐
│   Miembro   │◄───────►│   Reserva    │
│  (1:N)      │         │  (N:1)       │
└─────────────┘         └──────────────┘
                              │
                              ▼
┌─────────────┐         ┌──────────────┐
│ Entrenador  │◄──────►│   Actividad  │
│  (N:M)      │        │   (N:M)      │
└─────────────┘         └──────────────┘
```

### Entidades

#### 1. **Miembro** (Entity: miembros)
```
- id (PK, auto-incremento)
- nombre (String, no nulo)
- email (String, único)
- telefono (String)
- fechaInscripcion (LocalDateTime)
- saldoCuenta (Double)
- activo (Boolean)
```

#### 2. **Actividad** (Entity: actividades)
```
- id (PK, auto-incremento)
- nombre (String)
- descripcion (String)
- capacidadMaxima (Integer)
- plazasDisponibles (Integer)
- precioClase (Double)
- horaInicio (LocalTime)
- duracionMinutos (Integer)
- entrenadores (Set<Entrenador>) [N:M]
```

#### 3. **Reserva** (Entity: reservas) ⭐ CRÍTICA PARA TRANSACCIONES
```
- id (PK, auto-incremento)
- miembro (FK → Miembro) [N:1]
- actividad (FK → Actividad) [N:1]
- fechaReserva (LocalDateTime)
- fechaClase (LocalDateTime)
- estado (String: CONFIRMADA, CANCELADA, ASISTIO)
- montoPagado (Double)
```

#### 4. **Entrenador** (Entity: entrenadores)
```
- id (PK, auto-incremento)
- nombre (String)
- especialidad (String)
- email (String)
- telefono (String)
- experienciaAnos (Integer)
- actividades (Set<Actividad>) [N:M mappedBy]
```

## ✨ Funcionalidades Clave

### 1️⃣ CRUD Completo

#### CREATE
```java
// Crear Miembro
Miembro miembro = new Miembro("Juan López", "juan@email.com", "555-0001", 100.0);
Miembro creado = miembroService.registrarMiembro(miembro);

// Crear Actividad
Actividad actividad = new Actividad("Pesas", "Musculación", 15, 5.0, 
    LocalTime.of(9, 0), 60);
Actividad creada = actividadService.crearActividad(actividad);
```

#### READ
```java
// Obtener por ID
Miembro miembro = miembroService.obtenerMiembro(1L);

// Obtener por Email
Miembro miembro = miembroService.obtenerMiembroPorEmail("juan@email.com");

// Listar todos
List<Miembro> todos = miembroService.listarMiembros();
```

#### UPDATE
```java
Miembro miembro = miembroService.obtenerMiembro(1L);
miembro.setNombre("Nuevo nombre");
miembroService.actualizarMiembro(miembro);
```

#### DELETE
```java
miembroService.eliminarMiembro(1L);
```

### 2️⃣ Consultas HQL Avanzadas

#### Listar miembros de una actividad específica
```java
List<Miembro> miembros = reservaService.obtenerMiembrosPorActividad(actividadId);
```
**Query HQL:**
```sql
SELECT DISTINCT r.miembro FROM Reserva r 
WHERE r.actividad.id = :actividadId
```

#### Contar reservas confirmadas de un miembro
```java
Long cantidad = reservaService.contarReservasPorMiembro(miembroId);
```
**Query HQL:**
```sql
SELECT COUNT(r) FROM Reserva r 
WHERE r.miembro.id = :miembroId AND r.estado = 'CONFIRMADA'
```

#### Actividades sin plazas disponibles
```java
List<Actividad> llenas = reservaService.obtenerActividadesLlenas();
```
**Query HQL:**
```sql
FROM Actividad WHERE plazasDisponibles = 0
```

#### Actividades con plazas disponibles
```java
List<Actividad> disponibles = actividadService.listarActividadesDisponibles();
```
**Query HQL:**
```sql
FROM Actividad WHERE plazasDisponibles > 0
```

### 3️⃣ ⭐ TRANSACCIONES COMPLEJAS

#### Reservar una clase (OPERACIÓN TRANSACCIONAL CRÍTICA)

```java
Reserva reserva = reservaService.reservarClase(miembroId, actividadId, fechaClase);
```

**Pasos atómicos:**
1. ✅ Validar que el miembro existe
2. ✅ Validar que la actividad existe
3. ✅ Validar plazas disponibles > 0
4. ✅ Validar saldo del miembro >= precio
5. ✅ **CREAR** registro Reserva
6. ✅ **RESTAR** 1 plaza a Actividad.plazasDisponibles
7. ✅ **RESTAR** dinero de Miembro.saldoCuenta
8. ✅ **COMMIT** (si todo OK) o **ROLLBACK** (si error)

**Garantía ACID:**
- Si falla cualquier paso → **ROLLBACK automático**
- Ningún cambio se persiste si hay error
- Integridad de datos garantizada

**Ejemplo de rollback:**
```
Intento: Reservar clase de $5 (miembro tiene $3)
→ Validación falla: saldo insuficiente
→ ROLLBACK automático
→ No se crea reserva, no se restan plazas, saldo intacto
```

#### Cancelar una reserva (OPERACIÓN TRANSACCIONAL)

```java
reservaService.cancelarReserva(reservaId);
```

**Pasos atómicos:**
1. ✅ Obtener reserva
2. ✅ Validar que no esté ya cancelada
3. ✅ Cambiar estado a "CANCELADA"
4. ✅ **SUMAR** 1 plaza a Actividad
5. ✅ **DEVOLVER** dinero a Miembro
6. ✅ **COMMIT** o **ROLLBACK**

## 🚀 Cómo Ejecutar

### Requisitos
- Java 11+
- Maven 3.6+

### Compilar
```bash
cd GimnasioApp
mvn clean compile
```

### Ejecutar
```bash
mvn exec:java -Dexec.mainClass="com.gimnasio.app.GimnasioApp"
```

### Menú Interactivo

```
╔════════════════════════════════════════╗
║    SISTEMA DE GESTIÓN DEL GIMNASIO     ║
╚════════════════════════════════════════╝
1. Gestionar Miembros
   - Registrar nuevo
   - Ver por ID
   - Listar todos
   - Actualizar
   - Agregar saldo
   - Eliminar

2. Gestionar Actividades
   - Crear actividad
   - Ver por ID
   - Listar todas
   - Ver disponibles

3. Gestionar Reservas ⭐
   - Reservar clase (TRANSACCIÓN)
   - Ver reserva
   - Cancelar (TRANSACCIÓN)
   - Listar todas

4. Consultas Avanzadas
   - Miembros de una actividad
   - Contar reservas
   - Actividades llenas
```

## 🗄️ Base de Datos

**Motor:** H2 en memoria (jdbc:h2:mem:gimnasio)
- Creación automática de tablas
- Ideal para testing
- Datos se resetean cada ejecución

**Tablas generadas:**
```sql
CREATE TABLE miembros (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    fecha_inscripcion TIMESTAMP NOT NULL,
    saldo_cuenta DOUBLE,
    activo BOOLEAN
);

CREATE TABLE actividades (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    capacidad_maxima INT NOT NULL,
    plazas_disponibles INT NOT NULL,
    precio_clase DOUBLE,
    hora_inicio TIME,
    duracion_minutos INT
);

CREATE TABLE reservas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    miembro_id BIGINT NOT NULL REFERENCES miembros(id),
    actividad_id BIGINT NOT NULL REFERENCES actividades(id),
    fecha_reserva TIMESTAMP NOT NULL,
    fecha_clase TIMESTAMP,
    estado VARCHAR(20),
    monto_pagado DOUBLE
);

CREATE TABLE entrenadores (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    especialidad VARCHAR(50),
    email VARCHAR(100),
    telefono VARCHAR(20),
    experiencia_anos INT
);

CREATE TABLE actividad_entrenador (
    actividad_id BIGINT REFERENCES actividades(id),
    entrenador_id BIGINT REFERENCES entrenadores(id),
    PRIMARY KEY (actividad_id, entrenador_id)
);
```

## 📝 Configuración Hibernate

Archivo: `hibernate.cfg.xml`
```xml
<property name="connection.driver_class">org.h2.Driver</property>
<property name="connection.url">jdbc:h2:mem:gimnasio</property>
<property name="dialect">org.hibernate.dialect.H2Dialect</property>
<property name="show_sql">true</property>
<property name="hbm2ddl.auto">create</property>
```

## 🔍 Casos de Uso Demostrables

### Caso 1: Reserva Exitosa
```
1. Crear miembro: Juan ($100)
2. Crear actividad: Pesas ($5, 15 plazas)
3. Reservar: Juan en Pesas
   ✓ Reserva creada
   ✓ Plazas: 15 → 14
   ✓ Saldo: $100 → $95
   ✓ COMMIT exitoso
```

### Caso 2: Reserva con Rollback (Saldo insuficiente)
```
1. Crear miembro: María ($2)
2. Crear actividad: Yoga ($5, 10 plazas)
3. Intentar reservar: María en Yoga
   ✗ Validación falla: necesita $5, tiene $2
   ✗ ROLLBACK automático
   ✗ Plazas: 10 (sin cambios)
   ✗ Saldo: $2 (sin cambios)
```

### Caso 3: Cancelación y Reembolso
```
1. Reserva activa: Pedro en Cardio ($3, 1 plaza)
2. Cancelar reserva
   ✓ Estado: CANCELADA
   ✓ Plazas: 1 → 2 (recuperada)
   ✓ Saldo Pedro: +$3 (reembolso)
   ✓ COMMIT exitoso
```

### Caso 4: Consulta HQL - Miembros por Actividad
```
1. Actividad: "Pesas" (ID: 1)
2. Reservas activas:
   - Juan
   - Carlos
   - Elena
3. Query: SELECT DISTINCT miembros de la actividad 1
4. Resultado: [Juan, Carlos, Elena]
```

## 📦 Dependencias

```xml
<!-- Hibernate ORM -->
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>5.6.15.Final</version>
</dependency>

<!-- JPA API -->
<dependency>
    <groupId>javax.persistence</groupId>
    <artifactId>javax.persistence-api</artifactId>
    <version>2.2</version>
</dependency>

<!-- H2 Database -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.1.214</version>
</dependency>

<!-- Logging -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.36</version>
</dependency>
```

## 🎓 Conceptos ORM Demostrados

| Concepto | Ubicación | Ejemplo |
|----------|-----------|---------|
| **@Entity** | entity/* | `@Entity class Miembro` |
| **@Id + @GeneratedValue** | entity/* | Auto-increment de PK |
| **@Column** | entity/* | `@Column(nullable=false, unique=true)` |
| **@ManyToOne** | Reserva | FK a Miembro y Actividad |
| **@OneToMany** | (mappedBy implícito) | Miembro → múltiples Reservas |
| **@ManyToMany** | Actividad, Entrenador | N:M con @JoinTable |
| **@JoinTable** | Actividad | Tabla intermedia actividad_entrenador |
| **Cascade** | Actividad | `cascade = CascadeType.ALL` |
| **Fetch strategy** | Reserva | `fetch = FetchType.EAGER` |
| **HQL Query** | ReservaService | `createQuery(HQL)` |
| **Transaction** | DAO, Service | `beginTransaction() / commit() / rollback()` |
| **Session Management** | HibernateUtil | `openSession() / getCurrentSession()` |

## ⚠️ Puntos Críticos de Transacciones

El archivo [ReservaService.java](src/main/java/com/gimnasio/service/ReservaService.java) es **CRÍTICO** para demostrar:

```java
try {
    transaction = session.beginTransaction();
    
    // Validaciones
    if (plazasDisponibles <= 0) throw new Exception();
    if (saldoMiembro < precio) throw new Exception();
    
    // Operaciones múltiples
    session.save(reserva);      // Crear
    session.update(actividad);   // Restar plazas
    session.update(miembro);     // Restar dinero
    
    transaction.commit();  // ✓ TODO OK
} catch (Exception e) {
    transaction.rollback();  // ✗ DESHACE CAMBIOS
}
```

## 📄 Licencia

Proyecto educativo - DAM2 Acceso a Datos
