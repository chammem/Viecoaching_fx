package controllers;

import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import services.ServiceAuthentication;
import utils.MyDatabase;
import utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

	@FXML
	private TextField emailTextField;
	@FXML
	private PasswordField enterPasswordField;
	@FXML
	private TextField captchaTextField;  // TextField to enter CAPTCHA
	@FXML
	private Button loginButton;
	@FXML
	private Button registreButton;
	@FXML
	private Button refreshCaptchaButton;
	@FXML
	private ImageView image1View;
	@FXML
	private ImageView userView;
	@FXML
	private ImageView lockView;
	@FXML
	private Text captchaIrenderView;
	private ServiceAuthentication authService;
	private Connection connection;
	private String currentCaptcha;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		authService = new ServiceAuthentication();

		// Loading images
		loadImage(image1View, "images/image1.PNG");
		loadImage(userView, "images/image3.png");
		loadImage(lockView, "images/image2.png");

		// Set actions for buttons
		loginButton.setOnAction(actionEvent -> handleLogin());
		registreButton.setOnAction(actionEvent -> navigateToRegistreView());


		generateAndDisplayCaptcha();  // Generate initial CAPTCHA
	}

	private void loadImage(ImageView imageView, String filePath) {
		File file = new File(filePath);
		Image image = new Image(file.toURI().toString());
		imageView.setImage(image);
	}
	@FXML
	void refreshCaptchaButton(ActionEvent event) {
generateAndDisplayCaptcha();
	}
	private void generateAndDisplayCaptcha() {
		currentCaptcha = generateRandomString(6);
		captchaIrenderView.setText("");
		captchaIrenderView.setText(currentCaptcha);
	}

	private String generateRandomString(int length) {
		String charList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder randStr = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(charList.length());
			char ch = charList.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}

	@FXML
	private void handleLogin() {
		String email = emailTextField.getText();
		String password = enterPasswordField.getText();
		String enteredCaptcha = captchaTextField.getText();

		if (!enteredCaptcha.equals(currentCaptcha)) {
			showAlert(Alert.AlertType.ERROR, "CAPTCHA Error", "CAPTCHA does not match.");
			return;
		}

		try {
			MyDatabase myDatabase = MyDatabase.getInstance();
			Connection connection = myDatabase.getConnection();
			Utilisateur utilisateur = authService.login(email, password);
			if (utilisateur != null) {
				SessionManager.startSession(utilisateur);
				showAlert(Alert.AlertType.CONFIRMATION, "Connexion réussie", "Bienvenue " + utilisateur.getNom() + " " + utilisateur.getPrenom());
				String userPage = utilisateur.getRole_id() == 1 ? "/fxml/Home.fxml" : utilisateur.getRole_id() == 2 ? "/fxml/Dashboard.fxml" : "/fxml/Dashboard.fxml";
				navigateToUtilisateurView(userPage);
			} else {
				showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Adresse email ou mot de passe incorrect");
			}
		} catch (Exception e) {
			showAlert(Alert.AlertType.ERROR, "Error", "A problem occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void navigateToUtilisateurView(String nameroot) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(nameroot));
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();

		// Close the current window
		Stage stageConnexion = (Stage) enterPasswordField.getScene().getWindow();
		stageConnexion.close();
	}

	public void navigateToRegistreView() {
		try {
			MyDatabase myDatabase = MyDatabase.getInstance();
			Connection connection = myDatabase.getConnection();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registre.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = (Stage) registreButton.getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void GoToReset(ActionEvent event) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/emailReq.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void showAlert(Alert.AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType, message, ButtonType.OK);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
}
