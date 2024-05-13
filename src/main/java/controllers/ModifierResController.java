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
import java.io.File;
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
    private Cloudinary cloudinary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.serviceRessource = new ServiceRessource();

        // Initialisation de Cloudinary avec vos identifiants
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dppj3e5cp",
                "api_key", "647876725794588",
                "api_secret", "yQBtTPY_dzeUjCcUHQcmHvJevgg"));
    }

    public void initData(Ressources ressource) {
        this.ressource = ressource;

        // Pré-remplir les champs avec les valeurs de la ressource à modifier
        tTitre.setText(ressource.getTitre_r());
        tType.setText(ressource.getType_r());
        tDescription.setText(ressource.getDescription());

        // Charger l'image à partir de l'URL stockée dans la ressource
        loadImage(ressource.getUrl());
        try {
            Image image = new Image(ressource.getUrl());
            timage.setImage(image);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Vérifier si l'image est une URL Cloudinary
            if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                // Charger l'image à partir de l'URL distante (Cloudinary)
                timage.setImage(new Image(imageUrl));
            } else {
                // Charger l'image à partir du chemin local
                File file = new File(imageUrl);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    timage.setImage(image);
                } else {
                    System.err.println("Fichier image introuvable : " + imageUrl);
                }
            }
        }
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
        if (typeText.isEmpty() || tTitreText.isEmpty() || description.isEmpty()) {
            showAlert("Veuillez remplir tous les champs.");
            return;
        }

        try {
            // Mettre à jour les propriétés de la ressource avec les nouvelles valeurs
            ressource.setTitre_r(tTitreText);
            ressource.setDescription(description);
            ressource.setType_r(typeText);

            // Vérifier si une nouvelle image a été sélectionnée
            if (timage.getImage() != null) {
                // Uploader l'image vers Cloudinary et mettre à jour l'URL de la ressource
                String newImageUrl = uploadImageToCloudinary();
                if (newImageUrl != null) {
                    ressource.setUrl(newImageUrl);
                }
            }

            // Appeler le service pour effectuer la modification
            serviceRessource.modifier(ressource);

            // Afficher une boîte de dialogue d'information pour indiquer la réussite de la modification
            showAlert("Ressource modifiée avec succès!");

            // Charger la vue afficheRessource.fxml après la modification réussie
            loadAfficheRessourceView();

        } catch (SQLException e) {
            // En cas d'erreur lors de la modification, afficher l'erreur
            showAlert("Erreur lors de la modification de la ressource : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String uploadImageToCloudinary() {
        try {
            // Créer un fichier temporaire pour l'image
            File tempFile = File.createTempFile("temp-image", ".png");

            // Sauvegarder l'image dans le fichier temporaire
            ImageIO.write(SwingFXUtils.fromFXImage(timage.getImage(), null), "png", tempFile);

            // Uploader l'image vers Cloudinary
            return cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap()).get("url").toString();

        } catch (IOException e) {
            showAlert("Erreur lors du chargement de l'image vers Cloudinary : " + e.getMessage());
            e.printStackTrace();
            return null;
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

    private void loadAfficheRessourceView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
            Stage stage = (Stage) tTitre.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
