package com.iesochoa.grupo2.proyectogrupo2javafx.db;

import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Animal;
import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Cita;
import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Historial;
import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Tratamiento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase HistorialDAO - Maneja las operaciones de base de datos para la tabla Historial.
 * Implementa métodos CRUD (Crear, Leer, Actualizar, Eliminar) para gestionar el Historial.
 * Implementa el patrón Singleton para gestionar la conexión a la base de datos.
 *
 * @author Martín Davia Mora,
 * Rubén Expósito Vicente,
 * Juan Carlos Garcia,
 * Rauúl Qílez Ruiz
 *
 * @version 01-2025
 */
public class HistorialDAO {

    private static HistorialDAO instance;
    private Connection connection;

    public static final String CREAT_TABLE_HISTORIAL = """
            CREATE TABLE historial(
                id_cita INT UNSIGNED,
                id_animal INT UNSIGNED,
                id_tratamiento INT UNSIGNED,
                CONSTRAINT fk_idCita FOREIGN KEY (id_cita) REFERENCES cita(id)
                    ON UPDATE no action
                    ON DELETE no action,
                CONSTRAINT fk_idAnimal FOREIGN KEY (id_animal) REFERENCES animal(id)
                    ON UPDATE no action
                    ON DELETE no action,
                CONSTRAINT fk_idTratamiento FOREIGN KEY (id_tratamiento) REFERENCES tratamiento(id)
                    ON UPDATE no action
                    ON DELETE no action,
                PRIMARY KEY (id_cita, id_animal, id_tratamiento)
            );
            """;

    private static final String INSERT_HISTRORIAL_QUERY = "INSERT INTO historial (id_cita, id_animal, id_tratamiento) VALUES(?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "Select * From historial";
    private static final String SELECT_HISTORIAL_BY_ID_QUERY = "Select * From historial where id_cita=?";
    private static final String UPDATE_HISTORIAL_QUERY = "Update historial set id_animal=?, id_tratamiento=? where id_cita=?";
    private static final String DELETE_HISTORIAL_QUERY = "Delete from histrial where id_cita=?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) From historial";

    /**
     * Constructor privado para aplicar el patrón Singleton.
     */
    private HistorialDAO() { this.connection = DBConnection.getConnection(); }

    public static synchronized HistorialDAO getInstance() {
        if (instance == null) {
            instance = new HistorialDAO();
        }
        return instance;
    }

    public void insertHistorial(Historial historial) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_HISTRORIAL_QUERY)) {
            stmt.setString(1, String.valueOf(historial.getId_cita()));
            stmt.setString(2, String.valueOf(historial.getId_animal()));
            stmt.setString(3, String.valueOf(historial.getId_tratamiento()));
            stmt.executeUpdate();
        }
    }

    public List<Historial> getAllHistoriales() throws SQLException {
        List<Historial> historiales = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ALL_QUERY)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                historiales.add(resultSetToHistorial(rs));
            }
        }
        return historiales;
    }

    public Historial getHistorialById(int id) throws SQLException {
        Historial historial = null;
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_HISTORIAL_BY_ID_QUERY)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                historial = resultSetToHistorial(rs);
            }
        }
        return historial;
    }

    public void updateHistorial(Historial historial, int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_HISTORIAL_QUERY)) {
            stmt.setString(1, String.valueOf(historial.getId_cita()));
            stmt.setString(2, String.valueOf(historial.getId_animal()));
            stmt.setString(3, String.valueOf(historial.getId_tratamiento()));
            stmt.executeUpdate();
        }
    }

    public void deleteHistorial(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_HISTORIAL_QUERY)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Historial resultSetToHistorial(ResultSet resultSet) throws SQLException {
        int idCita = resultSet.getInt("id_cita");
        int idAnimal = resultSet.getInt("id_animal");
        int idTratamiento = resultSet.getInt("id_tratamiento");

        Cita cita = CitaDAO.getInstance().getCitaById(idCita);
        Animal animal = AnimalDAO.getInstance().getAnimalById(idAnimal);
        Tratamiento tratamiento = TratamientoDAO.getInstance().getTratamientoById(idTratamiento);

        return new Historial(idCita, idAnimal, idTratamiento, cita, animal, tratamiento);
    }

    public int totalHistorial() throws SQLException {
        int total = 0;
        try (PreparedStatement statement = connection.prepareStatement(COUNT_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getInt(1);  // Obtiene el conteo total de salas
            }
        }
        return total;
    }
}

