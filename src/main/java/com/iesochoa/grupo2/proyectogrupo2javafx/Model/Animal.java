package com.iesochoa.grupo2.proyectogrupo2javafx.Model;

import com.iesochoa.grupo2.proyectogrupo2javafx.db.AnimalDAO;
import com.iesochoa.grupo2.proyectogrupo2javafx.db.ClienteDAO;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Representa un animal asociado a un cliente en una base de datos veterinaria.
 * Incluye información como el nombre, especie, raza y fecha de nacimiento.
 * {@link AnimalDAO}
 */
public class Animal {
    private int id;
    private String dni_cliente;
    private String nombreAnimal;
    private String especie;
    private String raza;
    private Date fnac;


    public Animal(String dni_cliente, String nombreAnimal, String especie, String raza, Date fnac) {
        this.dni_cliente = dni_cliente;
        this.nombreAnimal = nombreAnimal;
        this.especie = especie;
        this.raza = raza;
        this.fnac = fnac;
    }

    /**
     * Constructor para crear un objeto Animal sin ID (ID se asigna automáticamente).
     *
     * @param id
     * @param dni_cliente  DNI del cliente dueño del animal.
     * @param nombreAnimal Nombre del animal.
     * @param especie      Especie del animal.
     * @param raza         Raza del animal.
     * @param fnac         Fecha de nacimiento del animal.
     */
    public Animal(int id, String dni_cliente, String nombreAnimal, String especie, String raza, Date fnac) {
        this.id = id;
        this.dni_cliente = dni_cliente;
        this.nombreAnimal = nombreAnimal;
        this.especie = especie;
        this.raza = raza;
        this.fnac = fnac;
    }

    //Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni_cliente() {
        return dni_cliente;
    }

    public void setDni_cliente(String dni_cliente) {
        this.dni_cliente = dni_cliente;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getNombreAnimal() {
        return nombreAnimal;
    }

    public void setNombreAnimal(String nombreAnimal) {
        this.nombreAnimal = nombreAnimal;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public LocalDate getFnac() {
        return fnac.toLocalDate();
    }

    public void setFnac(java.util.Date fnac) {
        this.fnac = Date.valueOf(String.valueOf(fnac));
    }


    /**
     * Convierte los datos de una lista de animales en formato XML.
     *
     * @param lista Lista de objetos Animal a convertir.
     * @return Representación en XML de la lista de animales.
     */
    public static String animalesToXML(List<Animal> lista) {
        StringBuilder xmlBuilder = new StringBuilder();
        // Añade la declaración XML para una mejor práctica
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<animales>\n");
        for (Animal a : lista) {
            xmlBuilder.append("   <animal>\n")
                    .append("      <id>").append(a.id).append("</id>\n")
                    .append("      <dni_cliente>").append(a.dni_cliente).append("</dni_cliente>\n")
                    .append("      <nombre>").append(a.nombreAnimal).append("</nombre>\n")
                    .append("      <especie>").append(a.especie).append("</especie>\n")
                    .append("      <raza>").append(a.raza).append("</raza>\n")
                    .append("      <fnac>").append(a.fnac).append("</fnac>\n") // fnac ya es Date, toString() funciona bien
                    .append("   </animal>\n");
        }
        xmlBuilder.append("</animales>\n");
        return xmlBuilder.toString();
    }

    /**
     * Convierte los datos de una lista de animales en formato JSON.
     *
     * @param lista Lista de objetos Animal a convertir.
     * @return Representación en JSON de la lista de animales.
     */
    public static String animalesToJSON(List<Animal> lista) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n"); // Abrir un array JSON para múltiples objetos
        for (int i = 0; i < lista.size(); i++) {
            Animal a = lista.get(i);
            jsonBuilder.append("   {\n")
                    .append("      \"id\": ").append(a.id).append(",\n") // ID como número, no string
                    .append("      \"dni_cliente\": \"").append(a.dni_cliente).append("\",\n")
                    .append("      \"nombre\": \"").append(a.nombreAnimal).append("\",\n")
                    .append("      \"especie\": \"").append(a.especie).append("\",\n")
                    .append("      \"raza\": \"").append(a.raza).append("\",\n")
                    .append("      \"fnac\": \"").append(a.fnac).append("\"\n") // Fecha como string
                    .append("   }");
            if (i < lista.size() - 1) {
                jsonBuilder.append(",\n"); // Añadir coma si no es el último elemento
            } else {
                jsonBuilder.append("\n"); // Nueva línea para el último
            }
        }
        jsonBuilder.append("]\n"); // Cerrar el array JSON
        return jsonBuilder.toString();
    }

    /**
     * Genera un archivo XML con los datos de una lista de animales, con nombre predeterminado.
     * El archivo se llamará "animales.xml".
     * @param lista Lista de objetos Animal a exportar.
     * @return true si el archivo se generó con éxito, false en caso contrario.
     */
    public static boolean exportarAnimalesXML(List<Animal> lista) {
        String nombreArchivo = "animales.xml"; // Nombre de archivo predeterminado
        String xmlContent = animalesToXML(lista);
        try (FileWriter fileWriter = new FileWriter(nombreArchivo)) {
            fileWriter.write(xmlContent);
            System.out.println("Archivo XML '" + nombreArchivo + "' generado con éxito.");
            return true;
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo XML '" + nombreArchivo + "': " + e.getMessage());
            return false;
        }
    }

    /**
     * Genera un archivo JSON con los datos de una lista de animales, con nombre predeterminado.
     * El archivo se llamará "animales.json".
     * @param lista Lista de objetos Animal a exportar.
     * @return true si el archivo se generó con éxito, false en caso contrario.
     */
    public static boolean exportarAnimalesJSON(List<Animal> lista) {
        String nombreArchivo = "animales.json"; // Nombre de archivo predeterminado
        String jsonContent = animalesToJSON(lista);
        try (FileWriter fileWriter = new FileWriter(nombreArchivo)) {
            fileWriter.write(jsonContent);
            System.out.println("Archivo JSON '" + nombreArchivo + "' generado con éxito.");
            return true;
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo JSON '" + nombreArchivo + "': " + e.getMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        return """
                ────────────────────────────────
                -------Animal-------
                ID:             %d
                DNI Cliente:    %s
                Nombre:         %s
                Especie:        %s
                Raza:           %s
                F. Nacimiento:  %s
                
                """.formatted(id, dni_cliente, nombreAnimal, especie, raza, fnac);
    }

    /**
     * Menú principal para gestionar animales mediante consola.
     * @throws Exception Si ocurre algún error en la creación o manipulación de animales.
     */
    public static void mostrarMenu() throws Exception {
        AnimalDAO animalDAO = new AnimalDAO();
        ClienteDAO clienteDAO = new ClienteDAO(); // ← Añadido
        Scanner sc = new Scanner(System.in);
        int opc;

        do {
            System.out.println("""
                    ===== MENÚ ANIMALES =====
                    1. Mostrar datos de todos los animales
                    2. Mostrar datos de un animal por ID
                    3. Insertar animal
                    4. Actualizar datos de animal
                    5. Eliminar un animal
                    6. Numero total de animales
                    7. SALIR
                    """);

            System.out.print("Selecciona una opción: ");
            while (!sc.hasNextInt()) {
                System.out.println("Por favor, ingresa un número válido.");
                sc.next();
            }
            opc = sc.nextInt();

            switch (opc) {
                case 1 -> System.out.println(animalDAO.getAllAnimal());
                case 2 -> System.out.println(animalDAO.getAnimalById(pedirIdAnimal()));
                case 3 -> animalDAO.insertAnimal(crearAnimal(sc, clienteDAO)); // ← pasa DAO
                case 4 -> animalDAO.updateAnimal(crearAnimal(sc, clienteDAO));
                case 5 -> animalDAO.deleteAnimal(pedirIdAnimal());
                case 6 -> System.out.println(animalDAO.totalAnimales());
                case 7 -> System.out.println("Saliendo del menú de animales...");
                default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        } while (opc != 7);
    }

    private static int pedirIdAnimal() {
        Scanner sc = new Scanner(System.in);
        int id = -1;
        boolean valido;

        do {
            System.out.print("Introduce el ID del animal: ");
            valido = true;

            try {
                id = Integer.parseInt(sc.next());

            } catch (NumberFormatException e) {
                valido = false;
                System.out.println("ERROR: Formato de ID no válido. Debe ser un número entero.");
            }
        } while (valido == false);

        return id;
    }


    private static Animal crearAnimal(Scanner sc, ClienteDAO clienteDAO) throws Exception {
        System.out.println("Introduce el DNI del dueño del animal: ");
        String dni = sc.next();
        if (!dni.matches("\\d{8}[A-Z]")) {
            throw new IllegalArgumentException("DNI no válido.");
        }

        // Verificar existencia del cliente
        if (clienteDAO.getClienteByDNI(dni) == null) {
            System.out.println("No existe ningún cliente con ese DNI. ¿Deseas añadirlo? (s/n)");
            String respuesta = sc.next();
            if (respuesta.equalsIgnoreCase("s")) {
                Cliente nuevoCliente = crearCliente(sc, dni);
                clienteDAO.insertCliente(nuevoCliente);
                System.out.println("Cliente añadido con éxito.");
            } else {
                throw new IllegalStateException("No se puede continuar sin un cliente válido.");
            }
        }

        System.out.println("Introduce el nombre del animal: ");
        String nombre = sc.next();
        if (!nombre.matches(".{1,64}")) {
            throw new IllegalArgumentException("Nombre no válido.");
        }

        System.out.println("Introduce la especie del animal: ");
        String especie = sc.next();
        if (!especie.matches(".{1,64}")) {
            throw new IllegalArgumentException("Especie no válida.");
        }

        System.out.println("Introduce la raza del animal: ");
        String raza = sc.next();
        if (!raza.matches(".{1,64}")) {
            throw new IllegalArgumentException("Raza no válida.");
        }

        System.out.println("Introduce la fecha de nacimiento del animal (YYYY-MM-DD): ");
        String fechaInput = sc.next();
        if (!fechaInput.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Formato de fecha inválido.");
        }

        Date fnac = Date.valueOf(fechaInput);
        return new Animal(dni, nombre, especie, raza, fnac);
    }

    private static Cliente crearCliente(Scanner sc, String dni) {
        System.out.println("Introduce el nombre del cliente: ");
        String nombre = sc.next();

        System.out.println("Introduce el teléfono del cliente: ");
        String telefono = sc.next();

        return new Cliente(dni, nombre, telefono);
    }
}