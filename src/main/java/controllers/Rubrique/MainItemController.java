package controllers.Rubrique;

import entities.Commentaire;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import services.ServiceCommentaire;

import java.net.URL;
import java.util.ResourceBundle;

public class MainItemController implements Initializable {
    public Label lblidcomment;
    Boolean isComment = false;

    @FXML
    private Label lblContenuCommentaire; //lblAppName;

    @FXML
    private Label lblDateCommentaire; //lblEmail;
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

   public void setItemInfo(String appName, String appEmail, String id) {
       lblidcomment.setText(id);
       lblContenuCommentaire.setText(appName);
       lblDateCommentaire.setText(appEmail);
       setSelectedComment(getServiceCommentaire().getCommentaireById(Integer.parseInt(id)));

   }

    public void setIsComment(Boolean isComment) {

        this.isComment = isComment;

        if (isComment) {
            // Make delete button visible only if it's a comment
            /*btnDelete.setVisible(true);*/
            likeButton.setVisible(true);
            dislikeButton.setVisible(true);
            lblLikes.setVisible(true);
            lblDislikes.setVisible(true);
        } else {
            // If it's not a comment, hide the delete button
           /* btnDelete.setVisible(false);*/
            likeButton.setVisible(false);
            dislikeButton.setVisible(false);
            lblLikes.setVisible(false);
            lblDislikes.setVisible(false);
        }
        }


    private ServiceCommentaire getServiceCommentaire() {
        return new ServiceCommentaire();
    }
    public void setSelectedComment(Commentaire comment) {
        this.selectedComment = comment;
        updateLikesAndDislikes();
    }

    @FXML
    void likeButtonClicked(ActionEvent event) {
        if (selectedComment != null) {
            getServiceCommentaire().likeCommentaire(selectedComment);
            updateLikesAndDislikes(); // Update the likes/dislikes display
        } else {
            // Handle the case where no comment is selected
        }
    }

    @FXML
    void dislikeButtonClicked(ActionEvent event) {
        if (selectedComment != null) {
            getServiceCommentaire().dislikeCommentaire(selectedComment);
            updateLikesAndDislikes(); // Update the likes/dislikes display
        } else {
            // Handle the case where no comment is selected
        }
    }

    private void updateLikesAndDislikes() {
        if (selectedComment == null) {
            lblLikes.setText("0");
            lblDislikes.setText("0");
            return;
        }        selectedComment = getServiceCommentaire().getCommentaireById(selectedComment.getId());
        lblLikes.setText(String.valueOf(selectedComment.getLikes()));
        lblDislikes.setText(String.valueOf(selectedComment.getDislikes()));
    }

}










