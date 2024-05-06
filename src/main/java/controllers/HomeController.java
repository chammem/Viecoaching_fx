package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    void SideCoach(ActionEvent event) {

    }

    @FXML
    void SideContact(ActionEvent event) {

    }

    @FXML
    void SideGroupe(ActionEvent event) {

    }
}
