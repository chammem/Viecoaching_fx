package controllers.Rubrique;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController {
    @FXML
    private BorderPane bp;
    private void loadPage(String page) {

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(page+".fxml"));
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        bp.setCenter(root);

    }

    public void AllBlogs(ActionEvent actionEvent) {
        loadPage("/fxml/Rubrique/Commentaire");
    }

    public void btnAddBlog(ActionEvent actionEvent) {
        loadPage("/fxml/Rubrique/Rubrique");
    }

    public void btnDashboard(ActionEvent actionEvent) {
        loadPage("/fxml/Rubrique/Main");
    }
}
