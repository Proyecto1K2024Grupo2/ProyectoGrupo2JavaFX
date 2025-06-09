package com.iesochoa.grupo2.proyectogrupo2javafx.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * La clase Application es el punto de entrada principal para iniciar la aplicación JavaFX.
 * Extiende la clase javafx.application.Application y sobrescribe el método start requerido
 * para configurar y mostrar la ventana (stage) principal de la aplicación.
 */
public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/iesochoa/grupo2/proyectogrupo2javafx/views/PrincipalView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Gestión de Centro");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}