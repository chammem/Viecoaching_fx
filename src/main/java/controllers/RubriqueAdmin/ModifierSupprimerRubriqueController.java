package controllers.RubriqueAdmin;

import com.google.protobuf.BoolValue;
import entities.Utilisateur;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import tests.Main;
import entities.Rubrique;
import services.ServiceRubrique;
import services.ServiceUtilisateur;

import java.net.URL;
import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;

/**************AdminDash***********/
public class ModifierSupprimerRubriqueController implements Initializable {

    @FXML
    private TableView<Rubrique> tbvRubrique;

    @FXML
    private TableColumn<Rubrique, String> Titre_Col;

    @FXML
    private TableColumn<Rubrique, String> Contenu_Col;

    @FXML
    private TableColumn<Rubrique, Date> DateCréation_Col;

    @FXML
    private TableColumn<Rubrique, Date> DatePublication_Col;

    @FXML
    private TableColumn<Rubrique, String> État_Col;

    @FXML
    private TableColumn<Rubrique, String> User_Col;

    @FXML
    private TableColumn<Rubrique, Void> Commentaires_Col;

    @FXML
    private TableColumn<Rubrique, Void> Supprimer_col;

    @FXML
    private TextField txttitle;

    @FXML
    private TextArea txtcontenu;

    @FXML
    private Label lblAuteur;

    @FXML
    private Label lbldate;
    @FXML
    private ComboBox<String> txtEtat;

    private ServiceRubrique rubriqueService;
    private ServiceUtilisateur utilisateurService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rubriqueService = new ServiceRubrique(); // Instantiate RubriqueService object
        utilisateurService = new ServiceUtilisateur();

        ShowBLs();

    }



    private void ShowBLs() {
        User_Col.setCellValueFactory(cellData -> {
            Rubrique rubrique = cellData.getValue();
            ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
            Utilisateur utilisateur = null;
            try {
                utilisateur = serviceUtilisateur.trouverParId(rubrique.getAuteur_id());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return new SimpleStringProperty(utilisateur.getNom() + " " + utilisateur.getPrenom());
        });
        User_Col.setCellFactory(column -> new TableCell<Rubrique, String>() {
            @Override
            protected void updateItem(String client, boolean empty) {
                super.updateItem(client, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(client);
                }
            }
        });
        DateCréation_Col.setCellValueFactory(new PropertyValueFactory<>("date_publication"));
        DateCréation_Col.setCellFactory(column -> new TableCell<Rubrique,Date>() {
            protected void updateItem(java.sql.Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(new SimpleDateFormat("dd/MM/yyyy").format(item));
                }
            }
        });
        DatePublication_Col.setCellValueFactory(new PropertyValueFactory<>("datePublication"));
        DatePublication_Col.setCellFactory(column -> new TableCell<Rubrique,Date>() {
            protected void updateItem(java.sql.Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(new SimpleDateFormat("dd/MM/yyyy").format(item));
                }
                Titre_Col.setCellValueFactory(new PropertyValueFactory<>("titre"));
                Contenu_Col.setCellValueFactory(new PropertyValueFactory<>("contenu"));
                Callback<TableColumn<Rubrique, Void>, TableCell<Rubrique, Void>> cellFactory = param -> new TableCell<>() {
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Button deleteIcon = new Button();
                            deleteIcon.setOnMouseClicked(event -> {

                                Rubrique rubrique = tbvRubrique.getSelectionModel().getSelectedItem();

                                if (rubrique != null) {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Confirmation Dialog");
                                    alert.setHeaderText("Confirm Deletion");
                                    alert.setContentText("Are you sure you want to delete this Rubrique?");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    ServiceRubrique serviceRubrique = new ServiceRubrique();
                                    if (result.isPresent() && result.get() == ButtonType.OK) {
                                        serviceRubrique.deleteRubrique(rubrique);
                                        ShowBLs(); // Refresh the table view
                                    }
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setHeaderText("No Rubrique Selected");
                                    alert.setContentText("Please select a Rubrique to delete.");
                                    alert.showAndWait();
                                }
                            });

                            HBox manageBtns = new HBox(deleteIcon);
                            manageBtns.setStyle("-fx-alignment: center");
                            HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                            setGraphic(manageBtns);

                            setText(null);
                        }
                    }
                };

                Supprimer_col.setCellFactory(cellFactory);
                État_Col.setCellValueFactory(new PropertyValueFactory<>("etat"));

                ServiceRubrique serviceRubrique = new ServiceRubrique();
                ObservableList<Rubrique> rubriquesList = null;
                rubriquesList = FXCollections.observableArrayList(serviceRubrique.listerRubrique());
                tbvRubrique.setItems(rubriquesList);

            }
        });
    }

    @FXML
    void btnAdd(ActionEvent event) throws SQLException {
        String title = txttitle.getText().trim(); // Get the title from the text field and trim any leading or trailing whitespaces
        String contenu = txtcontenu.getText().trim(); // Get the content from the text area and trim any leading or trailing whitespaces
        String etat = txtEtat.getValue();
        int auteurId = Main.userid; // Assuming you have the user ID stored in MainApplication.userid

        // Check if the title is empty
        if (title.isEmpty()) {
            // Display an error message if the title is empty
            showAlert("Error", "Empty Title", "Please enter a title for the Rubrique.","Please select état");
            return;
        }

        // Check if the content is empty
        if (contenu.isEmpty()) {
            // Display an error message if the content is empty
            showAlert("Error", "Empty Title", "Please enter a title for the Rubrique.","Please select état");
            return;

        }
        if (etat == null) {
            // Display an error message if the content is empty
            showAlert("Error", "Empty Title", "Please enter a title for the Rubrique.","Please select état");
            return;

        }
    }



    @FXML
    void btnmodifier(ActionEvent event) throws SQLException {
        Rubrique selectedRubrique = tbvRubrique.getSelectionModel().getSelectedItem(); // Get the selected Rubrique from the table view
        if (selectedRubrique != null) {
            String title = txttitle.getText().trim(); // Get the updated title from the text field and trim any leading or trailing whitespaces
            String contenu = txtcontenu.getText().trim(); // Get the updated content from the text area and trim any leading or trailing whitespaces
            String etat = txtEtat.getValue();
            ////////////Controle de saisie

            // Check if the title is empty
            if (title.isEmpty()) {
                // Display an error message if the title is empty
                showAlert("Error", "Empty Title", "Please enter a title for the Rubrique.","Please select état");
                return;
            }

            // Check if the content is empty
            if (contenu.isEmpty()) {
                // Display an error message if the content is empty
                showAlert("Error", "Empty Title", "Please enter a title for the Rubrique.","Please select état");
                return;
            }
            if (etat == null) {
                // Display an error message if the content is empty
                showAlert("Error", "Empty Title", "Please enter a title for the Rubrique.","Please select état");
                return;
            }

            // Update the Rubrique object with the new data
            selectedRubrique.setTitre(title); // Update the title
            selectedRubrique.setContenu(contenu); // Update the content
            selectedRubrique.setEtat(etat);

            // Call the RubriqueService method to update the Rubrique in the database
            ServiceRubrique serviceRubrique = new ServiceRubrique();

            // Call the modifier method on the instance of ServiceRubrique
            serviceRubrique.modifierRubrique(selectedRubrique);

            // Refresh the table view to reflect the changes
            ShowBLs();
        } else {
            // No Rubrique selected, display an error message or handle it appropriately
            showAlert("Error", "Empty Title", "Please enter a title for the Rubrique.","Please select état");
        }


    }

    @FXML
    void onSelected(MouseEvent mouseEvent) throws SQLException {
        Rubrique selectedRubrique = tbvRubrique.getSelectionModel().getSelectedItem(); // Get the selected Rubrique from the table view
        if (selectedRubrique != null) {
            ServiceUtilisateur u = new ServiceUtilisateur();

            lblAuteur.setText(u.trouverParId(selectedRubrique.getAuteur_id()).toString());
            // Update the Rubrique object with the new data
            lbldate.setText(selectedRubrique.getDateCréation().toString());
            txtcontenu.setText(selectedRubrique.getContenu());
            txttitle.setText(selectedRubrique.getTitre());
            txtEtat.setValue(selectedRubrique.getEtat());

        }
    }
    private void showAlert(String title, String header, String content,String etat) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.setTitle(etat);
        alert.showAndWait();
    }

}
