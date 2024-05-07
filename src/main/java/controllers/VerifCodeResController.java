package controllers;

import entities.Reset;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import services.ServiceAuthentication;
import utils.MyDatabase;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class VerifCodeResController  implements Serializable {
	private final Connection con;
	@FXML
	private TextField CodeVerif;

	@FXML
	private Text errorcaptcha;

	@FXML
	private ImageView image1View;

	@FXML
	private ImageView lockView;

	@FXML
	void nextBtn(ActionEvent event) {
		Reset t = new Reset(Integer.parseInt(CodeVerif.getText()));
		if (reset(t)) {
			try {
Parent page2 = javafx.fxml.FXMLLoader.load(getClass().getResource("/fxml/changmdp.fxml"));
				javafx.scene.Scene scene2 = new javafx.scene.Scene(page2);
				javafx.stage.Stage app_stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
				app_stage.hide();
				app_stage.setScene(scene2);
				app_stage.show();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		} else {
			errorcaptcha.setText("Code Incorrect !");
		}

	}

	public VerifCodeResController() {
		this.con = MyDatabase.getInstance().getConnection();
	}
	public static String EmailRes;

	public boolean reset(Reset t) {
		long end = System.currentTimeMillis();
		try {
			String req = "SELECT * from reset where code=?";
			PreparedStatement pst = con.prepareStatement(req);
			pst.setInt(1, t.getCode());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				long StartTime = Long.parseLong(rs.getString("timeMils"));
				long calT = end - StartTime;
				if (calT < 120000) {
					EmailRes=rs.getString("email");
					return true;
				} else {
					String reqs = "DELETE FROM reset WHERE code=?";
					PreparedStatement psts = con.prepareStatement(reqs);
					psts.setInt(1, t.getCode());
					psts.executeUpdate();
					errorcaptcha.setText("Code Expiré !");
					return false;
				}
			} else {
				errorcaptcha.setText("Code Incorrect !");
				return false ;
			}

		} catch (SQLException e) {

			System.out.println(e.getMessage());
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
