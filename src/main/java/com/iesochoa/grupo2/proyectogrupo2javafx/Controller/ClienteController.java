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
import java.time.LocalDate; // Import LocalDate for modern date handling
import java.time.ZoneId; // For converting Date to LocalDate
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.format.DateTimeParseException; // For handling date parsing errors

public class ClienteController {

    @FXML
    private Button actualizarClienteButton;

    @FXML
    private Button actualizarMascotaButton;

    @FXML
    private Button buscarAnimalButton;

    @FXML
    private Button buscarClienteButton;

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
    private TextField edadMascotaTextField;

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
    private TextField searchAnimalTextfield;

    @FXML
    private TextField searchClienteTextField;

    @FXML
    private TextField telefonoTextField;

    @FXML
    private Button tratamientoButton;

    // --- DAO Instances ---
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final AnimalDAO animalDAO = new AnimalDAO(); // You'll need to modify AnimalDAO based on fixes

    // --- Observable Lists for Tables ---
    private ObservableList<Cliente> listaClientes;
    private ObservableList<Animal> listaAnimales;

    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     * It sets up table column cell value factories and loads initial data.
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
        colFnac.setCellValueFactory(new PropertyValueFactory<>("fnac")); // Assuming fnac in Animal model is java.util.Date

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
     * Loads all clients from the database and updates the clients table.
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
     * Loads all animals from the database and updates the animals table.
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
     * Handles selection in the clients table, populating client fields and loading associated animals.
     * @param clienteSeleccionado The selected Cliente object.
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
     * Handles selection in the animals table, populating animal fields.
     * @param animalSeleccionado The selected Animal object.
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
     * Loads animals associated with a specific client DNI.
     * This method assumes AnimalDAO has a getAllAnimalsByClientDni method.
     *
     * @param dniClient The DNI of the client whose animals are to be loaded.
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

    // --- Utility Methods ---
    private void clearClientFields() {
        nombreTextField.clear();
        dniTextField.clear();
        telefonoTextField.clear();
        clientesTable.getSelectionModel().clearSelection(); // Deselect client
    }

    private void clearAnimalFields() {
        dniClienteMascotaTextField.clear();
        nombreMascotaTextField.clear();
        razaMascotaTextField.clear();
        especieMascotaTextField.clear();
        edadMascotaTextField.clear(); // Clear DatePicker
        animalesTable.getSelectionModel().clearSelection(); // Deselect animal
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // --- Client CRUD Operations ---
    @FXML
    void onCrearCliente(ActionEvent event) {
        String nombre = nombreTextField.getText();
        String dni = dniTextField.getText();
        String telefono = telefonoTextField.getText();

        if (nombre.isEmpty() || dni.isEmpty() || telefono.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all client fields.");
            return;
        }

        Cliente newCliente = new Cliente(dni, nombre, telefono); // DNI, Name, Phone in constructor
        try {
            clienteDAO.insertCliente(newCliente);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Client created successfully!");
            clearClientFields();
            loadAllClientes(); // Refresh the table
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to create client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onActualizarCliente(ActionEvent event) {

    }

    @FXML
    void onEliminarCliente(ActionEvent event) {
        Cliente selectedCliente = clientesTable.getSelectionModel().getSelectedItem();
        if (selectedCliente == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a client to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete client " + selectedCliente.getNombreCliente() + "? This will also delete associated animals.",
                ButtonType.YES, ButtonType.NO);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.showAndWait();

        if (confirmationAlert.getResult() == ButtonType.YES) {
            try {
                // Ensure your deleteCliente in DAO handles cascading deletion of animals if appropriate
                // or add a separate call to delete animals first if not cascaded by DB
                clienteDAO.deleteCliente(selectedCliente.getDniCliente());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Client deleted successfully!");
                clearClientFields();
                loadAllClientes(); // Refresh client table
                loadAllAnimales(); // Refresh animal table as associated animals are likely gone
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete client: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // --- Animal CRUD Operations ---
    @FXML
    void onCrearMascota(ActionEvent event) {

    }

    @FXML
    void onActualizarMascota(ActionEvent event) {

    }

    @FXML
    void onEliminarMascota(ActionEvent event) {
        Animal selectedAnimal = animalesTable.getSelectionModel().getSelectedItem();
        if (selectedAnimal == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select an animal to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete animal " + selectedAnimal.getNombreAnimal() + " (ID: " + selectedAnimal.getId() + ")?",
                ButtonType.YES, ButtonType.NO);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.showAndWait();

        if (confirmationAlert.getResult() == ButtonType.YES) {
            try {
                animalDAO.deleteAnimal(selectedAnimal.getId()); // Assuming your deleteAnimal method takes an ID
                showAlert(Alert.AlertType.INFORMATION, "Success", "Animal deleted successfully!");
                clearAnimalFields();
                // Refresh animals for the currently selected client, or all animals if none selected
                Cliente selectedClient = clientesTable.getSelectionModel().getSelectedItem();
                if (selectedClient != null) {
                    loadAnimalsByClientDni(selectedClient.getDniCliente());
                } else {
                    loadAllAnimales();
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete animal: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // --- Animal Search Operation ---
    @FXML
    void onBuscarAnimal(ActionEvent event) {

    }

    // --- Navigation Buttons ---
    @FXML
    void onClickCentro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/grupo2/proyectogrupo2javafx/views/CentroView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Gestión del Centro"); // Set title for the new window
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load Centro view.");
            e.printStackTrace();
        }
    }

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
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load Citas view.");
            e.printStackTrace();
        }
    }

    @FXML
    void onClickClientes(ActionEvent event) {
        // Already on ClienteView, no need to reload
        // For demonstration purposes, if you wanted to force a refresh:
        // loadAllClientes();
        // loadAllAnimales();
        // But typically, if you're already there, do nothing or just ensure tables are up-to-date.
    }

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
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load Empleado view.");
            e.printStackTrace();
        }
    }

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
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load Historial view.");
            e.printStackTrace();
        }
    }

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
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load Tratamiento view.");
            e.printStackTrace();
        }
    }
}