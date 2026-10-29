# Waste-to-Energy (WtE) Simulator - Bogotá D.C.

Sistema modular en Java para simular y evaluar la viabilidad de un modelo Waste-to-Energy (WtE) en la ciudad de Bogotá, conectando generación estocástica de residuos (IoT), optimización logística y análisis termoquímico.

## 🏛️ Arquitectura del Sistema

El proyecto está diseñado bajo principios de Clean Architecture y Modular Monolith dividido en tres componentes principales:

1. **Upstream (IoT & Simulación de Residuos)**
   - Simulación telemétrica de sensores en contenedores de Bogotá.
   - Generación estocástica de nivel de llenado y cálculo de peso a partir de densidades reales de RSU (180 - 240 kg/m³ según UAESP).
2. **Operativo (Logística & Ruteo)**
   - Optimización de rutas de recolección de contenedores críticos mediante Google OR-Tools (CVRP).
3. **Downstream (Termoquímica & Valorización Energética)**
   - Estimación del poder calorífico superior e inferior (HHV/LHV) de los residuos recogidos mediante la ecuación de Dulong.

## 🛠️ Tecnologías y Dependencias

- **Lenguaje:** Java 21+
- **Gestor de Construcción:** Apache Maven
- **Base de Datos:** PostgreSQL (Driver JDBC 42.7.4)
- **Serialización JSON:** Jackson Databind 2.18.0 (con módulo JSR-310 para fechas Java 8+)
- **Testing:** JUnit 5 (5.11.2)

## 🚀 Compilación y Ejecución

Compilar el proyecto y ejecutar las pruebas:
```bash
mvn clean test
```

Ejecutar la demostración de simulación:
```bash
mvn compile exec:java
```
