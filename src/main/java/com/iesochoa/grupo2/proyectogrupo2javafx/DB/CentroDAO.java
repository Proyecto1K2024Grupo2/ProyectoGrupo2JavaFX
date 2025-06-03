package com.iesochoa.grupo2.proyectogrupo2javafx.DB;

import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Centro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase CentroDAO - Maneja las operaciones de base de datos para la tabla Centro.
 * Implementa métodos CRUD (Crear, Leer, Actualizar, Eliminar) para gestionar los centros.
 * Implementa el patrón Singleton para gestionar la conexión a la base de datos.
 *
 * @version 01-2025
 * @author Martín Davia Mora
 */

public class CentroDAO {
    // Instancia única de CentroDAO
    private static CentroDAO instance;
    // Conexión a la base de datos
    private Connection connection;

    //Sentencia SQL para crear la si no existe
    public static final String CREATE_TABLE_PERSONA = """
                CREATE TABLE IF NOT EXIST centro(
                    cod INT UNSIGNED AUTO_INCREMENT,
                    nombre VARCHAR(64),
                    direccion VARCHAR(64),
                    cp VARCHAR(10),
                    PRIMARY KEY (cod)
                );
            """;
    // Consultas SQL predefinidas para operaciones CRUD
    private static final String INSERT_CENTRO_QUERY = "INSERT INTO centro (nombre, direccion, cp) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_CENTROS_QUERY = "SELECT * FROM centro";
    private static final String SELECT_BY_COD_QUERY = "SELECT * FROM centro WHERE cod = ?";
    private static final String UPDATE_CENTRO_QUERY = "UPDATE centro SET nombre = ?, direccion = ?, cp = ? WHERE cod = ?";
    private static final String DELETE_CENTRO_QUERY = "DELETE FROM centro WHERE cod = ?";
    private static final String TOTAL_CENTROS_QUERY = "SELECT COUNT(*) FROM centro";

    /**
     * Constructor privado para evitar instanciación externa.
     * Obtiene la conexión a la base de datos desde DBConnection.
     */
    public CentroDAO() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Método estático para obtener la única instancia de PersonaDAO.
     *
     * @return instancia única de CentroDAO.
     */
    public static synchronized CentroDAO getInstance() {
        if (instance == null) {
            instance = new CentroDAO();
        }
        return instance;
    }

    /**
     * Inserta un nuevo centro en la base de datos.
     *
     * @param centro Objeto Centro a insertar.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void insertCentro(Centro centro) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_CENTRO_QUERY)) {
            statement.setString(1, centro.getNombreCentro());        // Asigna el nombre del centro
            statement.setString(2, centro.getDireccion());     // Asigna la dirección del centro
            statement.setString(3, centro.getCp());            // Asigna el código postal del centro
            statement.executeUpdate();
        }
    }


    /**
     * Obtiene todos los centros almacenados en la base de datos.
     *
     * @return Lista de objetos Centro.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public List<Centro> getAllCentros() throws SQLException {
        List<Centro> centros = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_CENTROS_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                centros.add(resultSetToCentro(resultSet));  // Utiliza resultSetToCentro
            }
        }
        return centros;
    }


    /**
     * Obtiene un centro a partir de su código.
     *
     * @param cod Código único del centro.
     * @return Objeto Centro si se encuentra, null si no.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public Centro getCentroByCod(int cod) throws SQLException {
        Centro centro = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_COD_QUERY)) {
            statement.setInt(1, cod);  // Usamos el código del centro (cod)
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                centro = resultSetToCentro(resultSet);  // Convertimos el ResultSet a un objeto Centro
            }
        }
        return centro;
    }


    /**
     * Actualiza los datos de un centro en la base de datos.
     *
     * @param centro Objeto Centro con los datos actualizados.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void updateCentro(Centro centro) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_CENTRO_QUERY)) {
            statement.setString(1, centro.getNombreCentro());
            statement.setString(2, centro.getDireccion());
            statement.setString(3, centro.getCp());
            statement.setInt(4, centro.getCod());  // Usamos el 'cod' del centro
            statement.executeUpdate();
        }
    }


    /**
     * Elimina un centro de la base de datos por su código.
     *
     * @param cod Código único del centro a eliminar.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void deleteCentroByCod(int cod) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_CENTRO_QUERY)) {
            statement.setInt(1, cod);  // Usamos el 'cod' del centro
            statement.executeUpdate();
        }
    }


    /**
     * Convierte un ResultSet en un objeto Centro.
     *
     * @param resultSet Resultado de la consulta SQL.
     * @return Objeto Centro con los datos del ResultSet.
     * @throws SQLException Si ocurre un error en la conversión.
     */
    private Centro resultSetToCentro(ResultSet resultSet) throws SQLException {
        return new Centro(
                resultSet.getInt("cod"),
                resultSet.getString("nombre"),    // Obtiene el nombre del centro
                resultSet.getString("direccion"), // Obtiene la dirección
                resultSet.getString("cp")         // Obtiene el código postal
        );
    }


    /**
     * Obtiene el total de centros almacenados en la base de datos.
     *
     * @return Número total de centros.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public int totalCentros() throws SQLException {
        int total = 0;
        try (PreparedStatement statement = connection.prepareStatement(TOTAL_CENTROS_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getInt(1);  // Obtiene el conteo total
            }
        }
        return total;
    }
}

