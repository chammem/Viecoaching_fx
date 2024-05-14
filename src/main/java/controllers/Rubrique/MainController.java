package controllers.Rubrique;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Commentaire;
import entities.Rubrique;
import entities.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.ServiceCommentaire;
import services.ServiceRubrique;
import services.ServiceUtilisateur;
import tests.Main;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;


public class MainController  {/*

    @FXML
    private Label lblDate;

    @FXML
    private TextArea lblContenu;

    @FXML
    private Label lblUser;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblidrubrique;

    @FXML
    private TextField txtCommentaire;

    @FXML
    private VBox vItems;

    @FXML
    private Button btnGetEmoji;
    @FXML
    private ListView<Rubrique> lvRubriques;
    private Scene scene;

    private boolean[] isSelected;
    Commentaire selectedComment = null;
    int rubriqueId = 0;

    @FXML
    void handleButtonClicks(ActionEvent event) {



    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ShowRubriques();

    }

    public void ShowRubriques() {
        ServiceRubrique rubriqueService =  new ServiceRubrique();
        ServiceUtilisateur u = new ServiceUtilisateur();
        List<Rubrique> rubriques = rubriqueService.listerRubrique();

        ObservableList<Rubrique> rubriqueItems = FXCollections.observableArrayList(rubriques);

        lvRubriques.setItems(rubriqueItems);

        // Set the cell factory for the list view
        lvRubriques.setCellFactory(param -> new ListCell<Rubrique>() {
            @Override
            protected void updateItem(Rubrique item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    Utilisateur user = null;
                    try {
                        user = u.trouverParId(item.getAuteur_id());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    setText(user.getPrenom() + " " + user.getNom() + " - " + item.getTitre());
                }
            }
        });

        // Add the selection listener for the list view
        lvRubriques.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Utilisateur user = null;
                try {
                    user = u.trouverParId(newValue.getAuteur_id());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                lblUser.setText(user.getPrenom() + " " + user.getNom());
                // Update the rubriqueId with the selected rubrique's ID
                rubriqueId = newValue.getId();
                ShowCommentaires();
                lblidrubrique.setText(String.valueOf(newValue.getId()));
                lblTitle.setText(newValue.getTitre());
                lblDate.setText(newValue.getDatePublication().toString());
                lblContenu.setText(newValue.getContenu());
                lblContenu.setWrapText(true);
            }
        });
    }
    public void ShowCommentaires() {
        // Assuming rubriqueId is correctly set to the selected rubrique's ID
        ServiceRubrique rubriqueService = new ServiceRubrique();
        ServiceUtilisateur u = new ServiceUtilisateur(); // Declare u here
        ServiceCommentaire commentaireService = new ServiceCommentaire();
        List<Commentaire> commentaires = commentaireService.listerCommentaireByrubrique(rubriqueId);

        vItems.getChildren().clear(); // Clear existing nodes

        for (Commentaire commentaire : commentaires) {
            try {
                // Load the FXML file for MainItemController
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Rubrique/MainItem.fxml")); // Adjust the path as necessary
                Node node = loader.load();

                // Get the MainItemController instance
                MainItemController controller = loader.getController();

                // Set the comment information
                Utilisateur user = u.trouverParId(commentaire.getAuteur_id());
                controller.setItemInfo(
                        commentaire.getContenu(),  // appName
                        user.getPrenom() + " " + user.getNom(),  // appEmail
                        String.valueOf(commentaire.getId())
                );

                // Set whether it's a comment or not
                controller.setIsComment(true);

                // Add the comment item to the container
                vItems.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void displayComments(List<Commentaire> commentaires) {
        // Clear existing comments
        vItems.getChildren().clear();

        // Display each comment in the UI
        for (Commentaire commentaire : commentaires) {
            // Create a Label for the comment text
            Label commentLabel = new Label(commentaire.getContenu());
            commentLabel.setWrapText(true); // Wrap text if it's too long

            // Create a Label for the comment date
            Label dateLabel = new Label(commentaire.getDateCréation().toString());

            // Create a HBox to hold the comment and date labels
            HBox commentBox = new HBox(10); // 10 is the spacing between components
            commentBox.getChildren().addAll(commentLabel, dateLabel);

            // Add the comment box to the vItems container
            vItems.getChildren().add(commentBox);
        }
    }
    public void btnAddComment(ActionEvent actionEvent) {
        // Check if the comment field is not empty
        String comment = txtCommentaire.getText().trim();

        if (comment.isEmpty()) {
            // Display an error message if the comment field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Comment");
            alert.setContentText("Please enter a comment.");
            alert.showAndWait();
            return;
        }

        if (!lblidrubrique.getText().matches("\\d+")) {
            // Display an error message if the Rubrique ID is invalid
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Rubrique ID");
            alert.setContentText("Please select a rubrique first.");
            alert.showAndWait();
            return;
        }

        try {
            // Make API call to Profanity Filter API
            String apiKey = "wrrEHRZf+PUe2znFy2qN4g==CUx3p0aECPs71og7"; // Replace "YOUR_API_KEY" with your actual API key
            String urlString = "https://api.api-ninjas.com/v1/profanityfilter?text=" + comment;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("X-Api-Key", apiKey);

            // Get API response
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseStream);

            // Check if the comment contains profanity
            boolean hasProfanity = root.path("has_profanity").asBoolean();
            if (hasProfanity) {
                // Display a warning message if the comment contains profanity
                Alert alert = new Alert(Alert.AlertType.WARNING, "Votre commentaire contient des termes vulgaires !");
                alert.showAndWait();
                return;
            }

            // If the comment is valid, proceed with adding it
            int rubriqueId = Integer.parseInt(lblidrubrique.getText()); // Get the Rubrique ID from the label
            ServiceCommentaire commentaireService = new ServiceCommentaire();
            Commentaire c = new Commentaire(rubriqueId, Main.userid, comment, Date.valueOf(LocalDate.now()), selectedComment.getLikes(), selectedComment.getDislikes());
            commentaireService.ajouterCommentaire(c);
            ShowCommentaires();
            txtCommentaire.clear(); // Clear the comment field after successfully adding the comment

        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace();
            // Display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("API Error");
            alert.setContentText("An error occurred while processing the API request.");
            alert.showAndWait();
        }
    }
    @FXML
    void btnGetEmojiClicked(ActionEvent event) {
        try {
            // Encode the emoji name to handle spaces and special characters
            String emojiName = "slightly smiling face";
            String encodedName = URLEncoder.encode(emojiName, "UTF-8");

            // Create the URL for the API request
            URL apiUrl = new URL("https://api.api-ninjas.com/v1/emoji?name=" + encodedName);

            // Open a connection to the API URL
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestProperty("accept", "application/json");

            // Set the API key
            connection.setRequestProperty("X-Api-Key", "wrrEHRZf+PUe2znFy2qN4g==CUx3p0aECPs71og7");

            // Read the response from the API
            InputStream responseStream = connection.getInputStream();

            // Parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseStream);

            // Extract the emoji character from the response
            String emojiCharacter = root.get(0).get("character").asText();

            // Append the emoji character to the comment input field
            txtCommentaire.appendText(emojiCharacter);

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
}


