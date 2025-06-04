package com.iesochoa.grupo2.proyectogrupo2javafx.DB;

import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Animal;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase AnimalDAO - Maneja las operaciones de base de datos para la tabla Animal.
 * Implementa métodos CRUD (Crear, Leer, Actualizar, Eliminar) para gestionar los Animales.
 * Implementa el patrón Singleton para gestionar la conexión a la base de datos.
 *
 * @version 01-2025
 * @author Raúl Quílez Ruiz
 */
public class AnimalDAO {
    // Creamos la instancia de AnimalDAO
    private static AnimalDAO instance;
    // Creamos la conexión con la base de datos
    private Connection connection;


    /**
     *  Sentencias básicas para la tabla Animal
     */

    // Sentencia SQL para crear la tabla en caso de que no exista
    public static final String CREATE_TABLE_ANIMAL = """
                    CREATE TABLE if not exists animal(
                        id INT UNSIGNED AUTO_INCREMENT,
                        dni_cliente VARCHAR(9),
                        nombre VARCHAR(64),
                        especie VARCHAR(32),
                        raza VARCHAR(32),
                        fnac DATE,
                        CONSTRAINT fk_dniCliente FOREIGN KEY (dni_cliente) REFERENCES cliente(dni)
                            ON UPDATE CASCADE
                            ON DELETE NO ACTION,
                        PRIMARY KEY(id)
                    );
            """;

    private static final String INSERT_ANIMAL = "INSERT INTO animal (dni_cliente, nombre, especie, raza, fnac) VALUES(?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_ANIMAL = "SELECT * FROM animal";
    private static final String SELECT_ANIMAL_BY_ID = "SELECT * FROM animal WHERE id=?";
    private static final String SELECT_ANIMAL_BY_CLIENTE_DNI = "SELECT * FROM animal WHERE dni_cliente=?";
    private static final String UPDATE_ANIMAL = "UPDATE animal SET dni_cliente=?, nombre=?, especie=?, raza=?, fnac=? WHERE id=?";
    private static final String DELETE_ANIMAL = "DELETE FROM animal WHERE id=?";
    private static final String TOTAL_ANIMAL = "SELECT COUNT(id) FROM animal";

    /**
     * Constructor privado para evitar instancición interna
     * Obtine conexión con la base de datos
     */
    public AnimalDAO(){
        this.connection=DBConnection.getConnection();
    }

    /**
     * Método estático para obtener la instancia de AnimalDAO
     * @return la única instancia de AnimalDAO
     */
    public static synchronized AnimalDAO getInstance() {
        if (instance==null) instance=new AnimalDAO();
        return instance;
    }

    /**
     * Inserta un animal en la base de datos
     * @param animal Animal que se inserta
     * @throws SQLException Por si ocurre un error con la base de datos
     */
    public void insertAnimal(Animal animal) throws SQLException {
        ClienteDAO clienteDAO = new ClienteDAO();

        // Verificar si el DNI del cliente existe

        if (clienteDAO.getClienteByDNI(animal.getDni_cliente()) == null) {
            throw new IllegalArgumentException("El DNI del cliente no existe.");
        }

        // Validación de nombre, especie, raza
        if (animal.getNombreAnimal().isEmpty() || animal.getEspecie().isEmpty()) {
            throw new IllegalArgumentException("Los campos de nombre y especie raza no pueden estar vacíos.");
        }

        // Validar que la fecha de nacimiento no sea posterior a la fecha actual
        if (animal.getFnac().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser posterior a la fecha actual.");
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_ANIMAL)) {
            statement.setString(1, animal.getDni_cliente());
            statement.setString(2, animal.getNombreAnimal());
            statement.setString(3, animal.getEspecie());
            statement.setString(4, animal.getRaza());
            statement.setDate(5, Date.valueOf(animal.getFnac()));
            statement.executeUpdate();
        }
    }


    /**
     * Obtiene los datos de todos los animales
     * @return Lista de objetos Animal
     * @throws SQLException Por si ocurre un error con la base de datos
     */
    public List<Animal> getAllAnimal() throws SQLException {
        List<Animal> animales = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_ANIMAL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) animales.add(resultSetToAnimal(resultSet));
        }
        return animales;
    }

    /**
     * Método que obtiene un Animal a partir del DNI
     * @param id ID del animal que se quiere buscar
     * @return El objeto Animal al que le corresponde ese ID
     * @throws SQLException Por si ocurre un error con la base de datos
     */
    public Animal getAnimalById(int id) throws SQLException {
        Animal animal = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ANIMAL_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                animal = resultSetToAnimal(resultSet);
            }
        }
        return animal;
    }

    public Animal getAnimalByDni(String dniCliente) throws SQLException {
        Animal animal = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ANIMAL_BY_CLIENTE_DNI)) {
            statement.setString(1, dniCliente);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                animal = resultSetToAnimal(resultSet);
            }
        }
        return animal;
    }

    /**
     * Metodo que actualiza los datos de un animal por su ID
     * @param animal Objeto con los datos actualizados
     * @throws SQLException Por si ocurre un error con la base de datos
     */
    public void updateAnimal(Animal animal) {
        try {
            // Verificar si el animal existe
            Animal existingAnimal = getAnimalById(animal.getId());
            if (existingAnimal == null) {
                System.out.println("Animal con ID " + animal.getId() + " no encontrado.");
                return;
            }

            // Proceder a actualizar el animal en la base de datos
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_ANIMAL))  {
                statement.setString(1, animal.getDni_cliente());
                statement.setString(2, animal.getNombreAnimal());
                statement.setString(3, animal.getEspecie());
                statement.setString(4, animal.getRaza());
                statement.setDate(5, Date.valueOf(animal.getFnac()));
                statement.setInt(6, animal.getId());
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Animal actualizado correctamente.");
                } else {
                    System.out.println("No se pudo actualizar el animal.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el animal: " + e.getMessage());
        }
    }



    /**
     * Metodo que elimina un animal por el ID
     * @param id ID del animal que se quiere eliminar
     * @throws SQLException Por si ocurre un error con la base de datos
     */
    public void deleteAnimal(int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_ANIMAL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }


    /**
     * Método que convierte que traduce un ResultSet a un objeto Animal
     * @param resultSet Resultado de la consulta a traducir
     * @return Objeto Animal con los datos traducidos
     * @throws SQLException Por si ocurre un error con la base de datos
     */
    private Animal resultSetToAnimal(ResultSet resultSet) throws SQLException {
        return new Animal(
                resultSet.getInt("id"),
                resultSet.getString("dni_cliente"),
                resultSet.getString("nombre"),
                resultSet.getString("especie"),
                resultSet.getString("raza"),
                resultSet.getDate("fnac")
        );
    }

    /**
     * Método que devuelve el número del total de animales
     * @return El número total de animales
     * @throws SQLException Por si ocurre un error con la base de datos
     */
    public int totalAnimales() throws SQLException {
        int total=0;
        try (PreparedStatement statement = connection.prepareStatement(TOTAL_ANIMAL)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) total = resultSet.getInt(1);
        }
        return total;
    }
}
