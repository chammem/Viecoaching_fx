package controllers.RubriqueAdmin;

import com.google.api.translate.Language;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import entities.Commentaire;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import services.ServiceCommentaire;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
    /*public void btnDelete(ActionEvent actionEvent) {*/
        /*ServiceCommentaire s =new ServiceCommentaire();
        s.deleteCommentaire(s.getCommentaireById(Integer.parseInt(lblidcomment.getText())));*/
        /*ServiceCommentaire s = new ServiceCommentaire();
        Commentaire commentaire = s.getCommentaireById(Integer.parseInt(lblidcomment.getText()));
        if (commentaire != null) {
            s.deleteCommentaire(commentaire);
        } else {
            // Handle the case where the commentaire is null
        }*/

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

    private String translateText(String text, Language targetLanguage) {
        // Construct the JSON payload
        String jsonPayload = String.format("{\"texts\": [\"%s\"], \"to\": [\"%s\"], \"from\": \"auto\"}", text, targetLanguage.toString().toLowerCase());

        // Create the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.lecto.ai/v1/translate/text"))
                .header("content-type", "application/json")
                .header("X-RapidAPI-Key", "RNFAEHF-STB4VY3-N5PC12J-D3TJA14")
                .header("X-RapidAPI-Host", "lecto-translation.p.rapidapi.com")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        // Send the request and process the response
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                // Parse the JSON response and extract the translated text
                // You'll need to implement this based on the structure of the response from the Lecto Translation API
                String translatedText = parseTranslatedText(response.body());
                return translatedText;
            } else {
                System.out.println("Translation request failed with status code: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseTranslatedText(String responseBody) {
        try {
            // Parse the JSON response
            JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();

            // Check if the expected JSON elements are present
            if (json.has("translations") && json.get("translations").isJsonArray()) {
                // Extract the translated text from the JSON object
                String translatedText = json.getAsJsonArray("translations").get(0).getAsJsonObject().get("translatedText").getAsString();
                return translatedText;
            } else {
                System.out.println("Unexpected JSON structure: " + responseBody);
                return null;
            }
        } catch (JsonParseException | IllegalStateException | NullPointerException e) {
            // Handle parsing errors
            e.printStackTrace();
            return null;
        }
    }
    @FXML
    void translateButton(ActionEvent event) {
        Language currentLanguage = getCurrentLanguage();
        Language nextLanguage = getNextLanguage(currentLanguage);
        translateUIToLanguage(nextLanguage);
        setCurrentLanguage(nextLanguage);
    }

    private Language currentLanguage = Language.ENGLISH; // Default language

    private Language getNextLanguage(Language currentLanguage) {
        switch (currentLanguage) {
            case ENGLISH:
                return Language.FRENCH;
            case FRENCH:
                return Language.ARABIC;
            case ARABIC:
                return Language.ENGLISH;
            default:
                return Language.ENGLISH;
        }
    }

    private Language getCurrentLanguage() {
        return currentLanguage;
    }

    private void setCurrentLanguage(Language language) {
        currentLanguage = language;
    }

    private void translateUIToLanguage(Language language) {
        // Translate UI elements to the specified language
        lblContenuCommentaire.setText(translateText(lblContenuCommentaire.getText(), language));
        lblDateCommentaire.setText(translateText(lblDateCommentaire.getText(), language));
        // Translate other UI elements as needed
    }
}










