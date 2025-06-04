package com.iesochoa.grupo2.proyectogrupo2javafx.Controller;

import com.iesochoa.grupo2.proyectogrupo2javafx.DB.CirujanoDAO;
import com.iesochoa.grupo2.proyectogrupo2javafx.DB.CuidadorDAO;
import com.iesochoa.grupo2.proyectogrupo2javafx.DB.RecepcionistaDAO;
import com.iesochoa.grupo2.proyectogrupo2javafx.DB.VeterinarioDAO;
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
import java.util.List;

public class EmpleadosController {

    @FXML
    private Button actualizarButton;

    @FXML
    private Button buscarButton;

    @FXML
    private Button centroButton;

    @FXML
    private Button citasButton;

    @FXML
    private Button clientesButton;

    @FXML
    private TableColumn<Empleado, String> colDni;

    @FXML
    private TableColumn<Empleado, String> colNombre;

    @FXML
    private TableColumn<Empleado, String> colNumCuenta;

    @FXML
    private TableColumn<Empleado, Double> colSueldo; // Sueldo es Double

    @FXML
    private TableColumn<Empleado, String> colTelefono; // Si es un String, se mantiene String

    @FXML
    private TableView<Empleado> empleadosTable;

    @FXML
    private Button crearButton;

    @FXML
    private TextField dniTextField;

    @FXML
    private Button eliminarButton;

    @FXML
    private Button empleadosButton;

    @FXML
    private TextArea errorTextArea;

    @FXML
    private Button historialButton;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField numeroCuentaTextField;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField sueldoTextField;

    @FXML
    private TextField telefonoTextField;

    @FXML
    private ComboBox<String> tipoEmpleadoComboBox;

    @FXML
    private Button tratamientoButton;

    private final CirujanoDAO cirujanoDAO = new CirujanoDAO();
    private final VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
    private final RecepcionistaDAO recepcionistaDAO = new RecepcionistaDAO();
    private final CuidadorDAO cuidadorDAO = new CuidadorDAO();

    // Lista observable para la tabla
    private ObservableList<Empleado> listaEmpleados;

    /**
     * Método que se ejecuta al inicializar el controlador.
     * Configura las columnas de la tabla y carga los datos iniciales.
     */
    @FXML
    public void initialize() {
        // Inicializa las columnas de la tabla mapeando a las propiedades de la clase Empleado
        colDni.setCellValueFactory(new PropertyValueFactory<>("dniEmpleado"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colNumCuenta.setCellValueFactory(new PropertyValueFactory<>("numCuenta"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));

        // Inicializa la lista observable y la asigna a la tabla
        listaEmpleados = FXCollections.observableArrayList();
        empleadosTable.setItems(listaEmpleados);

        // Carga todos los empleados al iniciar la vista
        loadAllEmpleados();
    }

    /**
     * Carga todos los empleados de todos los tipos y los muestra en la tabla.
     */
    private void loadAllEmpleados() {
        listaEmpleados.clear(); // Limpia la lista antes de cargar nuevos datos
        try {
            // Llama a los métodos DAO para obtener todos los empleados de cada tipo
            List<Cirujano> cirujanos = cirujanoDAO.getAllCirujanos();
            List<Veterinario> veterinarios = veterinarioDAO.getAllVeterinarios();
            List<Recepcionista> recepcionistas = recepcionistaDAO.getAllRecepcionista();
            List<Cuidador> cuidadores = cuidadorDAO.getAllCuidadores();

            // Añade todos los empleados a la lista observable.
            // Es seguro añadir objetos de subclases (Cirujano, etc.) a una lista de la superclase (Empleado).
            listaEmpleados.addAll(cirujanos);
            listaEmpleados.addAll(veterinarios);
            listaEmpleados.addAll(recepcionistas);
            listaEmpleados.addAll(cuidadores);

            errorTextArea.setText(""); // Limpia cualquier mensaje de error anterior
        } catch (SQLException e) {
            System.err.println("Error al cargar todos los empleados: " + e.getMessage());
            errorTextArea.setText("Error al cargar los empleados: " + e.getMessage());
        }
    }



    @FXML
    void onClickActualizarEmpleado(ActionEvent event) {

    }

    @FXML
    void onClickBuscar(ActionEvent event) {
        String dniBusqueda = searchTextField.getText();
        listaEmpleados.clear(); // Siempre limpia la lista antes de una búsqueda

        if (dniBusqueda == null || dniBusqueda.trim().isEmpty()) {
            loadAllEmpleados(); // Si el campo está vacío, carga todos
            return;
        }

        boolean found = false;
        // Intenta buscar en cada tipo de empleado
        try {
            Cirujano cirujano = cirujanoDAO.getCirujanoByDni(dniBusqueda);
            if (cirujano != null) {
                listaEmpleados.add(cirujano);
                found = true;
            }
            Veterinario veterinario = veterinarioDAO.getVeterinarioByDni(dniBusqueda);
            if (veterinario != null) {
                listaEmpleados.add(veterinario);
                found = true;
            }
            Recepcionista recepcionista = recepcionistaDAO.getRecepcionistaByDni(dniBusqueda);
            if (recepcionista != null) {
                listaEmpleados.add(recepcionista);
                found = true;
            }
            Cuidador cuidador = cuidadorDAO.getCuidadorByDni(dniBusqueda);
            if (cuidador != null) {
                listaEmpleados.add(cuidador);
                found = true;
            }

            if (!found) {
                errorTextArea.setText("No se encontró ningún empleado con el DNI: " + dniBusqueda);
            } else {
                errorTextArea.setText("");
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar empleado por DNI: " + e.getMessage());
            errorTextArea.setText("Error al buscar empleado: " + e.getMessage());
        }
    }

    /**
     * Maneja la acción de buscar empleado (asociado a onAction del TextField).
     * Simplemente llama a onClickBuscar.
     */
    @FXML
    void onClickBuscarEmpleado(ActionEvent event) {
        onClickBuscar(event); // Reutiliza el método de búsqueda principal
    }

    /**
     * Maneja el evento de clic en una fila de la tabla.
     * Muestra los datos del empleado seleccionado en los campos de texto del panel "Info Empleado".
     */
    @FXML
    private void handleTableSelection() {
        Empleado selectedEmpleado = empleadosTable.getSelectionModel().getSelectedItem();
        if (selectedEmpleado != null) {
            dniTextField.setText(selectedEmpleado.getDniEmpleado());
            nombreTextField.setText(selectedEmpleado.getNombreEmpleado());
            telefonoTextField.setText(String.valueOf(selectedEmpleado.getTelefono()));
            numeroCuentaTextField.setText(selectedEmpleado.getNumCuenta());
            sueldoTextField.setText(String.valueOf(selectedEmpleado.getSueldo()));

            // Determinar el tipo de empleado seleccionado y establecer el ComboBox
            if (selectedEmpleado instanceof Cirujano) {
                tipoEmpleadoComboBox.getSelectionModel().select("Cirujano");
            } else if (selectedEmpleado instanceof Veterinario) {
                tipoEmpleadoComboBox.getSelectionModel().select("Veterinario");
            } else if (selectedEmpleado instanceof Cuidador) {
                tipoEmpleadoComboBox.getSelectionModel().select("Cuidador");
            } else if (selectedEmpleado instanceof Recepcionista) {
                tipoEmpleadoComboBox.getSelectionModel().select("Recepcionista");
            } else {
                tipoEmpleadoComboBox.getSelectionModel().clearSelection();
            }
        }
    }


    @FXML
    void onClickCentro(ActionEvent event) {

    }

    @FXML
    void onClickCitas(ActionEvent event) {

    }

    @FXML
    void onClickEmpleados(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/grupo2/proyectogrupo2javafx/views/EmpleadoView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClickClientes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/grupo2/proyectogrupo2javafx/views/ClienteView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClickCrearEmpleado(ActionEvent event) throws SQLException {
        try {
            String nombre = nombreTextField.getText();
            String telefonoStr = telefonoTextField.getText();
            String dni = dniTextField.getText();
            String cuenta = numeroCuentaTextField.getText();
            String sueldoStr = sueldoTextField.getText();
            String tipoEmpleado = tipoEmpleadoComboBox.getValue();
            if (tipoEmpleado == null) throw new IllegalArgumentException("⚠️ Selecciona un tipo de empleado.");

            int telefono = Integer.parseInt(telefonoStr);
            double sueldo = Double.parseDouble(sueldoStr);

            switch (tipoEmpleado) {
                case "Cirujano" -> cirujanoDAO.insertEmpleado(new Cirujano(dni, nombre, telefono, cuenta, sueldo));
                case "Veterinario" -> veterinarioDAO.insertEmpleado(new Veterinario(dni, nombre, telefono, cuenta, sueldo));
                case "Cuidador" -> cuidadorDAO.insertEmpleado(new Cuidador(dni, nombre, telefono, cuenta, sueldo));
                case "Recepcionista" -> recepcionistaDAO.insertEmpleado(new Recepcionista(dni, nombre, telefono, cuenta, sueldo));
                default -> throw new IllegalArgumentException("⚠️ Tipo de empleado no válido.");
            }
            errorTextArea.setText("✅ Empleado creado correctamente.");
        } catch (IllegalArgumentException e) {
            errorTextArea.setText(e.getMessage());
        }
    }


    @FXML
    void onClickEliminarEmpleado(ActionEvent event) {
        try {
            String dni = dniTextField.getText();
            String tipoEmpleado = tipoEmpleadoComboBox.getValue();
            if (tipoEmpleado == null) throw new IllegalArgumentException("⚠️ Selecciona un tipo de empleado.");

            switch (tipoEmpleado) {
                case "Cirujano" -> cirujanoDAO.deleteEmpleadoByDni(dni);
                case "Veterinario" -> veterinarioDAO.deleteEmpleadoByDni(dni);
                case "Cuidador" -> cuidadorDAO.deleteEmpleadoByDni(dni);
                case "Recepcionista" -> recepcionistaDAO.deleteEmpleadoByDni(dni);
                default -> throw new IllegalArgumentException("⚠️ Tipo de empleado no válido.");
            }
            errorTextArea.setText("✅ Empleado eliminado correctamente.");
        } catch (IllegalArgumentException | SQLException e) {
            errorTextArea.setText(e.getMessage());
        }
    }



    @FXML
    void onClickHistorial(ActionEvent event) {

    }

    @FXML
    void onClickTratamiento(ActionEvent event) {

    }

}