package controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import entities.Categorie;
import entities.Ressources;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceCategorie;
import services.ServiceRessource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CategorieController implements Initializable {

    @FXML
    private TextField tnom;

    @FXML
    private TextArea tDescription;

    @FXML
    private ImageView timage;

    @FXML
    private ChoiceBox<String> tressource;

    private ServiceCategorie serviceCategorie;
    private Cloudinary cloudinary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceCategorie = new ServiceCategorie();

        // Initialisation de Cloudinary avec vos identifiants
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "djtv7pcyp",
                "api_key", "216986526787688",
                "api_secret", "93b8-dqk94OppguTtG3BxbUA5cM"));

        // Initialisez votre ChoiceBox tressource avec des options
        ServiceRessource serviceRessources = new ServiceRessource();
        List<Ressources> ressourcesList = null;

        try {
            ressourcesList = serviceRessources.getAllRessources();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Remplissez la ChoiceBox avec les noms des ressources
        tressource.getItems().clear();
        for (Ressources ressource : ressourcesList) {
            tressource.getItems().add(ressource.getType_r());
        }
    }

    @FXML
    void AjouterCategorie(ActionEvent event) {
        String nomCategorie = tnom.getText().trim();
        String description = tDescription.getText().trim();
        String ressourceNom = tressource.getValue();

        if (!isValidTextField(nomCategorie, "Nom de catégorie") ||
                !isValidTextField(description, "Description") ||
                !isValidChoiceBox(tressource, "Ressource")) {
            return;
        }

        try {
            ServiceRessource serviceRessources = new ServiceRessource();
            Ressources ressource = serviceRessources.getRessourceByNom(ressourceNom);

            if (ressource == null) {
                showAlert("La ressource spécifiée n'existe pas.");
                return;
            }

            // Télécharger l'image sélectionnée dans Cloudinary
            String imageUrl = uploadImageToCloudinary();

            Categorie categorie = new Categorie(nomCategorie, description, imageUrl, ressource);
            serviceCategorie.ajouter(categorie);

            showAlert("Catégorie ajoutée avec succès !");

            loadAfficheCategorieView();

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la catégorie : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String uploadImageToCloudinary() {
        try {
            Image image = timage.getImage();

            if (image != null) {
                // Convertir l'ImageView en BufferedImage
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

                // Créer un fichier temporaire pour l'image
                File tempFile = File.createTempFile("temp-image", ".png");
                ImageIO.write(bufferedImage, "png", tempFile);

                // Uploader l'image vers Cloudinary
                Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());
                return (String) uploadResult.get("url");
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'image vers Cloudinary : " + e.getMessage());
            e.printStackTrace();
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

    private void loadAfficheCategorieView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
            Stage stage = (Stage) tnom.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheCategorie.fxml : " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
}
