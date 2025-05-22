-- DROP DATABASE IF EXISTS TabacApp; 
CREATE DATABASE IF NOT EXISTS TabacApp;

USE TabacApp;

CREATE TABLE proveedores (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    marca VARCHAR(50),
    tipo VARCHAR(50),
    precio DECIMAL(6,2),
    stock INT,
    fecha_alta DATE,
    id_proveedor INT,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor)
);

CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    edad INT NOT NULL,
    email VARCHAR(100),
    telefono VARCHAR(20)
);

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contraseña VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    id_cliente INT,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);

CREATE TABLE ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT,
    id_producto INT NOT NULL,
    fecha DATE NOT NULL,
    cantidad INT NOT NULL,
    total DECIMAL(6,2) NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE SET NULL,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

INSERT INTO proveedores (nombre, telefono, email) VALUES
('Tabacos España S.A.', '912345678', 'contacto@tabacosesp.com'),
('VapeWorld Distribution', '933456789', 'info@vapeworld.com'),
('Accesorios Estanco SL', '911112233', 'ventas@accesoriosestanco.com');

INSERT INTO productos (nombre, marca, tipo, precio, stock, fecha_alta, id_proveedor) VALUES
('Marlboro Gold', 'Marlboro', 'Cigarrillo', 5.00, 50, '2025-05-01', 1),
('Lucky Strike Red', 'Lucky Strike', 'Cigarrillo', 4.80, 40, '2025-05-02', 1),
('Habano Cohiba', 'Cohiba', 'Puro', 12.00, 20, '2025-05-02', 1),
('Tabaco Liar Drum Azul', 'Drum', 'Tabaco de liar', 6.50, 25, '2025-05-03', 1),
('Vaper Desechable Ice Mango', 'ElfBar', 'Vaper', 8.90, 30, '2025-05-01', 2),
('Recambio Vaper Blueberry', 'VapoHit', 'Vaper', 4.20, 35, '2025-05-03', 2),
('Papel Smoking Azul', 'Smoking', 'Accesorio', 1.20, 100, '2025-05-03', 3),
('Mechero Clipper', 'Clipper', 'Accesorio', 1.00, 75, '2025-05-04', 3);

INSERT INTO clientes (nombre, edad, email, telefono) VALUES
('Carlos Pérez', 30, 'carlos.perez@mail.com', '600123456'),
('Lucía Gómez', 24, 'lucia.gomez@mail.com', '611223344'),
('Pedro Sánchez', 40, 'pedro.sanchez@mail.com', '622334455');

-- Usuario administrador
INSERT INTO usuarios (nombre_usuario, contraseña, rol, id_cliente) VALUES
('admin', 'admin123', 'admin', NULL);

-- Usuarios clientes
INSERT INTO usuarios (nombre_usuario, contraseña, rol, id_cliente) VALUES
('carlos30', 'clavecarlos', 'cliente', 1),
('lucia24', 'clavelucia', 'cliente', 2),
('pedro40', 'clavepedro', 'cliente', 3);

INSERT INTO ventas (id_cliente, id_producto, fecha, cantidad, total) VALUES
(1, 1, '2025-05-10', 2, 10.00),
(1, 4, '2025-05-10', 1, 6.50),
(2, 5, '2025-05-11', 1, 8.90),
(3, 3, '2025-05-12', 2, 24.00),
(2, 7, '2025-05-12', 3, 3.60),
(1, 8, '2025-05-13', 1, 1.00);
