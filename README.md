# Aplicación de Gestión de Almacén

Aplicación web de gestión de almacén desarrollada con Java Spring Boot y MySQL.

## Características

- Dashboard con métricas en tiempo real
- Módulo de productos con búsqueda, filtros por categoría, paginación y acciones de editar/eliminar
- Módulo de entradas y salidas de inventario
- Módulo de proveedores
- Módulo de reportes y configuración
- Sistema de autenticación con rol de Administrador

## Requisitos

- Java 21
- Maven
- MySQL

## Configuración de base de datos

1. Crea la base de datos MySQL usando el script:

```sql
SOURCE warehouse-schema.sql;
```

2. Usa los siguientes datos de conexión en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/warehouse_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=Jose141006@
```

## Ejecutar la aplicación

```bash
cd c:\Users\USUARIO\Desktop\web
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`.

## Credenciales iniciales

- Usuario: `admin`
- Contraseña: `admin123`

## Notas

- El sistema crea automáticamente un usuario administrador inicial al arrancar.
- El diseño de la interfaz está implementado con Thymeleaf y estilos personalizados para replicar un dashboard atractivo.
