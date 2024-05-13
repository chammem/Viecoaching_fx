package controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import entities.Ressources;
import javafx.embed.swing.SwingFXUtils;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
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
    private Cloudinary cloudinary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceRessource = new ServiceRessource();

        // Initialisation de Cloudinary avec vos identifiants
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dppj3e5cp",
                "api_key", "647876725794588",
                "api_secret", "yQBtTPY_dzeUjCcUHQcmHvJevgg"));
    }

    @FXML
    void AjouterRessource(ActionEvent event) {
        String titre = tTitre.getText();
        String type = tType.getText();
        String description = tDescription.getText();

        if (!isValidTextField(titre, "Nom de ressource") ||
                !isValidTextField(type, "Type de Ressource") ||
                !isValidImageView(timage, "Image") ||
                !isValidTextField(description, "Description")) {
            return;
        }

        try {
            // Convertir l'ImageView en BufferedImage
            Image image = timage.getImage();
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

            // Créer un fichier temporaire pour l'image
            File tempFile = File.createTempFile("temp-image", ".png");
            ImageIO.write(bufferedImage, "png", tempFile);

            // Uploader l'image vers Cloudinary
            Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            // Enregistrer la ressource dans la base de données
            Ressources ressource = new Ressources(titre, type, imageUrl, description);
            serviceRessource.ajouter(ressource);

            showAlert("Ressource ajoutée avec succès !");

            // Charger la vue afficheRessource.fxml après l'ajout réussi
            loadAfficheRessourceView();

            clearFields(); // Effacer les champs après l'ajout réussi
        } catch (IOException | SQLException e) {
            showAlert("Erreur lors de l'ajout de la ressource : " + e.getMessage());
            e.printStackTrace();
        }
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

    private boolean isValidTextField(String value, String fieldName) {
        if (value.isEmpty()) {
            showAlert("Veuillez saisir un(e) " + fieldName + ".");
            return false;
        }
        return true;
    }

    private boolean isValidImageView(ImageView imageView, String fieldName) {
        if (imageView.getImage() == null) {
            showAlert("Veuillez sélectionner une " + fieldName + ".");
            return false;
        }
        return true;
    }

    // Fonction utilitaire pour charger la vue afficheRessource.fxml
    private void loadAfficheRessourceView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
            Stage stage = (Stage) tTitre.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
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
}
