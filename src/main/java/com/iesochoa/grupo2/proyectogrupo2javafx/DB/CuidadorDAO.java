package com.iesochoa.grupo2.proyectogrupo2javafx.DB;

import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Cuidador;
import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase CuidadorDAO - Maneja las operaciones de base de datos para la tabla Cuidador.
 * Implementa métodos CRUD (Crear, Leer, Actualizar, Eliminar) para gestionar los cuidadores.
 * Implementa el patrón Singleton para asegurar una única instancia.
 * Extiende de EmpleadoDAO {@link EmpleadoDAO}
 *
 * @version 01-2025
 * @author Juan Carlos Garcia
 */
public class CuidadorDAO extends EmpleadoDAO{
    // Instancia única de CuidadorDAO
    private static CuidadorDAO instance;

    // Conexión a la base de datos
    private Connection connection;

    //Sentencia SQL para crear la si no existe

    // Consultas SQL predefinidas para operaciones CRUD
    private static final String INSERT_QUERY = "INSERT INTO cuidador (dni) VALUES (?, ?, ?, ?,?)";
    private static final String INSERT_QUERY_EMPLEADO = "INSERT INTO empleado(dni) VALUES (?)";
    private static final String SELECT_ALL_QUERY = "SELECT e.dni, e.nombre, e.telefono, e.numcuenta, e.Sueldo FROM empleado e JOIN cuidador cu ON cu.dni = e.dni";
    private static final String SELECT_BY_DNI_QUERY = "SELECT e.dni, e.nombre, e.telefono, e.numcuenta, e.Sueldo FROM cuidador cu JOIN empleado e ON cu.dni = e.dni WHERE cu.dni = ?";
    private static final String UPDATE_QUERY = "UPDATE cuidador SET dni = ? WHERE dni = ?";
    private static final String UPDATE_QUERY_EMPLEADO = "UPDATE empleado SET  nombre = ?, telefono = ? , numcuenta = ?, saldo = ?  WHERE dni = ?";
    private static final String DELETE_QUERY = "DELETE FROM cuidador WHERE dni = ?";
    private static final String DELETE_QUERY_EMPLEADO = "DELETE FROM empleado WHERE dni = ?";
    private static final String TOTAL_PERSONAS_QUERY = "SELECT COUNT(*) FROM cuidador";

    /**
     * Constructor privado para evitar instanciación externa.
     * Obtiene la conexión a la base de datos desde DBConnection.
     */
    public CuidadorDAO() {
        super();
        this.connection = DBConnection.getConnection();

    }

    /**
     * Método estático para obtener la única instancia de PersonaDAO.
     *
     * @return instancia única de CuidadorDAO.
     */
    public static synchronized CuidadorDAO getInstance() {
        if (instance == null) {
            instance = new CuidadorDAO();
        }
        return instance;
    }

    /**
     * Inserta un nuevo cuidador en la base de datos.
     *
     * @param empleado Objeto Cuidador a insertar.
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
     * Obtiene todos los cuidadores almacenados en la base de datos.
     *
     * @return Lista de objetos Cuidador.
     * @throws SQLException Si ocurre un error en la base de datos.
     */

    public List<Cuidador> getAllCuidadores() throws SQLException {
        List<Cuidador> personas = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                personas.add(resultSetToCuidador(resultSet));
            }
        }
        return personas;
    }

    /**
     * Obtiene un cuidador a partir de su DNI.
     *
     * @param dni Identificador único del cuidador.
     * @return Objeto Cuidador si se encuentra, null si no.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public Cuidador getCuidadorByDni(String dni) throws SQLException {
        Cuidador persona = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_DNI_QUERY)) {
            statement.setString(1, dni);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                persona = resultSetToCuidador(resultSet);
            }
        }
        if (persona == null) {
            System.err.println("Este empleado no está registrado como cuidador en la base de datos.");
        }
        return persona;
    }

    /**
     * Actualiza los datos de un cuidador en la base de datos.
     *
     * @param empleado Objeto Cuidador con los datos actualizados.
     * @return
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    @Override
    public boolean updateEmpleado(Empleado empleado) throws SQLException {
        boolean autocommit = true;

        autocommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY_EMPLEADO)) {


            statement.setString(1, empleado.getDniEmpleado());
            statement.setString(2, empleado.getNombreEmpleado());
            statement.setInt(3, empleado.getTelefono());
            statement.setString(4, empleado.getNumCuenta());
            statement.setDouble(5, empleado.getSueldo());

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
     * Elimina un cuidador de la base de datos por su DNI.
     *
     * @param dni Identificador único del cuidador a eliminar.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    @Override
    public void deleteEmpleadoByDni(String dni) throws SQLException {
        boolean autocommit = true;
        autocommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
             PreparedStatement statement2 = connection.prepareStatement(DELETE_QUERY_EMPLEADO)) {
            statement.setString(1, dni);
            statement.executeUpdate();


            statement2.setString(1, dni);
            statement2.executeUpdate();

            connection.commit();
            connection.setAutoCommit(autocommit);
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }

    }

    /**
     * Convierte un ResultSet en un objeto Cuidador.
     *
     * @param resultSet Resultado de la consulta SQL.
     * @return Objeto Cuidador con los datos del ResultSet.
     * @throws SQLException Si ocurre un error en la conversión.
     */
    private Cuidador resultSetToCuidador(ResultSet resultSet) throws SQLException {
        return new Cuidador(
                resultSet.getString("DNI"),
                resultSet.getString("Nombre"),
                resultSet.getInt("Telefono"),
                resultSet.getString("numCuenta"),
                resultSet.getInt("Sueldo"));
    }

    /**
     * Obtiene el total de cuidadores almacenados en la base de datos.
     *
     * @return Número total de cuidadores.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public int totalCuidador() throws SQLException {
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


