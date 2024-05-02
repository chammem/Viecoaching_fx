package controllers.RubriqueClient;

import entities.Commentaire;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import services.ServiceCommentaire;

import java.net.URL;
import java.util.ResourceBundle;

public class MainItemClientController implements Initializable {
    public Label lblidcomment;
    Boolean isComment = false;

    @FXML
    private Label lblAppName;

    @FXML
    private Label lblEmail;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @FXML
    private Label lblEmoji;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // You can add initialization code here if needed
    }

    public void setItemInfo(String appName, String appEmail , String id) {
        lblidcomment.setText(id);
        lblAppName.setText(appName);
        lblEmail.setText(appEmail);
    }


    }









