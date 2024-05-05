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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.serviceCategorie = new ServiceCategorie();

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

    public void initData(Categorie categorie) {
        this.categorie = categorie;

        // Pré-remplir les champs avec les valeurs de la catégorie à modifier
        tnom.setText(categorie.getNom_categorie());
        tressource.setValue(categorie.getRessource_id().getTitre_r());
        tDescription.setText(categorie.getDescription());
        timage.setImage(new Image(categorie.getImage()));

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
            categorie.setRessource_id(ressource);
            categorie.setImage(timage.getImage().getUrl());

            // Appeler le service pour effectuer la modification
            serviceCategorie.modifier(categorie);

            // Afficher une boîte de dialogue d'information pour indiquer la réussite de la modification
            showAlert("Catégorie modifiée avec succès!");

            // Charger la vue afficheCategorie.fxml après la modification réussie
            loadAfficheCategorieView();

        } catch (SQLException e) {
            // En cas d'erreur lors de la modification, afficher l'erreur
            showAlert("Erreur lors de la modification de la catégorie : " + e.getMessage());
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

    @FXML
    private void selectImage(ActionEvent event) {
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