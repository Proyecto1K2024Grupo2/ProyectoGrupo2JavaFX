package com.iesochoa.grupo2.proyectogrupo2javafx.Model;

import com.iesochoa.grupo2.proyectogrupo2javafx.DB.CitaDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Clase Cita que necesita una Sala, y el DNI del empleado el cual va a tratar al animal.
 * Sirve para tener registro de quien la ha asignado, donde ha ocurrido y se conecta con historial para tener más información que puede ser útil en un futuro.
 */
public class Cita {

    public int id;
    public String nombreSala;
    public LocalDate fecha;
    public LocalTime hora;
    public String dniRecepcionista;

    /**
     * Constructor por defecto de la clase Cita.
     */
    public Cita() {
    }

    /**
     * Constructor de Cita que permite crear una instancia de cita.
     *
     * @param nombreSala       Nombre de la sala donde ha ocurrido la cita.
     * @param fecha            Fecha de la cita.
     * @param hora             Hora de la cita.
     * @param dniRecepcionista DNI del recepcionista que ha asignado la cita.
     */
    public Cita(String nombreSala, LocalDate fecha, LocalTime hora, String dniRecepcionista) {
        this.nombreSala = nombreSala;
        this.fecha = fecha;
        this.hora = hora;
        this.dniRecepcionista = dniRecepcionista;
    }

    public Cita(int id, String nombreSala, LocalDate fecha, LocalTime hora, String dniRecepcionista) {
        this.id = id;
        this.nombreSala = nombreSala;
        this.fecha = fecha;
        this.hora = hora;
        this.dniRecepcionista = dniRecepcionista;
    }


    //Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDniRecepcionista() {
        return dniRecepcionista;
    }

    public void setDniRecepcionista(String dniRecepcionista) {
        this.dniRecepcionista = dniRecepcionista;
    }


    /**
     * Convierte los datos de la Cita a XML.
     *
     * @return Devuelve un String con los datos de la Cita a XML.
     */
    public String citaToXML() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("      <id>").append(id).append("</id>")
                .append("      <nombre_sala>").append(nombreSala).append("</nombre_sala>")
                .append("      <fecha>").append(fecha).append("</fecha>")
                .append("      <hora>").append(hora).append("</hora>")
                .append("      <dniRecepcionista>").append(dniRecepcionista).append("</dniRecepcionista>");
        return xmlBuilder.toString();
    }

    /**
     * Convierte los datos de la Cita a JSON.
     *
     * @return Devuelve un String con los datos de la Cita a JSON.
     */
    public String citaToJSON() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{")
                .append("\"id\": \"").append(id).append("\", ")
                .append("\"nombre_sala\": \"").append(nombreSala).append("\", ")
                .append("\"fecha\": \"").append(fecha).append("\", ")
                .append("\"hora\": \"").append(hora).append("\", ")
                .append("\"dniRecepcionista\": \"").append(dniRecepcionista).append("\"")
                .append("}");
        return jsonBuilder.toString();
    }

    @Override
    public String toString() {
        return """
                ────────────────────────────────
                -------Cita-------
                ID Cita:              %d
                Sala:                 %s
                Fecha:                %s
                Hora:                 %s
                DNI Recepcionista:    %s
                
                """.formatted(id, nombreSala, fecha, hora, dniRecepcionista);
    }

    public static void mostrarMenu() throws Exception {
        CitaDAO citaDAO = new CitaDAO(); // Asumiendo que tienes una clase CitaDAO
        Scanner sc = new Scanner(System.in);
        String opc = "";

        do {
            try {
                System.out.println("""
                        ===== MENÚ CITAS =====
                        1. Mostrar todas las citas
                        2. Mostrar cita por ID
                        3. Insertar nueva cita
                        4. Actualizar cita
                        5. Eliminar cita
                        6. Número total de citas
                        7. SALIR
                        """);

                System.out.print("Selecciona una opción: ");
                opc = sc.nextLine();

                switch (opc) {
                    case "1" -> System.out.println(citaDAO.getAllCitas());
                    case "2" -> System.out.println(citaDAO.getCitaById(pedirIdCita(sc)));
                    case "3" -> citaDAO.insertCita(crearCita(sc));
                    case "4" -> {
                        Cita c = crearCita(sc);
                        c.setId(pedirIdCita(sc));
                        citaDAO.updateCita(c);
                    }
                    case "5" -> citaDAO.deleteCitaById(pedirIdCita(sc));
                    case "6" -> System.out.println(citaDAO.totalCitas());
                    case "7" -> System.out.println("Saliendo del menú de citas...");
                    default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
                }
            }catch (NumberFormatException e) {
                System.out.println("Error de formato de entrada: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error de validación: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Error en la base de datos: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("ERROR inesperado: " + e.getMessage());
            }
        } while (!opc.equals("7"));
    }

    private static int pedirIdCita(Scanner sc) {
        System.out.println("Introduce el ID de la cita: ");
        try {
            return sc.nextInt();
        } catch (Exception e) {
            sc.nextLine(); // Limpiar buffer
            System.out.println("ERROR, formato de ID no válido.");
            throw new RuntimeException(e);
        }
    }

    private static Cita crearCita(Scanner sc) throws Exception {
        System.out.print("Introduce el DNI del recepcionista: ");
        String dni = sc.nextLine();
        if (!dni.matches("\\d{8}[A-ZÑ]")) {
            throw new IllegalArgumentException("DNI no válido.");
        }

        System.out.print("Introduce el nombre de la sala: ");
        String nombreSala = sc.nextLine();
        if (!nombreSala.matches(".{1,64}")) {
            throw new IllegalArgumentException("Nombre de la sala no válido.");
        }

        System.out.print("Introduce la fecha de la cita (YYYY-MM-DD): ");
        String fechaInput = sc.nextLine();
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaInput);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido.");
        }

        System.out.print("Introduce la hora de la cita (HH:MM): ");
        String horaInput = sc.nextLine();
        horaInput += ":00";
        LocalTime hora;
        try {
            hora = LocalTime.parse(horaInput);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de hora inválido.");
        }

        return new Cita(nombreSala, fecha, hora, dni);
    }


}