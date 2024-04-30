package controllers;

import entities.Categorie;
import entities.Ressources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceCategorie;
import services.ServiceRessource;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CategorieController implements Initializable {
    @FXML
    private Button btnEnregistrer;

    @FXML
    private Button btnaffiche;

    @FXML
    private AnchorPane image;

    @FXML
    private Button insertView;

    @FXML
    private TextArea tDescription;

    @FXML
    private TextField tType;

    @FXML
    private ImageView timage;

    @FXML
    private TextField tnom;

    @FXML
    private ChoiceBox<String> tressource;
    private ServiceCategorie serviceCategorie ;
    private String selectedImageFileName;


    @FXML
    void AjouterCategorie(ActionEvent event) {
        String nomCategorie = tnom.getText().trim();
        String description = tDescription.getText().trim();
        String imageUrl = timage.getImage() != null ? timage.getImage().getUrl() : null;
        String imageName = FileNameNormalizer.normalizeFileName(imageUrl);
        String ressourceNom = tressource.getValue();

        // Vérifiez que tous les champs nécessaires sont remplis
        if (!isValidTextField(nomCategorie, "Nom de catégorie") ||
                !isValidTextField(description, "Description") ||
                !isValidTextField(imageName, "Image") ||
                !isValidChoiceBox(tressource, "Ressource")) {
            return;
        }

        try {
            // Récupère la ressource correspondant au nom sélectionné
            ServiceRessource serviceRessources = new ServiceRessource();
            Ressources ressource = serviceRessources.getRessourceByNom(ressourceNom);

            if (ressource == null) {
                showAlert("La ressource spécifiée n'existe pas.");
                return;
            }

            // Crée une nouvelle catégorie avec les données fournies
            Categorie categorie = new Categorie(nomCategorie, description, imageName, ressource);

            // Ajoute la catégorie à la base de données
            ServiceCategorie serviceCategorie = new ServiceCategorie();
            serviceCategorie.ajouter(categorie);

            // Affiche une boîte de dialogue pour confirmer l'ajout de la catégorie
            showAlert("Catégorie ajoutée avec succès !");

            // Recharge la vue afficheCategorie.fxml après l'ajout réussi
            loadAfficheCategorieView();

        } catch (SQLException e) {
            // En cas d'erreur lors de l'ajout de la catégorie, affiche l'erreur
            showAlert("Erreur lors de l'ajout de la catégorie : " + e.getMessage());
            e.printStackTrace(); // Affiche l'erreur dans la console
        }
    }
    private String extractFileNameFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }
        int lastIndex = imageUrl.lastIndexOf('/');
        if (lastIndex >= 0 && lastIndex < imageUrl.length() - 1) {
            return imageUrl.substring(lastIndex + 1); // Retourne le nom de fichier avec l'extension
        }
        return null;
    }

    private String normalizeFileName(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }

        // Extraire le nom de fichier de l'URL
        int lastSlashIndex = imageUrl.lastIndexOf('/');
        if (lastSlashIndex != -1 && lastSlashIndex < imageUrl.length() - 1) {
            String fileName = imageUrl.substring(lastSlashIndex + 1);
            return fileName.replace("%20", "-"); // Remplacer les espaces par des tirets
        }

        return null;
    }

    private boolean isValidTextField(String value, String fieldName) {
        if (value.isEmpty()) {
            showAlert("Veuillez saisir un(e) " + fieldName + ".");
            return false;
        }
        return true;
    }

    private boolean isValidChoiceBox(ChoiceBox<String> choiceBox, String fieldName) {
        if (choiceBox.getValue() == null || choiceBox.getValue().isEmpty()) {
            showAlert("Veuillez sélectionner une " + fieldName + ".");
            return false;
        }
        return true;
    }

    // Fonction utilitaire pour charger la vue afficheCategorie.fxml
    private void loadAfficheCategorieView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/afficheCategorie.fxml"));
            Stage stage = (Stage) tnom.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheCategorie.fxml : " + e.getMessage());
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null); // Enlever l'en-tête de la boîte de dialogue
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void selectImage() {
        JFileChooser chooser = new JFileChooser();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif")
        );

        Stage stage = (Stage) timage.getScene().getWindow(); // Récupérer la scène actuelle
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Récupérer le nom de fichier de l'image sélectionnée
            String selectedFileName = selectedFile.getName();

            // Afficher l'image dans l'ImageView
            Image image = new Image(selectedFile.toURI().toString());
            timage.setImage(image);

            // Stocker le nom de fichier de l'image sélectionnée
            selectedImageFileName = selectedFileName;
            System.out.println("Nom de fichier de l'image sélectionnée : " + selectedImageFileName);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceCategorie= new ServiceCategorie();

        // Initialisez votre ChoiceBox tressource avec des options
        ServiceRessource serviceRessources = new ServiceRessource();
        List<Ressources> ressourcesList = null; // Supposons que vous avez une méthode pour obtenir toutes les ressources
        try {
            ressourcesList = serviceRessources.getAllRessources();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Remplissez la ChoiceBox avec les noms des ressources
        tressource.getItems().clear(); // Efface les éléments existants (si nécessaire)
        for (Ressources ressource : ressourcesList) {
            tressource.getItems().add(ressource.getType_r()); // Ajoutez le nom de la ressource à la liste
        }
    }
}

