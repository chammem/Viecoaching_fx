package controllers;

import entities.Ressources;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.ServiceRessource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import entities.Ressources;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.ServiceRessource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AfficheResController implements Initializable {

    @FXML
    private GridPane gridPane;

    private ServiceRessource service;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        service = new ServiceRessource();

        loadResources();
    }

    private void loadResources() {
        try {
            int row = 1; // Commencer à partir de la deuxième ligne (index 1)
            for (Ressources resource : service.afficher()) {
                // Titre
                gridPane.add(new Button(resource.getTitre_r()), 0, row);

                // Type
                gridPane.add(new Button(resource.getType_r()), 1, row);

                // Image (Utiliser ImageView si nécessaire)
                gridPane.add(new Button("View Image"), 2, row);

                // Description
                gridPane.add(new Button(resource.getDescription()), 3, row);

                // Action (Supprimer et Mettre à jour)
                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(event -> deleteResource(resource));
                gridPane.add(deleteButton, 4, row);

                Button updateButton = new Button("Update");
                updateButton.setOnAction(event -> updateResource(resource));
                gridPane.add(updateButton, 5, row);

                row++; // Passer à la ligne suivante
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteResource(Ressources resource) {
        try {
            service.supprimer(resource.getId());
            refreshGridPane(); // Rafraîchir le contenu du GridPane après la suppression
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateResource(Ressources resource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifierRes.fxml"));
            Parent modif = loader.load();

            ModifierResController controller = loader.getController();
            controller.initData(resource); // Passer la ressource sélectionnée au contrôleur ModifierResController

            Stage updateStage = new Stage();
            updateStage.setScene(new Scene(modif));
            updateStage.setTitle("Update Resource");
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
