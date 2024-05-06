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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceCategorie;
import services.ServiceRessource;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierCatController implements Initializable {

    @FXML
    private Button btnModifier;

    @FXML
    private AnchorPane image;

    @FXML
    private Button insertView;

    @FXML
    private TextArea tDescription;

    @FXML
    private ImageView timage;

    @FXML
    private TextField tnom;

    @FXML
    private ChoiceBox<String> tressource;

    private Categorie categorie;
    private ServiceCategorie serviceCategorie;
    private Cloudinary cloudinary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.serviceCategorie = new ServiceCategorie();

        // Initialisation de Cloudinary avec vos identifiants
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "djtv7pcyp",
                "api_key", "216986526787688",
                "api_secret", "93b8-dqk94OppguTtG3BxbUA5cM"));

        ServiceRessource serviceRessources = new ServiceRessource();
        List<Ressources> ressourcesList = null;
        try {
            ressourcesList = serviceRessources.getAllRessources();
        } catch (SQLException e) {
            showAlert("Erreur lors de la récupération des ressources : " + e.getMessage());
            e.printStackTrace();
        }

        // Remplir la ChoiceBox avec les noms des ressources
        if (ressourcesList != null) {
            tressource.getItems().clear();
            for (Ressources ressource : ressourcesList) {
                tressource.getItems().add(ressource.getType_r());
            }
        }
    }

    public void initData(Categorie categorie) {
        this.categorie = categorie;

        // Pré-remplir les champs avec les valeurs de la catégorie à modifier
        tnom.setText(categorie.getNom_categorie());
        tressource.setValue(categorie.getRessource_id().getTitre_r());
        tDescription.setText(categorie.getDescription());

        // Chargement de l'image
        try {
            Image image = new Image(categorie.getImage());
            timage.setImage(image);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void ModifierCategorie(ActionEvent event) {
        // Vérifier si une catégorie est sélectionnée
        if (categorie == null) {
            showAlert("Veuillez sélectionner une catégorie à modifier.");
            return;
        }

        // Récupérer les nouvelles valeurs des champs
        String nomCategorie = tnom.getText().trim();
        String description = tDescription.getText().trim();
        String ressourceNom = tressource.getValue();

        // Vérifier chaque champ individuellement
        if (!isValidTextField(nomCategorie, "Nom de catégorie") ||
                !isValidTextField(description, "Description") ||
                !isValidChoiceBox(tressource, "Ressource")) {
            return;
        }

        try {
            // Vérifier si la ressource spécifiée existe
            ServiceRessource serviceRessource = new ServiceRessource();
            Ressources ressource = serviceRessource.getRessourceByNom(ressourceNom);

            if (ressource == null) {
                showAlert("La ressource spécifiée n'existe pas.");
                return;
            }

            // Mettre à jour les propriétés de la catégorie avec les nouvelles valeurs
            categorie.setNom_categorie(nomCategorie);
            categorie.setDescription(description);

            // Télécharger et mettre à jour l'image si elle a été modifiée
            if (timage.getImage() != null) {
                String imageUrl = uploadImageToCloudinary();
                if (imageUrl != null) {
                    categorie.setImage(imageUrl);
                }
            }

            categorie.setRessource_id(ressource);

            // Appeler le service pour effectuer la modification
            serviceCategorie.modifier(categorie);

            // Afficher un message de succès
            showAlert("Catégorie modifiée avec succès!");

            // Recharger la vue afficheCategorie.fxml après la modification
            loadAfficheCategorieView();

        } catch (SQLException e) {
            showAlert("Erreur lors de la modification de la catégorie : " + e.getMessage());
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
            Stage stage = (Stage) tnom.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur lors du chargement de afficheCategorie.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
