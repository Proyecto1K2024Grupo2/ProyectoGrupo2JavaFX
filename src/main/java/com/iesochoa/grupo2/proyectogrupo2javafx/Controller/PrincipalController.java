package com.iesochoa.grupo2.proyectogrupo2javafx.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PrincipalController {

    @FXML
    private Button centroButton;

    @FXML
    private Button citasButton;

    @FXML
    private Button clientesButton;

    @FXML
    private AnchorPane contentArea;

    @FXML
    private Button empleadosButton;

    @FXML
    private Button historialButton;

    @FXML
    private Button tratamientoButton;

    @FXML
    void onClickCentro(ActionEvent event) {
        // Implementar acción para botón "Centro"
    }

    @FXML
    void onClickCitas(ActionEvent event) {
        // Implementar acción para botón "Citas"
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
        // Implementar acción para botón "Historial"
    }

    @FXML
    void onClickTratamiento(ActionEvent event) {
        // Implementar acción para botón "Tratamiento"
    }
}
