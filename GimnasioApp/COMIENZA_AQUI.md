# 🚀 QUICK START - GimnasioApp

## ⚡ Ejecutar en 10 segundos

### Windows (Más fácil)
```bash
# Opción 1: Double-click en run.bat
C:\Users\jbuna\Documents\DAM2\AD\GimnasioApp\run.bat

# Opción 2: PowerShell
cd C:\Users\jbuna\Documents\DAM2\AD\GimnasioApp
.\run.ps1

# Opción 3: CMD
cd C:\Users\jbuna\Documents\DAM2\AD\GimnasioApp
run.bat
```

---

## 📋 Menú Principal

```
1. Gestionar Miembros          (CRUD de miembros)
2. Gestionar Actividades       (CRUD de actividades)
3. Gestionar Reservas          (Transacciones ACID)
4. Consultas Avanzadas         (HQL queries)
5. Salir                        (Exit)
```

---

## 🎯 Ejemplos Rápidos

### Ejemplo 1: Registrar Miembro
```
1 (Gestionar Miembros)
1 (Registrar nuevo miembro)
Juan López
juan@gmail.com
300 (saldo inicial)
```

### Ejemplo 2: Hacer una Reserva
```
3 (Gestionar Reservas)
1 (Hacer reserva)
1 (ID miembro)
1 (ID actividad)
2026-01-25 10:00 (fecha/hora)
```

### Ejemplo 3: Consulta Avanzada
```
4 (Consultas Avanzadas)
1 (Miembros por actividad)
1 (ID actividad)
```

---

## 📊 Datos de Prueba (Precargados)

### Miembros
- Juan López (juan@gym.com) - Saldo: 200€
- María García (maria@gym.com) - Saldo: 250€
- Pedro Rodríguez (pedro@gym.com) - Saldo: 180€

### Actividades
- Pesas (Capacidad: 20, Precio: 15€)
- Cardio (Capacidad: 25, Precio: 12€)
- Yoga (Capacidad: 15, Precio: 10€)

### Entrenadores
- Carlos Martínez (Pesas + Cardio)
- Laura Pérez (Yoga + Cardio)

---

## 📚 Documentación

| Documento | Contenido |
|---|---|
| [RESUMEN_EJECUTIVO.md](RESUMEN_EJECUTIVO.md) | 📊 Visión general del proyecto |
| [EJECUCION_EXITOSA.md](EJECUCION_EXITOSA.md) | ✅ Validación y logs |
| [README.md](README.md) | 📖 Guía técnica completa |
| [GUIA_RAPIDA.md](GUIA_RAPIDA.md) | ⚡ Quick start |
| [CRITERIOS_CUBIERTOS.md](CRITERIOS_CUBIERTOS.md) | 🎯 Mapeo de requisitos |
| [EJEMPLOS_PRACTICOS.md](EJEMPLOS_PRACTICOS.md) | 💡 Casos de uso |
| [SCRIPT_PRUEBAS.md](SCRIPT_PRUEBAS.md) | 🧪 Tests |
| [ARQUITECTURA.md](ARQUITECTURA.md) | 🏗️ Diagramas |
| [PROYECTO_COMPLETADO.md](PROYECTO_COMPLETADO.md) | ✨ Resumen final |

---

## 🔧 Requisitos

- ✅ Java 25.0.1 LTS (o superior)
- ✅ Maven 3.8.1 (incluido en script)

---

## ✨ Características Principales

### ✅ ORM Hibernate
- 4 entidades con relaciones 1:N y N:M
- 5 tablas con foreign keys automáticas
- Hibernate 5.6.15 con JPA 2.2

### ✅ Transacciones ACID
- `reservarClase()` - Crea reserva con decremento de plazas
- `cancelarReserva()` - Cancela con reembolso automático

### ✅ Consultas HQL
- SELECT DISTINCT con JOIN
- COUNT con condiciones
- ORDER BY y aggregations

### ✅ Patrón DAO + Service
- 4 DAOs con CRUD completo
- 4 Services con lógica de negocio
- Manejo automático de transacciones

---

## 🎓 Criterios Evaluación

- ✅ 3.1 - Análisis de ORM
- ✅ 3.2 - Implementación de DAOs
- ✅ 3.3 - Consultas Avanzadas HQL
- ✅ 3.4 - Operaciones CRUD
- ✅ 3.5 - Transacciones ACID
- ✅ 3.6 - Gestión de Relaciones
- ✅ 3.7 - Pruebas Funcionales

---

## 🏃 Flujo de Trabajo

1. **Ejecutar** `run.bat` o `run.ps1`
2. **Ver** el menú principal
3. **Probar** cualquier opción (1-4)
4. **Consultar** los logs de Hibernate
5. **Revisar** la documentación según necesites

---

## 🐛 Solución de Problemas

### Maven no encontrado
```powershell
# Ejecutar install_maven.ps1 si existe, o:
$env:PATH += ";C:\Users\jbuna\AppData\Local\maven\apache-maven-3.8.1\bin"
```

### Java no encontrado
```powershell
# Descargar desde: https://www.oracle.com/java/technologies/downloads/
# Requiere: Java 25.0.1 LTS
```

### Error de compilación
```bash
mvn clean compile -X  # Ver logs detallados
```

---

## 📞 Información del Proyecto

- **Nombre**: GimnasioApp ORM
- **Ciclo**: DAM2 (Ciclo Formativo)
- **Módulo**: Acceso a Datos (AD)
- **Criterios**: 3.1-3.7
- **Fecha**: 25 de enero de 2026
- **Estado**: ✅ COMPLETADO Y FUNCIONAL

---

**¡Listo para usar!** 🚀

Ejecuta `run.bat` o `run.ps1` y comienza a explorar el sistema.
