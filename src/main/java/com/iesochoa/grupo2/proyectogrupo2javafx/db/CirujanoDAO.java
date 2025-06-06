package com.iesochoa.grupo2.proyectogrupo2javafx.db;

import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Cirujano;
import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase CirujanoDAO - Maneja las operaciones de base de datos para la tabla Cirujano.
 * Implementa métodos CRUD (Crear, Leer, Actualizar, Eliminar) para gestionar los cirujanos.
 * Implementa el patrón Singleton para asegurar una única instancia.
 * Extiende de EmpleadoDAO {@link EmpleadoDAO}
 *
 * @version 01-2025
 * @author Juan Carlos Garcia
 */
public class CirujanoDAO extends EmpleadoDAO {



    // Instancia única de CirujanoDAO
    private static CirujanoDAO instance;

    // Conexión a la base de datos
    private Connection connection;

    //Sentencia SQL para crear la si no existe

    // Consultas SQL predefinidas para operaciones CRUD
    private static final String INSERT_QUERY = "INSERT INTO cirujano (dni) VALUES (?)";
    private static final String INSERT_QUERY_EMPLEADO = "INSERT INTO empleado(dni, nombre, telefono, numcuenta, Sueldo) VALUES (?, ?, ?, ?,?)";
    private static final String SELECT_ALL_QUERY = "SELECT e.dni, e.nombre, e.telefono, e.numcuenta, e.Sueldo FROM empleado e JOIN cirujano c ON c.dni = e.dni";
    private static final String SELECT_BY_DNI_QUERY = "SELECT e.dni, e.nombre, e.telefono, e.numcuenta, e.Sueldo FROM cirujano c JOIN empleado e ON c.dni = e.dni WHERE c.dni = ?";
    private static final String UPDATE_QUERY = "UPDATE cirujano SET dni = ?  WHERE dni = ?";
    private static final String UPDATE_QUERY_EMPLEADO = "UPDATE empleado SET nombre = ?, telefono = ? , numcuenta = ?, sueldo = ?  WHERE dni = ?";
    private static final String DELETE_QUERY = "DELETE FROM cirujano WHERE dni = ?";
    private static final String DELETE_QUERY_EMPLEADO = "DELETE FROM empleado WHERE dni = ?";
    private static final String TOTAL_PERSONAS_QUERY = "SELECT COUNT(*) FROM cirujano";

    /**
     * Constructor privado para evitar instanciación externa.
     * Obtiene la conexión a la base de datos desde DBConnection.
     */
    public CirujanoDAO() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Método estático para obtener la única instancia de PersonaDAO.
     *
     * @return instancia única de CirujanoDAO.
     */
    public static synchronized CirujanoDAO getInstance() {
        if (instance == null) {
            instance = new CirujanoDAO();
        }
        return instance;
    }

    // Método para comprobar si el DNI está en la tabla Veterinario
    public boolean isDniINCirujano(String dni) throws SQLException {
        String query = "SELECT COUNT(*) FROM cirujano WHERE dni = ?";

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
     * Inserta una nueva empleado en la base de datos.
     *
     * @param empleado Objeto Cirujano a insertar.
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
     * Obtiene todos los cirujanos almacenados en la base de datos.
     *
     * @return Lista de objetos cirujano.
     * @throws SQLException Si ocurre un error en la base de datos.
     */

    public List<Cirujano> getAllCirujanos() throws SQLException {
        List<Cirujano> personas = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                personas.add(resultSetToCirujano(resultSet));
            }
        }
        return personas;
    }

    /**
     * Obtiene un cirujano a partir de su DNI.
     *
     * @param dni Identificador único de cirujano.
     * @return Objeto Cirujano si se encuentra, null si no.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public Cirujano getCirujanoByDni(String dni) throws SQLException {
        Cirujano persona = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_DNI_QUERY)) {
            statement.setString(1, dni);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                persona = resultSetToCirujano(resultSet);
            }
        }
        if (persona == null) {
            System.err.println("Este empleado no está registrado como cirujano en la base de datos.");
        }
        return persona;
    }

    /**
     * Actualiza los datos de un cirujano en la base de datos.
     *
     * @param empleado Objeto Cirujano con los datos actualizados.
     * @return
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    @Override
    public boolean updateEmpleado(Empleado empleado) throws SQLException {
        boolean autocommit = true;

        autocommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY_EMPLEADO)) {


            statement.setString(1, empleado.getNombreEmpleado());
            statement.setInt(2, empleado.getTelefono());
            statement.setString(3, empleado.getNumCuenta());
            statement.setDouble(4, empleado.getSueldo());
            statement.setString(5, empleado.getDniEmpleado());
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
     * Elimina un cirujano de la base de datos por su DNI.
     *
     * @param dni Identificador único del cirujano a eliminar.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    @Override
    public void deleteEmpleadoByDni(String dni) throws SQLException {
        boolean autocommit = true;

        autocommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setString(1, dni);
            PreparedStatement statement2 = connection.prepareStatement(DELETE_QUERY_EMPLEADO);
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
     * Convierte un ResultSet en un objeto Cirujano.
     *
     * @param resultSet Resultado de la consulta SQL.
     * @return Objeto Persona con los datos del ResultSet.
     * @throws SQLException Si ocurre un error en la conversión.
     */
    private Cirujano resultSetToCirujano(ResultSet resultSet) throws SQLException {
        return new Cirujano(
                resultSet.getString("DNI"),
                resultSet.getString("Nombre"),
                resultSet.getInt("Telefono"),
                resultSet.getString("numCuenta"),
                resultSet.getInt("Sueldo"));
    }

    /**
     * Obtiene el total de cirujanos almacenados en la base de datos.
     *
     * @return Número total de cirujanos.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public int totalCirujanos() throws SQLException {
        int total = 0;
        try (PreparedStatement statement = connection.prepareStatement(TOTAL_PERSONAS_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getInt(1);
            }
        }
        return total;
    }
}