package com.iesochoa.grupo2.proyectogrupo2javafx.Model;

import com.iesochoa.grupo2.proyectogrupo2javafx.db.CentroDAO;
import com.iesochoa.grupo2.proyectogrupo2javafx.db.SalaDAO;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * La clase Sala hace referencia a una sala de un Centro
 * Sala está relacionada con su centro y tiene información identificativa
 */
public class Sala {

    private String nombre;
    private int codCentro;

    //Constructor por defecto
    public Sala() {
    }


    /**
     * Constructor de Sala
     *
     * @param nombre    Nombre de la Sala
     * @param codCentro Código del centro en el que se encuentra la sala
     */
    public Sala(String nombre, int codCentro) {
        this.nombre = nombre;
        this.codCentro = codCentro;
    }

    //Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCodCentro() {
        return codCentro;
    }

    public void setCodCentro(int codCentro) {
        this.codCentro = codCentro;
    }


    /**
     * Convierte los datos de la sala a XML.
     *
     * @return Devuelve un String con los datos de Sala a XML.
     */
    public String salaToXML() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("      <nombre>").append(nombre).append("</nombre>")
                .append("      <cod_centro>").append(codCentro).append("</cod_centro>");
        return xmlBuilder.toString();
    }

    /**
     * Convierte los datos de la sala a JSON.
     *
     * @return Devuelve un String con los datos de Sala a JSON.
     */
    public String salaToJSON() {
        return "{"
                + "\"nombre\":\"" + nombre + "\","
                + "\"cod_centro\":\"" + codCentro + "\""
                + "}";
    }

    @Override
    public String toString() {
        return """
                ────────────────────────────────
                -------Centro-------
                Nombre:         %s
                Código Centro:  %s
                
                """.formatted(
                getNombre(),
                getCodCentro()
        );
    }

    public static void mostrarMenu() throws SQLException {
        SalaDAO salaDAO = new SalaDAO();
        Scanner sc = new Scanner(System.in);
        String opc = "";

        do {
            try {
                System.out.println("""
                        ===== MENÚ SALAS =====
                        1. Mostrar datos de todas las salas
                        2. Mostrar datos de una sala por nombre
                        3. Insertar sala
                        4. Actualizar datos de sala
                        5. Eliminar una sala
                        6. Numero total de salas
                        7. SALIR
                        """);
                System.out.print("Selecciona una opción: ");
                opc = sc.nextLine();

                switch (opc) {
                    case "1" -> System.out.println(salaDAO.getAllSalas());
                    case "2" -> System.out.println(salaDAO.getSalaByNombre(pedirSala(sc)));
                    case "3" -> salaDAO.insertSala(crearSala(sc));
                    case "4" -> salaDAO.updateSala(crearSala(sc));
                    case "5" -> salaDAO.deleteSalaByNombre(pedirSala(sc));
                    case "6" -> System.out.println("Total de salas: " + salaDAO.totalSalas());
                    case "7" -> System.out.println("Saliendo del menú de salas...");
                    default -> System.out.println("Opcion no valida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error de formato de entrada: Debes ingresar un número.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error de validación: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Error en la base de datos: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("ERROR inesperado: " + e.getMessage());
            }
        } while (!opc.equals("7"));
    }

    private static String pedirSala(Scanner sc){
        System.out.println("Introduce el nombre de la sala: ");
        String nomSal = sc.nextLine();
        if (!nomSal.matches(".{1,32}")) throw new IllegalArgumentException("Nombre de la sala no válido.");
        return nomSal;
    }


    private static Sala crearSala(Scanner sc) throws SQLException {
        CentroDAO centroDAO = new CentroDAO();

        System.out.println("Introduce el nombre de la sala: ");
        String nomSal = sc.nextLine();
        if (!nomSal.matches(".{1,32}")) throw new IllegalArgumentException("Nombre de la sala no válido.");

        System.out.println("Introduce el código del centro de la sala: ");
        int codCen = sc.nextInt();
        if (centroDAO.getCentroByCod(codCen) == null) throw new IllegalArgumentException("Centro no existente.");
        return new Sala(nomSal, codCen);
    }
}