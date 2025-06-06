package com.iesochoa.grupo2.proyectogrupo2javafx.Controller;

import com.iesochoa.grupo2.proyectogrupo2javafx.DB.AnimalDAO;
import com.iesochoa.grupo2.proyectogrupo2javafx.DB.ClienteDAO;
import com.iesochoa.grupo2.proyectogrupo2javafx.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter; // For parsing and formatting dates
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Controlador para la vista de gestión de clientes y animales en la aplicación.
 * Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * tanto para clientes como para sus animales asociados. También facilita la navegación
 * a otras secciones de la aplicación.
 */
public class ClienteController {

    @FXML
    private Button actualizarClienteButton;

    @FXML
    private Button actualizarMascotaButton;

    @FXML
    private Button buscarAnimalButton; // Renamed to onClickBuscarAnimal for consistency with methods

    @FXML
    private Button buscarClienteButton; // Renamed to onClickBuscarCliente for consistency with methods

    @FXML
    private Button centroButton;

    @FXML
    private Button citasButton;

    @FXML
    private Button clientesButton;
    @FXML
    private TableView<Animal> animalesTable;

    @FXML
    private TableView<Cliente> clientesTable;

    @FXML
    private TableColumn<Cliente, String> colDniCli1;

    @FXML
    private TableColumn<Animal, Integer> colIdAnimal;

    @FXML
    private TableColumn<Animal, String> colDniCli2;

    @FXML
    private TableColumn<Animal, String> colEspecie;

    @FXML
    private TableColumn<Animal, Date> colFnac;

    @FXML
    private TableColumn<Animal, String> colNombreAnimal;

    @FXML
    private TableColumn<Cliente, String> colNombreCli;

    @FXML
    private TableColumn<Animal, String> colRaza;

    @FXML
    private TableColumn<Cliente, String> colTelefono;

    @FXML
    private Button crearClienteButton;

    @FXML
    private Button crearMascotaButton;

    @FXML
    private TextField dniClienteMascotaTextField;

    @FXML
    private TextField dniTextField;

    @FXML
    private TextField edadMascotaTextField; // Retained as TextField for date input

    @FXML
    private Button eliminarClienteButton;

    @FXML
    private Button eliminarMascotaButton;

    @FXML
    private Button empleadosButton;

    @FXML
    private TextField especieMascotaTextField;

    @FXML
    private Button historialButton;

    @FXML
    private TextField nombreMascotaTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField razaMascotaTextField;

    @FXML
    private TextField searchAnimalTextfield; // Renamed from searchAnimal

    @FXML
    private TextField searchClienteTextField; // Renamed from searchClient

    @FXML
    private TextField telefonoTextField;

    @FXML
    private Button tratamientoButton;

    // --- DAO Instances ---
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final AnimalDAO animalDAO = new AnimalDAO();

    // --- Observable Lists for Tables ---
    private ObservableList<Cliente> listaClientes;
    private ObservableList<Animal> listaAnimales;

    // Date formatter for TextField date input (adjust format as needed, e.g., "dd/MM/yyyy")
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Inicializa el controlador. Este método es llamado automáticamente después de que
     * el archivo FXML ha sido cargado. Configura las factorías de valor para las celdas de la tabla
     * y carga los datos iniciales.
     */
    @FXML
    public void initialize() {
        // --- Initialize Cliente Table Columns (corrected PropertyValueFactory names) ---
        colDniCli1.setCellValueFactory(new PropertyValueFactory<>("dniCliente"));
        colNombreCli.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        // --- Initialize Animal Table Columns (corrected PropertyValueFactory names) ---
        colIdAnimal.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDniCli2.setCellValueFactory(new PropertyValueFactory<>("dni_cliente"));
        colNombreAnimal.setCellValueFactory(new PropertyValueFactory<>("nombreAnimal"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colRaza.setCellValueFactory(new PropertyValueFactory<>("raza"));
        // Using PropertyValueFactory for Date, assuming getFnac() in Animal returns java.util.Date
        colFnac.setCellValueFactory(new PropertyValueFactory<>("fnac"));

        // --- Set up Observable Lists and link to Tables ---
        listaClientes = FXCollections.observableArrayList();
        clientesTable.setItems(listaClientes);

        listaAnimales = FXCollections.observableArrayList();
        animalesTable.setItems(listaAnimales);

        // --- Load all data on startup ---
        loadAllClientes();
        loadAllAnimales();

        // --- Add listeners for table selection ---
        clientesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> handleClienteTableSelection(newValue));
        animalesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> handleAnimalTableSelection(newValue));
    }

    /**
     * Carga todos los clientes de la base de datos y actualiza la tabla de clientes.
     */
    private void loadAllClientes() {
        listaClientes.clear();
        try {
            List<Cliente> clientes = clienteDAO.getAllClientes();
            listaClientes.addAll(clientes);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load clients: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga todos los animales de la base de datos y actualiza la tabla de animales.
     */
    private void loadAllAnimales() {
        listaAnimales.clear();
        try {
            List<Animal> animals = animalDAO.getAllAnimal(); // Ensure this method returns List<Animal>
            listaAnimales.addAll(animals);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load animals: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Maneja la selección de una fila en la tabla de clientes,
     * rellenando los campos del cliente y cargando los animales asociados.
     *
     * @param clienteSeleccionado El objeto {@link Cliente} seleccionado.
     */
    private void handleClienteTableSelection(Cliente clienteSeleccionado) {
        if (clienteSeleccionado != null) {
            nombreTextField.setText(clienteSeleccionado.getNombreCliente());
            dniTextField.setText(clienteSeleccionado.getDniCliente());
            telefonoTextField.setText(clienteSeleccionado.getTelefono());

            // Load only the pets of that client
            loadAnimalsByClientDni(clienteSeleccionado.getDniCliente());
        } else {
            clearClientFields();
            listaAnimales.clear(); // Clear animals if no client is selected
        }
    }

    /**
     * Maneja la selección de una fila en la tabla de animales,
     * rellenando los campos del animal.
     *
     * @param animalSeleccionado El objeto {@link Animal} seleccionado.
     */
    private void handleAnimalTableSelection(Animal animalSeleccionado) {
        if (animalSeleccionado != null) {
            dniClienteMascotaTextField.setText(animalSeleccionado.getDni_cliente());
            nombreMascotaTextField.setText(animalSeleccionado.getNombreAnimal());
            razaMascotaTextField.setText(animalSeleccionado.getRaza());
            especieMascotaTextField.setText(animalSeleccionado.getEspecie());
            // For date, you might need to format it for a TextField or use a DatePicker
            // For simplicity, converting to String here.
            if (animalSeleccionado.getFnac() != null) {
                edadMascotaTextField.setText(animalSeleccionado.getFnac().toString());
            } else {
                edadMascotaTextField.clear();
            }
        } else {
            clearAnimalFields();
        }
    }

    /**
     * Carga los animales asociados a un DNI de cliente específico.
     * Este método asume que {@link AnimalDAO} tiene un método para obtener animales por DNI de cliente.
     *
     * @param dniClient El DNI del cliente cuyos animales se van a cargar.
     */
    private void loadAnimalsByClientDni(String dniClient) {
        listaAnimales.clear();
        if (dniClient == null || dniClient.trim().isEmpty()) {
            return; // No DNI provided, no animals to load
        }
        try {
            // IMPORTANT: You need to implement this method in your AnimalDAO:
            // public List<Animal> getAllAnimalsByClientDni(String dniCliente) throws SQLException;
            List<Animal> animals = List.of(animalDAO.getAnimalByDni(dniClient));
            listaAnimales.addAll(animals);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load animals for client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Métodos de Utilidad ---

    /**
     * Limpia todos los campos de texto relacionados con el cliente y deselecciona cualquier cliente en la tabla.
     */
    private void clearClientFields() {
        nombreTextField.clear();
        dniTextField.clear();
        telefonoTextField.clear();
        clientesTable.getSelectionModel().clearSelection(); // Deselect client
    }

    /**
     * Limpia todos los campos de texto relacionados con el animal y deselecciona cualquier animal en la tabla.
     */
    private void clearAnimalFields() {
        dniClienteMascotaTextField.clear();
        nombreMascotaTextField.clear();
        razaMascotaTextField.clear();
        especieMascotaTextField.clear();
        edadMascotaTextField.clear();
        animalesTable.getSelectionModel().clearSelection(); // Deselect animal
    }

    /**
     * Muestra una ventana de alerta al usuario.
     *
     * @param type    El tipo de alerta (INFO, WARNING, ERROR, CONFIRMATION).
     * @param title   El título de la ventana de alerta.
     * @param message El mensaje a mostrar en la alerta.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // --- Operaciones CRUD de Cliente ---

    /**
     * Maneja el evento de clic para crear un nuevo cliente.
     * Recopila los datos de los campos de texto, valida la entrada e inserta
     * el nuevo cliente en la base de datos.
     * Muestra alertas de éxito o error.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onCrearCliente(ActionEvent event) {
        String nombre = nombreTextField.getText();
        String dni = dniTextField.getText();
        String telefono = telefonoTextField.getText();

        if (nombre.isEmpty() || dni.isEmpty() || telefono.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error de Entrada", "Por favor, complete todos los campos del cliente.");
            return;
        }

        Cliente newCliente = new Cliente(dni, nombre, telefono);
        try {
            clienteDAO.insertCliente(newCliente);
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente creado exitosamente.");
            clearClientFields();
            loadAllClientes(); // Refresh the table
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Base de Datos", "Fallo al crear cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de clic para actualizar un cliente existente.
     * Obtiene el cliente seleccionado de la tabla, actualiza sus datos
     * con los valores de los campos de texto y persiste los cambios en la base de datos.
     * Muestra alertas de éxito o error.
     *
     * @param event El evento de acción.
     * @throws SQLException Si ocurre un error de SQL durante la actualización.
     */
    @FXML
    void onActualizarCliente(ActionEvent event) throws SQLException {
        Cliente selectedCliente = clientesTable.getSelectionModel().getSelectedItem();
        if (selectedCliente == null) {
            showAlert(Alert.AlertType.WARNING, "Error de Selección", "Por favor, seleccione un cliente para actualizar.");
            return;
        }

        String nombre = nombreTextField.getText();
        String dni = dniTextField.getText(); // DNI is usually the primary key, handle changes carefully
        String telefono = telefonoTextField.getText();

        if (nombre.isEmpty() || dni.isEmpty() || telefono.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error de Entrada", "Por favor, complete todos los campos para actualizar el cliente.");
            return;
        }

        // Actualiza el objeto cliente seleccionado con los nuevos valores
        selectedCliente.setNombreCliente(nombre);
        selectedCliente.setDniCliente(dni); // Importante si el DNI puede ser actualizado
        selectedCliente.setTelefono(telefono);

        // Debes implementar updateCliente(Cliente cliente) en ClienteDAO
        clienteDAO.updateCliente(selectedCliente);
        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente actualizado exitosamente.");
        clearClientFields();
        loadAllClientes(); // Refresca la tabla de clientes
        loadAllAnimales(); // Refresca la tabla de animales por si el cambio de DNI afecta las asociaciones
    }

    /**
     * Maneja el evento de clic para eliminar un cliente.
     * Solicita confirmación al usuario y, si se confirma, elimina el cliente
     * (y sus animales asociados si la base de datos tiene una cascada de eliminación)
     * de la base de datos.
     * Muestra alertas de éxito o error.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onEliminarCliente(ActionEvent event) {
        Cliente selectedCliente = clientesTable.getSelectionModel().getSelectedItem();
        if (selectedCliente == null) {
            showAlert(Alert.AlertType.WARNING, "Error de Selección", "Por favor, seleccione un cliente para eliminar.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Está seguro de que desea eliminar al cliente " + selectedCliente.getNombreCliente() + "? Esto también eliminará los animales asociados.",
                ButtonType.YES, ButtonType.NO);
        confirmationAlert.setTitle("Confirmar Eliminación");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.showAndWait();

        if (confirmationAlert.getResult() == ButtonType.YES) {
            try {
                // Asegúrate de que tu método deleteCliente en DAO maneje la eliminación en cascada de animales si es apropiado,
                // o agrega una llamada separada para eliminar animales primero si la base de datos no lo hace en cascada.
                clienteDAO.deleteCliente(selectedCliente.getDniCliente());
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente eliminado exitosamente.");
                clearClientFields();
                loadAllClientes(); // Refresca la tabla de clientes
                loadAllAnimales(); // Refresca la tabla de animales ya que los asociados probablemente se eliminaron
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error de Base de Datos", "Fallo al eliminar cliente: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // --- Operaciones CRUD de Animal ---

    /**
     * Maneja el evento de clic para crear una nueva mascota.
     * Recopila los datos de los campos de texto, valida la entrada (incluido el formato de fecha)
     * e inserta la nueva mascota en la base de datos.
     * Muestra alertas de éxito o error.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onCrearMascota(ActionEvent event) {
        String dniCliente = dniClienteMascotaTextField.getText();
        String nombre = nombreMascotaTextField.getText();
        String especie = especieMascotaTextField.getText();
        String raza = razaMascotaTextField.getText();
        String fechaNacStr = edadMascotaTextField.getText(); // Usando TextField para la fecha

        if (dniCliente.isEmpty() || nombre.isEmpty() || especie.isEmpty() || raza.isEmpty() || fechaNacStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error de Entrada", "Por favor, complete todos los campos del animal.");
            return;
        }

        Date fnac = null;
        try {
            LocalDate localDate = LocalDate.parse(fechaNacStr, DATE_FORMATTER);
            fnac = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Formato de Fecha", "Formato de fecha de nacimiento inválido. Use YYYY-MM-DD.");
            return;
        }

        Animal newAnimal = new Animal(dniCliente, nombre, especie, raza, (java.sql.Date) fnac);
        try {
            animalDAO.insertAnimal(newAnimal);
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Animal creado exitosamente.");
            clearAnimalFields();
            Cliente selectedClient = clientesTable.getSelectionModel().getSelectedItem();
            if (selectedClient != null) {
                loadAnimalsByClientDni(selectedClient.getDniCliente());
            } else {
                loadAllAnimales(); // Si no hay cliente seleccionado, refresca todos los animales
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Base de Datos", "Fallo al crear animal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de clic para actualizar una mascota existente.
     * Obtiene el animal seleccionado de la tabla, actualiza sus datos
     * con los valores de los campos de texto y persiste los cambios en la base de datos.
     * Muestra alertas de éxito o error.
     * @param event El evento de acción.
     */
    @FXML
    void onActualizarMascota(ActionEvent event) {
        Animal selectedAnimal = animalesTable.getSelectionModel().getSelectedItem();
        if (selectedAnimal == null) {
            showAlert(Alert.AlertType.WARNING, "Error de Selección", "Por favor, seleccione un animal para actualizar.");
            return;
        }

        String dniCliente = dniClienteMascotaTextField.getText();
        String nombre = nombreMascotaTextField.getText();
        String especie = especieMascotaTextField.getText();
        String raza = razaMascotaTextField.getText();
        String fechaNacStr = edadMascotaTextField.getText(); // Using TextField for date

        if (dniCliente.isEmpty() || nombre.isEmpty() || especie.isEmpty() || raza.isEmpty() || fechaNacStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error de Entrada", "Por favor, complete todos los campos del animal para actualizar.");
            return;
        }

        Date fnac = null;
        try {
            LocalDate localDate = LocalDate.parse(fechaNacStr, DATE_FORMATTER);
            fnac = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Formato de Fecha", "Formato de fecha de nacimiento inválido. Use YYYY-MM-DD.");
            return;
        }

        // Update the selected animal object with new values
        selectedAnimal.setDni_cliente(dniCliente);
        selectedAnimal.setNombreAnimal(nombre);
        selectedAnimal.setEspecie(especie);
        selectedAnimal.setRaza(raza);
        selectedAnimal.setFnac(fnac); // Ensure setFnac in Animal model accepts java.util.Date

        // You MUST implement updateAnimal(Animal animal) in AnimalDAO
        animalDAO.updateAnimal(selectedAnimal);
        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Animal actualizado exitosamente.");
        clearAnimalFields();
        // Refresh animals for the currently selected client, or all animals if none selected
        Cliente selectedClient = clientesTable.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            loadAnimalsByClientDni(selectedClient.getDniCliente());
        } else {
            loadAllAnimales();
        }
    }

    /**
     * Maneja el evento de clic para eliminar una mascota.
     * Solicita confirmación al usuario y, si se confirma, elimina el animal
     * de la base de datos.
     * Muestra alertas de éxito o error.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onEliminarMascota(ActionEvent event) {
        Animal selectedAnimal = animalesTable.getSelectionModel().getSelectedItem();
        if (selectedAnimal == null) {
            showAlert(Alert.AlertType.WARNING, "Error de Selección", "Por favor, seleccione un animal para eliminar.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Está seguro de que desea eliminar al animal " + selectedAnimal.getNombreAnimal() + " (ID: " + selectedAnimal.getId() + ")?",
                ButtonType.YES, ButtonType.NO);
        confirmationAlert.setTitle("Confirmar Eliminación");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.showAndWait();

        if (confirmationAlert.getResult() == ButtonType.YES) {
            try {
                // You MUST implement deleteAnimal(int id) in AnimalDAO
                animalDAO.deleteAnimal(selectedAnimal.getId());
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Animal eliminado exitosamente.");
                clearAnimalFields();
                // Refresh animals for the currently selected client, or all animals if none selected
                Cliente selectedClient = clientesTable.getSelectionModel().getSelectedItem();
                if (selectedClient != null) {
                    loadAnimalsByClientDni(selectedClient.getDniCliente());
                } else {
                    loadAllAnimales();
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error de Base de Datos", "Fallo al eliminar animal: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // --- Search Operations ---
    /**
     * Maneja el evento de clic para buscar un animal.
     * Realiza una búsqueda de animales por DNI de cliente o nombre de animal
     * y actualiza la tabla de animales con los resultados.
     * Si el campo de búsqueda está vacío, carga todos los animales.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickBuscarAnimal(ActionEvent event) {
        String dniONombreAnimalBusqueda = searchAnimalTextfield.getText();

        listaAnimales.clear(); // Always clear the list before a new search

        if (dniONombreAnimalBusqueda == null || dniONombreAnimalBusqueda.trim().isEmpty()) {
            loadAllAnimales(); // If the field is empty, load all
            return;
        }

        try {
            // You MUST implement searchAnimals(String query) in AnimalDAO
            List<Animal> foundAnimals = Collections.singletonList(animalDAO.getAnimalByDni(dniONombreAnimalBusqueda));

            if (foundAnimals != null && !foundAnimals.isEmpty()) {
                listaAnimales.addAll(foundAnimals);
                showAlert(Alert.AlertType.INFORMATION, "Búsqueda Exitosa", "Se encontraron animales.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Encontrado", "No se encontró ningún animal con DNI de cliente o nombre: " + dniONombreAnimalBusqueda);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Base de Datos", "Error al buscar animales: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de clic para buscar un cliente.
     * Realiza una búsqueda de clientes por DNI y actualiza la tabla de clientes con el resultado.
     * Si el campo de búsqueda está vacío, carga todos los clientes.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickBuscarCliente(ActionEvent event) {
        String dniBusqueda = searchClienteTextField.getText();
        listaClientes.clear();

        if (dniBusqueda == null || dniBusqueda.trim().isEmpty()) {
            loadAllClientes();
            return;
        }

        boolean found = false;
        try {
            Cliente cliente = clienteDAO.getClienteByDNI(dniBusqueda);
            if (cliente != null) {
                listaClientes.add(cliente);
                found = true;
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar empleado por DNI: " + e.getMessage());
        }
    }

    // --- Navigation Buttons ---
    /**
     * Navega a la vista de "Centro".
     *
     * @param event El evento de acción que disparó la navegación.
     */
    @FXML
    void onClickCentro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/grupo2/proyectogrupo2javafx/views/CentroView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Gestión del Centro");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Navegación", "No se pudo cargar la vista del Centro.");
            e.printStackTrace();
        }
    }

    /**
     * Navega a la vista de "Citas".
     *
     * @param event El evento de acción que disparó la navegación.
     */
    @FXML
    void onClickCitas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/grupo2/proyectogrupo2javafx/views/CitasView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Gestión de Citas");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Navegación", "No se pudo cargar la vista de Citas.");
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de clic para el botón "Clientes".
     * Dado que esta es la vista de clientes, este método no realiza ninguna acción
     * de navegación explícita, pero podría usarse para refrescar la vista si fuera necesario.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickClientes(ActionEvent event) {
        // Already on ClienteView, no need to reload unless explicitly desired for refresh
    }

    /**
     * Navega a la vista de "Empleados".
     *
     * @param event El evento de acción que disparó la navegación.
     */
    @FXML
    void onClickEmpleados(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/grupo2/proyectogrupo2javafx/views/EmpleadoView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Gestión de Empleados");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Navegación", "No se pudo cargar la vista de Empleados.");
            e.printStackTrace();
        }
    }

    /**
     * Navega a la vista de "Historial Clínico".
     *
     * @param event El evento de acción que disparó la navegación.
     */
    @FXML
    void onClickHistorial(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/grupo2/proyectogrupo2javafx/views/HistorialView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Historial Clínico");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Navegación", "No se pudo cargar la vista de Historial.");
            e.printStackTrace();
        }
    }

    /**
     * Navega a la vista de "Tratamientos".
     *
     * @param event El evento de acción que disparó la navegación.
     */
    @FXML
    void onClickTratamiento(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/grupo2/proyectogrupo2javafx/views/TratamientoView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Gestión de Tratamientos");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Navegación", "No se pudo cargar la vista de Tratamientos.");
            e.printStackTrace();
        }
    }
}