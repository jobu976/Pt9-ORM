# 🏗️ ARQUITECTURA DEL PROYECTO

## Diagrama General

```
┌─────────────────────────────────────────────────────────────────┐
│                     APLICACIÓN CONSOLE                           │
│                   (GimnasioApp.java)                            │
│                 Menú interactivo con 5 opciones                 │
└──────────────────────────────┬──────────────────────────────────┘
                               │
         ┌─────────────────────┼──────────────────────┐
         │                     │                      │
    ┌────▼────┐        ┌──────▼──────┐      ┌────────▼──────┐
    │ SERVICE  │        │   SERVICE   │      │    SERVICE    │
    │ Miembro  │        │  Actividad  │      │    Reserva    │
    │ Service  │        │  Service    │      │    Service    │
    └────┬────┘        └──────┬──────┘      └────────┬──────┘
         │                    │                      │
         └────────────────────┼──────────────────────┘
                              │
         ┌────────────────────┼──────────────────────┐
         │                    │                      │
    ┌────▼────┐        ┌──────▼──────┐      ┌────────▼──────┐
    │   DAO    │        │    DAO      │      │     DAO       │
    │ Miembro  │        │  Actividad  │      │    Reserva    │
    │  DAO     │        │   DAO       │      │     DAO       │
    └────┬────┘        └──────┬──────┘      └────────┬──────┘
         │                    │                      │
         └────────────────────┼──────────────────────┘
                              │
                    ┌─────────▼─────────┐
                    │   Hibernate       │
                    │   SessionFactory  │
                    │  (HibernateUtil)  │
                    └────────┬──────────┘
                             │
              ┌──────────────┴──────────────┐
              │                             │
         ┌────▼─────┐              ┌───────▼──────┐
         │  H2 DB   │              │  Entity      │
         │ (Memory)  │              │  Mapping     │
         └──────────┘              │              │
                                   │ @Entity      │
                                   │ @ManyToOne   │
                                   │ @ManyToMany  │
                                   └──────────────┘
```

---

## Capas de la Aplicación

### 1️⃣ CAPA DE PRESENTACIÓN (Console)

```
┌─────────────────────────────────────────┐
│         GimnasioApp.java                │
│  ┌───────────────────────────────────┐  │
│  │ menuMiembros()                    │  │
│  │ menuActividades()                 │  │
│  │ menuReservas()                    │  │
│  │ menuConsultasAvanzadas()          │  │
│  └───────────────────────────────────┘  │
│         ↓                                 │
│  ┌───────────────────────────────────┐  │
│  │ Lectura entrada usuario           │  │
│  │ Validación básica                 │  │
│  │ Manejo de opciones                │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
         ↓
    CAPA SERVICE
```

### 2️⃣ CAPA DE SERVICIOS (Lógica de Negocio)

```
┌────────────────────────────────────────────────┐
│         Service Layer                          │
│  ┌──────────────────────────────────────────┐  │
│  │ MiembroService                           │  │
│  │ • registrarMiembro()                     │  │
│  │ • obtenerMiembro()                       │  │
│  │ • agregarSaldo()                         │  │
│  └──────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────┐  │
│  │ ActividadService                         │  │
│  │ • crearActividad()                       │  │
│  │ • listarActividadesDisponibles()         │  │
│  │ • asignarEntrenador()                    │  │
│  └──────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────┐  │
│  │ ReservaService ⭐ TRANSACCIONES CRÍTICAS │  │
│  │ • reservarClase() ← BEGIN/COMMIT/ROLLBACK│  │
│  │ • cancelarReserva() ← BEGIN/COMMIT/ROLLBACK│
│  │ • obtenerMiembrosPorActividad() ← HQL   │  │
│  │ • contarReservasPorMiembro() ← HQL      │  │
│  └──────────────────────────────────────────┘  │
└────────────────────────────────────────────────┘
         ↓
    CAPA DAO
```

### 3️⃣ CAPA DAO (Acceso a Datos)

```
┌─────────────────────────────────────────────────┐
│         DAO Layer - CRUD Operations             │
│  ┌───────────────────────────────────────────┐  │
│  │ MiembroDAO                                │  │
│  │ • save(Miembro)           ← CREATE       │  │
│  │ • findById(id)            ← READ         │  │
│  │ • findAll()               ← READ ALL     │  │
│  │ • findByEmail(email)      ← READ CUSTOM  │  │
│  │ • update(Miembro)         ← UPDATE       │  │
│  │ • delete(id)              ← DELETE       │  │
│  └───────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────┐  │
│  │ ActividadDAO                              │  │
│  │ • save(Actividad)         ← CREATE       │  │
│  │ • findById(id)            ← READ         │  │
│  │ • findAll()               ← READ ALL     │  │
│  │ • findWithAvailableSpaces() ← HQL CUSTOM │  │
│  │ • update(Actividad)       ← UPDATE       │  │
│  │ • delete(id)              ← DELETE       │  │
│  └───────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────┐  │
│  │ ReservaDAO                                │  │
│  │ • save(Reserva)           ← CREATE       │  │
│  │ • findById(id)            ← READ         │  │
│  │ • findAll()               ← READ ALL     │  │
│  │ • findByMiembroId(id)     ← HQL CUSTOM   │  │
│  │ • findByActividadId(id)   ← HQL CUSTOM   │  │
│  │ • update(Reserva)         ← UPDATE       │  │
│  │ • delete(id)              ← DELETE       │  │
│  └───────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────┐  │
│  │ EntrenadorDAO                             │  │
│  │ • CRUD básico (igual estructura)          │  │
│  └───────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
         ↓
    HIBERNATE ORM
```

### 4️⃣ CAPA ORM (Hibernate/JPA)

```
┌──────────────────────────────────────────────┐
│           Hibernate ORM                       │
│  ┌────────────────────────────────────────┐  │
│  │ HibernateUtil.java                     │  │
│  │ ├─ buildSessionFactory()               │  │
│  │ ├─ getSessionFactory()                 │  │
│  │ ├─ getCurrentSession()                 │  │
│  │ └─ openSession()                       │  │
│  └────────────────────────────────────────┘  │
│           ↓                                   │
│  ┌────────────────────────────────────────┐  │
│  │ SessionFactory (singletón)             │  │
│  │ └─ crea Session por cada DAO           │  │
│  └────────────────────────────────────────┘  │
│           ↓                                   │
│  ┌────────────────────────────────────────┐  │
│  │ Entity Mapping                         │  │
│  │ ├─ Miembro          (@Entity, @Id, @Column)│
│  │ ├─ Actividad        (@Entity, @ManyToMany) │
│  │ ├─ Reserva          (@Entity, @ManyToOne)  │
│  │ └─ Entrenador       (@Entity, @ManyToMany) │
│  └────────────────────────────────────────┘  │
│           ↓                                   │
│  ┌────────────────────────────────────────┐  │
│  │ HQL Query Engine                       │  │
│  │ └─ Traduce HQL → SQL nativo            │  │
│  └────────────────────────────────────────┘  │
└──────────────────────────────────────────────┘
         ↓
    BASE DE DATOS
```

### 5️⃣ CAPA DE BASE DE DATOS

```
┌──────────────────────────────────────────────┐
│        H2 Database (en memoria)               │
│  jdbc:h2:mem:gimnasio                        │
│                                               │
│  ┌──────────────────────────────────────┐    │
│  │ Tabla: miembros                      │    │
│  │ ├─ id (BIGINT PK)                    │    │
│  │ ├─ nombre (VARCHAR)                  │    │
│  │ ├─ email (VARCHAR UNIQUE)            │    │
│  │ ├─ saldo_cuenta (DOUBLE)             │    │
│  │ └─ ...                               │    │
│  └──────────────────────────────────────┘    │
│  ┌──────────────────────────────────────┐    │
│  │ Tabla: actividades                   │    │
│  │ ├─ id (BIGINT PK)                    │    │
│  │ ├─ nombre (VARCHAR)                  │    │
│  │ ├─ plazas_disponibles (INT)          │    │
│  │ ├─ precio_clase (DOUBLE)             │    │
│  │ └─ ...                               │    │
│  └──────────────────────────────────────┘    │
│  ┌──────────────────────────────────────┐    │
│  │ Tabla: reservas                      │    │
│  │ ├─ id (BIGINT PK)                    │    │
│  │ ├─ miembro_id (FK)                   │    │
│  │ ├─ actividad_id (FK)                 │    │
│  │ ├─ estado (VARCHAR)                  │    │
│  │ └─ ...                               │    │
│  └──────────────────────────────────────┘    │
│  ┌──────────────────────────────────────┐    │
│  │ Tabla: entrenadores                  │    │
│  │ ├─ id (BIGINT PK)                    │    │
│  │ ├─ nombre (VARCHAR)                  │    │
│  │ ├─ especialidad (VARCHAR)            │    │
│  │ └─ ...                               │    │
│  └──────────────────────────────────────┘    │
│  ┌──────────────────────────────────────┐    │
│  │ Tabla: actividad_entrenador (N:M)   │    │
│  │ ├─ actividad_id (FK)                 │    │
│  │ └─ entrenador_id (FK)                │    │
│  └──────────────────────────────────────┘    │
└──────────────────────────────────────────────┘
```

---

## Flujo de una Transacción Compleja

### Caso: Reservar una clase

```
USUARIO
  │
  ├─ Selecciona: "Reservar clase"
  ├─ Ingresa: miembroId=1, actividadId=2
  │
  └──────────────────────────────────────────┐
                                             │
GimnasioApp.reservarClase()                  │
  │                                          │
  ├─ Llama a ReservaService.reservarClase()  │
  │                                          │
  └──────────────────────────────────────────┤
                                             │
ReservaService.reservarClase()               │
  │                                          │
  ├─ HibernateUtil.openSession() ← Abre DB   │
  │                                          │
  ├─ session.beginTransaction()              │
  │  └─ BEGIN; (en BD)                       │
  │                                          │
  ├─ session.get(Miembro, 1) ← Lee miembro   │
  │  └─ SELECT * FROM miembros WHERE id=1    │
  │                                          │
  ├─ Validación 1: ¿Existe? → SÍ ✓          │
  │                                          │
  ├─ session.get(Actividad, 2) ← Lee activ. │
  │  └─ SELECT * FROM actividades WHERE id=2│
  │                                          │
  ├─ Validación 2: ¿Existe? → SÍ ✓          │
  │                                          │
  ├─ Validación 3: ¿Plazas > 0? → SÍ ✓      │
  │                                          │
  ├─ Validación 4: ¿Saldo ≥ Precio? → SÍ ✓  │
  │                                          │
  ├─ session.save(new Reserva(...))          │
  │  └─ INSERT INTO reservas (...)           │
  │                                          │
  ├─ actividad.setPlazasDisponibles(19)      │
  ├─ session.update(actividad)               │
  │  └─ UPDATE actividades SET ...           │
  │                                          │
  ├─ miembro.setSaldoCuenta(97.0)            │
  ├─ session.update(miembro)                 │
  │  └─ UPDATE miembros SET ...              │
  │                                          │
  ├─ transaction.commit()                    │
  │  └─ COMMIT; (en BD)                      │
  │                                          │
  └─ session.close() ← Cierra conexión       │
     
     ✓ Resultado: Reserva creada, datos consistentes
```

### Caso: Error en validación (Rollback)

```
USUARIO
  │
  ├─ Selecciona: "Reservar clase"
  ├─ Ingresa: miembroId=2 ($2), actividadId=1 ($5)
  │
  └──────────────────────────────────────────┐
                                             │
ReservaService.reservarClase()               │
  │                                          │
  ├─ BEGIN;                                  │
  │                                          │
  ├─ Validación 4: ¿$2 ≥ $5? → NO ✗ FALLA   │
  │                                          │
  ├─ Lanza excepción:                        │
  │  "Saldo insuficiente"                    │
  │                                          │
  ├─ CATCH excepción                         │
  │                                          │
  ├─ transaction.rollback()                  │
  │  └─ ROLLBACK; (en BD)                    │
  │                                          │
  ├─ Deshace:                                │
  │  ├─ Ningún INSERT se guardó              │
  │  ├─ Ningún UPDATE se guardó              │
  │  └─ BD vuelve al estado anterior         │
  │                                          │
  └─ session.close()                         │
     
     ✗ Resultado: Nada guardado, datos consistentes
```

---

## Flujo de una Consulta HQL

```
USUARIO
  │
  ├─ Selecciona: "Listar miembros de una actividad"
  ├─ Ingresa: actividadId=2
  │
  └─────────────────────────────────────────┐
                                            │
ReservaService.obtenerMiembrosPorActividad()│
  │                                         │
  ├─ HibernateUtil.openSession()            │
  │                                         │
  ├─ session.createQuery(HQL)               │
  │  └─ "SELECT DISTINCT r.miembro FROM ... │
  │     WHERE r.actividad.id = :actividadId"│
  │                                         │
  ├─ setParameter("actividadId", 2)         │
  │                                         │
  ├─ getResultList()                        │
  │                                         │
  ├─ Hibernate traduce a SQL nativo:        │
  │  └─ SELECT DISTINCT m.* FROM miembros m│
  │     INNER JOIN reservas r ON m.id...    │
  │     WHERE r.actividad_id = 2            │
  │                                         │
  ├─ Base de datos ejecuta SQL              │
  │                                         │
  ├─ Retorna List<Miembro>                  │
  │                                         │
  └─ session.close()                        │
     
     ✓ Resultado: Lista de miembros filtrados
```

---

## Jerarquía de Clases

```
Object
├── Entity (JPA)
│   ├── Miembro
│   │   ├─ id: Long
│   │   ├─ nombre: String
│   │   ├─ email: String
│   │   ├─ saldoCuenta: Double
│   │   └─ getters/setters
│   │
│   ├── Actividad
│   │   ├─ id: Long
│   │   ├─ nombre: String
│   │   ├─ plazasDisponibles: Integer
│   │   ├─ precio: Double
│   │   ├─ entrenadores: Set<Entrenador>
│   │   └─ getters/setters
│   │
│   ├── Reserva
│   │   ├─ id: Long
│   │   ├─ miembro: Miembro (FK)
│   │   ├─ actividad: Actividad (FK)
│   │   ├─ estado: String
│   │   ├─ montoPagado: Double
│   │   └─ getters/setters
│   │
│   └── Entrenador
│       ├─ id: Long
│       ├─ nombre: String
│       ├─ especialidad: String
│       ├─ actividades: Set<Actividad>
│       └─ getters/setters
│
├── DAO
│   ├── MiembroDAO
│   │   ├─ save(Miembro): Miembro
│   │   ├─ findById(Long): Miembro
│   │   ├─ findAll(): List<Miembro>
│   │   ├─ update(Miembro): Miembro
│   │   └─ delete(Long): void
│   │
│   ├── ActividadDAO
│   │   ├─ save(Actividad): Actividad
│   │   ├─ findById(Long): Actividad
│   │   ├─ findAll(): List<Actividad>
│   │   ├─ findWithAvailableSpaces(): List<Actividad>
│   │   ├─ update(Actividad): Actividad
│   │   └─ delete(Long): void
│   │
│   ├── ReservaDAO
│   │   ├─ save(Reserva): Reserva
│   │   ├─ findById(Long): Reserva
│   │   ├─ findAll(): List<Reserva>
│   │   ├─ findByMiembroId(Long): List<Reserva>
│   │   ├─ findByActividadId(Long): List<Reserva>
│   │   ├─ update(Reserva): Reserva
│   │   └─ delete(Long): void
│   │
│   └── EntrenadorDAO
│       └─ (métodos CRUD)
│
├── Service
│   ├── MiembroService
│   │   ├─ registrarMiembro(Miembro): Miembro
│   │   ├─ obtenerMiembro(Long): Miembro
│   │   ├─ listarMiembros(): List<Miembro>
│   │   ├─ actualizarMiembro(Miembro): Miembro
│   │   ├─ eliminarMiembro(Long): void
│   │   └─ agregarSaldo(Long, Double): void
│   │
│   ├── ActividadService
│   │   ├─ crearActividad(Actividad): Actividad
│   │   ├─ obtenerActividad(Long): Actividad
│   │   ├─ listarActividades(): List<Actividad>
│   │   ├─ listarActividadesDisponibles(): List<Actividad>
│   │   ├─ actualizarActividad(Actividad): Actividad
│   │   ├─ eliminarActividad(Long): void
│   │   └─ asignarEntrenador(Long, Long): void
│   │
│   ├── ReservaService ⭐
│   │   ├─ reservarClase(Long, Long, LocalDateTime): Reserva
│   │   ├─ cancelarReserva(Long): void
│   │   ├─ obtenerMiembrosPorActividad(Long): List<Miembro>
│   │   ├─ contarReservasPorMiembro(Long): Long
│   │   ├─ obtenerActividadesLlenas(): List<Actividad>
│   │   └─ obtenerTodasLasReservas(): List<Reserva>
│   │
│   └── EntrenadorService
│       └─ (métodos básicos)
│
├── Util
│   └── HibernateUtil
│       ├─ buildSessionFactory(): SessionFactory
│       ├─ getSessionFactory(): SessionFactory
│       ├─ getCurrentSession(): Session
│       └─ openSession(): Session
│
└── App
    └── GimnasioApp
        ├─ main(String[]): void
        ├─ inicializarDatos(): void
        ├─ mostrarMenuPrincipal(): void
        ├─ menuMiembros(): void
        ├─ menuActividades(): void
        ├─ menuReservas(): void
        └─ menuConsultasAvanzadas(): void
```

---

## Patrón Arquitectónico: LAYERED (3-Tiers)

```
┌─────────────────────────────────────────────────────────┐
│              PRESENTATION LAYER                         │
│                  (Console App)                          │
│              GimnasioApp.java                           │
│          (Menú, entrada usuario, output)               │
└─────────────────────────────────────────────────────────┘
                         ↓↑
┌─────────────────────────────────────────────────────────┐
│              BUSINESS LOGIC LAYER                       │
│                 (Service Classes)                       │
│    MiembroService, ActividadService, ReservaService    │
│           (Lógica, validaciones, transacciones)         │
└─────────────────────────────────────────────────────────┘
                         ↓↑
┌─────────────────────────────────────────────────────────┐
│              DATA ACCESS LAYER                          │
│                  (DAO Classes)                          │
│    MiembroDAO, ActividadDAO, ReservaDAO                │
│       (CRUD, HQL queries, Transaction control)         │
└─────────────────────────────────────────────────────────┘
                         ↓↑
┌─────────────────────────────────────────────────────────┐
│              PERSISTENCE LAYER                          │
│           (Hibernate ORM + Database)                    │
│    Entity classes, SessionFactory, H2 Database          │
│        (Object-Relational Mapping)                      │
└─────────────────────────────────────────────────────────┘
```

---

## Gestión de Dependencias

```
GimnasioApp
├── Usa: MiembroService
├── Usa: ActividadService
├── Usa: ReservaService
└── Usa: EntrenadorService
   │
   ├─ MiembroService
   │  └── Usa: MiembroDAO
   │
   ├─ ActividadService
   │  ├── Usa: ActividadDAO
   │  └── Usa: EntrenadorDAO
   │
   ├─ ReservaService
   │  ├── Usa: ReservaDAO
   │  ├── Usa: MiembroDAO
   │  ├── Usa: ActividadDAO
   │  └── Usa: HibernateUtil
   │
   └─ EntrenadorService
      └── Usa: EntrenadorDAO
   
   Todos los DAOs dependen de:
   └── HibernateUtil
       ├── Usa: SessionFactory
       └── Usa: Entidades (Entity classes)
   
   Las entidades usan:
   ├── @Entity (JPA)
   ├── @ManyToOne (JPA)
   ├── @ManyToMany (JPA)
   └── @Column, @Id, etc. (JPA)
```

---

## Resumen Arquitectónico

| Componente | Responsabilidad | Archivo |
|-----------|-----------------|---------|
| **GimnasioApp** | Menú, entrada usuario | app/GimnasioApp.java |
| **Service** | Lógica negocio, transacciones | service/*.java |
| **DAO** | CRUD, HQL, sesiones | dao/*.java |
| **Entity** | Mapeo objeto-relacional | entity/*.java |
| **HibernateUtil** | Gestión SessionFactory | util/HibernateUtil.java |
| **hibernate.cfg.xml** | Configuración ORM | resources/hibernate.cfg.xml |
| **H2 DB** | Persistencia datos | Memoria (jdbc:h2:mem) |

**Ventajas de esta arquitectura:**
✅ Separación de responsabilidades
✅ Fácil testing (DAO mockeables)
✅ Escalabilidad
✅ Mantenibilidad
✅ Reutilización de código
