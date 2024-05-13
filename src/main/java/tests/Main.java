package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    double x, y = 0;
    public static int userid = 1 ;
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Rubrique/home.fxml"));
        FXMLLoader loader = new FXMLLoader();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        /*Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        root.setOnMousePressed(evt -> {
            x = evt.getSceneX();
            y = evt.getSceneY();
        });
        root.setOnMouseDragged(evt -> {
            primaryStage.setX(evt.getScreenX() - x);
            primaryStage.setY(evt.getScreenY() - y);
        });

        primaryStage.show();*/
    }


    public static void main(String[] args) {
        launch(args);
}

}