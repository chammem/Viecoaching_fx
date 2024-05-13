package controllers;



import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import services.ServiceUtilisateur;
import utils.MyDatabase;
import java.io.*;

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
    private Cloudinary cloudinary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser le service utilisateur
        MyDatabase myDatabase = MyDatabase.getInstance();
        Connection connection = myDatabase.getConnection();
        this.serviceUtilisateur = new ServiceUtilisateur(connection);
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dppj3e5cp",
                "api_key", "647876725794588",
                "api_secret", "yQBtTPY_dzeUjCcUHQcmHvJevgg"));

        // Charger et afficher les coachs
        loadCoaches();
    }

    private void loadCoaches() {
        try {
            for (Utilisateur coach : serviceUtilisateur.getCoaches()) {
                // Créer une carte VBox pour afficher les informations du coach
                VBox card = createCoachCard(coach);
                card.getStyleClass().add("coach-card");
                contentVBox.getChildren().add(card); // Ajouter la carte au VBox de contenu
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des coachs", e);
        }
    }
    @FXML
    private VBox createCoachCard(Utilisateur coach) {
        VBox card = new VBox(5); // VBox pour la carte avec un espacement
        card.getStyleClass().add("coach-card"); // Appliquer le style CSS pour la carte

        // ImageView pour l'image du coach
        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);

        try {
            String imagePath = coach.getImage();
            Image coachImage = null; // Initialisation à null
            if (imagePath != null) { // Vérifier si le chemin d'accès n'est pas null
                if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                    // Charger l'image à partir de l'URL Cloudinary
                    coachImage = new Image(imagePath);
                } else {
                    // Charger l'image à partir du dossier local sur le PC
                    File imageFile = new File("C:/Users/LENOVO/Desktop/3A55/Pidev/viecoaching/public/images/user/" + imagePath);
                    if (imageFile.exists()) {
                        coachImage = new Image(imageFile.toURI().toString());
                    }
                }
            }
            imageView.setImage(coachImage);
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
            imageView.setImage(null); // Assurez-vous de définir l'image sur null en cas d'erreur
        }


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