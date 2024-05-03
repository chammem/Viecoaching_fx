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

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    private String nomFichierurlSelectionne; // Déclaration de la variable pour stocker le nom du fichier sélectionné
    private ServiceCategorie serviceCategorie ;





    @FXML
    void AjouterCategorie(ActionEvent event) {
        String nomCategorie = tnom.getText().trim();
        String description = tDescription.getText().trim();
        String imageUrl = (timage.getImage() != null) ? timage.getImage().getUrl() : null;
        String ressourceNom = tressource.getValue();

        // Vérifiez que tous les champs nécessaires sont remplis
        if (!isValidTextField(nomCategorie, "Nom de catégorie") ||
                !isValidTextField(description, "Description") ||
                !isValidImageView(timage, "Image") ||
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

            Categorie categorie = new Categorie(nomCategorie, description, imageUrl, ressource);
            serviceCategorie.ajouter(categorie);

            // Afficher une boîte de dialogue d'information pour indiquer que la catégorie a été ajoutée avec succès
            showAlert("Catégorie ajoutée avec succès !");

            // Charger la vue afficheCategorie.fxml après l'ajout réussi
            loadAfficheCategorieView();

        } catch (SQLException e) {
            // En cas d'erreur lors de l'ajout de la catégorie, afficher l'erreur dans la console
            System.out.println("Erreur lors de l'ajout de la catégorie : " + e.getMessage());
            e.printStackTrace();
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une url");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("urls", "*.png", "*.jpg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Charger l'url sélectionnée
            Image image = new Image(selectedFile.toURI().toString());

            // Afficher l'url dans l'urlView approprié
            timage.setImage(image);

            // Stocker le nom du fichier sélectionné
            nomFichierurlSelectionne = selectedFile.getName();
            System.out.println("Nom du fichier sélectionné : " + nomFichierurlSelectionne);
        }
    }


    // Méthode pour charger une url dans l'urlView
    public void setImage(Image image) {
        timage.setImage(image);
    }

    // Méthode getter pour obtenir l'ImageView
    public ImageView getImageView() {
        return timage;
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