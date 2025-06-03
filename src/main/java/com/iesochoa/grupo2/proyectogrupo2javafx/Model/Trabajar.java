package com.iesochoa.grupo2.proyectogrupo2javafx.Model;

/**
 * La clase Trabajar es la relación entre un @Empleado y el @Centro en el que trabaja
 */
public class Trabajar {

    public String dniEmpleado;
    public int codCentro;

    /**
     * Constructor por defecto de la clase Trabajar
     */
    public Trabajar() {
    }


    /**
     * Constructor de la clase trabajar que recibe el dni del empleado y el codigo del centro en el que trabaja
     * @param dniEmpleado DNI del empleado
     * @param codCentro Código del centro
     */
    public Trabajar(String dniEmpleado, int codCentro) {
        this.dniEmpleado = dniEmpleado;
        this.codCentro = codCentro;
    }

    //Getters y Setters
    public String getDniEmpleado() {
        return dniEmpleado;
    }

    public void setDniEmpleado(String dniEmpleado) {
        this.dniEmpleado = dniEmpleado;
    }

    public int getCodCentro() {
        return codCentro;
    }

    public void setCodCentro(int codCentro) {
        this.codCentro = codCentro;
    }



    /**
     * Convierte los datos de Trabajar a XML.
     * @return Devuelve un String con los datos de Trabajar a XML.
     */
    public String trabajarToXML(){
        StringBuilder xmlBuilder=new StringBuilder();
        xmlBuilder.append("      <dniEmpleado>").append(dniEmpleado).append("</dniEmpleado>")
                .append("      <codCentro>").append(codCentro).append("</codCentro");
        return xmlBuilder.toString();
    }

    /**
     * Convierte los datos de Trabajar a JSON.
     * @return Devuelve un String con los datos de Trabajar a JSON.
     */
    public String trabajarToJSON() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{")
                .append("\"dniEmpleado\": \"").append(dniEmpleado).append("\", ")
                .append("\"codCentro\": \"").append(codCentro).append("\"")
                .append("}");
        return jsonBuilder.toString();
    }

}