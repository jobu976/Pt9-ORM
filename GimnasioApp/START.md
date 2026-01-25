# 🎯 INICIO RÁPIDO - GIMNASIO ORM

## ⚡ En 3 pasos

### 1️⃣ Compilar (30 segundos)
```bash
cd c:\Users\jbuna\Documents\DAM2\AD\GimnasioApp
mvn clean compile
```

### 2️⃣ Ejecutar (5 segundos)
```bash
mvn exec:java -Dexec.mainClass="com.gimnasio.app.GimnasioApp"
```

### 3️⃣ Usar (15 minutos)
```
Menú aparece automáticamente
Selecciona opción 1-5
Prueba cada funcionalidad
```

---

## ✅ Qué verás

```
✓ Datos de prueba cargados automáticamente
✓ 3 miembros, 3 actividades, 2 entrenadores
✓ Menú console funcional
✓ SQL generado en consola (LOG)
✓ Errores de validación claros
```

---

## 🧪 Prueba clave (⭐ Transacción)

```
1. Selecciona: 3 (Gestionar Reservas)
2. Selecciona: 1 (Reservar una clase)
3. Ingresa: 
   ID miembro: 1
   ID actividad: 2
   
Resultado esperado:
✓ Reserva confirmada para Juan en Cardio

Cambios automáticos:
├─ Reserva creada
├─ Plazas: 20 → 19
├─ Saldo: $100 → $97
└─ Transacción completada ✓
```

---

## 📚 Documentación (elegir 1)

### Para ver cómo se ejecuta
👉 **[GUIA_RAPIDA.md](GUIA_RAPIDA.md)** (5 minutos)

### Para entender qué hace
👉 **[README.md](README.md)** (15 minutos)

### Para verificar criterios
👉 **[CRITERIOS_CUBIERTOS.md](CRITERIOS_CUBIERTOS.md)** (20 minutos)

### Para ver ejemplos concretos
👉 **[EJEMPLOS_PRACTICOS.md](EJEMPLOS_PRACTICOS.md)** (25 minutos)

### Para validar con pruebas
👉 **[SCRIPT_PRUEBAS.md](SCRIPT_PRUEBAS.md)** (20 minutos)

### Para entender la arquitectura
👉 **[ARQUITECTURA.md](ARQUITECTURA.md)** (15 minutos)

### Índice completo
👉 **[INDICE.md](INDICE.md)** (para navegar todo)

---

## 🎯 Lo que demuestra este proyecto

### ✅ ORM Configurado
- Hibernate instalado y configurado
- SessionFactory centralizado
- H2 Database automático

### ✅ Entidades (4)
```
Miembro → Entidad de negocio (1:N con Reserva)
Actividad → Clase del gimnasio (1:N y N:M)
Reserva → Vínculo (N:1 bidireccional)
Entrenador → Recurso (N:M con Actividad)
```

### ✅ CRUD Completo
```
CREATE: Crear nuevos registros
READ:   Obtener por ID y listar
UPDATE: Modificar datos
DELETE: Eliminar registros
```

### ✅ Consultas Avanzadas (HQL)
```
SELECT DISTINCT r.miembro FROM Reserva r WHERE r.actividad.id = ?
SELECT COUNT(r) FROM Reserva r WHERE ... AND r.estado = 'CONFIRMADA'
FROM Actividad WHERE plazasDisponibles = 0
```

### ⭐ Transacciones ACID
```
reservarClase()
├─ BEGIN
├─ Validaciones múltiples
├─ CREATE, UPDATE, UPDATE
├─ COMMIT ✓ o ROLLBACK ✗
└─ Garantía de consistencia
```

---

## 🔥 Puntos destacados

### 1. Reservar con garantía ACID
```
Intenta: Crear reserva + restar plazas + restar dinero
Si falla: ROLLBACK automático (nada se modifica)
Si éxito: COMMIT (todos los cambios se guardan)
Resultado: Base de datos SIEMPRE consistente
```

### 2. Relaciones correctas
```
1:N  → Miembro tiene muchas Reservas
N:M  → Actividad tiene muchos Entrenadores
```

### 3. Menú interactivo
```
5 opciones principales
Submenús CRUD
Consultas avanzadas
Mensajes claros
```

---

## ⏱️ Timeline recomendado

```
0:00  → Compilación (30 seg)
0:30  → Inicio app (5 seg)
1:00  → Crear miembro (CRUD CREATE)
2:00  → Ver miembro (CRUD READ)
3:00  → Listar miembros (CRUD READ ALL)
4:00  → Agregar saldo (CRUD UPDATE)
5:00  → Reservar clase (TRANSACCIÓN EXITOSA)
7:00  → Listar reservas (verificar cambios)
8:00  → Consulta HQL #1 (miembros por actividad)
9:00  → Consulta HQL #2 (contar reservas)
10:00 → Cancelar reserva (TRANSACCIÓN + REEMBOLSO)
12:00 → Demostración completa ✓
```

---

## 🚨 Si hay error

### "Command not found: mvn"
```bash
# Usar ruta completa
"C:\Program Files\Apache\maven\bin\mvn" clean compile
```

### "Java version mismatch"
```bash
java -version  # Ver tu versión de Java
# Editar pom.xml: cambiar <maven.compiler.source>11</maven.compiler.source>
```

### "Error de compilación"
```bash
mvn clean compile  # Asegurarse de limpiar antes
mvn compile -X     # Ver más detalles
```

### "La app se cierra sin menú"
```bash
# Revisar que compile correctamente
mvn clean compile 2>&1 | grep ERROR
```

---

## 💡 Notas importantes

1. **Base de datos en memoria** → Se crea al ejecutar, se pierde al cerrar
2. **Datos de prueba** → Se crean automáticamente (3 miembros, 3 actividades)
3. **SQL visible** → Se muestra en consola (hibernate.cfg.xml lo permite)
4. **Transacciones seguras** → ROLLBACK automático si hay error
5. **Código documentado** → Comentarios explicativos en puntos clave

---

## 🎓 Antes de presentar

✅ Compila sin errores:
```bash
mvn clean compile
```

✅ Se ejecuta sin errors:
```bash
mvn exec:java -Dexec.mainClass="com.gimnasio.app.GimnasioApp"
```

✅ Menú aparece:
```
╔════════════════════════════════════════╗
║    SISTEMA DE GESTIÓN DEL GIMNASIO     ║
╚════════════════════════════════════════╝
```

✅ Datos iniciales se cargan:
```
✓ Entrenadores creados: Carlos Martínez, Laura Pérez
✓ Actividades creadas: Pesas, Cardio, Yoga
✓ Miembros registrados: Juan López, María García, Pedro Rodríguez
```

---

## 📊 Estructura simplificada

```
GimnasioApp/
├── Código
│   ├── Entidades (4)
│   ├── DAOs (4) ← CRUD
│   ├── Services (4) ← Lógica + Transacciones
│   ├── Utilidades ← SessionFactory
│   └── App ← Menú console
│
├── Documentación (7 archivos)
│   ├── GUIA_RAPIDA.md ← Empezar aquí
│   ├── README.md ← Entender qué hace
│   ├── CRITERIOS_CUBIERTOS.md ← Validar criterios
│   ├── EJEMPLOS_PRACTICOS.md ← Ver casos de uso
│   ├── SCRIPT_PRUEBAS.md ← Probar todo
│   ├── ARQUITECTURA.md ← Entender diseño
│   └── INDICE.md ← Navegar documentación
│
└── Config
    ├── pom.xml ← Dependencias Maven
    └── hibernate.cfg.xml ← Configuración ORM
```

---

## 🎯 Objetivos cubiertos

| Objetivo | ✅ Demostrado | Cómo |
|----------|:-:|--------|
| ORM instalado | ✅ | pom.xml + hibernate.cfg.xml |
| 3+ Entidades | ✅ | 4 entidades (Miembro, Actividad, Reserva, Entrenador) |
| Relación 1:N | ✅ | Miembro → Reserva |
| Relación N:M | ✅ | Actividad ↔ Entrenador |
| CRUD Create | ✅ | save() en DAOs |
| CRUD Read | ✅ | findById(), findAll() |
| CRUD Update | ✅ | update() en DAOs |
| CRUD Delete | ✅ | delete() en DAOs |
| HQL Avanzado | ✅ | 4 queries complejas |
| Transacciones | ✅ | reservarClase(), cancelarReserva() |
| ACID + Rollback | ✅ | try-catch-finally con control TX |

---

## 🏁 Listo para empezar

```
1. Abrir terminal
2. cd c:\Users\jbuna\Documents\DAM2\AD\GimnasioApp
3. mvn clean compile
4. mvn exec:java -Dexec.mainClass="com.gimnasio.app.GimnasioApp"
5. ¡Menú aparece!
6. Prueba las funcionalidades
```

**Tiempo total:** 20 minutos para demostración completa

---

## 📞 Guía rápida de navegación

**Para empezar:** Este archivo (5 minutos)
**Para entender:** [README.md](README.md) (10 minutos)
**Para ejecutar:** [GUIA_RAPIDA.md](GUIA_RAPIDA.md) (15 minutos)
**Para probar:** [SCRIPT_PRUEBAS.md](SCRIPT_PRUEBAS.md) (20 minutos)
**Para todo:** [INDICE.md](INDICE.md) (navegar)

---

**¡Ahora sí, a compilar y ejecutar! 🚀**
