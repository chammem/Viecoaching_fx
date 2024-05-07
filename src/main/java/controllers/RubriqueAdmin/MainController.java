package controllers.RubriqueAdmin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Commentaire;
import entities.Rubrique;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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


public class MainController implements Initializable {

    public VBox vRubrique;
    public Label lblTitle;
    public Label lblidrubrique;
    @FXML
    private Button btnAllItems;

    @FXML
    private Button btnFavorites;

    @FXML
    private Button btnTrash;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnCard;

    @FXML
    private Button btnIdentity;

    @FXML
    private Button btnNote;

    @FXML
    private Button btnWork;

    @FXML
    private Button btnSocial;

    @FXML
    private Button btnPersonal;
    @FXML
    private Label lblCompanyName;



    @FXML
    private Label lblWebsite;

    @FXML
    private TextArea lblNotes;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField tfSearch;

    @FXML
    private ImageView btnAdd;

    @FXML
    private VBox vItems;

    @FXML
    private Button btnGetEmoji;
    private Scene scene;

    private boolean[] isSelected;
    Commentaire selectedComment = null;

    @FXML
    void handleButtonClicks(ActionEvent event) {


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ShowRubriques();
        scene = vRubrique.getScene();

    }

    public void ShowRubriques(){
        try {
            ServiceRubrique rubriqueService =  new ServiceRubrique();
            ServiceUtilisateur u = new ServiceUtilisateur();
            List<Rubrique> rubriques = rubriqueService.listerRubrique();
            List<AppModel> apps = new ArrayList<>();
            vRubrique.getChildren().clear();

            for (Rubrique rubrique : rubriques) {
                Utilisateur user = u.trouverParId(rubrique.getAuteur_id());
                AppModel app = new AppModel(
                        String.valueOf(rubrique.getId()) ,
                        user.getPrenom()+" "+user.getNom(),
                        rubrique.getTitre(),
                        rubrique.getDatePublication().toString(),
                        rubrique.getContenu()
                );
                apps.add(app);
            }

            Node[] nodes = new Node[rubriques.size()];

            for (int i = 0; i < nodes.length; i++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/fxml/RubriqueAdmin/mainitem.fxml"));
                nodes[i] = loader.load();

                isSelected = new boolean[apps.size()];
                final int h = i;
                MainItemController controller = loader.getController();
                controller.setItemInfo(apps.get(i).getAuteur(), apps.get(i).getTitle(), apps.get(i).getId());

                nodes[i].setOnMouseEntered(evt -> {
                    if (!isSelected[h]) {
                        nodes[h].setStyle("-fx-background-color: #899b77");
                    }
                });

                nodes[i].setOnMouseExited(evt -> {
                    if (!isSelected[h]) {
                        nodes[h].setStyle("-fx-background-color: rgba(129,170,255,0.1)");
                    }
                });

                nodes[i].setOnMousePressed(evt -> {
                    Arrays.fill(isSelected, Boolean.FALSE);
                    isSelected[h] = true;

                    for (Node n : nodes) {
                        n.setStyle("-fx-background-color: rgba(129,170,255,0.1)");
                    }

                    nodes[h].setStyle("-fx-background-color: #899b77");
                    lblCompanyName.setText(apps.get(h).getAuteur());
                    ShowCommentaires(Integer.valueOf(apps.get(h).getId()));
                    lblidrubrique.setText(apps.get(h).getId());
                    lblTitle.setText(apps.get(h).getTitle());
                    lblWebsite.setText(apps.get(h).getDate_publication());
                    lblNotes.setText(apps.get(h).getContenu());
                    lblNotes.setWrapText(true);
                });

                vRubrique.getChildren().add(nodes[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
   /* public void ShowRubriques() {
        try {
            ServiceRubrique rubriqueService = new ServiceRubrique();
            ServiceUtilisateur u = new ServiceUtilisateur();
            List<Rubrique> rubriques = rubriqueService.listerRubrique();
            List<AppModel> apps = new ArrayList<>();
            vRubrique.getChildren().clear();
            scene = vRubrique.getScene(); // Initialize the scene variable here

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

            for (int i = 0; i < nodes.length; i++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/fxml/RubriqueAdmin/mainitem.fxml"));
                nodes[i] = loader.load();

                isSelected = new boolean[apps.size()];
                final int h = i;
                MainItemController controller = loader.getController();
                controller.setItemInfo(apps.get(i).getAuteur(), apps.get(i).getTitle(), apps.get(i).getId());

                nodes[i].setOnMouseEntered(evt -> {
                    if (!isSelected[h]) {
                        nodes[h].setStyle("-fx-background-color: #899b77");
                    }
                });

                nodes[i].setOnMouseExited(evt -> {
                    if (!isSelected[h]) {
                        nodes[h].setStyle("-fx-background-color: rgba(129,170,255,0.1)");
                    }
                });

                nodes[i].setOnMousePressed(evt -> {
                    Arrays.fill(isSelected, Boolean.FALSE);
                    isSelected[h] = true;

                    for (Node n : nodes) {
                        n.setStyle("-fx-background-color: rgba(129,170,255,0.1)");
                    }

                    nodes[h].setStyle("-fx-background-color: #899b77");
                    lblCompanyName.setText(apps.get(h).getAuteur());
                    ShowCommentaires(Integer.valueOf(apps.get(h).getId()));
                    lblidrubrique.setText(apps.get(h).getId());
                    lblTitle.setText(apps.get(h).getTitle());
                    lblWebsite.setText(apps.get(h).getDate_publication());
                    lblNotes.setText(apps.get(h).getContenu());
                    lblNotes.setWrapText(true);
                });

                vRubrique.getChildren().add(nodes[i]);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

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
                loader.setLocation(Main.class.getResource("/fxml/RubriqueAdmin/mainitem.fxml"));
                Node node = loader.load();
                MainItemController controller = loader.getController();
                controller.setItemInfo(app.getAuteur(), app.getTitle(), app.getId());


                controller.setIsComment(true); // Set the additional buttons in the controller

                vItems.getChildren().add(node);
            }
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


    }*/





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

}


