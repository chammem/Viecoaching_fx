package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import services.EmailSender;

public class MyController {

    @FXML
    private TextField recipientField;

    @FXML
    private TextField subjectField;

    @FXML
    private TextField contentField;

    @FXML
    void sendEmail(ActionEvent event) {
        String recipientEmail = recipientField.getText();
        String subject = subjectField.getText();
        String content = contentField.getText();

        EmailSender.sendEmail(recipientEmail, subject, content);

        showAlert("Email sent successfully!");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
