package com.iesochoa.grupo2.proyectogrupo2javafx.Model;

import com.iesochoa.grupo2.proyectogrupo2javafx.db.AnimalDAO;
import com.iesochoa.grupo2.proyectogrupo2javafx.db.CitaDAO;
import com.iesochoa.grupo2.proyectogrupo2javafx.db.HistorialDAO;
import com.iesochoa.grupo2.proyectogrupo2javafx.db.TratamientoDAO;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * Historial sirve para tener una correlacion entre las clases Cita, Animal y tratamiento.
 * Es útil a nivel de información, ya que se pueden ver por ejemplo, las anteriores citas de un animal en concreto o los tratamientos
 * Cabe recalcar que la información de Historial tiene correlacion entre sí, es decir, que en un registro la información que aparece ha ocurrido de esa manera.
 */
public class Historial {

    private int id_cita;
    private int id_animal;
    private int id_tratamiento;

    private Cita cita;
    private Animal animal;
    private Tratamiento tratamiento;

    /**
     * Constructor de la clase Historial
     *
     * @param id_cita        id de la cita
     * @param id_animal      id del animal
     * @param id_tratamiento id del tratamiento
     */
    public Historial(int id_cita, int id_animal, int id_tratamiento) {
        this.id_cita = id_cita;
        this.id_animal = id_animal;
        this.id_tratamiento = id_tratamiento;
    }

    public Historial(int id_cita, int id_animal, int id_tratamiento,
                     Cita cita, Animal animal, Tratamiento tratamiento) {
        this.id_cita = id_cita;
        this.id_animal = id_animal;
        this.id_tratamiento = id_tratamiento;
        this.cita = cita;
        this.animal = animal;
        this.tratamiento = tratamiento;
    }


    //Getters y Setters
    public int getId_cita() {
        return id_cita;
    }

    public void setId_cita(int id_cita) {
        this.id_cita = id_cita;
    }

    public int getId_animal() {
        return id_animal;
    }

    public void setId_animal(int id_animal) {
        this.id_animal = id_animal;
    }

    public int getId_tratamiento() {
        return id_tratamiento;
    }

    public void setId_tratamiento(int id_tratamiento) {
        this.id_tratamiento = id_tratamiento;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    /**
     * Convierte los datos del historial a XML.
     *
     * @return Devuelve un String con los datos de Historial a XML.
     */
    public String historialToXML() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("  <historial>\n")
                .append("    <id_cita>").append(id_cita).append("</id_cita>\n")
                .append("    <id_animal>").append(id_animal).append("</id_animal>\n")
                .append("    <id_tratamiento>").append(id_tratamiento).append("</id_tratamiento>\n");

        if (cita != null) {
            xmlBuilder.append("    <cita>\n")
                    .append(cita.citaToXML())  // Este método debe existir en la clase Cita
                    .append("    </cita>\n");
        }

        if (animal != null) {
            xmlBuilder.append("    <animal>\n")
                    .append(animal.animalToXML())  // Este método debe existir en la clase Animal
                    .append("    </animal>\n");
        }

        if (tratamiento != null) {
            xmlBuilder.append("    <tratamiento>\n")
                    .append(tratamiento.tratamientoToXML())  // Este método debe existir en la clase Tratamiento
                    .append("    </tratamiento>\n");
        }

        xmlBuilder.append("  </historial>\n");
        return xmlBuilder.toString();
    }

    /**
     * Convierte los datos del historial a JSON.
     *
     * @return Devuelve un String con los datos de Historial a JSON.
     */
    public String historialToJSON() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{")
                .append("\"id_cita\": ").append(id_cita).append(", ")
                .append("\"id_animal\": ").append(id_animal).append(", ")
                .append("\"id_tratamiento\": ").append(id_tratamiento);

        if (cita != null) {
            jsonBuilder.append(", \"cita\": ").append(cita.citaToJSON()); // Este método debe devolver JSON válido
        }

        if (animal != null) {
            jsonBuilder.append(", \"animal\": ").append(animal.animalToJSON());
        }

        if (tratamiento != null) {
            jsonBuilder.append(", \"tratamiento\": ").append(tratamiento.tratamientoToJSON());
        }

        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

    @Override
    public String toString() {
        return """
                Historial de Cita y Tratamiento:
                -------------------------------------------------
                ID Cita: %d
                Animal: %s
                Tratamiento: %s
                -------------------------------------------------
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
                id_cita,
                animal != null ? animal.getNombreAnimal() : "N/A",
                tratamiento != null ? tratamiento.getTratamiento() : "N/A",
                tratamiento != null ? tratamiento.getDni_cuidador() : "N/A",
                tratamiento != null ? tratamiento.getFechaCuidador() : "N/A",
                tratamiento != null ? tratamiento.getHoraCuidador() : "N/A",
                tratamiento != null ? tratamiento.getDni_veterinario() : "N/A",
                tratamiento != null ? tratamiento.getFechaVeterinario() : "N/A",
                tratamiento != null ? tratamiento.getHoraVeterinario() : "N/A",
                tratamiento != null ? tratamiento.getDni_cirujano() : "N/A",
                tratamiento != null ? tratamiento.getFechaCirujano() : "N/A",
                tratamiento != null ? tratamiento.getHoraCirujano() : "N/A"
        );
    }


    public static void mostrarMenu() throws Exception {
        HistorialDAO historialDAO = HistorialDAO.getInstance();
        CitaDAO citaDAO = CitaDAO.getInstance();
        AnimalDAO animalDAO = AnimalDAO.getInstance();
        TratamientoDAO tratamientoDAO = TratamientoDAO.getInstance();
        Scanner sc = new Scanner(System.in);
        int opc;

        do {
            try {
                System.out.println("""
                        ===== MENÚ HISTORIAL =====
                        1. Mostrar todos los historiales
                        2. Mostrar historial por ID
                        3. Insertar historial
                        4. Actualizar historial
                        5. Eliminar historial
                        6. Total de historiales
                        7. SALIR
                        """);
                System.out.print("Selecciona una opción: ");

                while (!sc.hasNextInt()) {
                    System.out.println("Por favor, introduce un número válido.");
                    sc.next();
                }
                opc = sc.nextInt();

                switch (opc) {
                    case 1 -> mostrarHistoriales(historialDAO);
                    case 2 -> mostrarHistorialPorId(historialDAO, sc);
                    case 3 -> insertarHistorial(historialDAO, sc, citaDAO, animalDAO, tratamientoDAO);
                    case 4 -> actualizarHistorial(historialDAO, sc);
                    case 5 -> eliminarHistorial(historialDAO, sc);
                    case 6 -> System.out.println("Total de historiales: " + historialDAO.totalHistorial());
                    case 7 -> System.out.println("Saliendo del menú de historiales...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
                opc = 0;
            }
        } while (opc != 7);
    }

    private static void mostrarHistoriales(HistorialDAO historialDAO) {
        try {
            for (Historial h : historialDAO.getAllHistoriales()) {
                System.out.println(h);
            }
        } catch (Exception e) {
            System.out.println("Error al mostrar los historiales: " + e.getMessage());
        }
    }

    private static void mostrarHistorialPorId(HistorialDAO historialDAO, Scanner sc) {
        try {
            System.out.print("ID del historial: ");
            int id = sc.nextInt();
            Historial h = historialDAO.getHistorialById(id);
            System.out.println(h != null ? h : "Historial no encontrado.");
        } catch (Exception e) {
            System.out.println("Error al obtener el historial: " + e.getMessage());
        }
    }

    private static void insertarHistorial(HistorialDAO historialDAO, Scanner sc, CitaDAO citaDAO, AnimalDAO animalDAO, TratamientoDAO tratamientoDAO) {
        try {
            Cita cita = crearCita(sc);
            Animal animal = crearAnimal(sc);
            Tratamiento tratamiento = crearTratamiento(sc);

            if (cita == null || animal == null || tratamiento == null) {
                System.out.println("No se pudo crear el historial debido a errores previos.");
                return;
            }

            citaDAO.insertCita(cita);
            animalDAO.insertAnimal(animal);
            tratamientoDAO.insertTratamiento(tratamiento);

            Historial h = new Historial(cita.getId(), animal.getId(), tratamiento.getId(), cita, animal, tratamiento);
            historialDAO.insertHistorial(h);
            System.out.println("Historial insertado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al insertar el historial: " + e.getMessage());
        }
    }

    private static void actualizarHistorial(HistorialDAO historialDAO, Scanner sc) {
        try {
            System.out.print("ID del historial a actualizar: ");
            int id = sc.nextInt();
            Historial h = historialDAO.getHistorialById(id);

            if (h != null) {
                // Aquí podrías añadir lógica para cambiar citas o tratamientos si se desea
                historialDAO.updateHistorial(h, id);
                System.out.println("Historial actualizado.");
            } else {
                System.out.println("Historial no encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar historial: " + e.getMessage());
        }
    }

    private static void eliminarHistorial(HistorialDAO historialDAO, Scanner sc) {
        try {
            System.out.print("ID del historial a eliminar: ");
            int id = sc.nextInt();
            historialDAO.deleteHistorial(id);
            System.out.println("Historial eliminado.");
        } catch (Exception e) {
            System.out.println("Error al eliminar historial: " + e.getMessage());
        }
    }

    private static Cita crearCita(Scanner sc) {
        try {
            System.out.print("Fecha (YYYY-MM-DD): ");
            LocalDate fecha = LocalDate.parse(sc.next());
            System.out.print("Hora (HH:MM): ");
            LocalTime hora = LocalTime.parse(sc.next());
            System.out.print("Sala: ");
            String sala = sc.next();
            System.out.print("DNI Recepcionista: ");
            String dni = sc.next();

            return new Cita(sala, fecha, hora, dni);
        } catch (Exception e) {
            System.out.println("Error al crear la cita: " + e.getMessage());
            return null;
        }
    }

    private static Animal crearAnimal(Scanner sc) {
        try {
            System.out.print("Nombre: ");
            String nombre = sc.next();
            System.out.print("Especie: ");
            String especie = sc.next();
            System.out.print("Raza: ");
            String raza = sc.next();
            System.out.print("Fecha nacimiento (YYYY-MM-DD): ");
            Date fecha = Date.valueOf(sc.next());

            return new Animal("DNI", nombre, especie, raza, fecha);
        } catch (Exception e) {
            System.out.println("Error al crear el animal: " + e.getMessage());
            return null;
        }
    }

    private static Tratamiento crearTratamiento(Scanner sc) {
        try {
            System.out.print("Nombre del tratamiento: ");
            String nombre = sc.next();
            System.out.print("Fecha (YYYY-MM-DD): ");
            String fecha = sc.next();
            System.out.print("Hora (HH:MM): ");
            String hora = sc.next();

            return new Tratamiento(nombre, fecha, hora);
        } catch (Exception e) {
            System.out.println("Error al crear el tratamiento: " + e.getMessage());
            return null;
        }
    }
}