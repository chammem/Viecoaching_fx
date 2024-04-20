package controllers;

import entities.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.ServiceCategorie;
import services.ServiceRessource;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AfficheCatController implements Initializable {


    @FXML
    private GridPane gridPane;

    private  ServiceCategorie service ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        service=new ServiceCategorie();
        loadResources();
    }

    private void loadResources() {
        try {
            int row = 1; // Commence à partir de la première ligne (index 0)
            for (Categorie categorie : service.afficher()) {
                // Titre
                Label typeLabel = new Label(categorie.getRessource_id().getType_r()); // Utiliser le nom du type de groupe
                gridPane.add(typeLabel, 0, row);

                Label titleLabel = new Label(categorie.getNom_categorie());
                gridPane.add(titleLabel, 1, row);

                Label descriptionLabel = new Label(categorie.getDescription());
                gridPane.add(descriptionLabel, 2, row);

                // Image (Si besoin d'afficher une image, configurez-la dans ImageView)
                ImageView imageView = new ImageView(new Image(categorie.getImage()));
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                gridPane.add(imageView, 4, row);

                //Actions (Supprimer et Mettre à jour)
                // Ajoutez les boutons de suppression et de mise à jour si nécessaire
                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(event -> deleteCategory(categorie));
                gridPane.add(deleteButton, 5, row);

                Button updateButton = new Button("Update");
                updateButton.setOnAction(event -> updateCategory(categorie));
                gridPane.add(updateButton, 6, row);
                row++; // Passer à la ligne suivante
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void deleteCategory(Categorie categorie) {
        try {
            service.supprimer(categorie.getId());
            loadResources(); // Rafraîchir la liste des catégories après la suppression
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCategory(Categorie categorie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifierCat.fxml"));
            Parent modif = loader.load();

            ModifierCatController controller = loader.getController();
            controller.initData(categorie); // Passer la catégorie sélectionnée au contrôleur ModifierCatController

            Stage updateStage = new Stage();
            updateStage.setScene(new Scene(modif));
            updateStage.setTitle("Modifier Catégorie");
            updateStage.showAndWait();

            loadResources(); // Rafraîchir la liste des catégories après la mise à jour
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void refreshGridPane() {
        gridPane.getChildren().clear(); // Effacer le contenu actuel du GridPane
        loadResources(); // Recharger les ressources et mettre à jour le GridPane
    }
}
