-- ========================================
-- SCRIPT PARA CREAR LA BASE DE DATOS MYSQL
-- GIMNASIO APP
-- ========================================

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS gimnasio_db;
USE gimnasio_db;

-- ========================================
-- TABLA: MIEMBROS
-- ========================================
CREATE TABLE miembros (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    fecha_inscripcion DATETIME NOT NULL,
    saldo_cuenta DOUBLE,
    activo BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: ENTRENADORES
-- ========================================
CREATE TABLE entrenadores (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    especialidad VARCHAR(50),
    email VARCHAR(100),
    telefono VARCHAR(20),
    experiencia_anos INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: ACTIVIDADES
-- ========================================
CREATE TABLE actividades (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    capacidad_maxima INT NOT NULL,
    plazas_disponibles INT NOT NULL,
    precio_clase DOUBLE,
    hora_inicio TIME,
    duracion_minutos INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: RELACIÓN ACTIVIDAD-ENTRENADOR (N:M)
-- ========================================
CREATE TABLE actividad_entrenador (
    actividad_id BIGINT NOT NULL,
    entrenador_id BIGINT NOT NULL,
    PRIMARY KEY (actividad_id, entrenador_id),
    FOREIGN KEY (actividad_id) REFERENCES actividades(id) ON DELETE CASCADE,
    FOREIGN KEY (entrenador_id) REFERENCES entrenadores(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: RESERVAS
-- ========================================
CREATE TABLE reservas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    miembro_id BIGINT NOT NULL,
    actividad_id BIGINT NOT NULL,
    fecha_reserva DATETIME NOT NULL,
    fecha_clase DATETIME,
    estado VARCHAR(20),
    monto_pagado DOUBLE,
    FOREIGN KEY (miembro_id) REFERENCES miembros(id) ON DELETE CASCADE,
    FOREIGN KEY (actividad_id) REFERENCES actividades(id) ON DELETE CASCADE,
    INDEX idx_miembro (miembro_id),
    INDEX idx_actividad (actividad_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- INSERTAR DATOS DE PRUEBA
-- ========================================

-- Insertar Miembros
INSERT INTO miembros (nombre, email, telefono, fecha_inscripcion, saldo_cuenta, activo) VALUES
('Juan García', 'juan.garcia@email.com', '654321789', NOW(), 150.50, TRUE),
('María López', 'maria.lopez@email.com', '687654321', NOW(), 200.00, TRUE),
('Carlos Martínez', 'carlos.martinez@email.com', '691234567', NOW(), 100.75, TRUE),
('Ana Rodríguez', 'ana.rodriguez@email.com', '698765432', DATE_SUB(NOW(), INTERVAL 30 DAY), 250.00, TRUE),
('Pedro Sánchez', 'pedro.sanchez@email.com', '685432109', DATE_SUB(NOW(), INTERVAL 15 DAY), 0.00, FALSE);

-- Insertar Entrenadores
INSERT INTO entrenadores (nombre, especialidad, email, telefono, experiencia_anos) VALUES
('Luis Fernández', 'Fitness', 'luis.fernandez@gym.com', '612345678', 10),
('Elena Moreno', 'Yoga', 'elena.moreno@gym.com', '612345679', 8),
('Antonio Díaz', 'Pilates', 'antonio.diaz@gym.com', '612345680', 6),
('Sofía García', 'Zumba', 'sofia.garcia@gym.com', '612345681', 5);

-- Insertar Actividades
INSERT INTO actividades (nombre, descripcion, capacidad_maxima, plazas_disponibles, precio_clase, hora_inicio, duracion_minutos) VALUES
('Clase de Yoga', 'Yoga relajante para todos los niveles', 20, 15, 15.00, '09:00:00', 60),
('Spinning', 'Clases de spinning de alta intensidad', 30, 20, 20.00, '18:00:00', 45),
('Pilates', 'Fortalecimiento del core', 15, 10, 18.00, '10:30:00', 55),
('Zumba', 'Baile y diversión', 25, 22, 12.00, '19:00:00', 50),
('Entrenamiento Funcional', 'Ejercicio con pesas e intensidad', 18, 5, 25.00, '17:00:00', 60);

-- Asociar Entrenadores con Actividades (Relación N:M)
INSERT INTO actividad_entrenador (actividad_id, entrenador_id) VALUES
(1, 2),  -- Yoga - Elena
(2, 1),  -- Spinning - Luis
(3, 3),  -- Pilates - Antonio
(4, 4),  -- Zumba - Sofía
(5, 1);  -- Entrenamiento Funcional - Luis

-- Insertar Reservas
INSERT INTO reservas (miembro_id, actividad_id, fecha_reserva, fecha_clase, estado, monto_pagado) VALUES
(1, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 5 DAY), 'CONFIRMADA', 15.00),
(1, 2, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 3 DAY), 'CONFIRMADA', 20.00),
(2, 3, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY), 'CONFIRMADA', 18.00),
(2, 4, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY), 'ASISTIO', 12.00),
(3, 1, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 10 DAY), 'CONFIRMADA', 15.00),
(3, 5, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 'CONFIRMADA', 25.00),
(4, 2, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_ADD(NOW(), INTERVAL 15 DAY), 'CANCELADA', 0.00);

-- ========================================
-- VERIFICAR DATOS INSERTADOS
-- ========================================
SELECT COUNT(*) AS total_miembros FROM miembros;
SELECT COUNT(*) AS total_entrenadores FROM entrenadores;
SELECT COUNT(*) AS total_actividades FROM actividades;
SELECT COUNT(*) AS total_reservas FROM reservas;

-- Ver estructura completa
SELECT * FROM miembros;
SELECT * FROM entrenadores;
SELECT * FROM actividades;
SELECT * FROM reservas;
