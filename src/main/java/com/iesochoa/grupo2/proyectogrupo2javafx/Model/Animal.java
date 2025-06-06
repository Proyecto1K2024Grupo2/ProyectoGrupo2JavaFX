package com.iesochoa.grupo2.proyectogrupo2javafx.Model;

import com.iesochoa.grupo2.proyectogrupo2javafx.DB.AnimalDAO;
import com.iesochoa.grupo2.proyectogrupo2javafx.DB.ClienteDAO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Representa un animal asociado a un cliente en una base de datos veterinaria.
 * Incluye información como el nombre, especie, raza y fecha de nacimiento.
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
     * Convierte los datos del animal en formato XML.
     *
     * @return Representación en XML del animal.
     */
    public String animalToXML() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("      <id>").append(id).append("</id>")
                .append("      <dni_cliente>").append(dni_cliente).append("</dni_cliente>")
                .append("      <nombre>").append(nombreAnimal).append("</nombre>")
                .append("      <especie>").append(especie).append("</especie>")
                .append("      <raza>").append(raza).append("</raza>")
                .append("      <fnac>").append(fnac).append("</fnac>");
        return xmlBuilder.toString();
    }

    /**
     * Convierte los datos del animal en formato JSON.
     *
     * @return Representación en JSON del animal.
     */
    public String animalToJSON() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{")
                .append("\"id\": \"").append(id).append("\", ")
                .append("\"dni_cliente\": \"").append(dni_cliente).append("\", ")
                .append("\"nombre\": \"").append(nombreAnimal).append("\", ")
                .append("\"especie\": \"").append(especie).append("\", ")
                .append("\"raza\": \"").append(raza).append("\", ")
                .append("\"fnac\": \"").append(fnac).append("\"")
                .append("}");
        return jsonBuilder.toString();
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