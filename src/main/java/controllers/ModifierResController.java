package controllers;

import entities.Ressources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceRessource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifierResController implements Initializable {

    @FXML
    private TextArea tDescription;

    @FXML
    private TextField tTitre;

    @FXML
    private TextField tType;

    @FXML
    private ImageView timage;

    private Ressources ressource; // Ressource à modifier
    private ServiceRessource serviceRessource; // Service pour la gestion des ressources

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.serviceRessource = new ServiceRessource();
    }

    public void initData(Ressources ressource) {
        this.ressource = ressource;

        // Pré-remplir les champs avec les valeurs de la ressource à modifier
        tTitre.setText(ressource.getTitre_r());
        tType.setText(ressource.getType_r());
        tDescription.setText(ressource.getDescription());
        timage.setImage(new Image(ressource.getUrl()));
    }

    @FXML
    void ModifierRessource(ActionEvent event) {
        // Vérifier si une ressource est sélectionnée
        if (ressource == null) {
            showAlert("Veuillez sélectionner une ressource à modifier.");
            return;
        }

        // Récupérer les nouvelles valeurs des champs
        String typeText = tType.getText().trim();
        String tTitreText = tTitre.getText().trim();
        String description = tDescription.getText().trim();

        // Vérifier chaque champ individuellement
        if (typeText.isEmpty() || !isValidTitre(tTitreText) || !isValidDescription(description) || !isValidtype(typeText)) {
            return; // Sortie si l'un des champs est invalide
        }

        try {
            // Mettre à jour les propriétés de la ressource avec les nouvelles valeurs
            ressource.setTitre_r(tTitreText);
            ressource.setDescription(description);
            ressource.setType_r(typeText);
            ressource.setUrl(timage.getImage().getUrl());

            // Appeler le service pour effectuer la modification
            serviceRessource.modifier(ressource);

            // Afficher une boîte de dialogue d'information pour indiquer la réussite de la modification
            showAlert("Ressource modifiée avec succès!");

            // Charger la vue afficheCategorie.fxml après la modification réussie
            loadAfficheCategorieView();

        } catch (SQLException e) {
            // En cas d'erreur lors de la modification, afficher l'erreur
            showAlert("Erreur lors de la modification de la ressource : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isValidTitre(String titre) {
        if (titre.isEmpty()) {
            showAlert("Veuillez saisir un titre.");
            return false;
        }
        // Ajoutez d'autres conditions de validation au besoin
        return true;
    }

    private boolean isValidDescription(String description) {
        if (description.isEmpty()) {
            showAlert("Veuillez saisir une description.");
            return false;
        }
        // Ajoutez d'autres conditions de validation au besoin
        return true;
    }
    private boolean isValidtype(String type) {
        if (type.isEmpty()) {
            showAlert("Veuillez saisir Type.");
            return false;
        }
        // Ajoutez d'autres conditions de validation au besoin
        return true;
    }

    // Fonction utilitaire pour charger la vue afficheCategorie.fxml
    private void loadAfficheCategorieView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/afficheRessource.fxml"));
            Stage stage = (Stage) tTitre.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheCategorie.fxml : " + e.getMessage());
        }
    }

    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Charger l'image sélectionnée
            Image image = new Image(selectedFile.toURI().toString());

            // Afficher l'image dans l'ImageView approprié
            timage.setImage(image);

            // Stocker le nom du fichier sélectionné
            String nomFichierSelectionne = selectedFile.getName();
            System.out.println("Nom du fichier sélectionné : " + nomFichierSelectionne);
        }
    }

    private void showAlert(String message) {
        // Afficher une boîte de dialogue d'alerte avec le message spécifié
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
