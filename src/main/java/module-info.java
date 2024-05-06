module com.baha.baha {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.asma.asma to javafx.fxml;
    opens com.asma.asma.controllers; // Open the controllers package

    exports com.asma.asma;
    opens com.asma.asma.entities;

    // Add any additional configurations if necessary
}
