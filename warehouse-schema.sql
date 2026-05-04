-- Script de creación de base de datos para la aplicación de gestión de almacén
CREATE DATABASE IF NOT EXISTS warehouse_db;
USE warehouse_db;

CREATE TABLE IF NOT EXISTS roles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS suppliers (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  contact VARCHAR(255),
  email VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  quantity INT,
  cost_price DECIMAL(12,2),
  sale_price DECIMAL(12,2),
  status VARCHAR(50),
  supplier_id BIGINT,
  category_id BIGINT,
  image_url VARCHAR(500),
  FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
  FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS inventory_entries (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  date DATE,
  quantity INT,
  note VARCHAR(500),
  product_id BIGINT,
  supplier_id BIGINT,
  FOREIGN KEY (product_id) REFERENCES products(id),
  FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);

CREATE TABLE IF NOT EXISTS inventory_exits (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  date DATE,
  quantity INT,
  destination VARCHAR(255),
  product_id BIGINT,
  FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Usuario administrador inicial
INSERT INTO roles (name) VALUES ('ROLE_ADMIN')
ON DUPLICATE KEY UPDATE name = name;

-- La contraseña se guarda encriptada desde la aplicación Spring Boot.
