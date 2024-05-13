package controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import entities.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceUtilisateur;
import utils.MyDatabase;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class AjouterUserController implements Initializable {
    @FXML
    public Button ajouterId;
    @FXML
    public ImageView imageId;
    @FXML
    public Button insertId;
    @FXML
    public TextField nomId;
    @FXML
    public TextField prenomId;
    @FXML
    public TextField emailId;
    @FXML
    public PasswordField mdpId;
    @FXML
    public PasswordField confId;
    @FXML
    public TextField VilleId;
    @FXML
    public TextField numeroId;
    public RadioButton hommeId;
    public RadioButton femmeId;
    public ComboBox RoleId;
    public Button supprimerId;
    private ServiceUtilisateur serviceUtilisateur;
    private Utilisateur utilisateurInitial;
    private String nomFichierImageSelectionne;

    private static final String IMAGE_DIRECTORY = "src/main/resources/photos";
    private Cloudinary cloudinary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MyDatabase myDatabase = MyDatabase.getInstance();
        Connection connection = myDatabase.getConnection();
        this.serviceUtilisateur = new ServiceUtilisateur(connection);

        controleBtnRadio();
        // Initialisation de Cloudinary avec vos identifiants
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dppj3e5cp",
                "api_key", "647876725794588",
                "api_secret", "yQBtTPY_dzeUjCcUHQcmHvJevgg"));
        //ajouter les roles
        RoleId.getItems().add("ROLE_PATIENT");
        RoleId.getItems().add("ROLE_COACH");
        RoleId.getItems().add("ROLE_ADMIN");

        insertId.setOnAction(actionEvent -> selectImage());
        ajouterId.setOnAction(actionEvent -> handleAjouterUtilisateur());
        supprimerId.setOnAction(event -> {
            // Fermer la fenêtre actuelle
            Stage stage = (Stage) supprimerId.getScene().getWindow();
            stage.close();

            // Charger et afficher la nouvelle fenêtre (utilisateur.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
            Parent root;
            try {
                root = loader.load();
                Stage utilisateurStage = new Stage();
                utilisateurStage.setScene(new Scene(root));
                utilisateurStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Créer un InputStream à partir du fichier sélectionné
                InputStream imageStream = new FileInputStream(selectedFile);

                // Enregistrer le chemin du fichier sélectionné
                nomFichierImageSelectionne = selectedFile.getAbsolutePath();

                // Afficher l'image sélectionnée dans l'interface utilisateur
                Image image = new Image(selectedFile.toURI().toString());
                imageId.setImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Fichier introuvable", "Le fichier sélectionné est introuvable.");
            }
        }
    }
    private String uploadImage(InputStream imageStream, String nomFichierImage) throws IOException {
        // Créer un tableau de bytes pour contenir le contenu du flux d'entrée
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = imageStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        byte[] imageData = byteArrayOutputStream.toByteArray();

        // Charger l'image vers Cloudinary
        Map<String, Object> uploadResult = cloudinary.uploader().upload(imageData, ObjectUtils.asMap(
                "folder", "votre_dossier_images",
                "public_id", nomFichierImage));

        // Retourner l'URL de l'image téléchargée
        return (String) uploadResult.get("url");
    }
    @FXML
    private void handleAjouterUtilisateur() {
        String nom = nomId.getText();
        String prenom = prenomId.getText();
        String email = emailId.getText();
        String mdp = mdpId.getText();
        String ville = VilleId.getText();
        String tel = numeroId.getText();
        String genre = hommeId.isSelected() ? "Homme" : (femmeId.isSelected() ? "Femme" : "");
        String roleString = RoleId.getValue().toString();
        int roleId = mapRoleToInt(roleString);

        // Appeler la méthode selectImage pour sélectionner une image
        selectImage();

        // Récupérer l'URL de l'image sélectionnée
        String imageUrl = null;

        // Si une image est sélectionnée, téléversez-la vers Cloudinary
        if (nomFichierImageSelectionne != null) {
            try {
                // Créer un InputStream à partir du fichier sélectionné
                InputStream imageStream = new FileInputStream(nomFichierImageSelectionne);

                // Si une image est sélectionnée, générez un nom de fichier unique
                String nomFichierImageSansExtension = "profile_" + System.currentTimeMillis();

                // Enregistrez l'image sur Cloudinary et récupérez son URL
                imageUrl = uploadImage(imageStream, nomFichierImageSansExtension);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'enregistrement de l'image", "Une erreur s'est produite lors de l'enregistrement de l'image.");
                return; // Arrêtez le processus si une erreur se produit lors de l'enregistrement de l'image
            }
        }

        // Créer un utilisateur avec les informations saisies et l'URL de l'image
        Utilisateur utilisateur = new Utilisateur(nom, prenom, email, mdp, ville, tel, genre, imageUrl, roleId);

        try {
            // Ajouter l'utilisateur dans la base de données
            MyDatabase myDatabase = MyDatabase.getInstance();
            Connection connection = myDatabase.getConnection();
            ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur(connection);
            serviceUtilisateur.ajouter(utilisateur);

            // Afficher une confirmation
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Utilisateur ajouté", "L'utilisateur a été ajouté avec succès.");
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout", "Une erreur s'est produite lors de l'ajout de l'utilisateur.");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Erreur de validation", "Erreur", e.getMessage());
        }
    }

    private void controleBtnRadio() {
        ToggleGroup genreToggleGroup = new ToggleGroup();
        hommeId.setToggleGroup(genreToggleGroup);
        femmeId.setToggleGroup(genreToggleGroup);

        hommeId.setOnAction(event -> {
            if (hommeId.isSelected()) {
                femmeId.setSelected(false);
            }
        });

        femmeId.setOnAction(event -> {
            if (femmeId.isSelected()) {
                hommeId.setSelected(false);
            }
        });    }
    private String mapIntToRole(int roleId) {
        switch (roleId) {
            case 1:
                return "ROLE_PATIENT";
            case 2:
                return "ROLE_COACH";
            case 3:
                return "ROLE_ADMIN";
            default:
                // Gérez le cas où aucun rôle valide n'est sélectionné
                return ""; // Ou une autre valeur par défaut appropriée
        }
    }
    private void clearFields() {
        nomId.clear();
        prenomId.clear();
        emailId.clear();
        mdpId.clear();
        confId.clear();
        VilleId.clear();
        numeroId.clear();
        hommeId.setSelected(false);
        femmeId.setSelected(false);
        RoleId.getSelectionModel().clearSelection();
        imageId.setImage(null);
    }


    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private int mapRoleToInt(String role) {
        switch (role) {
            case "ROLE_PATIENT":
                return 1;
            case "ROLE_COACH":
                return 2;
            case "ROLE_ADMIN":
                return 3;
            default:
                return -1;
        }
    }




}
