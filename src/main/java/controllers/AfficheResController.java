package controllers;

import entities.Ressources;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.ServiceRessource;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class AfficheResController implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField tText;

    private ServiceRessource service;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        service = new ServiceRessource();
        loadResources();

        // Add listener to the TextField for search functionality
        tText.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                filterResources(newValue);
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception gracefully
            }
        });
    }

    private void loadResources() {
        try {
            ObservableList<Ressources> resources = FXCollections.observableArrayList(service.afficher());
            gridPane.getChildren().clear(); // Clear existing items in GridPane
            AtomicInteger row = new AtomicInteger(1); // Start from the second row (index 1)
            resources.forEach(ressource -> addResourceToGrid(ressource, row.getAndIncrement()));
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception gracefully
        }
    }

    private void addResourceToGrid(Ressources ressources, int row) {
        // Titre
        Label typeLabel = new Label(ressources.getType_r());
        gridPane.add(typeLabel, 0, row);

        Label titleLabel = new Label(ressources.getTitre_r());
        gridPane.add(titleLabel, 1, row);

        Label descriptionLabel = new Label(ressources.getDescription());
        gridPane.add(descriptionLabel, 2, row);

        // Image (Si besoin d'afficher une image, configurez-la dans ImageView)
        ImageView imageView = new ImageView(new Image(ressources.getUrl()));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        gridPane.add(imageView, 4, row);

        //Actions (Supprimer et Mettre à jour)
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteResource(ressources));
        gridPane.add(deleteButton, 5, row);

        Button updateButton = new Button("Update");
        updateButton.setOnAction(event -> updateResource(ressources));
        gridPane.add(updateButton, 6, row);
    }

    private void deleteResource(Ressources ressources) {
        try {
            service.supprimer(ressources.getId());
            Platform.runLater(this::loadResources); // Refresh the resource list after deletion
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception gracefully
        }
    }

    private void updateResource(Ressources ressources) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifierRes.fxml"));
            Parent modif = loader.load();

            ModifierResController controller = loader.getController();
            controller.initData(ressources); // Pass selected resource to ModifierResController

            Stage updateStage = new Stage();
            updateStage.setScene(new Scene(modif));
            updateStage.setTitle("Modifier res");
            updateStage.showAndWait();

            Platform.runLater(this::loadResources); // Refresh the resource list after update
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception gracefully
        }
    }

    private void filterResources(String searchText) throws SQLException {
        ObservableList<Ressources> resources = FXCollections.observableArrayList(service.afficher());
        gridPane.getChildren().clear(); // Clear existing items in GridPane
        AtomicInteger row = new AtomicInteger(1); // Start from the second row (index 1)

        resources.stream()
                .filter(ressource -> ressource.getTitre_r().toLowerCase().contains(searchText.toLowerCase()))
                .forEach(ressource -> addResourceToGrid(ressource, row.getAndIncrement()));
    }
}
