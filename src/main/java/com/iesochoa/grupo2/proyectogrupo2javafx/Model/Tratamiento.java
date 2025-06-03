package com.iesochoa.grupo2.proyectogrupo2javafx.Model;

import com.iesochoa.grupo2.proyectogrupo2javafx.DB.TratamientoDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * La clase tratamiento está relacionada con Empleado e Historial.
 * Esta clase sirve para proporcionar información sobre el tratamiento que se le ha realizado al Animal, quien ha sido y cuándo.
 */
public class Tratamiento {

    private int id;
    private String tratamiento;
    private String medicamento;
    private String posologia;
    private LocalDate fechaCuidador;
    private LocalTime horaCuidador;
    private LocalDate fechaVeterinario;
    private LocalTime horaVeterinario;
    private LocalDate fechaCirujano;
    private LocalTime horaCirujano;
    private String dni_veterinario;
    private String dni_cirujano;
    private String dni_cuidador;

    //Constructor por defecto
    public Tratamiento(String tratamiento, String medicamento, String posologia) {
        this.tratamiento = tratamiento;
        this.medicamento = medicamento;
        this.posologia = posologia;
    }

    public Tratamiento(String tratamiento, String medicamento, String posologia, LocalDate fechaCuidador,
                       LocalTime horaCuidador, String dni_cuidador) {
        this.tratamiento = tratamiento;
        this.medicamento = medicamento;
        this.posologia = posologia;
        this.fechaCuidador = fechaCuidador;
        this.horaCuidador = horaCuidador;
        this.dni_cuidador = dni_cuidador;
    }

    public Tratamiento(String tratamiento, String medicamento, String posologia,
                       LocalDate fechaVeterinario, LocalTime horaVeterinario, String dni_veterinario, boolean esVeterinario) {
        this.tratamiento = tratamiento;
        this.medicamento = medicamento;
        this.posologia = posologia;
        this.fechaVeterinario = fechaVeterinario;
        this.horaVeterinario = horaVeterinario;
        this.dni_veterinario = dni_veterinario;
    }

    public Tratamiento(String tratamiento, String medicamento, String posologia,
                       LocalDate fechaCirujano, LocalTime horaCirujano, String dni_cirujano, int esCirujano) {
        this.tratamiento = tratamiento;
        this.medicamento = medicamento;
        this.posologia = posologia;
        this.fechaCirujano = fechaCirujano;
        this.horaCirujano = horaCirujano;
        this.dni_cirujano = dni_cirujano;
    }

    public Tratamiento(int id, String tratamiento, String medicamento, String posologia,
                       LocalDate fechaCuidador, LocalTime horaCuidador, String dni_cuidador,
                       LocalDate fechaVeterinario, LocalTime horaVeterinario, String dni_veterinario,
                       LocalDate fechaCirujano, LocalTime horaCirujano, String dni_cirujano) {
        this.id = id;
        this.tratamiento = tratamiento;
        this.medicamento = medicamento;
        this.posologia = posologia;
        this.fechaCuidador = fechaCuidador;
        this.horaCuidador = horaCuidador;
        this.dni_cuidador = dni_cuidador;
        this.fechaVeterinario = fechaVeterinario;
        this.horaVeterinario = horaVeterinario;
        this.dni_veterinario = dni_veterinario;
        this.fechaCirujano = fechaCirujano;
        this.horaCirujano = horaCirujano;
        this.dni_cirujano = dni_cirujano;
    }


    //Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getPosologia() {
        return posologia;
    }

    public void setPosologia(String posologia) {
        this.posologia = posologia;
    }

    public LocalDate getFechaCuidador() {
        return fechaCuidador;
    }

    public void setFechaCuidador(LocalDate fechaCuidador) {
        this.fechaCuidador = fechaCuidador;
    }

    public LocalTime getHoraCuidador() {
        return horaCuidador;
    }

    public void setHoraCuidador(LocalTime horaCuidador) {
        this.horaCuidador = horaCuidador;
    }

    public LocalDate getFechaVeterinario() {
        return fechaVeterinario;
    }

    public void setFechaVeterinario(LocalDate fechaVeterinario) {
        this.fechaVeterinario = fechaVeterinario;
    }

    public LocalTime getHoraVeterinario() {
        return horaVeterinario;
    }

    public void setHoraVeterinario(LocalTime horaVeterinario) {
        this.horaVeterinario = horaVeterinario;
    }

    public LocalDate getFechaCirujano() {
        return fechaCirujano;
    }

    public void setFechaCirujano(LocalDate fechaCirujano) {
        this.fechaCirujano = fechaCirujano;
    }

    public LocalTime getHoraCirujano() {
        return horaCirujano;
    }

    public void setHoraCirujano(LocalTime horaCirujano) {
        this.horaCirujano = horaCirujano;
    }

    public String getDni_veterinario() {
        return dni_veterinario;
    }

    public void setDni_veterinario(String dni_veterinario) {
        this.dni_veterinario = dni_veterinario;
    }

    public String getDni_cirujano() {
        return dni_cirujano;
    }

    public void setDni_cirujano(String dni_cirujano) {
        this.dni_cirujano = dni_cirujano;
    }

    public String getDni_cuidador() {
        return dni_cuidador;
    }

    public void setDni_cuidador(String dni_cuidador) {
        this.dni_cuidador = dni_cuidador;
    }

    /**
     * Convierte los datos del tratamiento a XML.
     *
     * @return Devuelve un String con los datos de Tratamiento a XML.
     */
    public String tratamientoToXML() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("      <id>").append(id).append("</id>")
                .append("      <tratamiento>").append(tratamiento).append("</tratamiento>")
                .append("      <numCuenta>").append(medicamento).append("</numCuenta>")
                .append("      <posologia>").append(posologia).append("</posologia>")
                .append("      <fechaCuidador>").append(fechaCuidador).append("</fechaCuidador>")
                .append("      <horaCuidador>").append(horaCuidador).append("</horaCuidador>")
                .append("      <fechaVeterinario>").append(fechaVeterinario).append("</fechaVeterinario>")
                .append("      <horaVeterinario>").append(horaVeterinario).append("</horaVeterinario>")
                .append("      <fechaCirujano>").append(fechaCirujano).append("</fechaCirujano>")
                .append("      <horaCirujano>").append(horaCirujano).append("</horaCirujano>")
                .append("      <dni_cuidador>").append(dni_cuidador).append("</dni_cuidador>")
                .append("      <dni_veterinario>").append(dni_veterinario).append("</dni_veterinario>")
                .append("      <dni_cirujano>").append(dni_cirujano).append("</dni_cirujano>");
        return xmlBuilder.toString();
    }


    /**
     * Convierte los datos del tratamiento a JSON.
     *
     * @return Devuelve un String con los datos de Tratamiento a JSON.
     */
    public String tratamientoToJSON() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{")
                .append("\"id\": \"").append(id).append("\", ")
                .append("\"tratamiento\": \"").append(tratamiento).append("\", ")
                .append("\"numCuenta\": \"").append(medicamento).append("\", ")
                .append("\"posologia\": \"").append(posologia).append("\", ")
                .append("\"fechaCuidador\": \"").append(fechaCuidador).append("\", ")
                .append("\"horaCuidador\": \"").append(horaCuidador).append("\", ")
                .append("\"fechaVeterinario\": \"").append(fechaVeterinario).append("\", ")
                .append("\"horaVeterinario\": \"").append(horaVeterinario).append("\", ")
                .append("\"fechaCirujano\": \"").append(fechaCirujano).append("\", ")
                .append("\"horaCirujano\": \"").append(horaCirujano).append("\", ")
                .append("\"dni_cuidador\": \"").append(dni_cuidador).append("\", ")
                .append("\"dni_veterinario\": \"").append(dni_veterinario).append("\", ")
                .append("\"dni_cirujano\": \"").append(dni_cirujano).append("\"")
                .append("}");
        return jsonBuilder.toString();
    }

    public String toString() {
        return """
                ───────────────────────────────────────────────
                --------------- TRATAMIENTO -------------------
                ID:                  %d
                Tratamiento:         %s
                Medicamento:         %s
                Posología:           %s
                
                --- Cuidador ---
                DNI:                 %s
                Fecha:               %s
                Hora:                %s
                
                --- Veterinario ---
                DNI:                 %s
                Fecha:               %s
                Hora:                %s
                
                --- Cirujano ---
                DNI:                 %s
                Fecha:               %s
                Hora:                %s
                """.formatted(
                id,
                tratamiento,
                medicamento,
                posologia,
                dni_cuidador != null ? dni_cuidador : "N/A",
                fechaCuidador != null ? fechaCuidador : "N/A",
                horaCuidador != null ? horaCuidador : "N/A",
                dni_veterinario != null ? dni_veterinario : "N/A",
                fechaVeterinario != null ? fechaVeterinario : "N/A",
                horaVeterinario != null ? horaVeterinario : "N/A",
                dni_cirujano != null ? dni_cirujano : "N/A",
                fechaCirujano != null ? fechaCirujano : "N/A",
                horaCirujano != null ? horaCirujano : "N/A"
        );
    }

    public static void mostrarMenu() throws Exception {
        TratamientoDAO tratamientoDAO = new TratamientoDAO();
        Scanner sc = new Scanner(System.in);
        int opc = 0;

        do {
            try {
            System.out.println("""
                    ===== MENÚ TRATAMIENTOS =====
                    1. Mostrar todos los tratamientos
                    2. Buscar tratamiento por ID
                    3. Insertar nuevo tratamiento
                    4. Actualizar tratamiento
                    5. Eliminar tratamiento
                    6. Total de tratamientos
                    7. Salir
                    """);
            System.out.print("Selecciona una opción: ");
            while (!sc.hasNextInt()) {
                System.out.println("Por favor, ingresa un número válido.");
                sc.next();
            }
            opc = sc.nextInt();

            switch (opc) {
                case 1 -> System.out.println(tratamientoDAO.getAllTratamientos());
                case 2 -> System.out.println(tratamientoDAO.getTratamientoById(pedirId(sc)));
                //case 3 -> tratamientoDAO.insertTratamiento(crearTratamiento(sc));
                //case 4 -> tratamientoDAO.updateTratamiento(crearTratamiento(sc));
                case 5 -> tratamientoDAO.deleteTratamientoById(pedirId(sc));
                case 6 -> System.out.println("Total de tratamientos: " + tratamientoDAO.totalTratamientos());
                case 7 -> System.out.println("Saliendo del menú de tratamientos...");
                default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha u hora incorrecto. Usa el formato YYYY-MM-DD para la fecha y HH:MM para la hora.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error de validación: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Error en la base de datos: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("ERROR inesperado: " + e.getMessage());
            }
        } while (opc != 7);
    }

    private static int pedirId(Scanner sc) {
        System.out.print("Introduce el ID del tratamiento: ");
        while (!sc.hasNextInt()) {
            System.out.println("Por favor, introduce un número válido.");
            sc.nextInt();
        }
        return sc.nextInt();
    }

    /*private static Tratamiento crearTratamiento(Scanner sc) {
        sc.nextLine(); // Limpiar buffer

        // Tratamiento
        String tratamiento = "";
        boolean tratamientoValido = false;
        while (!tratamientoValido) {
            System.out.print("Tratamiento: ");
            tratamiento = sc.nextLine();
            if (!tratamiento.isEmpty()) {
                tratamientoValido = true;
            } else {
                System.out.println("El nombre del tratamiento no puede estar vacío.");
            }
        }

        // Medicamento
        String medicamento = "";
        boolean medicamentoValido = false;
        while (!medicamentoValido) {
            System.out.print("Medicamento: ");
            medicamento = sc.nextLine();
            if (!medicamento.isEmpty()) {
                medicamentoValido = true;
            } else {
                System.out.println("El medicamento no puede estar vacío.");
            }
        }

        // Posología
        String posologia = "";
        boolean posologiaValida = false;
        while (!posologiaValida) {
            System.out.print("Posología: ");
            posologia = sc.nextLine();
            if (!posologia.isEmpty()) {
                posologiaValida = true;
            } else {
                System.out.println("La posología no puede estar vacía.");
            }
        }

        // Tipo de tratamiento
        int tipo = 0;
        boolean tipoValido = false;
        while (!tipoValido) {
            System.out.println("""
                ¿Quién realiza el tratamiento?
                1. Cuidador
                2. Veterinario
                3. Cirujano
                """);
            System.out.print("Selecciona una opción (1-3): ");
            if (sc.hasNextInt()) {
                tipo = sc.nextInt();
                sc.nextLine(); // Limpiar buffer
                if (tipo >= 1 && tipo <= 3) {
                    tipoValido = true;
                } else {
                    System.out.println("Opción no válida.");
                }
            } else {
                sc.next(); // Limpiar entrada incorrecta
                System.out.println("Opción no válida.");
            }
        }

        // Fecha
        LocalDate fecha = null;
        boolean fechaValida = false;
        while (!fechaValida) {
            try {
                System.out.print("Fecha (YYYY-MM-DD): ");
                String fechaStr = sc.nextLine();
                fecha = LocalDate.parse(fechaStr);
                fechaValida = true;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha incorrecto. Usa el formato YYYY-MM-DD.");
            }
        }

        // Hora
        LocalTime hora = null;
        boolean horaValida = false;
        while (!horaValida) {
            try {
                System.out.print("Hora (HH:MM): ");
                String horaStr = sc.nextLine();
                hora = LocalTime.parse(horaStr);
                horaValida = true;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de hora incorrecto. Usa el formato HH:MM.");
            }
        }

        String dni = "";
        boolean dniValido = false;
        while (!dniValido) {
            System.out.print("DNI del profesional: ");
            dni = sc.nextLine();
            if (dni.matches("\\d{8}[A-Z]")) {
                dniValido = true;
            } else {
                System.out.println("DNI no válido. Debe tener 8 dígitos seguidos de una letra mayúscula.");
            }
        }
        return switch (tipo) {
            case 1 -> {
                Tratamiento t = new Tratamiento(tratamiento,medicamento,posologia);
                t.setFechaCuidador(fecha);
                t.setHoraCuidador(hora);

            }
            case 2 ->
            case 3 ->
            default -> null;
        };
    }*/

}
