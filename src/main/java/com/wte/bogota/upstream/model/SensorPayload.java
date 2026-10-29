package com.wte.bogota.upstream.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/**
 * Representa la lectura telemétrica de un sensor IoT instalado en un contenedor de basura.
 */
public record SensorPayload(
        @JsonProperty("container_id")
        String containerId,

        @JsonProperty("fill_level_percentage")
        double fillLevelPercentage,

        @JsonProperty("weight_kg")
        double weightKg,

        @JsonProperty("density_kg_m3")
        double densityKgM3,

        @JsonProperty("volume_capacity_m3")
        double volumeCapacityM3,

        @JsonProperty("timestamp")
        @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
        Instant timestamp
) {}
