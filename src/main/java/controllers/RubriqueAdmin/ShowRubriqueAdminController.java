package controllers.RubriqueAdmin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowRubriqueAdminController {
    @FXML
    private BorderPane bp;
    private void loadPage(String page) {

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(page+".fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ShowRubriqueAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        bp.setCenter(root);

    }

    public void AllBlogs(ActionEvent actionEvent) {
        loadPage("/fxml/Main");
    }

    public void btnAddBlog(ActionEvent actionEvent) {
        loadPage("/fxml/addRubrique");
    }

    public void btnDashboard(ActionEvent actionEvent) {
        loadPage("/fxml/AdminDash");
    }
}

