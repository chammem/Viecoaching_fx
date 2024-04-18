package controllers;

import entities.Categorie;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.ServiceCategorie;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AfficheCatController implements Initializable {

    @FXML
    private GridPane gridPane;
    private ServiceCategorie service;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        service = new ServiceCategorie();
        loadResources();
    }

    private void loadResources() {
        try {
            int row = 1; // Commence à partir de la deuxième ligne (index 1)
            for (Categorie categorie : service.afficher()) {
                // Titre
                Label titleLabel = new Label(categorie.getNom_categorie());
                gridPane.add(titleLabel, 0, row);

                // Type (Exemple d'utilisation d'une Label pour le type, à ajuster selon vos besoins)
                Label typeLabel = new Label(String.valueOf(categorie.getRessource_id()));
                gridPane.add(typeLabel, 1, row);

                // Image (Si besoin d'afficher une image, configurez-la dans ImageView)
                ImageView imageView = new ImageView();
                // Assurez-vous de configurer l'image dans l'ImageView
                gridPane.add(imageView, 2, row);

                // Description
                Label descriptionLabel = new Label(categorie.getDescription());
                gridPane.add(descriptionLabel, 3, row);

                // Actions (Supprimer et Mettre à jour)
                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(event -> deleteResource(categorie));
                gridPane.add(deleteButton, 4, row);

                Button updateButton = new Button("Update");
                updateButton.setOnAction(event -> updateResource(categorie));
                gridPane.add(updateButton, 5, row);

                row++; // Passer à la ligne suivante
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteResource(Categorie categorie) {
        try {
            service.supprimer(categorie.getId());
            refreshGridPane(); // Rafraîchir le contenu du GridPane après la suppression
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateResource(Categorie categorie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifierCat.fxml"));
            Parent modif = loader.load();

            ModifierCatController controller = loader.getController();
            controller.initData(categorie); // Passer la catégorie sélectionnée au contrôleur ModifierCatController

            Stage updateStage = new Stage();
            updateStage.setScene(new Scene(modif));
            updateStage.setTitle("Modifier Catégorie");
            updateStage.showAndWait();

            refreshGridPane(); // Rafraîchir le contenu du GridPane après la mise à jour
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshGridPane() {
        gridPane.getChildren().clear(); // Effacer le contenu actuel du GridPane
        loadResources(); // Recharger les ressources et mettre à jour le GridPane
    }



}
