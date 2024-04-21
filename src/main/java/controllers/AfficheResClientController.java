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
        rating.setRating(3); // Example rating, you can bind this to ressource rating
        card.getChildren().add(rating);

        Label msg =new Label();
        card.getChildren().add(msg);
        rating.ratingProperty().addListener((observable, oldValue, newValue) -> {
            msg.setText("Rating : " + newValue.intValue()); // Met à jour le texte du label
        });

        // Image
        ImageView imageView = new ImageView(new Image(ressource.getUrl()));
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        card.getChildren().add(imageView);

        return card;
    }
}
