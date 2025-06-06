package com.iesochoa.grupo2.proyectogrupo2javafx.Model;

/**
 * Clase Empleado que sirve para guardar información de los empleados de la empresa.
 * {@link com.iesochoa.grupo2.proyectogrupo2javafx.db.EmpleadoDAO}
 * {@link com.iesochoa.grupo2.proyectogrupo2javafx.Controller.EmpleadosController}
 */
public abstract class Empleado {

    protected String dniEmpleado;
    protected String nombreEmpleado;
    protected int telefono;
    protected String numCuenta;
    protected double sueldo;

    /**
     * Construcor sin parámetros de la clase Empleado.
     */
    public Empleado() {

    }

    /**
     * Constructor para crear un empleado con sus datos necesarios para inscribirlos en la empresa.
     * @param dni DNI del empleado.
     * @param nombre Nombre completo del empleado.
     * @param telefono Teléfono del empleado.
     * @param numCuenta Número de Cuenta del empleado.
     * @param sueldo Sueldo del empleado.
     */
    public Empleado(String dni, String nombre, int telefono, String numCuenta, double sueldo) {
        this.dniEmpleado = dni;
        this.nombreEmpleado = nombre;
        this.telefono = telefono;
        this.numCuenta = numCuenta;
        this.sueldo = sueldo;
    }


    //Getters y Setters
    public String getDniEmpleado() {
        return dniEmpleado;
    }

    public void setDniEmpleado(String dniEmpleado) {
        this.dniEmpleado = dniEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    /**
     * Convierte los datos del empleado a XML.
     * @return Devuelve un String con los datos de Empleado a XML.
     */
    public String empleadoToXML() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("      <dni>").append(dniEmpleado).append("</dni>")
                .append("      <nombre>").append(nombreEmpleado).append("</nombre>")
                .append("      <telefono>").append(telefono).append("</telefono>")
                .append("      <numCuenta>").append(numCuenta).append("</numCuenta>")
                .append("      <sueldo>").append(sueldo).append("</sueldo>");
        return xmlBuilder.toString();
    }

    /**
     * Convierte los datos del empleado a JSON.
     * @return Devuelve un String con los datos de Empleado a JSON.
     */
    public String empleadoToJSON() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{")
                .append("\"dni\": \"").append(dniEmpleado).append("\", ")
                .append("\"nombre\": \"").append(nombreEmpleado).append("\", ")
                .append("\"telefono\": \"").append(telefono).append("\", ")
                .append("\"numCuenta\": \"").append(numCuenta).append("\", ")
                .append("\"sueldo\": ").append(sueldo)
                .append("}");
        return jsonBuilder.toString();
    }
}