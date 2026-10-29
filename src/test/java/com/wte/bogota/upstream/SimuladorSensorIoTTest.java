package com.wte.bogota.upstream;

import com.wte.bogota.upstream.model.SensorPayload;
import com.wte.bogota.upstream.service.SimuladorSensorIoT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimuladorSensorIoTTest {

    private SimuladorSensorIoT simulador;

    @BeforeEach
    void setUp() {
        simulador = new SimuladorSensorIoT();
    }

    @Test
    @DisplayName("Debe generar una lectura con rangos válidos de llenado y peso")
    void testGenerarLecturaRangosValidos() {
        String containerId = "BOG-TEST-001";
        SensorPayload payload = simulador.generarLectura(containerId);

        assertNotNull(payload);
        assertEquals(containerId, payload.containerId());
        assertTrue(payload.fillLevelPercentage() >= 0.0 && payload.fillLevelPercentage() <= 100.0,
                "El porcentaje de llenado debe estar entre 0% y 100%");
        assertTrue(payload.densityKgM3() >= SimuladorSensorIoT.DENSIDAD_MIN_BOGOTA &&
                        payload.densityKgM3() <= SimuladorSensorIoT.DENSIDAD_MAX_BOGOTA,
                "La densidad debe estar dentro del rango característico de Bogotá");
        assertTrue(payload.weightKg() >= 0.0, "El peso no puede ser negativo");
        assertNotNull(payload.timestamp(), "El timestamp debe estar presente");
    }

    @Test
    @DisplayName("Debe generar un JSON válido no vacío que contenga las propiedades esperadas")
    void testGenerarPayloadJson() {
        String containerId = "BOG-TEST-002";
        String json = simulador.generarPayloadJson(containerId);

        assertNotNull(json);
        assertFalse(json.isBlank());
        assertTrue(json.contains("container_id"));
        assertTrue(json.contains("BOG-TEST-002"));
        assertTrue(json.contains("fill_level_percentage"));
        assertTrue(json.contains("weight_kg"));
        assertTrue(json.contains("timestamp"));
    }
}
