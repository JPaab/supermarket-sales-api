![Repo](https://img.shields.io/badge/GitHub-JPaab%2Fsupermarket--sales--api-black)
![Stars](https://img.shields.io/github/stars/JPaab/supermarket-sales-api?style=flat)
![Last Commit](https://img.shields.io/github/last-commit/JPaab/supermarket-sales-api?style=flat)

![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-green)
![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen)
![Database](https://img.shields.io/badge/Database-MySQL-orange)

# 🛒 Supermercados API — Gestión de Ventas (Spring Boot)
**API REST** para gestionar ventas de una cadena de supermercados:

- ✅ CRUD de **Productos** y **Sucursales**
- ✅ Registro y anulación (borrado lógico) de **Ventas**
- ✅ Estadísticas: **ingresos totales**, **producto más vendido**, top 5, etc.
- ✅ **DTOs + Mappers** (no se exponen entidades directamente)
- ✅ Manejo de errores centralizado con **@RestControllerAdvice**
- ✅ Documentación automática con **Swagger (springdoc-openapi)**
- ✅ Seguridad con **JWT**: solo peticiones **GET** sin autentificación

---

## 📌 Tecnologías usadas
- Java 21 ♨️
- Spring Boot 🍃
  - Spring Web
  - Spring Validation
  - Spring Data JPA
  - Spring Security
- Hibernate / JPA 🧠
- MySQL 🐘
- Lombok 🫑
- JWT 🔐
- Swagger UI (springdoc-openapi) 📄
- Maven 🪶
- Postman 🚀

---

## 📁 Estructura del proyecto
- `controllers/` → Endpoints REST, validación y respuestas (ApiResponse).
- `services/` → Lógica de negocio (ventas, stock, auth, estadísticas).
- `repositories/` → Acceso a datos con JPA.
- `dtos/` → DTOs de entrada/salida + Mappers.
- `models/` → Entidades JPA + modelos base (`ApiResponse`).
- `configs/` → SecurityConfig, SwaggerConfig, ObjectMapperConfig, filtro JWT.
- `exceptions/` → Excepciones propias + handler global.
- `util/` → Utilidades JWT.

---

## ✅ Requisitos
- **Java 21**
- **Maven**
- **MySQL** corriendo (local o docker)

### Lombok (imprescindible)
1. IntelliJ: `Settings → Plugins` → instalar **Lombok**
2. IntelliJ: `Settings → Build, Execution, Deployment → Compiler → Annotation Processors`
   - Activar `Enable annotation processing`

---

## ⚙️ Configuración DB (MySQL)

Config en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/supermercados_db
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## ⚙️ ¿Como arrancarlo?

Para ejecturar el programa, confirma que los siguientes puntos estan correctos.

1. **Dependencias y plugins correctos**

   * Compruebalos en `pom.xml`.

2. **Arrancar APP**

   * Ejecuta `SupermercadosAPIApplication.java` en el proyecto de IntelliJ
   * O desde la terminal (raíz del proyecto)
```
mvn spring-boot:run
```
### La API queda disponible en:
- `http://localhost:8080`
---

## 🔐 Autenticación (JWT)

### Regla de acceso

- `GET /api/` → Acceso sin TOKEN

- `POST/PUT/DELETE` → Requieren autorizacion: Bearer <TOKEN>

- `Endpoints ADMIN` → Requieren rol ADMIN

### Endpoints de Auth

- `POST /api/auth/register` → Registra usuario y devuelve TOKEN

- `POST /api/auth/login` → Login y devuelve TOKEN

### Ejemplo body register/login:

```json
{
  "username": "user_demo",
  "password": "123456"
}
```

---

## 📄 Swagger (Documentación)

`Swagger UI:`
http://localhost:8080/swagger-ui/index.html

---

## 🚀 Probar con Postman

1. Abrir Postman
2. Click Import
3. Importar la colección:

`Supermercados_API.postman_collection.json`

4. Request Auth - Login
   Copiar el **Bearer TOKEN** y añadirlo a `Variables - Token`
   
   Con esto los request que requieren Auth ya recogen el TOKEN.

---

## 🧰 Endpoints principales

### 🧺 Productos

- Listar productos (GET público)

`GET /api/productos`

- Obtener por ID (GET público)

`GET /api/productos/{id}`

- Crear producto (POST requiere token)

`POST /api/productos`
```json
{
  "nombre": "Arroz 1kg",
  "precio": 2.50,
  "categoria": "Alimentos",
  "stock": 50
}
```

- Actualizar producto (PUT requiere token)

`PUT /api/productos/{id}`

```json
{
  "nombre": "Arroz 1KG (Editado)",
  "precio": 2.75,
  "categoria": "Alimentos",
  "stock": 50
}
```

- Borrado lógico del producto (DELETE requiere token)

`DELETE /api/productos/{id}`

---

### 🏪 Sucursales

- Listar sucursales (GET público)

`GET /api/sucursales`

- Crear sucursal (POST requiere token)

`POST /api/sucursales`

```json
{
  "nombre": "Sucursal Centro",
  "direccion": "Calle Falsa 123"
}
```
- Actualizar sucursal (PUT requiere token)

`PUT /api/sucursales/{id}`

```json
{
  "nombre": "Sucursal Calle Real (Editado)",
  "direccion": "Calle Real 123"
}
```

- Eliminar sucursal (DELETE requiere token)

`DELETE /api/sucursales/{id}`

---

### 💰 Ventas

- Registrar nueva venta (POST requiere token)

`POST /api/ventas`

```json
{
  "sucursalId": 1,
  "detalle": [
    { "productoId": 10, "cantidad": 2 },
    { "productoId": 5, "cantidad": 1 }
  ]
}
```
- Buscar ventas (GET público)

`GET /api/ventas?sucursalId=1&fecha=2026-02-05`

Ejemplos:

`/api/ventas`

`/api/ventas?sucursalId=1`

`/api/ventas?fecha=2025-06-01`

`/api/ventas?sucursalId=1&fecha=2025-06-01`

- Anular venta (DELETE requiere token)

`DELETE /api/ventas/{id}`

---

### 📊 Estadísticas (GET público)

- Producto más vendido

`GET /api/estadisticas/producto-mas-vendido`

- Ingresos totales

`GET /api/estadisticas/ingresos-totales`

`GET /api/estadisticas/ingresos-totales?sucursalId=1`

- Top 5 productos

`GET /api/estadisticas/productos/top-5`

- Estadísticas por producto

`GET /api/estadisticas/productos/{productoId}`

- Estadísticas por sucursal

`GET /api/estadisticas/sucursales/{sucursalId}`

---

## 🧪 Testing

Incluye:

- 1 test unitario

- 1 test de integración con rollback (@Transactional) para no modificar la DB.

---

## 🧟 Troubleshooting

### ❌ MySQL no conecta (Communications link failure)

- MySQL apagado o puerto incorrecto

`url/usuario/password mal configurados en application.properties`

### ❌ Lombok no funciona

- Instalar plugin Lombok

`Activar annotation processing ✅`

### ❌ 401 / 403 en Postman

- Ejecutar Auth - Login primero y agregar el TOKEN a `Variables - Token`

`Confirmar header: Authorization: Bearer {{token}}`
