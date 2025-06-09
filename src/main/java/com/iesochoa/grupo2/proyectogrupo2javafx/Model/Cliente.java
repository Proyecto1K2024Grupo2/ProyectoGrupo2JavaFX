package com.iesochoa.grupo2.proyectogrupo2javafx.Model;

import com.iesochoa.grupo2.proyectogrupo2javafx.db.ClienteDAO;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Clase cliente con atributos básicos como DNI, nombre y teléfono.
 */
public class Cliente {
    private String dniCliente;
    private String nombreCliente;
    private String telefono;

    /**
     * Constructor sin parámetros de la clase Cliente
     */
    public Cliente() {
    }

    /**
     * Constructor para crear un Cliente
     *
     * @param dni      DNI del cliente.
     * @param nombre   Nombre del cliente.
     * @param telefono Número de teléfono del cliente.
     */
    public Cliente(String dni, String nombre, String telefono) {
        this.dniCliente = dni;
        this.nombreCliente = nombre;
        this.telefono = telefono;
    }

    //Getters y Setters
    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    /**
     * Convierte los datos del Cliente a XML.
     *
     * @return Devuelve un String con los datos de Cliente a XML.
     */
    public String clienteToXML() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("      <dni>").append(dniCliente).append("</dni>")
                .append("      <nombre>").append(nombreCliente).append("</nombre>")
                .append("      <telefono>").append(telefono).append("</telefono>");
        return xmlBuilder.toString();
    }

    /**
     * Convierte los datos del Cliente a JSON.
     *
     * @return Devuelve un String con los datos de Cliente a JSON.
     */
    public String clienteToJSON() {
        return "{"
                + "\"dni\":\"" + dniCliente + "\","
                + "\"nombre\":\"" + nombreCliente + "\","
                + "\"telefono\":\"" + telefono + "\""
                + "}";
    }


    @Override
    public String toString() {
        return """
                ────────────────────────────────
                -------Animal-------
                DNI:            %s
                Nombre:         %s
                Teléfono:       %s
                
                """.formatted(dniCliente, nombreCliente, telefono);
    }

    public static void mostrarMenu() throws Exception {
        ClienteDAO clienteDAO = new ClienteDAO();
        Scanner sc = new Scanner(System.in);
        int opc = 0;

        while (opc != 7) {
            try {
                System.out.println("""
                        ===== MENÚ TRATAMIENTOS =====
                        1. Mostrar datos de todos los clientes
                        2. Mostrar datos de un cliente por el DNI
                        3. Insertar cliente
                        4. Actualizar datos del cliente
                        5. Eliminar un cliente
                        6. Número total de clientes
                        7. Salir
                        """);

                System.out.print("Selecciona una opción: ");
                while (!sc.hasNextInt()) {
                    System.out.println("Por favor, ingresa un número válido.");
                    sc.next();
                }
                opc = sc.nextInt();
                sc.nextLine(); // limpiar el búfer

                switch (opc) {
                    case 1 -> System.out.println(clienteDAO.getAllClientes());
                    case 2 -> System.out.println(clienteDAO.getClienteByDNI(pedirDni(sc)));
                    case 3 -> clienteDAO.insertCliente(crearCliente(sc));
                    case 4 -> clienteDAO.updateCliente(crearCliente(sc));
                    case 5 -> clienteDAO.deleteCliente(pedirDni(sc));
                    case 6 -> System.out.println(clienteDAO.totalClientes());
                    case 7 -> System.out.println("Saliendo del menú de clientes...");
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
        }
    }

    private static String pedirDni(Scanner sc) {
        System.out.print("Introduce el DNI: ");
        String dni = sc.nextLine().trim();
        if (!dni.matches("\\d{8}[A-Z]")) {
            throw new IllegalArgumentException("DNI no válido.");
        }
        return dni;
    }

    private static String pedirDniCliente(Scanner sc) {
        System.out.print("Introduce el DNI del cliente: ");
        String dni = sc.next();
        while (!esCorrectoDNI(dni)) {
            System.out.println("DNI no válido. Por favor, ingresa un DNI válido.");
            dni = sc.next();
        }
        return dni;
    }

    private static Cliente crearCliente(Scanner sc) {
        String dni = pedirDniCliente(sc);

        System.out.println("Introduce el nombre del cliente");
        String nombre = sc.next();

        System.out.println("Introduce el teléfono del cliente");
        String telefono = sc.next();
        while (!telefono.matches("\\+?\\d+")) {
            System.out.println("Teléfono no válido. Introduce solo números (y opcional + al principio):");
            telefono = sc.next();
        }

        return new Cliente(dni, nombre, telefono);
    }

    private static boolean esCorrectoDNI(String DNI) {
        boolean dniValido = true;

        if (DNI.length() != 9)
            dniValido = false;

        if (dniValido) {
            for (int i = 0; i < 8; i++) {
                if (!Character.isDigit(DNI.charAt(i))) {
                    dniValido = false;
                    break;
                }
            }
        }

        if (dniValido) {
            char letra = DNI.charAt(8);
            letra = Character.toUpperCase(letra);
            if (!Character.isLetter(letra)) {
                dniValido = false;
            }

            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
            String numeros = DNI.substring(0, 8);
            int num = Integer.parseInt(numeros);
            char letraEsperada = letras.charAt(num % 23);

            if (letraEsperada != letra) {
                dniValido = false;
            }
        }

        return dniValido;
    }
}

