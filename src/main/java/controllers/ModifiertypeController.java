package controllers;

import entities.Typegroupe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import services.ServiceTypegroupe;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifiertypeController implements Initializable {
    @FXML
    private Button btnModifier;

    @FXML
    private AnchorPane image;

    @FXML
    private TextField tTitre;

    private Typegroupe typegroupe;
    private ServiceTypegroupe serviceTypegroupe;

    @FXML
    void ModifierType(ActionEvent event) {
        typegroupe.setNomtype(tTitre.getText());

        try {
            serviceTypegroupe.modifier(typegroupe);
            showAlert("Ressource modifiée avec succès !");
        } catch (SQLException e) {
            showAlert("Erreur lors de la modification de la ressource : " + e.getMessage());
        }
    }

    public void initData(Typegroupe typegroupe) {
        this.typegroupe = typegroupe;
        tTitre.setText(typegroupe.getNomtype());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.serviceTypegroupe = new ServiceTypegroupe();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
