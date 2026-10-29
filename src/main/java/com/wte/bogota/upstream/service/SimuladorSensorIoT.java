package com.wte.bogota.upstream.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wte.bogota.upstream.model.SensorPayload;

import java.time.Instant;
import java.util.Random;

/**
 * Simulador de sensores IoT instalados en contenedores de residuos urbanos.
 * Genera telemetría estocástica considerando densidades reales de residuos en Bogotá.
 */
public class SimuladorSensorIoT {

    // Capacidad volumétrica estándar de contenedores de carga lateral en Bogotá (en m³)
    public static final double DEFAULT_CONTAINER_VOLUME_M3 = 2.4;

    // Rango de densidad típica de Residuos Sólidos Urbanos (RSU) no compactados en Bogotá (kg/m³)
    // Según estudios de caracterización de la UAESP (180 - 240 kg/m³)
    public static final double DENSIDAD_MIN_BOGOTA = 180.0;
    public static final double DENSIDAD_MAX_BOGOTA = 240.0;

    private final Random random;
    private final ObjectMapper objectMapper;

    public SimuladorSensorIoT() {
        this.random = new Random();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Genera un objeto de lectura estocástica para un contenedor determinado.
     *
     * @param containerId Identificador único del contenedor (ej. "BOG-USAQUEN-001")
     * @return SensorPayload con los datos simulados
     */
    public SensorPayload generarLectura(String containerId) {
        return generarLectura(containerId, DEFAULT_CONTAINER_VOLUME_M3, DENSIDAD_MIN_BOGOTA, DENSIDAD_MAX_BOGOTA);
    }

    /**
     * Genera una lectura con parámetros específicos de volumen y rango de densidad.
     *
     * @param containerId       Identificador del contenedor
     * @param volumenTotalM3    Volumen total del contenedor en m³
     * @param densidadMin       Densidad mínima esperada en kg/m³
     * @param densidadMax       Densidad máxima esperada en kg/m³
     * @return SensorPayload generado
     */
    public SensorPayload generarLectura(String containerId, double volumenTotalM3, double densidadMin, double densidadMax) {
        // Nivel de llenado aleatorio estocástico entre 0.0% y 100.0%
        double fillPercentage = Math.round((random.nextDouble() * 100.0) * 100.0) / 100.0;

        // Densidad estocástica dentro del rango real de Bogotá
        double density = densidadMin + (random.nextDouble() * (densidadMax - densidadMin));
        density = Math.round(density * 100.0) / 100.0;

        // Cálculo físico del peso: Volumen ocupado (m3) * Densidad (kg/m3)
        double volumeOccupied = volumenTotalM3 * (fillPercentage / 100.0);
        double weightKg = Math.round((volumeOccupied * density) * 100.0) / 100.0;

        return new SensorPayload(
                containerId,
                fillPercentage,
                weightKg,
                density,
                volumenTotalM3,
                Instant.now()
        );
    }

    /**
     * Genera un payload en formato JSON simulando la telemetría del sensor IoT.
     *
     * @param containerId Identificador único del contenedor
     * @return String con el JSON resultante
     * @throws RuntimeException si ocurre un error en la serialización JSON
     */
    public String generarPayloadJson(String containerId) {
        SensorPayload payload = generarLectura(containerId);
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al serializar el payload del sensor a JSON", e);
        }
    }
}
