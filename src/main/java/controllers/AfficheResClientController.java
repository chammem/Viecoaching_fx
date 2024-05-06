package controllers;

import entities.Ressources;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;
import services.ServiceRessource;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AfficheResClientController implements Initializable {

    @FXML
    private VBox contentVBox;

    private ServiceRessource service;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the resource service
        service = new ServiceRessource();

        // Load and display resources
        loadResources();
    }

    private void loadResources() {
        try {
            for (Ressources ressource : service.afficher()) {
                // Create a card VBox to hold resource information
                VBox card = createResourceCard(ressource);
                contentVBox.getChildren().add(card); // Add the card to the content VBox
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading resources", e);
        }
    }

    private VBox createResourceCard(Ressources ressource) {
        VBox card = new VBox(10); // VBox for the card with spacing
        card.getStyleClass().add("resource-card"); // Apply CSS style for the card

        String imageUrl = ressource.getUrl();
        ImageView imageView;

        try {
            // Attempt to load the image
            imageView = new ImageView(new Image(imageUrl));
            imageView.setFitWidth(250);
            imageView.setFitHeight(250);
        } catch (IllegalArgumentException e) {
            // Handle invalid URL or resource not found
            System.err.println("Error loading image for resource: " + e.getMessage());
            // Display a default image or error message
            imageView = new ImageView(); // Empty image view
            imageView.setFitWidth(350);
            imageView.setFitHeight(350);
        }

        card.getChildren().add(imageView);

        // Title
        Label titleLabel = new Label(ressource.getTitre_r());
        titleLabel.getStyleClass().add("card-title"); // Apply CSS style for the title
        card.getChildren().add(titleLabel);

        // Type
        Label typeLabel = new Label(ressource.getType_r());
        card.getChildren().add(typeLabel);

        // Description
        Label descriptionLabel = new Label(ressource.getDescription());
        card.getChildren().add(descriptionLabel);

        // Rating
        Rating rating = new Rating();
        rating.setRating(ressource.getRating()); // Set initial rating based on resource data
        card.getChildren().add(rating);

        // Label for displaying rating value
        Label ratingLabel = new Label("Notation : " + ressource.getRating());
        card.getChildren().add(ratingLabel);

        // Update resource rating when user changes the rating
        rating.ratingProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newRatingValue = newValue.intValue();
                service.updateRating(ressource.getId(), newRatingValue);
                ressource.setRating(newRatingValue); // Update resource rating locally
                ratingLabel.setText("Notation : " + newRatingValue); // Update displayed rating value
            } catch (SQLException ex) {
                throw new RuntimeException("Error updating resource rating", ex);
            }
        });

        return card;
    }


}