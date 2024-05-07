package controllers;



import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import services.ServiceUtilisateur;
import utils.MyDatabase;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CoachController implements Initializable {
    private ServiceUtilisateur serviceUtilisateur;

    @FXML
    private VBox contentVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser le service utilisateur
        MyDatabase myDatabase = MyDatabase.getInstance();
        Connection connection = myDatabase.getConnection();
        this.serviceUtilisateur = new ServiceUtilisateur(connection);

        // Charger et afficher les coachs
        loadCoaches();
    }

    private void loadCoaches() {
        try {
            for (Utilisateur coach : serviceUtilisateur.getCoaches()) {
                // Créer une carte VBox pour afficher les informations du coach
                VBox card = createCoachCard(coach);
                card.getStyleClass().add("resource-card");
                contentVBox.getChildren().add(card); // Ajouter la carte au VBox de contenu
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des coachs", e);
        }
    }

    private VBox createCoachCard(Utilisateur coach) {
        VBox card = new VBox(10); // VBox pour la carte avec un espacement
        card.getStyleClass().add("coach-card"); // Appliquer le style CSS pour la carte

        // ImageView pour l'image du coach
        ImageView imageView = new ImageView();
        imageView.setFitWidth(250);
        imageView.setFitHeight(250);

        // Charger l'image du coach à partir de la méthode loadImage de ServiceUtilisateur
        Image coachImage = serviceUtilisateur.loadImage(coach.getImage());
        imageView.setImage(coachImage);

        card.getChildren().add(imageView);

        // Ajouter le nom du coach
        Label nameLabel = new Label(coach.getNom());
        nameLabel.getStyleClass().add("card-title"); // Appliquer le style CSS pour le nom
        card.getChildren().add(nameLabel);

        // Ajouter l'email du coach
        Label emailLabel = new Label(coach.getEmail());
        card.getChildren().add(emailLabel);

        return card;
    }
}