package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Chargement du fichier FXML
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Groupe.fxml"));
        // Création de la scène
        Scene scene = new Scene(root, 600, 400);
        // Configuration de la scène du stage avec la scène créée
        primaryStage.setScene(scene);
        // Définition du titre de la fenêtre principale
        primaryStage.setTitle("Gestion des groupes");
        // Affichage de la fenêtre principale
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Lancement de l'application JavaFX
        launch(args);
    }
}
