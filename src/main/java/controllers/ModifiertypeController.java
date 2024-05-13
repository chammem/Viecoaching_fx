package controllers;

import entities.Typegroupe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.ServiceTypegroupe;

import java.io.IOException;
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
            loadAfficheCategorieView();
        } catch (SQLException e) {
            showAlert("Erreur lors de la modification de la ressource : " + e.getMessage());
        }
    }
    private void  modiftype() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AfficheType.fxml"));
            Stage stage = (Stage) tTitre.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }
    private void loadAfficheCategorieView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
            Stage stage = (Stage) tTitre.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheCategorie.fxml : " + e.getMessage());
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
