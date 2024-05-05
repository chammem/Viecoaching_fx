package tests;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class Main extends Application {
    @Override
  //  public void start(Stage stage) throws Exception {
        //Parent root;
        //root = FXMLLoader.load(getClass().getResource("/fxml/afficheRessource.fxml"));
      //  Scene scene = new Scene(root);
    public void start(Stage stage) throws Exception {
      Parent root;
        root = FXMLLoader.load(getClass().getResource("/fxml/Groupe.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
      //  stage.setTitle("Bibliothèque de  Ressources");
        stage.setTitle("Login");
        stage.show();
    }

    public static void main(String[] args) {

       // launch(args);
        launch(args);
    }

}

      
