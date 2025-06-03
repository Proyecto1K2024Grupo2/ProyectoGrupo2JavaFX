package com.iesochoa.grupo2.proyectogrupo2javafx.DB;

import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Empleado;

import java.sql.SQLException;

/**
 * Clase EmpleadoDAO - Almacena los métodos abstractos que se usan en sus subclases.
 * {@link RecepcionistaDAO},{@link CuidadorDAO}, {@link VeterinarioDAO},{@link CirujanoDAO}
 *
 * @version 01-2025
 * @author Juan Carlos Garcia
 */
public abstract class EmpleadoDAO {

    public abstract void insertEmpleado(Empleado empleado) throws SQLException;

    public abstract boolean updateEmpleado(Empleado empleado) throws SQLException;

    public abstract void deleteEmpleadoByDni(String dni) throws SQLException;
}
