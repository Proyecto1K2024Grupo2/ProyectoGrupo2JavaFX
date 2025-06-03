package com.iesochoa.grupo2.proyectogrupo2javafx.Model;

import com.iesochoa.grupo2.proyectogrupo2javafx.DB.CirujanoDAO;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Cirujano es un tipo de empleado por lo que empleado es su clase padre y cirujano hereda sus atributos.
 * Un cirujano puede operar a los animales, por lo cual tiene relación con Cita y con Centro.
 * {@link Empleado}
 */
public class Cirujano extends Empleado {
    private String dni;

    /**
     * Constructor de la clase cirujano la cual todos los datos que necesita son heredados por Empleado.
     * @param dni Dni del empleado.
     * @param nombre Nombre completo del empleado.
     * @param telefono Número de teléfono del empleado.
     * @param numCuenta Número de cuenta bancaria del empleado.
     * @param sueldo Sueldo mensual del empleado.
     */
    public Cirujano(String dni, String nombre, int telefono, String numCuenta, double sueldo) {
        super(dni, nombre, telefono, numCuenta, sueldo);
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Convierte los datos del cirujano a XML.
     * @return Devuelve un String con los datos de Cirujano a XML.
     */
    public String cirujanoToXML(){
        StringBuilder xmlBuilder=new StringBuilder();
        xmlBuilder.append("      <dni>").append(dni).append("</dni>")
                .append("      <nombre>").append(nombreEmpleado).append("</nombre")
                .append("      <telefono>").append(telefono).append("</telefono>")
                .append("      <numCuenta>").append(numCuenta).append("</numCuenta>")
                .append("      <sueldo>").append(sueldo).append("</sueldo>");
        return xmlBuilder.toString();
    }

    /**
     * Convierte los datos del cirujano a JSON.
     * @return Devuelve un String con los datos de Cirujano a JSON.
     */
    public String cirujanoToJSON() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{")
                .append("\"dni\": \"").append(dni).append("\", ")
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

    public static void mostrarMenu() throws SQLException {
        CirujanoDAO cirujanoDAO = new CirujanoDAO();
        Scanner sc = new Scanner(System.in);
        int opc = 0;

        do {
            try {
                System.out.println("""
                ===== MENÚ CIRUJANOS =====
                1. Mostrar todos los cirujanos
                2. Mostrar cirujano por DNI
                3. Insertar cirujano
                4. Actualizar cirujano
                5. Eliminar cirujano
                6. Total de cirujanos
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
                    case 1 -> System.out.println(cirujanoDAO.getAllCirujanos());
                    case 2 -> System.out.println(cirujanoDAO.getCirujanoByDni(pedirDni(sc)));
                    case 3 -> cirujanoDAO.insertEmpleado(crearCirujano(sc));
                    case 4 -> cirujanoDAO.updateEmpleado(crearCirujano(sc));
                    case 5 -> cirujanoDAO.deleteEmpleadoByDni(pedirDni(sc));
                    case 6 -> System.out.println("Total de cirujanos: " + cirujanoDAO.totalCirujanos());
                    case 7 -> System.out.println("Saliendo del menú de cirujanos...");
                    default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Error de validación: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Error en la base de datos: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Error de formato de entrada: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("ERROR inesperado: " + e.getMessage());
            }
        } while (opc != 7);
    }

    private static String pedirDni(Scanner sc) {
        System.out.print("Introduce el DNI del cirujano: ");
        String dni = sc.nextLine().trim();
        if (!dni.matches("\\d{8}[A-Z]")) {
            throw new IllegalArgumentException("DNI no válido.");
        }
        return dni;
    }

    private static Cirujano crearCirujano(Scanner sc) {
        String dni = pedirDni(sc);

        System.out.print("Introduce el nombre del cirujano: ");
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

        return new Cirujano(dni, nombre, telefono, cuenta, sueldo);
    }
}