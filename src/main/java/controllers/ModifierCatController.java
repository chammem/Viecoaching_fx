package controllers;

import entities.Categorie;
import entities.Ressources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import services.ServiceCategorie;
import services.ServiceRessource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

        try {
            // Charger et afficher l'image de la catégorie (s'il y en a une)
            if (categorie.getImage() != null && !categorie.getImage().isEmpty()) {
                Image image = new Image(new FileInputStream(categorie.getImage()));
                timage.setImage(image);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Image file not found: " + e.getMessage());
            // Charger une image par défaut si l'image spécifiée n'est pas trouvée
            timage.setImage(new Image("file:path/to/placeholder/image.png"));
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
        String nomCategorie = tnom.getText();
        String description = tDescription.getText();
        String ressourceNom = tressource.getValue(); // Utiliser getValue() pour obtenir la valeur sélectionnée dans la ChoiceBox

        // Vérifier que les champs nécessaires ne sont pas vides
        if (nomCategorie.isEmpty() || description.isEmpty() || ressourceNom.isEmpty()) {
            showAlert("Veuillez remplir tous les champs.");
            return;
        }

        try {
            // Récupérer la ressource associée au nom spécifié
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

            // Appeler le service pour effectuer la modification
            serviceCategorie.modifier(categorie);

            // Afficher une boîte de dialogue d'information pour indiquer la réussite de la modification
            showAlert("Catégorie modifiée avec succès!");
        } catch (SQLException e) {
            // En cas d'erreur lors de la modification, afficher l'erreur
            showAlert("Erreur lors de la modification de la catégorie : " + e.getMessage());
            e.printStackTrace();
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
