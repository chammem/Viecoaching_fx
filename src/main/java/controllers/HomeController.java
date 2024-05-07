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
