package controllers.RubriqueClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.RubriqueAdmin.AppModel;
import controllers.RubriqueAdmin.MainItemController;
import entities.Commentaire;
import entities.Rubrique;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import javafx.scene.image.ImageView;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class RubriqueClientController {
  /*  @FXML
    private AnchorPane sideRubrique;
    @FXML
    private AnchorPane sideCoaching;

    @FXML
    private AnchorPane sideCommentaire;

    @FXML
    private VBox vRubrique;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtContenu;

    @FXML
    private VBox vItems;

    @FXML
    private TextField tfSearch;

    @FXML
    private ImageView btnAdd;

    @FXML
    private Button btnGetEmoji;
    @FXML
    private Button back;
    public Label lblidrubrique;
    public Label lblTitle;



    @FXML
    private Label lblWebsite;

    @FXML
    private TextArea lblNotes;
    private boolean[] isSelected;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sideCommentaire.setTranslateX(0);
        sideCoaching.setTranslateX(-sideCoaching.getWidth());
        ShowRubriques();
    }

    public void ShowRubriques(){
        try {
            ServiceRubrique rubriqueService = new ServiceRubrique();
            ServiceUtilisateur u = new ServiceUtilisateur();
            List<Rubrique> rubriques = rubriqueService.listerRubrique();
            List<AppModel> apps = new ArrayList<>();
            vRubrique.getChildren().clear();

            for (Rubrique rubrique : rubriques) {
                Utilisateur user = u.trouverParId(rubrique.getAuteur_id());
                AppModel app = new AppModel(
                        String.valueOf(rubrique.getId()),
                        user.getPrenom() + " " + user.getNom(),
                        rubrique.getTitre(),
                        rubrique.getDatePublication().toString(),
                        rubrique.getContenu()
                );
                apps.add(app);
            }

            Node[] nodes = new Node[rubriques.size()];
            isSelected = new boolean[apps.size()];

            for (int i = 0; i < nodes.length; i++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/fxml/RubriqueAdmin/mainitem.fxml"));
                nodes[i] = loader.load();

                final int h = i;
                MainItemController controller = loader.getController();
                controller.setItemInfo(apps.get(i).getAuteur(), apps.get(i).getTitle(), apps.get(i).getId());

                nodes[i].setOnMousePressed(evt -> {
                    Arrays.fill(isSelected, Boolean.FALSE);
                    isSelected[h] = true;

                    for (Node n : nodes) {
                        n.setStyle("-fx-background-color: rgba(129,170,255,0.1)");
                    }

                    nodes[h].setStyle("-fx-background-color: #899b77");
                    ShowCommentaires(Integer.valueOf(apps.get(h).getId()));

                    // Slide the sideCoaching pane to hide rubrique and show comment session
                    TranslateTransition slide = new TranslateTransition(Duration.millis(500), sideCoaching);
                    slide.setByX(-sideCoaching.getWidth()); // Slide by the width of sideCoaching
                    slide.play();
                });

                vRubrique.getChildren().add(nodes[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void ShowCommentaires(int rubriqueId) {
        try {
            ServiceUtilisateur u = new ServiceUtilisateur();
            ServiceCommentaire commentaireService = new ServiceCommentaire();
            List<Commentaire> commentaires = commentaireService.listerCommentaireByrubrique(rubriqueId);

            vItems.getChildren().clear(); // Clear existing nodes

            for (Commentaire commentaire : commentaires) {
                Utilisateur user = u.trouverParId(commentaire.getAuteur_id());
                AppModel app = new AppModel(
                        String.valueOf(commentaire.getId()),
                        commentaire.getContenu(),
                        user.getPrenom() + " " + user.getNom(),
                        commentaire.getDateCréation().toString(),
                        commentaire.getContenu()
                );

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/fxml/RubriqueClient/mainitemClient.fxml"));
                Node node = loader.load();
                MainItemClientController controller = loader.getController();
                controller.setItemInfo(app.getAuteur(), app.getTitle(), app.getId());
                vItems.getChildren().add(node);
            }

            // Show the sideComment pane
            sideCommentaire.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*public void btnAddComment(ActionEvent actionEvent) {
        String comment = tfSearch.getText().trim();

        if (comment.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Comment");
            alert.setContentText("Please enter a comment.");
            alert.showAndWait();
            return;
        }

        if (!lblidrubrique.getText().matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Rubrique ID");
            alert.setContentText("Please select a rubrique first.");
            alert.showAndWait();
            return;
        }


    }


    public void btnAddComment(ActionEvent actionEvent) {
        // Check if the comment field is not empty
        String comment = tfSearch.getText().trim();

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
            ShowCommentaires(rubriqueId);
            tfSearch.clear(); // Clear the comment field after successfully adding the comment

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
            tfSearch.appendText(emojiCharacter);

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void postRubrique(ActionEvent actionEvent) {
        // Check if the title and content fields are not empty
        String title = txtTitle.getText().trim();
        String content = txtContenu.getText().trim();

        if (title.isEmpty() || content.isEmpty()) {
            // Display an error message if any of the fields are empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Fields");
            alert.setContentText("Please fill in all the fields.");
            alert.showAndWait();
            return;
        }

        // Proceed with adding the Rubrique if all fields are filled
        Rubrique r = new Rubrique();
        r.setAuteur_id(Main.userid);
        r.setContenu(content);
        r.setDateCréation(Date.valueOf(LocalDate.now()));
        r.setDatePublication(Date.valueOf(LocalDate.now()));
        r.setTitre(title);
        r.setEtat("Published");

        ServiceRubrique service = new ServiceRubrique();
        service.ajouterRubrique(r);

        // Optionally, you can clear the input fields after successfully posting the Rubrique
        clearFields();
    }

    // Helper method to clear input fields after posting Rubrique
    private void clearFields() {
        txtTitle.clear();
        txtContenu.clear();
    }
    @FXML
    void back(ActionEvent event) {
        if (!lblidrubrique.getText().isEmpty()) {
            // Slide back and hide the comments panel
            TranslateTransition slide = new TranslateTransition(Duration.millis(500), sideCoaching);
            slide.setByX(sideCoaching.getWidth()); // Slide by the width of sideCoaching
            slide.play();
            sideCommentaire.setVisible(false);
        }
    }






*/}
