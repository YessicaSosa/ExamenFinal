module com.mycompany.appinvestigacion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.logging;
    requires java.base;

    opens com.mycompany.appinvestigacion to javafx.fxml;
    exports com.mycompany.appinvestigacion;
}
