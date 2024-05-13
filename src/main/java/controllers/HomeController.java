package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private HBox hbox;

    @FXML
    private VBox vbox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    void SideBlog(ActionEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/afficheResClient.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vbox.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vbox.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }
    }

    @FXML
    void openLogin(MouseEvent event) { // Changer le type d'événement en MouseEvent
        try {
            // Charger login.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent loginParent = loader.load();

            // Créer une nouvelle scène
            Scene loginScene = new Scene(loginParent);

            // Obtenir la fenêtre principale à partir de n'importe quel élément racine
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Définir la scène sur la fenêtre principale
            primaryStage.setScene(loginScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();  // Gérer IOException (par exemple, fichier introuvable ou FXML invalide)
        }
    }

    @FXML
    void SideCoach(ActionEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/coach.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vbox.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vbox.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }

    }

    @FXML
    void SideContact(ActionEvent event) {

        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/calendar.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vbox.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vbox.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }


    }

    @FXML
    void SideGroupe(ActionEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Affichep.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vbox.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vbox.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }
    }
}
