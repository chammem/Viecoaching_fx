package controllers.Rubrique;

import entities.Commentaire;
import entities.Rubrique;
import entities.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ServiceCommentaire;
import services.ServiceRubrique;
import services.ServiceUtilisateur;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


public class AjouterCommentaire  {/*
    @FXML
    private ListView<Rubrique> lvRubriques;

    @FXML
    private TableView<Commentaire> tbvCommentaire;

    @FXML
    private TableColumn<Commentaire, String> contenuCommentaire_Col;

    @FXML
    private TableColumn<Commentaire, Date> dateCréation_Col;

    @FXML
    private Label lblAuteur;

    @FXML
    private Label lbldate;

    @FXML
    private Label lbluser;

    @FXML
    private TextArea lblContenu;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblidrubrique;

    @FXML
    private Label lblidcomment;


    int rubriqueId = 0;


    @FXML
    void btnDelete(ActionEvent event) {
        Commentaire selectedCommentaire = tbvCommentaire.getSelectionModel().getSelectedItem();
        if (selectedCommentaire != null) {
            ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
            serviceCommentaire.deleteCommentaire(selectedCommentaire);
            ShowCommentaires(); // Refresh the table view
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Comment Selected");
            alert.setContentText("Please select a comment to delete.");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ShowRubriques();
    }
    public void ShowRubriques() {
        ServiceRubrique rubriqueService =  new ServiceRubrique();
        ServiceUtilisateur u = new ServiceUtilisateur();
        List<Rubrique> rubriques = rubriqueService.listerRubrique();

        ObservableList<Rubrique> rubriqueItems = FXCollections.observableArrayList(rubriques);

        lvRubriques.setItems(rubriqueItems);

        lvRubriques.setCellFactory(param -> new ListCell<Rubrique>() {
            @Override
            protected void updateItem(Rubrique item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    Utilisateur user = null;
                    try {
                        user = u.trouverParId(item.getAuteur_id());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    setText(user.getPrenom() + " " + user.getNom() + " - " + item.getTitre());
                }
            }
        });

        lvRubriques.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Utilisateur user = null;
                try {
                    user = u.trouverParId(newValue.getAuteur_id());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                lbluser.setText(user.getPrenom() + " " + user.getNom());
                ShowCommentaires();
                lblidrubrique.setText(String.valueOf(newValue.getId()));
                lblTitle.setText(newValue.getTitre());
                lbldate.setText(newValue.getDatePublication().toString());
                lblContenu.setText(newValue.getContenu());
                lblContenu.setWrapText(true);
            }
        });

    }

    private void ShowCommentaires() {
        // Modify the method to fetch commentaires without requiring an ID
        // Use the currently selected rubrique's ID
        Rubrique selectedRubrique = lvRubriques.getSelectionModel().getSelectedItem();
        if (selectedRubrique != null) {
            ServiceCommentaire commentaireService = new ServiceCommentaire();
            List<Commentaire> commentaires = commentaireService.listerCommentaireByrubrique(selectedRubrique.getId());
            ObservableList<Commentaire> commentairesList = FXCollections.observableArrayList(commentaires);

            // Bind columns to properties of Commentaire
            contenuCommentaire_Col.setCellValueFactory(new PropertyValueFactory<>("contenu"));
            dateCréation_Col.setCellValueFactory(new PropertyValueFactory<>("dateCréation"));

            tbvCommentaire.setItems(commentairesList);
        }
    }
*/

    }





