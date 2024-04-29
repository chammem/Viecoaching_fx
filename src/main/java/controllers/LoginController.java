package controllers;

import entities.Utilisateur;
import utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import services.ServiceAuthentication;
import utils.MyDatabase;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField enterPasswordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registreButton;
    @FXML
    private ImageView image1View;
    @FXML
    private ImageView userView;
    @FXML
    private ImageView lockView;
    private ServiceAuthentication authService;
    private Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        authService = new ServiceAuthentication();

        File image1File = new File("images/image1.PNG");
        Image image1 = new Image(image1File.toURI().toString());
        image1View.setImage(image1);

        File userFile = new File("images/image3.png");
        Image user = new Image(userFile.toURI().toString());
        userView.setImage(user);

        File lockFile = new File("images/image2.png");
        Image lock = new Image(lockFile.toURI().toString());
        lockView.setImage(lock);

        loginButton.setOnAction(actionEvent -> handleLogin());
        registreButton.setOnAction(actionEvent -> handleRegister());


    }

    @FXML
    private void handleLogin() {
        String email = emailTextField.getText();
        String password = enterPasswordField.getText();

        try {
            MyDatabase myDatabase = MyDatabase.getInstance();
            Connection connection = myDatabase.getConnection();
            Utilisateur utilisateur = authService.login(email, password);

            if (utilisateur != null) {
                // Démarrer une session avec l'utilisateur connecté
                SessionManager.startSession(utilisateur);
                showAlert(Alert.AlertType.CONFIRMATION, "Connexion réussie", "Bienvenue " + utilisateur.getNom() + " " + utilisateur.getPrenom());
                // Rediriger vers la page utilisateur
                navigateToUtilisateurView();
                Stage stageConnexion = (Stage) enterPasswordField.getScene().getWindow();
                stageConnexion.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Adresse email ou mot de passe incorrect");
            }
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Erreur de validation", e.getMessage());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement de la page", "Impossible de charger la page utilisateur.");
            e.printStackTrace();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion à la base de données", "Impossible d'établir une connexion à la base de données.");
            e.printStackTrace();
        }
    }

    private void navigateToUtilisateurView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/utilisateur.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        // Fermer la fenêtre de connexion
        Stage stageConnexion = (Stage) enterPasswordField.getScene().getWindow();
        stageConnexion.close();
    }
    public void navigateToRegistreView() {
        try {MyDatabase myDatabase = MyDatabase.getInstance();
            Connection connection = myDatabase.getConnection();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registre.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) registreButton.getScene().getWindow(); // Récupère la fenêtre actuelle
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    @FXML
    private void handleRegister() {

        MyDatabase myDatabase = MyDatabase.getInstance();
        Connection connection = myDatabase.getConnection();
        navigateToRegistreView();

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }}
