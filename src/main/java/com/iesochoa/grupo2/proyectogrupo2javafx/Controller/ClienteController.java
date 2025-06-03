package com.iesochoa.grupo2.proyectogrupo2javafx.Controller;

import com.iesochoa.grupo2.proyectogrupo2javafx.DB.ClienteDAO;
import com.iesochoa.grupo2.proyectogrupo2javafx.Model.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ClienteController {

    @FXML
    private Button actualizarClienteButton;

    @FXML
    private Button actualizarMascotaButton;

    @FXML
    private TextField apellido1TextField;

    @FXML
    private TextField apellido2TextField;

    @FXML
    private Button buscarAnimalButton;

    @FXML
    private Button centroButton;

    @FXML
    private Button citasButton;

    @FXML
    private Button clientesButton;

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
    private TextField idMascotaTextField;

    @FXML
    private TextField nombreMascotaTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField razaMascotaTextField;

    @FXML
    private TextArea resultadoDeBusquedaTextArea;

    @FXML
    private TextField searchAnimal;

    @FXML
    private TextField telefonoTextField;

    @FXML
    private Button tratamientoButton;

    private boolean esFormatoTelefonoValido(String telefono) {
        // Permite un '+' opcional al inicio, seguido de uno o más dígitos.
        // Ajusta esta regex si necesitas una validación más específica (longitud, prefijos, etc.)
        boolean esValido = true;
        if (telefono == null || !telefono.matches("\\+?\\d{7,15}")) { // Ejemplo: entre 7 y 15 dígitos
            esValido = false;
        }
        return esValido;
    }
    @FXML
    void onActualizarCliente(ActionEvent event) {

    }

    @FXML
    void onActualizarMascota(ActionEvent event) {

    }

    @FXML
    void onClickCentro(ActionEvent event) {

    }

    @FXML
    void onClickCitas(ActionEvent event) {

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
    void onClickHistorial(ActionEvent event) {

    }

    @FXML
    void onClickTratamiento(ActionEvent event) {

    }

    @FXML
    void onCrearCliente(ActionEvent event) {
        String feedback = "";
        boolean exito = true;

        String dni = dniTextField.getText().trim();
        String nombre = nombreTextField.getText().trim();
        String apellido1 = apellido1TextField.getText().trim();
        String apellido2 = apellido2TextField.getText().trim(); // Opcional
        String telefono = telefonoTextField.getText().trim();

        if (dni.isEmpty() || nombre.isEmpty() || apellido1.isEmpty() || telefono.isEmpty()) {
            feedback = "Error: DNI, Nombre, Primer Apellido y Teléfono son obligatorios.";
            exito = false;
        }

        if (exito && !dni.matches("\\d{8}[A-Z]")) {
            feedback = "Error: Formato de DNI incorrecto.";
            exito = false;
        }

        if (exito && !esFormatoTelefonoValido(telefono)) {
            feedback = "Error: Formato de teléfono incorrecto.";
            exito = false;
        }

        if (exito) {
            String nombreCompleto = nombre + " " + apellido1 + (apellido2.isEmpty() ? "" : " " + apellido2);
            Cliente nuevoCliente = new Cliente(dni, nombreCompleto, telefono);
            try {
                Cliente clienteExistente = ClienteDAO.getInstance().getClienteByDNI(dni);
                if (clienteExistente != null) {
                    feedback = "Error: Ya existe un cliente con el DNI " + dni;
                    exito = false;
                }
                if (exito) {
                    ClienteDAO.getInstance().insertCliente(nuevoCliente);
                    feedback = "Cliente creado con éxito:\n" + nuevoCliente.toString();
                    limpiarCamposCliente();
                    // listarTodosLosClientes();
                }
            } catch (SQLException e) {
                feedback = "Error de base de datos al crear cliente: " + e.getMessage();
                e.printStackTrace();
                exito = false; // Aunque ya debería estarlo si hay excepción
            }
        }
        resultadoDeBusquedaTextArea.setText(feedback);
    }

    @FXML
    void onCrearMascota(ActionEvent event) {

    }

    @FXML
    void onEliminarCliente(ActionEvent event) {

    }

    @FXML
    void onEliminarMascota(ActionEvent event) {

    }

    private void limpiarCamposCliente() {
        dniTextField.clear();
        nombreTextField.clear();
        apellido1TextField.clear();
        apellido2TextField.clear();
        telefonoTextField.clear();
    }

    private void limpiarCamposClienteMenosDNI() {
        nombreTextField.clear();
        apellido1TextField.clear();
        apellido2TextField.clear();
        telefonoTextField.clear();
    }

    private void limpiarCamposMascota() {
        idMascotaTextField.clear();
        dniClienteMascotaTextField.clear();
        nombreMascotaTextField.clear();
        especieMascotaTextField.clear();
        razaMascotaTextField.clear();
        edadMascotaTextField.clear();
        searchAnimal.clear();
    }
}
