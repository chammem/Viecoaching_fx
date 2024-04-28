package controllers.RubriqueAdmin;

import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import tests.Main;
import entities.Rubrique;
import services.ServiceRubrique;
import services.ServiceUtilisateur;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddRubrique implements Initializable {
    public Label lbldate;
    public TextArea txtContenu;
    public Label lbluser;
    public Label lblTitle;
    public Label lblidrubrique;
    public TextField txtTitle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        lbldate.setText(LocalDate.now().toString());

        ServiceUtilisateur u = new ServiceUtilisateur();
        Utilisateur currentUser = null;
        String Currentuser = null;
        try {
            currentUser = u.trouverParId(Main.userid);
            if (currentUser != null) {
                Currentuser = currentUser.toString();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        lbluser.setText(Currentuser);


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

}
