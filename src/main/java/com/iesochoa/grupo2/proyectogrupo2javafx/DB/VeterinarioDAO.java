package com.iesochoa.grupo2.proyectogrupo2javafx.DB;

import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Empleado;
import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Veterinario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase VeterinarioDAO - Maneja las operaciones de base de datos para la tabla Veterinario.
 * Implementa métodos CRUD (Crear, Leer, Actualizar, Eliminar) para gestionar los veterinarios.
 * Implementa el patrón Singleton para asegurar una única instancia.
 * Extiende de EmpleadoDAO {@link EmpleadoDAO}
 *
 * @version 01-2025
 * @author Juan Carlos Garcia
 */
public class VeterinarioDAO extends EmpleadoDAO {
    // Instancia única de VeterinarioDAO
    private static VeterinarioDAO instance;

    // Conexión a la base de datos
    private Connection connection;

    //Sentencia SQL para crear la si no existe

    // Consultas SQL predefinidas para operaciones CRUD
    private static final String INSERT_QUERY = "INSERT INTO veterinario (dni) VALUES (?)";
    private static final String INSERT_QUERY_EMPLEADO = "INSERT INTO empleado(dni, nombre, telefono, numcuenta, sueldo) VALUES (?, ?, ?, ?,?)";
    private static final String SELECT_ALL_QUERY = "SELECT e.dni, e.nombre, e.telefono, e.numcuenta, e.Sueldo FROM veterinario v JOIN empleado e ON v.dni = e.dni";
    private static final String SELECT_BY_DNI_QUERY = "SELECT e.dni, e.nombre, e.telefono, e.numcuenta, e.Sueldo FROM veterinario v JOIN empleado e ON v.dni = e.dni WHERE e.dni = ?";
    private static final String UPDATE_QUERY = "UPDATE veterinario SET dni = ? WHERE dni = ?";
    private static final String UPDATE_QUERY_EMPLEADO = "UPDATE empleado SET nombre = ?, telefono = ? , numcuenta = ?, saldo=?  WHERE dni = ?";
    private static final String DELETE_QUERY = "DELETE FROM veterinario WHERE dni = ?";
    private static final String DELETE_QUERY_EMPLEADO = "DELETE FROM empleado WHERE dni = ?";
    private static final String TOTAL_VETERINARIOS_QUERY = "SELECT COUNT(*) FROM veterinario";

    /**
     * Constructor privado para evitar instanciación externa.
     * Obtiene la conexión a la base de datos desde DBConnection.
     */
    public VeterinarioDAO() {
        super();
        this.connection = DBConnection.getConnection();

    }

    /**
     * Método estático para obtener la única instancia de VeterinarioDAO.
     * @return instancia única de VeterinarioDAO.
     */
    public static synchronized VeterinarioDAO getInstance() {
        if (instance == null) {
            instance = new VeterinarioDAO();
        }
        return instance;
    }

    // Método para comprobar si el DNI está en la tabla Veterinario
    public boolean isDniInVeterinario(String dni) throws SQLException {
        String query = "SELECT COUNT(*) FROM veterinario WHERE dni = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dni);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Si el contador es mayor que 0, significa que el DNI está en Veterinario
            }
        }

        return false;
    }


    /**
     * Inserta un nuevo Veterinario en la base de datos.
     * @param empleado Objeto veterinario a insertar.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    @Override
    public void insertEmpleado(Empleado empleado) throws SQLException {
        boolean autocommit = true;

        autocommit = connection.getAutoCommit();
        connection.setAutoCommit(false);

        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY_EMPLEADO)) {
            statement.setString(1, empleado.getDniEmpleado());
            statement.setString(2, empleado.getNombreEmpleado());
            statement.setInt(3, empleado.getTelefono());
            statement.setString(4, empleado.getNumCuenta());
            statement.setDouble(5, empleado.getSueldo());
            statement.executeUpdate();

            PreparedStatement statement2 = connection.prepareStatement(INSERT_QUERY);
            statement2.setString(1, empleado.getDniEmpleado());
            statement2.executeUpdate();

            connection.commit();
            connection.setAutoCommit(autocommit);
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }

    }

    /**
     * Obtiene todos los veterinarios almacenados en la base de datos.
     * @return Lista de objetos Veterinario.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public List<Veterinario> getAllVeterinarios() throws SQLException {
        List<Veterinario> personas = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                personas.add(resultSetToVeterinario(resultSet));
            }
        }
        return personas;
    }

    /**
     * Obtiene un veterinario a partir de su DNI.
     * @param dni Identificador único del veterinario.
     * @return Objeto veterinario si se encuentra, null si no.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public Veterinario getVeterinarioByDni(String dni) throws SQLException {
        Veterinario persona = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_DNI_QUERY)) {
            statement.setString(1, dni);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                persona = resultSetToVeterinario(resultSet);
            }
        }
        if (persona == null) {
            System.err.println("Este empleado no está registrado como veterinario en la base de datos.");
        }

        return persona;
    }


    /**
     * Actualiza los datos de un veterinario en la base de datos.
     *
     * @param persona Objeto veterinario con los datos actualizados.
     * @return
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    @Override
    public boolean updateEmpleado(Empleado persona) throws SQLException {
        boolean autocommit = true;

        autocommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY_EMPLEADO)) {


            statement.setString(1, persona.getNombreEmpleado());
            statement.setInt(2, persona.getTelefono());
            statement.setString(3, persona.getNumCuenta());
            statement.setDouble(4,persona.getSueldo());
            statement.setString(5, persona.getDniEmpleado());
            statement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(autocommit);
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }


        return autocommit;
    }

    /**
     * Elimina un veterinario de la base de datos por su DNI.
     * @param dni Identificador único del veterinario a eliminar.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    @Override
    public void deleteEmpleadoByDni(String dni) throws SQLException {
        boolean autocommit = true;
        autocommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setString(1, dni);
            PreparedStatement statement2= connection.prepareStatement(DELETE_QUERY_EMPLEADO);
            statement2.setString(1, dni);

            statement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(autocommit);
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }

    }

    /**
     * Convierte un ResultSet en un objeto Veterinario.
     * @param resultSet Resultado de la consulta SQL.
     * @return Objeto Veterinario con los datos del ResultSet.
     * @throws SQLException Si ocurre un error en la conversión.
     */
    private Veterinario resultSetToVeterinario(ResultSet resultSet) throws SQLException {
        return new Veterinario(
                resultSet.getString("DNI"),
                resultSet.getString("Nombre"),
                resultSet.getInt("Telefono"),
                resultSet.getString("numCuenta"),
                resultSet.getInt("Sueldo"));
    }

    /**
     * Obtiene el total de veterinarios almacenados en la base de datos.
     * @return Número total de veterinarios.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public int totalVeterinarios() throws SQLException {
        int total = 0;
        try (PreparedStatement statement = connection.prepareStatement(TOTAL_VETERINARIOS_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getInt(1);
            }
        }
        return total;
    }
}
