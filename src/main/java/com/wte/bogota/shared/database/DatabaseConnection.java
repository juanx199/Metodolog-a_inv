package com.wte.bogota.shared.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestor Singleton para la conexión a la base de datos PostgreSQL.
 * Proporciona una única instancia y gestiona el ciclo de vida de la conexión JDBC.
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private String url;
    private String user;
    private String password;

    private DatabaseConnection() {
        cargarConfiguracion();
    }

    /**
     * Obtiene la instancia única del Singleton (Thread-safe con sincronización).
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Carga los parámetros de conexión desde variables de entorno o archivo properties,
     * con valores por defecto para entorno local.
     */
    private void cargarConfiguracion() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            System.err.println("Advertencia: No se pudo leer database.properties, usando valores por defecto.");
        }

        // Permite sobreescritura mediante variables de entorno si están presentes
        this.url = System.getenv("DB_URL") != null 
                ? System.getenv("DB_URL") 
                : props.getProperty("db.url", "jdbc:postgresql://localhost:5432/wte_bogota");

        this.user = System.getenv("DB_USER") != null 
                ? System.getenv("DB_USER") 
                : props.getProperty("db.user", "postgres");

        this.password = System.getenv("DB_PASSWORD") != null 
                ? System.getenv("DB_PASSWORD") 
                : props.getProperty("db.password", "postgres");
    }

    /**
     * Retorna una conexión activa a PostgreSQL. Si la conexión está cerrada o es nula,
     * abre una nueva.
     *
     * @return Connection conexión JDBC activa.
     * @throws SQLException si ocurre un error al conectar.
     */
    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    /**
     * Cierra la conexión activa si está abierta.
     */
    public synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}
