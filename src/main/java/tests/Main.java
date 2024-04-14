package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

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