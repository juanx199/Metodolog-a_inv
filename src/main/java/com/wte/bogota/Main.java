package com.wte.bogota;

import com.wte.bogota.upstream.service.SimuladorSensorIoT;
import com.wte.bogota.shared.database.DatabaseConnection;

/**
 * Clase principal para verificar el arranque de la infraestructura base WtE Bogotá.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("  SISTEMA DE SIMULACIÓN Y VIABILIDAD WtE - BOGOTÁ D.C.   ");
        System.out.println("==========================================================\n");

        // 1. Verificación del Singleton de Base de Datos
        System.out.println("[Infraestructura] Verificando DatabaseConnection Singleton...");
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        DatabaseConnection db2 = DatabaseConnection.getInstance();
        System.out.println("¿Instancia única verificada?: " + (db1 == db2));

        // 2. Verificación del Módulo 1 (Upstream - IoT Simulator)
        System.out.println("\n[Upstream IoT] Generando payload estocástico de prueba...");
        SimuladorSensorIoT simulador = new SimuladorSensorIoT();

        String[] contenedoresEjemplo = {
            "BOG-USAQUEN-042",
            "BOG-CHAPINERO-108",
            "BOG-SUBA-215"
        };

        for (String id : contenedoresEjemplo) {
            String jsonPayload = simulador.generarPayloadJson(id);
            System.out.println("\nPayload generado para " + id + ":");
            System.out.println(jsonPayload);
        }

        System.out.println("\n==========================================================");
        System.out.println("  FASE 1 COMPLETADA CON ÉXITO: Base lista para continuar ");
        System.out.println("==========================================================");
    }
}
