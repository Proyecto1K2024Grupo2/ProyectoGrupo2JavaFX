package com.iesochoa.grupo2.proyectogrupo2javafx.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase DBConnection - Maneja la conexión a la base de datos MySQL.
 * Utiliza el patrón Singleton para asegurar que solo haya una instancia de la conexión.
 *
 * @version 01-2025
 */

public class DBConnection {
    // URL de conexión a la base de datos MySQL
    private static final String URL = "jdbc:mysql://proyecto.ch4kyy668r3o.us-east-1.rds.amazonaws.com:3306/VETERINARIO";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "juREmaRA?8k6!";

    private static Connection connection;

    /**
     * Constructor privado para evitar instancias directas.
     * Se usa el patrón Singleton, por lo que no es necesario instanciar esta clase.
     */
    private DBConnection() {
    }

    /**
     * Método estático para obtener la instancia única de la conexión.
     * Si la conexión no existe, la crea; de lo contrario, la reutiliza.
     *
     * @return La instancia de la conexión a la base de datos.
     */
    public static Connection getConnection() {
        if (connection == null) {
            // Bloqueo sincronizado para evitar concurrencia
            synchronized (DBConnection.class) {
                if (connection == null) {
                    try {
                        // Establecer la conexión
                        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }

    /**
     * Método estático para cerrar la conexión a la base de datos.
     * Se asegura de que la conexión se cierre correctamente y se liberen los recursos.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
