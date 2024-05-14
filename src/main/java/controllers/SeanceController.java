
import com.google.zxing.common.BitMatrix;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class SeanceController implements Initializable {

    @FXML
    public Button ts;


    @FXML
    private ListView<seanceDTO> mylist;
    int x;
    @FXML
    private TextField title;
    @FXML
    private TextField lien;
    @FXML
    private PasswordField mdp;


    @FXML
    private ChoiceBox<typeseance> typeseance;
    @FXML
    private Text itemselcted;
	@FXML
	private ImageView qrim;
    @FXML
    private Spinner<Integer> time; // Spinner for hours



    int idseance;
    int typeSeanceId;



    private String selectedTitle;
    private String selectedLien;
    private Time selectedTime;
    private Integer selectedTypeSeance;
    private String selectedMdp;



    @FXML
    void save(ActionEvent event) throws WriterException {
        if (title.getText().isEmpty()) {
            showAlert("Validation Error", "Title is required!", Alert.AlertType.ERROR);
            return;
        }

        if (lien.getText().isEmpty()) {
            showAlert("Validation Error", "Lien is required!", Alert.AlertType.ERROR);
            return;
        }

        if (typeseance.getSelectionModel().isEmpty()) {
            showAlert("Validation Error", "Type is required!", Alert.AlertType.ERROR);
            return;
        }

        if (time.getValue() == null) {
            showAlert("Validation Error", "Time is required!", Alert.AlertType.ERROR);
            return;
        }

        SeanceService l = new SeanceService();

        // Convert Spinner value to Time object
        Time duree = Time.valueOf(String.format("%02d:%02d:00", time.getValue(), 0));
System.out.println("duree"+filePath);
        // Use static USER_ID
		String fqr=generateQRCodeAndSave(lien.getText(), title.getText());

		l.addSeance(new Seance(title.getText(), duree, lien.getText(), mdp.getText(), typeSeanceId, 1,fqr));
        // Show success message
        showAlert("Success", "Seance ajouté!", Alert.AlertType.INFORMATION);

        // Reload data
        mylist.getItems().clear();
        mylist.getItems().addAll(l.affichage());
        loadData();
    }
	String filePath;
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


	public String generateQRCodeAndSave(String text, String fileName) throws WriterException {
		// Generate the QR code
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 250, 250);
		BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

		// Convert the BufferedImage to a JavaFX Image
		Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

		// Save the image to the specified directory
		String directoryPath = "C:\\Users\\DADO MAZHOUD\\Downloads\\viecoashingfx\\viecoashingfx\\src\\main\\java\\com\\asma\\asma\\images\\images\\qr";
		Path directory = Paths.get(directoryPath);
		if (!Files.exists(directory)) {
			try {
				Files.createDirectories(directory);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		 filePath = directoryPath + "/" + fileName + ".png";
		File file = new File(filePath);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(fxImage, null), "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}


    @FXML
    void update(ActionEvent event) {

            SeanceService l = new SeanceService();

            // Convert Spinner value to Time object
            Time duree = Time.valueOf(String.format("%02d:%02d:00", time.getValue(), 0));

            // Use static USER_ID
            l.modifier(new Seance(idseance, title.getText(), duree, lien.getText(), mdp.getText(), typeSeanceId, 1));

            // Clear error message
            itemselcted.setText("Seance mise à jour!");

            // Reload data
            loadData();

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadtypeseance();
        getLoad();

        // Initialize Spinner for hours
        SpinnerValueFactory<Integer> heureFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        time.setValueFactory(heureFactory);

        // Initialize Spinner for minutes
        SpinnerValueFactory<Integer> minuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        time.setValueFactory(minuteFactory);

        // Bind selected item from ListView to input fields
        mylist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                idseance = newValue.getIdseance();
                title.setText(newValue.getTitre());
                lien.setText(newValue.getLien());
                mdp.setText(newValue.getMotDePasse());
                typeseance.setValue(new typeseance(newValue.getTypeSeanceId(), newValue.getNom_type()));

                // Convert Time to Spinner value
                int hours = newValue.getDuree().getHours();

                time.getValueFactory().setValue(hours);
            }
        });
    }
    public void getLoad() {
        SeanceService l = new SeanceService();
        //   mylist.getItems().clear(); // Clear existing items
        mylist.getItems().addAll(l.affichage());
        mylist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                idseance = newValue.getIdseance();
                selectedTitle = newValue.getTitre();
                selectedLien = newValue.getLien();
                selectedTime = Time.valueOf(String.valueOf(newValue.getDuree()));
                selectedTypeSeance = newValue.getTypeSeanceId();
                selectedMdp = newValue.getMotDePasse();

                // Set the values of the text fields
                title.setText(selectedTitle);
                lien.setText(selectedLien);
                mdp.setText(selectedMdp);
				qrim.setImage(new Image("file:"+newValue.getQr()));

                // Set the values of the ChoiceBox
                typeseance.setValue(new typeseance(selectedTypeSeance, newValue.getNom_type()));

                // Set the values of the Spinner
                time.getValueFactory().setValue(selectedTime.getHours());

            }
        });
    }


    @FXML
    void deleteietm(ActionEvent event) {
        SeanceService l = new SeanceService();
        l.supprimer(new Seance(idseance));
        itemselcted.setText("seance supprimée");
        loadData();

    }
    void loadtypeseance() {
        TypeSeanceService v = new TypeSeanceService();
        v.afficher().forEach(typeseance1 -> {
            typeseance.getItems().add(new typeseance(typeseance1.getTypeSeanceId(), typeseance1.getNomtype()));
            typeSeanceId = typeseance1.getTypeSeanceId();
        });
    }



    private void loadData() {
        SeanceService l = new SeanceService();
        mylist.getItems().clear();
        mylist.getItems().addAll(l.affichage());
    }



	@FXML
	void GoType(ActionEvent event) throws IOException {

		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("typeseance.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		Stage stage = new Stage();
		stage.setTitle("Hello!");
		stage.setScene(scene);
		stage.show();

	}

	@FXML
	void Quiz(ActionEvent event) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("quiz.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		Stage stage = new Stage();
		stage.setTitle("Hello!");
		stage.setScene(scene);
		stage.show();
	}
}
