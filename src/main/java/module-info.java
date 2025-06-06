module com.iesochoa.grupo2.proyectogrupo2javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    opens com.iesochoa.grupo2.proyectogrupo2javafx.db to javafx.fxml;
    exports com.iesochoa.grupo2.proyectogrupo2javafx.db;
    opens com.iesochoa.grupo2.proyectogrupo2javafx.Model to javafx.fxml;
    exports com.iesochoa.grupo2.proyectogrupo2javafx.Model;
    opens com.iesochoa.grupo2.proyectogrupo2javafx.Controller to javafx.fxml;
    exports com.iesochoa.grupo2.proyectogrupo2javafx.Controller;
}
