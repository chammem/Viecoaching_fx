package controllers;

import entities.Ressources;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.HBox;
import org.controlsfx.control.Rating;
import services.ServiceRessource;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AfficheResClientController implements Initializable {

    @FXML
    private TilePane tilePane;

    private ServiceRessource service;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        service = new ServiceRessource();
        loadResources();
    }

    private void loadResources() {
        try {
            for (Ressources ressource : service.afficher()) {
                HBox card = createResourceCard(ressource);
                // Set spacing and alignment for the card (optional)
                card.setSpacing(10);
                card.setAlignment(javafx.geometry.Pos.CENTER); // Adjust alignment as needed
                tilePane.getChildren().add(card);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading resources", e);
        }
    }

    private HBox createResourceCard(Ressources ressource) {
        HBox card = new HBox();
        card.getStyleClass().add("resource-card"); // Apply CSS style for the card

        ImageView imageView = new ImageView(new Image(ressource.getUrl()));
        imageView.setFitWidth(180);
        imageView.setFitHeight(180);

        Label titleLabel = new Label(ressource.getTitre_r());
        titleLabel.getStyleClass().add("card-title");

        Label descriptionLabel = new Label(ressource.getDescription());

        Rating rating = new Rating();
        double initialRating = ressource.getRating();
        rating.setRating(initialRating);

        rating.ratingProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newRatingValue = newValue.intValue();
                service.updateRating(ressource.getId(), newRatingValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        card.getChildren().addAll(imageView, titleLabel, descriptionLabel, rating);

        return card;
    }
}
