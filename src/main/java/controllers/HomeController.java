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

package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tests.Main;

public class HomeController {

    @FXML
    private Button playquizbtn;

    @FXML
    private void initialize() {

        playquizbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    Stage thisstage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    thisstage.close();
					FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("quiz.fxml"));
					Scene scene = new Scene(fxmlLoader.load());
					Stage stage = new Stage();
					stage.initStyle(StageStyle.TRANSPARENT);
					scene.setFill(Color.TRANSPARENT);
					stage.setScene(scene);
					stage.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });

    }

}
