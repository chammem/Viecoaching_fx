package controllers;

import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.mindrot.jbcrypt.BCrypt;
import utils.MyDatabase;
import utils.PasswordHasher;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ChangeMdpController {

	@FXML
	private TextField cpwd;

	@FXML
	private Text errorcaptcha;

	@FXML
	private ImageView image1View;

	@FXML
	private ImageView lockView;

	@FXML
	private ImageView lockView1;

	@FXML
	private TextField pwd;

	private final Connection con;

	public ChangeMdpController() {
		this.con = MyDatabase.getInstance().getConnection();
		try {
			this.con.setAutoCommit(false);  // Ensure that auto-commit is off
		} catch (SQLException e) {
			System.err.println("Error setting auto-commit: " + e.getMessage());
		}
	}

	@FXML
	void nextBtn(ActionEvent event) {
		Utilisateur t = new Utilisateur();
		t.setMdp(cpwd.getText());
		t.setEmail(VerifCodeResController.EmailRes);

		// Check if password and confirm password are equal
		if (!pwd.getText().equals(cpwd.getText())) {
			errorcaptcha.setText("Password and confirmation password do not match.");
			return; // Stop further processing if they don't match
		}

		if (modifierMopasst(t)) {
			try {
				Parent page2 = javafx.fxml.FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
				javafx.scene.Scene scene2 = new javafx.scene.Scene(page2);
				javafx.stage.Stage app_stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
				app_stage.hide(); // Hide current stage
				app_stage.setScene(scene2); // Set scene to login page
				app_stage.show(); // Show the stage
			} catch (Exception e) {
				System.err.println("Error loading login page: " + e.getMessage());
			}
		} else {
			errorcaptcha.setText("Password update failed!");
		}
	}


	public boolean modifierMopasst(Utilisateur t) {
		String motDePasseHache = PasswordHasher.hashPassword(t.getMdp());
		try {
			String reqs = "UPDATE utilisateur SET mdp=? WHERE email=?";
			PreparedStatement pst = con.prepareStatement(reqs);
			pst.setString(1, motDePasseHache);
			pst.setString(2, t.getEmail());
			int affectedRows = pst.executeUpdate();
			if (affectedRows == 0) {
				System.out.println("No rows updated, check if the email is correct.");
				con.rollback();  // Rollback if no rows are affected
				return false;
			}
			con.commit();  // Commit the transaction
			System.out.println("Password updated successfully!");
			return true;
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			try {
				con.rollback();  // Attempt to rollback on error
			} catch (SQLException e) {
				System.err.println("Rollback failed: " + e.getMessage());
			}
			return false;
		}
	}
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// Loading images
		loadImage(image1View, "images/image1.PNG");}
	private void loadImage(ImageView imageView, String filePath) {
		File file = new File(filePath);
		Image image = new Image(file.toURI().toString());
		imageView.setImage(image);
	}
}
