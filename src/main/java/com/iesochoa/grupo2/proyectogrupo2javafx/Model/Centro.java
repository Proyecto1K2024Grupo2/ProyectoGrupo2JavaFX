package com.iesochoa.grupo2.proyectogrupo2javafx.Model;

import com.iesochoa.grupo2.proyectogrupo2javafx.db.CentroDAO;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Representa un centro (local físico).
 * A un centro estan asociaciadas las salas de este, sus empleados y las citas.
 */
public class Centro {

    private int cod;
    private String nombreCentro;
    private String direccion;
    private String cp;

    //Constructor por defecto

    public Centro(int cod, String nombre, String direccion, String cp) {
        this.cod = cod;
        this.nombreCentro = nombre;
        this.cp = cp;
        this.direccion = direccion;
    }

    /**
     * Constructor de la clase Centro el cual te permite crear un centro.
     *
     * @param nombre    nombre del centro.
     * @param direccion Dirección del centro.
     * @param cp        Código Postal de la zona en la cual se encuentra el centro.
     */
    public Centro(String nombre, String direccion, String cp) {
        this.nombreCentro = nombre;
        this.cp = cp;
        this.direccion = direccion;
    }


    //Getters y Setters
    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNombreCentro() {
        return nombreCentro;
    }

    public void setNombreCentro(String nombreCentro) {
        this.nombreCentro = nombreCentro;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }


    /**
     * Convierte los datos del centro a XML.
     *
     * @return Devuelve un String con los datos de Centro a XML.
     */
    public String centroToXML() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("      <codigo>").append(cod).append("</codigo>")
                .append("      <nombre>").append(nombreCentro).append("</nombre")
                .append("      <direccion>").append(direccion).append("</direccion>")
                .append("      <cp>").append(cp).append("</cp>");
        return xmlBuilder.toString();
    }

    /**
     * Convierte los datos del centro a JSON.
     *
     * @return Devuelve un String con los datos de Centro a JSON.
     */
    public String centroToJSON() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{")
                .append("\"codigo\": \"").append(cod).append("\", ")
                .append("\"nombre\": \"").append(nombreCentro).append("\", ")
                .append("\"direccion\": \"").append(direccion).append("\", ")
                .append("\"cp\": \"").append(cp).append("\"")
                .append("}");
        return jsonBuilder.toString();
    }

    @Override
    public String toString() {
        return """
                ────────────────────────────────
                -------Centro-------
                Código:         %d
                Nombre:         %s
                Dirección:      %s
                Código Postal:  %s
                
                """.formatted(
                getCod(),
                getNombreCentro(),
                getDireccion(),
                getCp()
        );
    }

    public static void mostrarMenu() throws SQLException {
        CentroDAO centroDAO = new CentroDAO();
        Scanner sc = new Scanner(System.in);
        int opc = -1;

        do {
            try {
                System.out.println("""
                        ===== MENÚ CENTROS =====
                        1. Mostrar datos de todos los centros
                        2. Mostrar datos de un centro por ID
                        3. Insertar centro
                        4. Actualizar datos de centro
                        5. Eliminar un centro
                        6. Número total de centros
                        7. SALIR
                        """);
                System.out.print("Opción: ");
                opc = Integer.parseInt(sc.nextLine());

                switch (opc) {
                    case 1 -> {
                        var centros = centroDAO.getAllCentros();
                        if (centros.isEmpty()) {
                            System.out.println("No hay centros registrados.");
                        } else {
                            centros.forEach(System.out::println);
                        }
                    }

                    case 2 -> {
                        int id = pedirCentro(sc);
                        var centro = centroDAO.getCentroByCod(id);
                        if (centro != null) {
                            System.out.println(centro);
                        } else {
                            System.out.println("Centro no encontrado con ese ID.");
                        }
                    }

                    case 3 -> {
                        Centro nuevo = crearCentro(sc);
                        centroDAO.insertCentro(nuevo);
                        System.out.println("Centro insertado correctamente.");
                    }

                    case 4 -> {
                        int idActualizar = pedirCentro(sc);
                        Centro centroExistente = centroDAO.getCentroByCod(idActualizar);
                        if (centroExistente != null) {
                            Centro actualizado = crearCentro(sc);
                            actualizado.setCod(idActualizar);
                            centroDAO.updateCentro(actualizado);
                            System.out.println("Centro actualizado.");
                        } else {
                            System.out.println("No existe un centro con ese ID.");
                        }
                    }

                    case 5 -> {
                        int idEliminar = pedirCentro(sc);
                        Centro centro = centroDAO.getCentroByCod(idEliminar);
                        if (centro != null) {
                            centroDAO.deleteCentroByCod(idEliminar);
                            System.out.println("Centro eliminado.");
                        } else {
                            System.out.println("No existe un centro con ese ID.");
                        }
                    }

                    case 6 -> System.out.println("Total de centros: " + centroDAO.totalCentros());

                    case 7 -> System.out.println("Saliendo del menú de centros...");

                    default -> System.out.println("Opción no válida, intenta de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debes ingresar un número válido.");
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR de validación: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("ERROR en la base de datos: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("ERROR inesperado: " + e.getMessage());
            }

            System.out.println();

        } while (opc != 7);
    }

    public static int pedirCentro(Scanner sc) {
        System.out.println("Introduce el ID del centro: ");
        try {
            return sc.nextInt();
        } catch (Exception e) {
            sc.nextLine(); // Limpiar buffer
            System.out.println("ERROR, formato de ID no válido.");
            throw new RuntimeException(e);
        }
    }

    public static Centro crearCentro(Scanner sc) {
        System.out.println("Introduce el nombre del centro");
        String nomCen = sc.nextLine();
        if (!nomCen.matches(".{1,64}")) throw new IllegalArgumentException("Nombre del centro no válido.");

        System.out.println("Introduce la dirección del centro");
        String direc = sc.nextLine();
        if (!direc.matches(".{1,64}")) throw new IllegalArgumentException("Dirección del centro no válida.");

        System.out.println("Introduce el código postal del centro");
        String cp = sc.next();
        if (nomCen.matches("\\d{5}")) throw new IllegalArgumentException("Código postal del centro no válido.");

        return new Centro(nomCen, direc, cp);
    }

}
