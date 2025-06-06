package com.iesochoa.grupo2.proyectogrupo2javafx.db;

import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Tratamiento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase TratamientoDAO - Maneja las operaciones de base de datos para la tabla Tratamiento.
 * Implementa métodos CRUD (Crear, Leer, Actualizar, Eliminar) para gestionar los Tratamientos.
 * Implementa el patrón Singleton para gestionar la conexión a la base de datos.
 *
 * @author Martín Davia Mora,
 * Rubén Expósito Vicente,
 * Juan Carlos Garcia,
 * Rauúl Qílez Ruiz
 *
 * @version 01-2025
 */
public class TratamientoDAO {

    // Instancia única de SalaDAO
    private static TratamientoDAO instance;
    // Conexión a la base de datos
    private Connection connection;

    //Sentencia SQL para crear la si no existe
    public static final String CREATE_TABLE_TRATAMIENTO = """
               CREATE IF NOT EXIST TABLE tratamiento(
                      id INT UNSIGNED AUTO_INCREMENT,
                          tratamiento TEXT,
                          medicamento VARCHAR(64),
                          posologia VARCHAR(64),
                          fechaCuidador DATE,
                          horaCuidador TIME,
                          fechaVeterinario DATE,
                          horaVeterinario TIME,
                          fechaCirujano DATE,
                          horaCirujano TIME,
                          dni_veterinario VARCHAR(9),
                          dni_cirujano VARCHAR(9),
                          dni_cuidador VARCHAR(9),
                          PRIMARY KEY (id),
                          CONSTRAINT fk_dniveterinariotr FOREIGN KEY (dni_veterinario) REFERENCES veterinario(dni)
                              ON UPDATE NO ACTION
                              ON DELETE NO ACTION,
                          CONSTRAINT fk_dnicirujanotr FOREIGN KEY (dni_cirujano) REFERENCES cirujano(dni)
                              ON UPDATE NO ACTION
                              ON DELETE NO ACTION,
                          CONSTRAINT fk_dnicuidadortr FOREIGN KEY (dni_cuidador) REFERENCES cuidador(dni)
                              ON UPDATE NO ACTION
                              ON DELETE NO ACTION
                  );
            """;

    // Consultas SQL predefinidas para operaciones CRUD
    private static final String INSERT_TRATAMIENTO_QUERY = "INSERT INTO tratamiento (tratamiento, medicamento," +
            " posologia, fechaCuidador, horaCuidador, fechaVeterinario, horaVeterinario, fechaCirujano," +
            " horaCirujano, dni_veterinario, dni_cirujano, dni_cuidador) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_QUERY = "SELECT * FROM tratamiento";
    private static final String SELECT_TRATAMIENTO_BY_ID_QUERY = "SELECT * FROM tratamiento WHERE id = ?";
    private static final String UPDATE_TRATAMIENTO_QUERY = "UPDATE tratamiento SET tratamiento = ?, medicamento = ?," +
            " posologia = ?, fechaCuidador = ?, horaCuidador = ?, fechaVeterinario = ?, horaVeterinario = ?," +
            " fechaCirujano = ?, horaCirujano = ?, dni_veterinario = ?, dni_cirujano = ?, dni_cuidador = ? WHERE id = ?";

    private static final String DELETE_TRATAMIENTO_QUERY = "DELETE FROM tratamiento WHERE id = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) FROM tratamiento";


    /**
     * Constructor privado para evitar instanciación externa.
     * Obtiene la conexión a la base de datos desde DBConnection.
     */
    public TratamientoDAO() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Método estático para obtener la única instancia de PersonaDAO.
     *
     * @return instancia única de TratamientoDAO.
     */
    public static synchronized TratamientoDAO getInstance() {
        if (instance == null) {
            instance = new TratamientoDAO();
        }
        return instance;
    }

    /**
     * Inserta un nuevo tratamiento en la base de datos.
     *
     * @param tratamiento Objeto Tratamiento a insertar
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void insertTratamiento(Tratamiento tratamiento) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_TRATAMIENTO_QUERY)) {
            stmt.setString(1, tratamiento.getTratamiento());
            stmt.setString(2, tratamiento.getMedicamento());
            stmt.setString(3, tratamiento.getPosologia());
            stmt.setDate(4, Date.valueOf(tratamiento.getFechaCuidador()));
            stmt.setTime(5, Time.valueOf(tratamiento.getHoraCuidador()));
            stmt.setDate(6, Date.valueOf(tratamiento.getFechaVeterinario()));
            stmt.setTime(7, Time.valueOf(tratamiento.getHoraVeterinario()));
            stmt.setDate(8, Date.valueOf(tratamiento.getFechaCirujano()));
            stmt.setTime(9, Time.valueOf(tratamiento.getHoraCirujano()));
            stmt.setString(10, tratamiento.getDni_veterinario());
            stmt.setString(11, tratamiento.getDni_cirujano());
            stmt.setString(12, tratamiento.getDni_cuidador());
            stmt.executeUpdate();
        }
    }


    /**
     * Obtiene todos los tratamientos almacenados en la base de datos.
     *
     * @return Lista de tratamientos.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public List<Tratamiento> getAllTratamientos() throws SQLException {
        List<Tratamiento> tratamientos = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ALL_QUERY)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tratamientos.add(resultSetToTratamiento(rs));
            }
        }
        return tratamientos;
    }


    /**
     * Obtiene un tratamiento por su ID.
     *
     * @param id ID del tratamiento.
     * @return Objeto Tratamiento si se encuentra, null si no.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public Tratamiento getTratamientoById(int id) throws SQLException {
        Tratamiento tratamiento = null;
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_TRATAMIENTO_BY_ID_QUERY)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tratamiento = resultSetToTratamiento(rs);
            }
        }
        return tratamiento;
    }


    /**
     * Actualiza los datos de un tratamiento en la base de datos.
     *
     * @param tratamiento Objeto Tratamiento con los datos actualizados.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void updateTratamiento(Tratamiento tratamiento) {
        try {
            // Verificar si el tratamiento existe
            Tratamiento existingTratamiento = getTratamientoById(tratamiento.getId());
            if (existingTratamiento == null) {
                System.out.println("Tratamiento con ID " + tratamiento.getId() + " no encontrado.");
                return;
            }

            // Proceder a actualizar el tratamiento en la base de datos
            try (PreparedStatement stmt = connection.prepareStatement(UPDATE_TRATAMIENTO_QUERY)) {
                stmt.setString(1, tratamiento.getTratamiento());
                stmt.setString(2, tratamiento.getMedicamento());
                stmt.setString(3, tratamiento.getPosologia());
                stmt.setDate(4, Date.valueOf(tratamiento.getFechaCuidador()));
                stmt.setTime(5, Time.valueOf(tratamiento.getHoraCuidador()));
                stmt.setDate(6, Date.valueOf(tratamiento.getFechaVeterinario()));
                stmt.setTime(7, Time.valueOf(tratamiento.getHoraVeterinario()));
                stmt.setDate(8, Date.valueOf(tratamiento.getFechaCirujano()));
                stmt.setTime(9, Time.valueOf(tratamiento.getHoraCirujano()));
                stmt.setString(10, tratamiento.getDni_veterinario());
                stmt.setString(11, tratamiento.getDni_cirujano());
                stmt.setString(12, tratamiento.getDni_cuidador());
                stmt.setInt(13, tratamiento.getId());

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Tratamiento actualizado correctamente.");
                } else {
                    System.out.println("No se pudo actualizar el tratamiento.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el tratamiento: " + e.getMessage());
        }
    }


    /**
     * Elimina un tratamiento de la base de datos por su ID.
     *
     * @param id ID del tratamiento a eliminar.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void deleteTratamientoById(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_TRATAMIENTO_QUERY)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    /**
     * Convierte un ResultSet en un objeto Tratamiento.
     *
     * @param resultSet Resultado de la consulta SQL.
     * @return Objeto Tratamiento con los datos del ResultSet.
     * @throws SQLException Si ocurre un error en la conversión.
     */
    private Tratamiento resultSetToTratamiento(ResultSet resultSet) throws SQLException {
        Date fechaCui = resultSet.getDate("fechaCuidador");
        Time horaCui = resultSet.getTime("horaCuidador");
        Date fechaVet = resultSet.getDate("fechaVeterinario");
        Time horaVet = resultSet.getTime("horaVeterinario");
        Date fechaCir = resultSet.getDate("fechaCirujano");
        Time horaCir = resultSet.getTime("horaCirujano");

        return new Tratamiento(
                resultSet.getInt("id"),
                resultSet.getString("tratamiento"),
                resultSet.getString("medicamento"),
                resultSet.getString("posologia"),
                fechaCui != null ? fechaCui.toLocalDate() : null,
                horaCui != null ? horaCui.toLocalTime() : null,
                resultSet.getString("dni_cuidador"),
                fechaVet != null ? fechaVet.toLocalDate() : null,
                horaVet != null ? horaVet.toLocalTime() : null,
                resultSet.getString("dni_veterinario"),
                fechaCir != null ? fechaCir.toLocalDate() : null,
                horaCir != null ? horaCir.toLocalTime() : null,
                resultSet.getString("dni_cirujano")
        );
    }




    /**
     * Obtiene el total de tratamientos almacenados en la base de datos.
     *
     * @return Número total de tratamientos.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public int totalTratamientos() throws SQLException {
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
