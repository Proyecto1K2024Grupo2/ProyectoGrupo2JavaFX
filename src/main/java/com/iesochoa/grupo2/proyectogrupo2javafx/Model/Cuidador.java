package com.iesochoa.grupo2.proyectogrupo2javafx.Model;

import com.iesochoa.grupo2.proyectogrupo2javafx.DB.CuidadorDAO;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * La clase Cuidador es un tipo de empleado por lo que es hijo de Empleado y hereda sus atributos.
 * Un cuidador puede cuidar a los animales.
 * {@link Empleado}
 */
public class Cuidador extends Empleado {
    private String dni;

    /**
     * Constructor de la clase Cuidador la cual todos los datos que necesita son heredados por Empleado.
     *
     * @param dni       Dni del empleado.
     * @param nombre    Nombre completo del empleado.
     * @param telefono  Número de teléfono del empleado.
     * @param numCuenta Número de cuenta bancaria del empleado.
     * @param sueldo    Sueldo mensual del empleado.
     */
    public Cuidador(String dni, String nombre, int telefono, String numCuenta, double sueldo) {
        super(dni, nombre, telefono, numCuenta, sueldo);
    }

    //Getters y Setters
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }


    /**
     * Convierte los datos del Cuidador a XML.
     *
     * @return Devuelve un String con los datos de Cuidador a XML.
     */
    public String cuidadorToXML() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("      <dni>").append(dniEmpleado).append("</dni>")
                .append("      <nombre>").append(nombreEmpleado).append("</nombre")
                .append("      <telefono>").append(telefono).append("</telefono>")
                .append("      <numCuenta>").append(numCuenta).append("</numCuenta>")
                .append("      <sueldo>").append(sueldo).append("</sueldo>");
        return xmlBuilder.toString();
    }

    /**
     * Convierte los datos del Cuidador a JSON.
     *
     * @return Devuelve un String con los datos de Cuidador a JSON.
     */
    public String cuidadorToJSON() {
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

    @Override
    public String toString() {
        return """
                ────────────────────────────────
                -------Veterinario-------
                DNI:            %s
                Nombre:         %s
                Teléfono:       %d
                Nº Cuenta:      %s
                Sueldo:         %.2f
                
                """.formatted(
                getDniEmpleado(),
                getNombreEmpleado(),
                getTelefono(),
                getNumCuenta(),
                getSueldo()
        );
    }

    public static void mostrarMenu() throws Exception {
        CuidadorDAO cuidadorDAO = new CuidadorDAO();
        Scanner sc = new Scanner(System.in);
        int opc = 0;

        do {
            try {
                System.out.println("""
                ===== MENÚ CUIDADORES =====
                1. Mostrar todos los cuidadores
                2. Mostrar cuidador por DNI
                3. Insertar cuidador
                4. Actualizar cuidador
                5. Eliminar cuidador
                6. Total de cuidadores
                7. SALIR
                """);

                System.out.print("Selecciona una opción: ");
                while (!sc.hasNextInt()) {
                    System.out.println("Por favor, ingresa un número válido.");
                    sc.next();
                }
                opc = sc.nextInt();
                sc.nextLine(); // Limpiar buffer

                switch (opc) {
                    case 1 -> System.out.println(cuidadorDAO.getAllCuidadores());
                    case 2 -> System.out.println(cuidadorDAO.getCuidadorByDni(pedirDni(sc)));
                    case 3 -> cuidadorDAO.insertEmpleado(crearCuidador(sc));
                    case 4 -> cuidadorDAO.updateEmpleado(crearCuidador(sc));
                    case 5 -> cuidadorDAO.deleteEmpleadoByDni(pedirDni(sc));
                    case 6 -> System.out.println("Total de cuidadores: " + cuidadorDAO.totalCuidador());
                    case 7 -> System.out.println("Saliendo del menú de cuidadores...");
                    default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Error de formato de entrada: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error de validación: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Error en la base de datos: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("ERROR inesperado: " + e.getMessage());
            }
        } while (opc != 7);
    }

    private static String pedirDni(Scanner sc) {
        System.out.print("Introduce el DNI del cuidador: ");
        String dni = sc.nextLine().trim();
        if (!dni.matches("\\d{8}[A-Z]")) {
            throw new IllegalArgumentException("DNI no válido.");
        }
        return dni;
    }

    private static Cuidador crearCuidador(Scanner sc) {
        String dni = pedirDni(sc);

        System.out.print("Introduce el nombre del cuidador: ");
        String nombre = sc.nextLine();
        if (!nombre.matches(".{1,64}")) {
            throw new IllegalArgumentException("Nombre no válido.");
        }

        System.out.print("Introduce el teléfono: ");
        int telefono;
        try {
            telefono = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Teléfono no válido.");
        }

        System.out.print("Introduce el número de cuenta: ");
        String cuenta = sc.nextLine();
        if (!cuenta.matches(".{1,34}")) {
            throw new IllegalArgumentException("Número de cuenta no válido.");
        }

        System.out.print("Introduce el sueldo: ");
        double sueldo;
        try {
            sueldo = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Sueldo no válido.");
        }

        return new Cuidador(dni, nombre, telefono, cuenta, sueldo);
    }
}
