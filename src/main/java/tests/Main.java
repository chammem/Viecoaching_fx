package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
<<<<<<< HEAD
    public void start(Stage primaryStage) throws IOException {
        // Chargement du fichier FXML
        Parent root = FXMLLoader.load(getClass().getResource("/Affichegr.fxml"));
        // Création de la scène
        Scene scene = new Scene(root, 600, 400);
        // Configuration de la scène du stage avec la scène créée
        primaryStage.setScene(scene);
        // Définition du titre de la fenêtre principale
        primaryStage.setTitle("Gestion des groupes");
        // Affichage de la fenêtre principale
        primaryStage.show();
=======
    public void start(Stage stage) throws Exception {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
>>>>>>> 400a8ba329c716088c97e277c00b2f54dbc8e483
    }

    public static void main(String[] args) {
        launch(args);
    }
<<<<<<< HEAD
}
=======

  /*public static void main(String[] args) {
      ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();

      try {
          // Récupérer l'utilisateur avec l'ID 15 de la base de
          Utilisateur utilisateurAModifier = serviceUtilisateur.trouverParId(15);

          // Vérifier si l'utilisateur existe
          if (utilisateurAModifier != null) {
              // Afficher l'utilisateur avant la modification
              System.out.println("Utilisateur avant la modification : ");
              System.out.println(utilisateurAModifier);

              utilisateurAModifier.setEmail("javasinda@hotmail.fr");

              // Appeler la méthode de modification dans le service utilisateur
              serviceUtilisateur.modifier(utilisateurAModifier);

              // Afficher l'utilisateur après la modification
              System.out.println("Utilisateur après la modification : ");
              System.out.println(utilisateurAModifier);
          } else {
              System.out.println("L'utilisateur avec l'ID 15 n'existe pas.");
          }
      } catch (SQLException e) {
          System.out.println("Erreur : " + e.getMessage());
      }
}*/
}
>>>>>>> 400a8ba329c716088c97e277c00b2f54dbc8e483
