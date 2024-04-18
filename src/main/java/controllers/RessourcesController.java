package controllers;

import entities.Ressources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import services.ServiceRessource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RessourcesController implements Initializable {

    @FXML
    private TextField tTitre;

    @FXML
    private TextField tType;

    @FXML
    private TextArea tDescription;

    @FXML
    private ImageView timage;

    private ServiceRessource serviceRessource;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceRessource = new ServiceRessource();
    }

    @FXML
    void AjouterRessource(ActionEvent event) {
        String titre = tTitre.getText();
        String type = tType.getText();
        String description = tDescription.getText();
        String imageUrl = (timage.getImage() != null) ? timage.getImage().getUrl() : null;

        boolean isValid = true;

        if (titre.isEmpty()) {
            showAlert("Veuillez saisir un titre.");
            isValid = false;
        }

        if (type.isEmpty()) {
            showAlert("Veuillez saisir un type.");
            isValid = false;
        }

        if (description.isEmpty()) {
            showAlert("Veuillez saisir une description.");
            isValid = false;
        }

        if (imageUrl == null) {
            showAlert("Veuillez sélectionner une image.");
            isValid = false;
        }

        if (isValid) {
            try {
                Ressources ressource = new Ressources(titre, type, imageUrl, description);
                serviceRessource.ajouter(ressource);
                showAlert("Ressource ajoutée avec succès !");
                clearFields();
            } catch (SQLException e) {
                showAlert("Erreur lors de l'ajout de la ressource : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        tTitre.clear();
        tType.clear();
        tDescription.clear();
        timage.setImage(null);
    }
    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            timage.setImage(image);
        }
    }
    public void setImage(Image image) {
        timage.setImage(image);
    }

    // Méthode getter pour obtenir l'ImageView
    public ImageView getImageView() {
        return timage;
    }
    @FXML
    void Afficher(ActionEvent event) {
        try {
            Parent root= FXMLLoader.load(getClass().getResource("/fxml/afficheRessource.fxml"));
            tTitre.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }
}
