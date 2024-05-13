package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class  Main extends Application {

        @Override
        public void start(Stage stage) throws Exception {
            Parent root;
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Home.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        }
  

        public static void main(String[] args) {
            launch(args);
        }


  

}
