package controllers;

import entities.Ressources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import services.ServiceRessource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

        try {
            // Charger et afficher l'image de la ressource (s'il y en a une)
            if (ressource.getUrl() != null && !ressource.getUrl().isEmpty()) {
                Image image = new Image(new FileInputStream(ressource.getUrl()));
                timage.setImage(image);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Image file not found: " + e.getMessage());
            // Charger une image par défaut si l'image spécifiée n'est pas trouvée
            timage.setImage(new Image("file:path/to/placeholder/image.png"));
        }
    }

    @FXML
    void ModifierRessource(ActionEvent event) {
        // Mettre à jour les propriétés de la ressource avec les valeurs des champs
        ressource.setTitre_r(tTitre.getText());
        ressource.setType_r(tType.getText());
        ressource.setDescription(tDescription.getText());

        // Appeler le service pour effectuer la modification
        try {
            serviceRessource.modifier(ressource);
            showAlert("Ressource modifiée avec succès !");
        } catch (SQLException e) {
            showAlert("Erreur lors de la modification de la ressource : " + e.getMessage());
            e.printStackTrace();
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
