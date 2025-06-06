package com.iesochoa.grupo2.proyectogrupo2javafx.db;

import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Sala;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase SalaDAO - Maneja las operaciones de base de datos para la tabla Sala.
 * Implementa métodos CRUD (Crear, Leer, Actualizar, Eliminar) para gestionar las salas.
 * Implementa el patrón Singleton para gestionar la conexión a la base de datos.
 *
 * @version 01-2025
 * @author Martín Davia Mora
 */

public class SalaDAO {
    // Instancia única de SalaDAO
    private static SalaDAO instance;
    // Conexión a la base de datos
    private Connection connection;

    //Sentencia SQL para crear la si no existe
    public static final String CREATE_TABLE_PERSONA = """
               CREATE IF NOT EXIST TABLE sala(
                      nombre VARCHAR(32),
                      cod_centro INT UNSIGNED,
                      CONSTRAINT fk_cod_centro FOREIGN KEY (cod_Centro) REFERENCES centro(cod)
                          ON UPDATE NO ACTION
                          ON DELETE no action,
                      PRIMARY KEY(nombre)
                  );
            """;

    // Consultas SQL predefinidas para operaciones CRUD
    private static final String INSERT_SALA_QUERY = "INSERT INTO sala (nombre, cod_centro) VALUES (?, ?)";
    private static final String SELECT_ALL_SALAS_QUERY = "SELECT * FROM sala";
    private static final String SELECT_BY_NOMBRE_QUERY = "SELECT * FROM sala WHERE nombre = ?";
    private static final String UPDATE_SALA_QUERY = "UPDATE sala SET cod_centro = ? WHERE nombre = ?";
    private static final String DELETE_SALA_QUERY = "DELETE FROM sala WHERE nombre = ?";
    private static final String TOTAL_SALAS_QUERY = "SELECT COUNT(*) FROM sala";


    /**
     * Constructor privado para evitar instanciación externa.
     * Obtiene la conexión a la base de datos desde DBConnection.
     */
    public SalaDAO() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Método estático para obtener la única instancia de PersonaDAO.
     *
     * @return instancia única de SalaDAO.
     */
    public static synchronized SalaDAO getInstance() {
        if (instance == null) {
            instance = new SalaDAO();
        }
        return instance;
    }

    /**
     * Inserta una nueva sala en la base de datos.
     *
     * @param sala Objeto Sala a insertar.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void insertSala(Sala sala) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SALA_QUERY)) {
            statement.setString(1, sala.getNombre());       // Nombre de la sala
            statement.setInt(2, sala.getCodCentro());      // Código del centro asociado
            statement.executeUpdate();
        }
    }


    /**
     * Obtiene todas las salas almacenadas en la base de datos.
     *
     * @return Lista de objetos Sala.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public List<Sala> getAllSalas() throws SQLException {
        List<Sala> salas = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SALAS_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                salas.add(resultSetToSala(resultSet));  // Convertir ResultSet a objeto Sala
            }
        }
        return salas;
    }


    /**
     * Obtiene una sala a partir de su nombre.
     *
     * @param nombre Nombre de la sala.
     * @return Objeto Sala si se encuentra, null si no.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public Sala getSalaByNombre(String nombre) throws SQLException {
        Sala sala = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_NOMBRE_QUERY)) {
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                sala = resultSetToSala(resultSet);
            }
        }
        return sala;
    }


    /**
     * Actualiza los datos de una sala en la base de datos.
     *
     * @param sala Objeto Sala con los datos actualizados.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void updateSala(Sala sala) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SALA_QUERY)) {
            statement.setInt(1, sala.getCodCentro());  // Código del centro asociado
            statement.setString(2, sala.getNombre());  // Nombre de la sala
            statement.executeUpdate();
        }
    }


    /**
     * Elimina una sala de la base de datos por su nombre.
     *
     * @param nombre Nombre de la sala a eliminar.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void deleteSalaByNombre(String nombre) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SALA_QUERY)) {
            statement.setString(1, nombre);
            statement.executeUpdate();
        }
    }


    /**
     * Convierte un ResultSet en un objeto Sala.
     *
     * @param resultSet Resultado de la consulta SQL.
     * @return Objeto Sala con los datos del ResultSet.
     * @throws SQLException Si ocurre un error en la conversión.
     */
    private Sala resultSetToSala(ResultSet resultSet) throws SQLException {
        return new Sala(
                resultSet.getString("nombre"),       // Nombre de la sala
                resultSet.getInt("cod_centro")       // Código del centro asociado
        );
    }


    /**
     * Obtiene el total de salas almacenadas en la base de datos.
     *
     * @return Número total de salas.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public int totalSalas() throws SQLException {
        int total = 0;
        try (PreparedStatement statement = connection.prepareStatement(TOTAL_SALAS_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getInt(1);  // Obtiene el conteo total de salas
            }
        }
        return total;
    }
}
