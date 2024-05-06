module com.baha.baha {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.xml.bind;

    opens fxml to javafx.fxml;
    opens controllers; // Open the controllers package
    opens entities;
    exports tests;
    opens tests to javafx.fxml;

    // Add any additional configurations if necessary
}
