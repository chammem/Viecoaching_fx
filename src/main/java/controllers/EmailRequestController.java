package controllers;

import entities.Reset;
import entities.Sendmail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.ServiceReset;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class EmailRequestController implements Serializable {
	Random rnd = new Random();
	int number = rnd.nextInt(999999);
	long start = System.currentTimeMillis();
	String sTime = Long.toString(start);
	String Object="Réinitialiser Votre mot de passe";
	String Subject="Votre Code est :  "+number+"\n S'il te plait ne passe pas 10 min De maintenant";

	public static String emailed;
	@FXML
	private Button ResetButton;

	@FXML
	private TextField emailReq;

	@FXML
	private Text errorcaptcha;

	@FXML
	private ImageView image1View;


	public void initialize(URL url, ResourceBundle resourceBundle) {
		// Loading images
		loadImage(image1View, "images/image1.PNG");}
	private void loadImage(ImageView imageView, String filePath) {
		File file = new File(filePath);
		Image image = new Image(file.toURI().toString());
		imageView.setImage(image);
	}

	@FXML
	void ResetButton(ActionEvent event) throws IOException {
		ServiceReset imr = new ServiceReset();
		Sendmail sn = new Sendmail();
		if (emailReq.getText().isEmpty()) {
			errorcaptcha.setText(" Champs manquants!!!");

		}else if (imr.ajout(new Reset(emailReq.getText(),number,sTime))) {
emailed = emailReq.getText();
			sn.envoyer(emailReq.getText(), Object, Subject);
			Parent page2 = FXMLLoader.load(getClass().getResource("/fxml/VerifCodeRes.fxml"));

			Scene scene2 = new Scene(page2);
			Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			app_stage.setScene(scene2);
			app_stage.show();

		}else{
			errorcaptcha.setText("Compte N'existe Pas !!");
		}


	}
}
