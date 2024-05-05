package controllers.RubriqueAdmin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Commentaire;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import services.ServiceCommentaire;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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

    @FXML
    private Label lblEmoji;
    @FXML
    private Button likeButton;

    @FXML
    private Button dislikeButton;
    @FXML
    private Label lblLikes;

    @FXML
    private Label lblDislikes;
    Commentaire selectedComment = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblLikes.setText("0");
        lblDislikes.setText("0");
        updateLikesAndDislikes();
    }

   /* public void setItemInfo(String appName, String appEmail , String id) {
        lblidcomment.setText(id);
        lblAppName.setText(appName);
        lblEmail.setText(appEmail);
    }*/
   public void setItemInfo(String appName, String appEmail, String id) {
       lblidcomment.setText(id);
       lblAppName.setText(appName);
       lblEmail.setText(appEmail);
       updateLikesAndDislikes();
   }

    public void setIsComment(Boolean isComment) {

        this.isComment = isComment;

        if (isComment) {
            // Make delete button visible only if it's a comment
            btnDelete.setVisible(true);
            likeButton.setVisible(true);
            dislikeButton.setVisible(true);
            lblLikes.setVisible(true);
            lblDislikes.setVisible(true);
        } else {
            // If it's not a comment, hide the delete button
            btnDelete.setVisible(false);
            likeButton.setVisible(false);
            dislikeButton.setVisible(false);
            lblLikes.setVisible(false);
            lblDislikes.setVisible(false);
        }
        }
    public void btnDelete(ActionEvent actionEvent) {
        /*ServiceCommentaire s =new ServiceCommentaire();
        s.deleteCommentaire(s.getCommentaireById(Integer.parseInt(lblidcomment.getText())));*/
        ServiceCommentaire s = new ServiceCommentaire();
        Commentaire commentaire = s.getCommentaireById(Integer.parseInt(lblidcomment.getText()));
        if (commentaire != null) {
            s.deleteCommentaire(commentaire);
        } else {
            // Handle the case where the commentaire is null
        }

    }
    private void updateLikesAndDislikes() {
        lblLikes.setText(String.valueOf(selectedComment.getLikes()));
        lblDislikes.setText(String.valueOf(selectedComment.getDislikes()));
    }
    private ServiceCommentaire getServiceCommentaire() {
        return new ServiceCommentaire();
    }
    @FXML
    void likeButtonClicked(ActionEvent event) {
        int commentId = Integer.parseInt(lblidcomment.getText());
        Commentaire selectedComment = getServiceCommentaire().getCommentaireById(commentId);
        if (selectedComment != null) {
            ServiceCommentaire.likeCommentaire(selectedComment);
            updateLikesAndDislikes();
        }
    }

    @FXML
    void dislikeButtonClicked(ActionEvent event) {
        int commentId = Integer.parseInt(lblidcomment.getText());
        Commentaire selectedComment = getServiceCommentaire().getCommentaireById(commentId);
        if (selectedComment != null) {
            ServiceCommentaire.dislikeCommentaire(selectedComment);
            updateLikesAndDislikes();
        }
    }
}




