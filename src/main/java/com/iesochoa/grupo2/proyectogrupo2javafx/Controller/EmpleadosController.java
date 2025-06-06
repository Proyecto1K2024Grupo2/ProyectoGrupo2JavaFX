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

/**
 * Controlador para la vista de gestión de empleados en la aplicación.
 * Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los diferentes tipos de empleados
 * (Cirujano, Veterinario, Recepcionista, Cuidador) y navegar a otras secciones de la aplicación.
 * {@link Empleado} {@link com.iesochoa.grupo2.proyectogrupo2javafx.DB.EmpleadoDAO}
 * {@link Cirujano} {@link com.iesochoa.grupo2.proyectogrupo2javafx.DB.CirujanoDAO}
 * {@link Cuidador} {@link com.iesochoa.grupo2.proyectogrupo2javafx.DB.CuidadorDAO}
 * {@link Recepcionista} {@link com.iesochoa.grupo2.proyectogrupo2javafx.DB.RecepcionistaDAO}
 * {@link Veterinario} {@link com.iesochoa.grupo2.proyectogrupo2javafx.DB.VeterinarioDAO}
 */
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

    /**
     * Lista observable para la tabla de empleados.
     */
    private ObservableList<Empleado> listaEmpleados;

    /**
     * Método que se ejecuta al inicializar el controlador.
     * Configura las columnas de la tabla y carga los datos iniciales de todos los empleados.
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

        // Agrega un listener para manejar la selección de filas en la tabla
        empleadosTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> handleClienteTableSelection(newValue));
    }

    /**
     * Carga todos los empleados de todos los tipos desde la base de datos y los muestra en la tabla.
     * Limpia la lista observable antes de cargar nuevos datos.
     * Muestra mensajes de error en {@code errorTextArea} si ocurre una excepción SQL.
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

    /**
     * Maneja la selección de una fila en la tabla de empleados.
     * Rellena los campos de texto del formulario con los datos del empleado seleccionado.
     * Si no hay empleado seleccionado, los campos se limpian.
     * @param empleadoSeleccionado El objeto {@link Empleado} seleccionado en la tabla.
     */
    private void handleClienteTableSelection(Empleado empleadoSeleccionado) {
        if (empleadoSeleccionado != null) {
            nombreTextField.setText(empleadoSeleccionado.getNombreEmpleado());
            dniTextField.setText(empleadoSeleccionado.getDniEmpleado());
            telefonoTextField.setText(String.valueOf(empleadoSeleccionado.getTelefono()));
            numeroCuentaTextField.setText(empleadoSeleccionado.getNumCuenta());
            sueldoTextField.setText(String.valueOf(empleadoSeleccionado.getSueldo()));
        } else {
            clearClientFields();
            listaEmpleados.clear(); // Clear animals if no client is selected
        }
    }


    /**
     * Maneja el evento de clic en el botón "Actualizar".
     * (Actualmente sin implementar lógica de actualización).
     * @param event El evento de acción.
     */
    @FXML
    void onClickActualizarEmpleado(ActionEvent event) {
    }

    /**
     * Maneja el evento de clic en el botón "Buscar".
     * Busca empleados por DNI en todos los tipos de empleados.
     * Si el campo de búsqueda está vacío, carga todos los empleados.
     * Muestra el resultado de la búsqueda en la tabla y mensajes de error/éxito en {@code errorTextArea}.
     * @param event El evento de acción.
     */
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
     * Maneja la acción de buscar empleado (asociado a onAction del TextField de búsqueda).
     * Simplemente llama a {@link #onClickBuscar(ActionEvent)}.
     * @param event El evento de acción.
     */
    @FXML
    void onClickBuscarEmpleado(ActionEvent event) {
        onClickBuscar(event); // Reutiliza el método de búsqueda principal
    }

    /**
     * Maneja el evento de selección de una fila en la tabla de empleados.
     * Rellena los campos de texto del formulario con los datos del empleado seleccionado
     * y selecciona el tipo de empleado correspondiente en el ComboBox.
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

    /**
     * Limpia todos los campos de texto del formulario de empleado y deselecciona cualquier fila en la tabla.
     */
    private void clearClientFields() {
        nombreTextField.clear();
        dniTextField.clear();
        telefonoTextField.clear();
        numeroCuentaTextField.clear();
        sueldoTextField.clear();
        empleadosTable.getSelectionModel().clearSelection(); // Deselect client
    }

    /**
     * Maneja el evento de clic en el botón "Centro".
     * (Actualmente sin implementar, pero podría navegar a una vista de centro).
     * @param event El evento de acción.
     */
    @FXML
    void onClickCentro(ActionEvent event) {

    }

    /**
     * Maneja el evento de clic en el botón "Citas".
     * (Actualmente sin implementar, pero podría navegar a una vista de citas).
     * @param event El evento de acción.
     */
    @FXML
    void onClickCitas(ActionEvent event) {

    }

    /**
     * Maneja el evento de clic en el botón "Empleados".
     * Navega a la vista de empleados ({@code EmpleadoView.fxml}).
     * @param event El evento de acción.
     */
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

    /**
     * Maneja el evento de clic en el botón "Clientes".
     * Navega a la vista de clientes ({@code ClienteView.fxml}).
     * @param event El evento de acción.
     */
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

    /**
     * Maneja el evento de clic en el botón "Crear Empleado".
     * Recopila los datos del formulario, valida la entrada y crea un nuevo empleado
     * del tipo seleccionado en la base de datos.
     * Muestra mensajes de éxito o error en {@code errorTextArea}.
     * @param event El evento de acción.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
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


    /**
     * Maneja el evento de clic en el botón "Eliminar Empleado".
     * Obtiene el DNI y el tipo de empleado de los campos del formulario y elimina el empleado
     * correspondiente de la base de datos.
     * Muestra mensajes de éxito o error en {@code errorTextArea}.
     * @param event El evento de acción.
     */
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


    /**
     * Maneja el evento de clic en el botón "Historial".
     * (Actualmente sin implementar, pero podría navegar a una vista de historial).
     * @param event El evento de acción.
     */
    @FXML
    void onClickHistorial(ActionEvent event) {

    }

    /**
     * Maneja el evento de clic en el botón "Tratamiento".
     * (Actualmente sin implementar, pero podría navegar a una vista de tratamientos).
     * @param event El evento de acción.
     */
    @FXML
    void onClickTratamiento(ActionEvent event) {

    }

}