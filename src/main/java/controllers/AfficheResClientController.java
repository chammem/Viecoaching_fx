package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;
import entities.Ressources;
import services.ServiceRessource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class AfficheResClientController implements Initializable {

    @FXML
    private VBox contentVBox;

    private ServiceRessource service;
    @FXML
    private TextField tText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        service = new ServiceRessource(); // Initialize the resource service
        loadResources(); // Load and display resources
        tText.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                filterResources(newValue);
            } catch (SQLException e) {
                System.err.println("Error filtering resources: " + e.getMessage());
            }
        });
    }

    private void filterResources(String searchText) throws SQLException {
        ObservableList<Ressources> resources = FXCollections.observableArrayList(service.afficher());
        contentVBox.getChildren().clear(); // Clear existing items in VBox
        AtomicInteger row = new AtomicInteger(1); // Start from the first row

        resources.stream()
                .filter(resource -> resource.getType_r().toLowerCase().contains(searchText.toLowerCase()))
                .forEach(resource -> {
                    try {
                        VBox card = createResourceCard(resource);
                        contentVBox.getChildren().add(card);
                    } catch (IOException e) {
                        System.err.println("Error creating resource card: " + e.getMessage());
                    }
                });
    }

    private void loadResources() {
        try {
            for (Ressources resource : service.afficher()) {
                VBox card = createResourceCard(resource);
                contentVBox.getChildren().add(card);
            }
        } catch (SQLException e) {
            System.err.println("Error loading resources: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private VBox createResourceCard(Ressources resource) throws IOException {
        VBox card = new VBox(10); // VBox for the card with spacing
        card.getStyleClass().add("resource-card"); // Apply CSS style for the card

        String imageUrl = resource.getUrl();
        ImageView imageView;

        try {
            imageView = new ImageView(new Image(imageUrl));
            imageView.setFitWidth(250);
            imageView.setFitHeight(250);
        } catch (Exception e) {
            System.err.println("Error loading image for resource: " + e.getMessage());
            imageView = new ImageView(); // Empty image view
            imageView.setFitWidth(250);
            imageView.setFitHeight(250);
        }

        card.getChildren().add(imageView);
        card.getChildren().add(new Label(resource.getTitre_r())); // Title
        card.getChildren().add(new Label(resource.getType_r())); // Type
        card.getChildren().add(new Label(resource.getDescription())); // Description

        Rating rating = new Rating();
        rating.setRating(resource.getRating());
        card.getChildren().add(rating);

        Label ratingLabel = new Label("Notation: " + resource.getRating());
        card.getChildren().add(ratingLabel);

        rating.ratingProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newRatingValue = newValue.intValue();
                service.updateRating(resource.getId(), newRatingValue);
                resource.setRating(newRatingValue);
                ratingLabel.setText("Notation: " + newRatingValue);
            } catch (SQLException ex) {
                System.err.println("Error updating resource rating: " + ex.getMessage());
            }
        });

        return card;
    }
}
