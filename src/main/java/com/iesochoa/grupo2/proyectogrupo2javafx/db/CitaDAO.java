package com.iesochoa.grupo2.proyectogrupo2javafx.db;

import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Cita;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase CitaDAO - Maneja las operaciones de base de datos para la tabla Cita.
 * Implementa métodos CRUD (Crear, Leer, Actualizar, Eliminar) para gestionar las citas.
 * Implementa el patrón Singleton para gestionar la conexión a la base de datos.
 *
 * @version 01-2025
 * @author Rubén Expósito Vicente
 */
public class CitaDAO {
    // Instancia única de CitaDAO
    private static CitaDAO instance;
    // Conexión a la base de datos
    private Connection connection;

    //Sentencia SQL para crear la si no existe
    public static final String CREATE_TABLE_PERSONA = """
                CREATE TABLE IF NOT EXIST cita(
                    id INT UNSIGNED AUTO_INCREMENT,
                    nombre_sala VARCHAR(32),
                    fecha DATE,
                    hora TIME,
                    dniRecepcionista VARCHAR(9),
                    CONSTRAINT fk_nombreSala FOREIGN KEY (nombre_sala) REFERENCES sala(nombre)
                        ON UPDATE no action
                        ON DELETE no action,
                    CONSTRAINT fk_dnirecepcionista_cita FOREIGN KEY (dniRecepcionista) REFERENCES recepcionista(dni)
                        ON UPDATE no action
                        ON DELETE no action,
                    PRIMARY KEY(id)
                );
            """;
    // Consultas SQL predefinidas para operaciones CRUD
    private static final String INSERT_QUERY = "INSERT INTO cita (nombre_sala, fecha, hora, dniRecepcionista) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM cita";
    private static final String SELECT_BY_ID = "SELECT * FROM cita WHERE id = ?";
    private static final String UPDATE_CITA_QUERY = "UPDATE cita SET nombre_sala = ?, fecha = ?, hora = ?, dniRecepcionista = ? WHERE id = ?";
    private static final String DELETE_CITA_QUERY = "DELETE FROM cita WHERE id = ?";
    private static final String TOTAL_CITAS_QUERY = "SELECT COUNT(*) FROM cita";


    /**
     * Constructor privado para evitar instanciación externa.
     * Obtiene la conexión a la base de datos desde DBConnection.
     */
    public CitaDAO() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Método estático para obtener la única instancia de CitaDAO.
     * @return instancia única de CitaDAO.
     */
    public static synchronized CitaDAO getInstance() {
        if (instance == null) {
            instance = new CitaDAO();
        }
        return instance;
    }

    /**
     * Inserta una nueva persona en la base de datos.
     *
     * @param cita Objeto Persona a insertar.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void insertCita(Cita cita) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, cita.getNombreSala());  // Ajustado para usar el método correcto
            statement.setDate(2, Date.valueOf(cita.getFecha()));  // Asegura que se pase un java.sql.Date
            statement.setTime(3, Time.valueOf(cita.getHora()));  // Asegura que se pase un java.sql.Time
            statement.setString(4, cita.getDniRecepcionista());  // Ajustado para usar el método correcto
            statement.executeUpdate();
        }
    }

    /**
     * Obtiene todas las citas almacenadas en la base de datos.
     *
     * @return Lista de objetos Cita.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public List<Cita> getAllCitas() throws SQLException {
        List<Cita> citas = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                citas.add(resultSetToCita(resultSet));
            }
        }
        return citas;
    }


    /**
     * Obtiene una lista de citas a partir de una fecha específica.
     *
     * @param id Id de las citas a buscar.
     * @return Lista de objetos Cita si se encuentran, lista vacía si no hay citas.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public Cita getCitaById(int id) throws SQLException {
        Cita cita = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                cita = resultSetToCita(resultSet);
            }
        }
        return cita;
    }

    /**
     * Actualiza los datos de una cita en la base de datos.
     *
     * @param cita Objeto Cita con los datos actualizados.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void updateCita(Cita cita) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_CITA_QUERY)) {
            statement.setString(1, cita.getNombreSala());
            statement.setDate(2, Date.valueOf(cita.getFecha()));
            statement.setTime(3, Time.valueOf(cita.getHora()));
            statement.setString(4, cita.getDniRecepcionista());
            statement.setInt(5, cita.getId());
            statement.executeUpdate();
        }
    }


    /**
     * Elimina una cita de la base de datos por su ID.
     *
     * @param id Identificador único de la cita a eliminar.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void deleteCitaById(int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_CITA_QUERY)) {
            statement.setInt(1, id);  // Usamos el ID para eliminar la cita
            statement.executeUpdate();
        }
    }


    /**
     * Convierte un ResultSet en un objeto Cita.
     *
     * @param resultSet Resultado de la consulta SQL.
     * @return Objeto Cita con los datos del ResultSet.
     * @throws SQLException Si ocurre un error en la conversión.
     */
    private Cita resultSetToCita(ResultSet resultSet) throws SQLException {
        return new Cita(
                resultSet.getInt("id"),
                resultSet.getString("nombre_sala"),
                resultSet.getDate("fecha").toLocalDate(),  // Convierte java.sql.Date a LocalDate
                resultSet.getTime("hora").toLocalTime(),  // Convierte java.sql.Time a LocalTime
                resultSet.getString("dniRecepcionista")
        );
    }


    /**
     * Obtiene el total de citas almacenadas en la base de datos.
     *
     * @return Número total de citas.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public int totalCitas() throws SQLException {
        int total = 0;
        try (PreparedStatement statement = connection.prepareStatement(TOTAL_CITAS_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getInt(1);  // Obtiene el conteo total
            }
        }
        return total;
    }
}

