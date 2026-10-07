# Microservicio Administrador de RabbitMQ - Pedidos360

Microservicio desarrollado en **Java 17** con **Spring Boot** para la administración dinámica de recursos de mensajería (Colas, Exchanges, Bindings y DLQs) en el clúster de **RabbitMQ**, utilizando la abstracción de alto nivel `AmqpAdmin` de Spring AMQP y validación estricta de parámetros mediante Bean Validation.

---

## 🎯 Cumplimiento de la Rúbrica de Evaluación

Este microservicio aborda específicamente el 30% de la evaluación:
1. **Endpoints REST bien definidos (13%)**: Rutas semánticas `POST /api/admin/queues`, `DELETE /api/admin/queues/{name}`, `POST /api/admin/exchanges`, `POST /api/admin/bindings`.
2. **Lógica encapsulada en servicio dedicado (10%)**: Toda la interacción con el broker reside en `RabbitAdminService`, manteniendo los controladores desacoplados.
3. **Validación de parámetros con DTOs (7%)**: Prevención de nombres vacíos o configuraciones inválidas mediante `@Valid`, `@NotBlank` y `@Pattern`, con respuestas de error HTTP 400 claras vía `GlobalExceptionHandler`.

---

## 🚀 Requisitos Previos

- **Java**: JDK 17 o 21
- **Maven**: 3.8+ o Maven Wrapper (`mvnw.cmd`)
- **RabbitMQ**: Broker en ejecución (puerto 5672)
- **Puerto del servicio**: `8084`

---

## ⚙️ Configuración del Entorno (`application.properties`)

```properties
server.port=8084
spring.application.name=admin-api

# Conexión a RabbitMQ Broker
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=pedidos360_user
spring.rabbitmq.password=pedidos360_pass
```

---

## 🛠️ Cómo Compilar y Ejecutar

1. **Compilar el proyecto**:
   ```bash
   .\mvnw.cmd clean package -DskipTests
   ```

2. **Ejecutar localmente**:
   ```bash
   .\mvnw.cmd spring-boot:run
   ```

---

## 📌 Documentación de Endpoints REST

### 1. Gestión de Colas (Queues)

#### a) Crear Cola
- **Método:** `POST`
- **Ruta:** `/api/admin/queues`
- **Body JSON:**
  ```json
  {
    "nombre": "cola.nueva.prueba",
    "durable": true,
    "exclusive": false,
    "autoDelete": false,
    "deadLetterExchange": "pedidos.dlx.exchange",
    "deadLetterRoutingKey": "cola.nueva.dlq"
  }
  ```
- **Respuesta (`201 CREATED`):**
  ```json
  {
    "status": "SUCCESS",
    "mensaje": "Cola 'cola.nueva.prueba' creada o verificada exitosamente en RabbitMQ",
    "recurso": "cola.nueva.prueba",
    "tipoOperacion": "CREATE_QUEUE"
  }
  ```

#### b) Eliminar Cola
- **Método:** `DELETE`
- **Ruta:** `/api/admin/queues/{name}`

#### c) Consultar Estadísticas de Cola
- **Método:** `GET`
- **Ruta:** `/api/admin/queues/{name}`
- **Respuesta (`200 OK`):**
  ```json
  {
    "nombre": "pedidos.stock.queue",
    "totalMensajes": 0,
    "totalConsumidores": 1,
    "propiedades": { ... }
  }
  ```

#### d) Purgar Mensajes de una Cola
- **Método:** `POST`
- **Ruta:** `/api/admin/queues/{name}/purge`

---

### 2. Gestión de Exchanges

#### a) Crear Exchange
- **Método:** `POST`
- **Ruta:** `/api/admin/exchanges`
- **Body JSON:**
  ```json
  {
    "nombre": "pedidos.custom.topic",
    "tipo": "topic",
    "durable": true,
    "autoDelete": false
  }
  ```

#### b) Eliminar Exchange
- **Método:** `DELETE`
- **Ruta:** `/api/admin/exchanges/{name}`

---

### 3. Gestión de Bindings (Enlaces)

#### a) Vincular Cola a Exchange
- **Método:** `POST`
- **Ruta:** `/api/admin/bindings`
- **Body JSON:**
  ```json
  {
    "cola": "cola.nueva.prueba",
    "exchange": "pedidos.custom.topic",
    "routingKey": "pedidos.*.evento"
  }
  ```

#### b) Desvincular Cola de Exchange
- **Método:** `DELETE`
- **Ruta:** `/api/admin/bindings`
- **Body JSON:**
  ```json
  {
    "cola": "cola.nueva.prueba",
    "exchange": "pedidos.custom.topic",
    "routingKey": "pedidos.*.evento"
  }
  ```
