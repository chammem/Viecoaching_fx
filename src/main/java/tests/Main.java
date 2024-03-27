package tests;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        // Création d'un label avec le texte "Hello World"
        Label l = new Label("Hello, JavaFX" + javafxVersion + ", running on Java " + javaVersion + ".");
        // Création d'une scène avec la disposition de pile (stack pane) comme racine et de dimensions 300x200
        Scene scene = new Scene(new StackPane(l), 640, 480);
        // Configuration de la scène du stage (fenêtre principale) avec la scène créée
        stage.setScene(scene);
        // Affichage de la fenêtre principale
        stage.show();
    }

    public static void main(String[] args) {
        // Lancement de l'application JavaFX
        launch();
    }
}