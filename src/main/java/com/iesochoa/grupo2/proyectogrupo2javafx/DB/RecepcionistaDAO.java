package com.iesochoa.grupo2.proyectogrupo2javafx.DB;

import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Empleado;
import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Recepcionista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase EmpleadoDAO - Maneja las operaciones de base de datos para la tabla Recepcionista.
 * Implementa métodos CRUD (Crear, Leer, Actualizar, Eliminar) para gestionar los Recepcionistas.
 * Implementa el patrón Singleton para asegurar una única instancia.
 * @version 01-2025
 * @author Rubén Expósito Vicente
 */
public class RecepcionistaDAO extends EmpleadoDAO{



    // Instancia única de PersonaDAO
    private static RecepcionistaDAO instance;

    // Conexión a la base de datos
    private Connection connection;

    //Sentencia SQL para crear la si no existe

    // Consultas SQL predefinidas para operaciones CRUD
    private static final String INSERT_QUERY = "INSERT INTO recepcionista (dni) VALUES (?)";
    private static final String INSERT_QUERY_EMPLEADO = "INSERT INTO empleado(dni, nombre, telefono, numcuenta, Sueldo) VALUES (?, ?, ?, ?,?)";
    private static final String SELECT_ALL_QUERY = "SELECT e.dni, e.nombre, e.telefono, e.numcuenta, e.Sueldo FROM recepcionista r JOIN empleado e ON r.dni = e.dni";
    private static final String SELECT_BY_DNI_QUERY = "SELECT e.dni, e.nombre, e.telefono, e.numcuenta, e.Sueldo FROM recepcionista r JOIN empleado e ON r.dni = e.dni WHERE e.dni = ?";
    private static final String UPDATE_QUERY = "UPDATE recepcionista SET dni = ? WHERE dni = ?";
    private static final String UPDATE_QUERY_EMPLEADO = "UPDATE empleado SET nombre = ?, telefono = ? , numcuenta = ?, saldo = ?  WHERE dni = ?";
    private static final String DELETE_QUERY = "DELETE FROM recepcionista WHERE dni = ?";
    private static final String DELETE_QUERY_EMPLEADO = "DELETE FROM empleado WHERE dni = ?";
    private static final String TOTAL_PERSONAS_QUERY = "SELECT COUNT(*) FROM recepcionista";

    /**
     * Constructor privado para evitar instanciación externa.
     * Obtiene la conexión a la base de datos desde DBConnection.
     */
    public RecepcionistaDAO() {
        super();
        this.connection =DBConnection.getConnection();
    }

    /**
     * Método estático para obtener la única instancia de RecepcionistaDAO.
     * @return instancia única de RecepcionistaDAO.
     */
    public static synchronized RecepcionistaDAO getInstance() {
        if (instance == null) {
            instance = new RecepcionistaDAO();
        }
        return instance;
    }

    /**
     * Inserta un nuevo recepcionista en la base de datos.
     * @param empleado Objeto Recepcionista a insertar.
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
     * Obtiene todos los recepcionistas almacenadas en la base de datos.
     * @return Lista de objetos Recepcionista.
     * @throws SQLException Si ocurre un error en la base de datos.
     */

    public List<Recepcionista> getAllRecepcionista() throws SQLException {
        List<Recepcionista> personas = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                personas.add(resultSetToRecepcionista(resultSet));
            }
        }
        return personas;
    }

    /**
     * Obtiene un recepcionista a partir de su DNI.
     * @param dni Identificador único del recepcionista.
     * @return Objeto Recepcionista si se encuentra, null si no.
     * @throws SQLException Si ocurre un error en la base de datos.
     */

    public Recepcionista getRecepcionistaByDni(String dni) throws SQLException {
        Recepcionista persona = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_DNI_QUERY)) {
            statement.setString(1, dni);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                persona = resultSetToRecepcionista(resultSet);
            }
        }
        if (persona == null) {
            System.err.println("Este empleado no está registrado como recepcionista en la base de datos.");
        }
        return persona;
    }

    /**
     * Actualiza los datos de un recepcionista en la base de datos.
     *
     * @param empleado Objeto Recepcionista con los datos actualizados.
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
            statement.setDouble(4,empleado.getSueldo());
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
     * Elimina un recepcionista de la base de datos por su DNI.
     * @param dni Identificador único del recepcionista a eliminar.
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
     * Convierte un ResultSet en un objeto Recepcionista.
     * @param resultSet Resultado de la consulta SQL.
     * @return Objeto Recepcionista con los datos del ResultSet.
     * @throws SQLException Si ocurre un error en la conversión.
     */
    private Recepcionista resultSetToRecepcionista(ResultSet resultSet) throws SQLException {
        return new Recepcionista(
                resultSet.getString("DNI"),
                resultSet.getString("Nombre"),
                resultSet.getInt("Telefono"),
                resultSet.getString("numCuenta"),
                resultSet.getInt("Sueldo"));
    }

    /**
     * Obtiene el total de recepcionistas almacenados en la base de datos.
     * @return Número total de recepcionistas.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public int totalRecepcionista() throws SQLException {
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
