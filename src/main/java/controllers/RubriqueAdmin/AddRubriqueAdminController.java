package controllers.RubriqueAdmin;

import entities.Rubrique;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import services.IService;
import services.ServiceRubrique;
import services.ServiceUtilisateur;
import tests.Main;

import java.sql.SQLException;
import java.time.LocalDateTime;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddRubriqueAdminController implements Initializable {
    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtContenu;

    @FXML
    private ComboBox<String> txtEtat;

    @FXML
    private Label lbldate;

    @FXML
    private Label lbluser;
    /*private TableColumn<Rubrique, String> État_Col;*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBox();
        lbldate.setText(LocalDate.now().toString());

        /*ServiceUtilisateur u = new ServiceUtilisateur();
        String Currentuser = null;
        try {
            Currentuser = u.trouverParId(Main.userid).toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/

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

    @FXML
    void postRubrique(ActionEvent event) throws SQLException {
        String title = txtTitle.getText().trim();
        String content = txtContenu.getText().trim();
        String etat = txtEtat.getValue();

        if (title.isEmpty() || content.isEmpty() || etat == null) {
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
        r.setDateCréation(Date.valueOf((LocalDate.now())));
        r.setDatePublication(Date.valueOf(LocalDate.now()));
        r.setTitre(title);
        r.setEtat(txtEtat.getValue());

        ServiceRubrique service = new ServiceRubrique();
        service.ajouterRubrique(r);


        clearFields();
    }
    public void comboBox(){
        String[] items = {"En attente", "Active", "Inactive"};
        txtEtat.getItems().addAll(items);

}
    private void clearFields() {
        txtTitle.clear();
        txtContenu.clear();
    }
}
