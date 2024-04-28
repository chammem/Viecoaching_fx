package controllers.RubriqueAdmin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import tests.Main;
import services.ServiceCommentaire;

import java.net.URL;
import java.util.ResourceBundle;

public class MainItemController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // You can add initialization code here if needed
    }

    public void setItemInfo(String appName, String appEmail , String id) {
        lblidcomment.setText(id);
        lblAppName.setText(appName);
        lblEmail.setText(appEmail);
    }

    public void setIsComment(Boolean isComment) {

        this.isComment = isComment;

        if (isComment) {
            // Make delete button visible only if it's a comment
            btnDelete.setVisible(true);

        } else {
            // If it's not a comment, hide the delete button
            btnDelete.setVisible(false);
        }


    }

    public void btnDelete(ActionEvent actionEvent) {
        ServiceCommentaire s =new ServiceCommentaire();
        s.deleteCommentaire(s.getCommentaireById(Integer.parseInt(lblidcomment.getText())));

    }
}
