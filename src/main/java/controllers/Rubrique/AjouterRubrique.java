package controllers.Rubrique;


import entities.Rubrique;
import entities.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import services.ServiceRubrique;
import services.ServiceUtilisateur;
import tests.Main;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class AjouterRubrique  {
    /*    @FXML
        private TextField txtTitle;

        @FXML
        private TextField txtContenu;

        @FXML
        private TableView<Rubrique> tbvRubrique;

        @FXML
        private TableColumn<Rubrique, String> title_Col;

        @FXML
        private TableColumn<Rubrique, String> contenu_Col;

        @FXML
        private TableColumn<Rubrique, Date> dateCréation_Col;

        @FXML
        private TableColumn<Rubrique, Date> datePublication_Col;

        @FXML
        private TableColumn<Rubrique, String> état_col;
        @FXML
        private Label lblAuteur;

        @FXML
        private Label lbldate;

        @FXML
        private Label lbluser;
    private ServiceRubrique rubriqueService;
    private ServiceUtilisateur utilisateurService;


    @FXML
    void btnAdd(ActionEvent event) {
        String title = txtTitle.getText().trim();
        String content = txtContenu.getText().trim();
        if (title.isEmpty() || content.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Fields");
            alert.setContentText("Please fill in all the fields.");
            alert.showAndWait();
            return;
        }
        Rubrique r = new Rubrique();
        r.setAuteur_id(Main.userid);
        r.setContenu(content);
        r.setDateCréation(Date.valueOf(LocalDate.now()));
        r.setDatePublication(Date.valueOf(LocalDate.now()));
        r.setTitre(title);
        r.setEtat("Published");
        ServiceRubrique service = new ServiceRubrique();
        service.ajouterRubrique(r);
        tbvRubrique.getItems().add(r); // Add the new rubrique to the table view's items list
        clearFields();
    }


    private void clearFields() {
        txtTitle.clear();
        txtContenu.clear();
    }

    @FXML
    void btnDelete(ActionEvent event) {
        Rubrique selectedRubrique = tbvRubrique.getSelectionModel().getSelectedItem();
        if (selectedRubrique != null) {
            ServiceRubrique serviceRubrique = new ServiceRubrique();
            serviceRubrique.deleteRubrique(selectedRubrique);
            ShowBLs(); // Refresh the table view
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Rubrique Selected");
            alert.setContentText("Please select a Rubrique to delete.");
            alert.showAndWait();
        }
    }
    @FXML
    void btnmodifier(ActionEvent event) {
        Rubrique selectedRubrique = tbvRubrique.getSelectionModel().getSelectedItem();
        if (selectedRubrique != null) {
            String title = txtTitle.getText().trim();
            String contenu = txtContenu.getText().trim();
            if (title.isEmpty()) {
                showAlert("Error", "Empty Title", "Please enter a title for the Rubrique.");
                return;
            }
            if (contenu.isEmpty()) {
                showAlert("Error", "Empty Content", "Please enter content for the Rubrique.");
                return;
            }
            selectedRubrique.setTitre(title);
            selectedRubrique.setContenu(contenu);
            rubriqueService.modifierRubrique(selectedRubrique);
            ShowBLs();
        } else {
            showAlert("Error", "No Rubrique Selected", "Please select a Rubrique to modify.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void onSelected(MouseEvent mouseEvent) throws SQLException {

        Rubrique selectedRubrique = tbvRubrique.getSelectionModel().getSelectedItem(); // Get the selected Rubrique from the table view
        if (selectedRubrique != null) {
            ServiceUtilisateur u = new ServiceUtilisateur();

            lblAuteur.setText(u.trouverParId(selectedRubrique.getAuteur_id()).toString());
            // Update the Rubrique object with the new data
            lbldate.setText(selectedRubrique.getDateCréation().toString());
            txtContenu.setText(selectedRubrique.getContenu());
            txtTitle.setText(selectedRubrique.getTitre());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rubriqueService = new ServiceRubrique(); // Instantiate RubriqueService object
        utilisateurService = new ServiceUtilisateur();
        ShowBLs();
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

    private void ShowBLs() {
        title_Col.setCellValueFactory(new PropertyValueFactory<>("titre"));
        contenu_Col.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        dateCréation_Col.setCellValueFactory(new PropertyValueFactory<>("dateCréation"));
        datePublication_Col.setCellValueFactory(new PropertyValueFactory<>("datePublication"));
        état_col.setCellValueFactory(new PropertyValueFactory<>("etat"));
        ServiceRubrique rubriqueService = new ServiceRubrique();
        ObservableList<Rubrique> rubriquesList;
        rubriquesList = FXCollections.observableArrayList(rubriqueService.listerRubrique());
        tbvRubrique.setItems(rubriquesList);
    }*/
}