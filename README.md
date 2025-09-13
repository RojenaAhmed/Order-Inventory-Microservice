# Order & Inventory Microservices (Spring Boot 3.3.4, Java 21)

Two microservices:
- **inventory-service**: manages stock & availability
- **order-service**: creates orders after checking inventory and decreasing stock

## Tech
- Spring Boot 3.3.4 (Java 21)
- Spring Data JPA + H2
- REST + Springdoc OpenAPI (Swagger UI)
- JUnit + Mockito (unit tests) + MockWebServer (integration)
- Docker & Docker Compose

## Quick Start (Docker)
```bash
docker compose up --build
```
- Inventory Service: http://localhost:8081/swagger-ui.html
- Order Service: http://localhost:8080/swagger-ui.html

## Local Run (IDE or Maven)
Open terminal in each service and run:
```bash
./mvnw spring-boot:run
```

## API Examples
- Inventory:
  - `GET /api/inventory/{sku}` → `{ "sku":"PEN-001", "available": true, "stock": 42 }`
  - `POST /api/inventory/decrease` body: `{ "sku":"PEN-001", "quantity": 2 }`
- Order:
  - `POST /api/orders` body: `{ "sku":"PEN-001", "quantity": 2 }`
  - `GET /api/orders/{id}`
  - `GET /api/orders`

