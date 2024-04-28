package controllers.RubriqueAdmin;

/*import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;*/
import entities.Utilisateur;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminDash implements Initializable {
    public TableColumn<Rubrique,Integer> id_Col;
    public TableView<Rubrique> tbvRubrique;
    public TableColumn<Rubrique,String> User_Col;
    public TableColumn<Rubrique,String> Title_Col;
    public TableColumn<Rubrique,Date> Date_Col;
    public TableColumn<Rubrique,Void> Action_Col;
    public TextField txttitle;
    public TextArea txtcontenu;
    public Label lblAuteur;
    public Label lbldate;

    private ServiceRubrique rubriqueService;
    private ServiceUtilisateur utilisateurService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rubriqueService = new ServiceRubrique(); // Instantiate RubriqueService object
        utilisateurService = new ServiceUtilisateur();

        ShowBLs();
    }

    private void ShowBLs() {
        // Mapping des colonnes de la table avec les propriétés de l'objet Rubrique
        id_Col.setCellValueFactory(new PropertyValueFactory<>("id"));
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

        Date_Col.setCellValueFactory(new PropertyValueFactory<>("datePublication"));
        Date_Col.setCellFactory(column -> new TableCell<Rubrique, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(new SimpleDateFormat("dd/MM/yyyy").format(item));
                }
            }
        });

        Title_Col.setCellValueFactory(new PropertyValueFactory<>("titre"));

        Callback<TableColumn<Rubrique, Void>, TableCell<Rubrique, Void>> cellFactory = param -> new TableCell<>() {
            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button deleteIcon = new Button();

                    deleteIcon.setStyle("-fx-cursor: hand; -glyph-size: 28px; -fx-fill: #ff1744;");


                    deleteIcon.setOnMouseClicked(event -> {

                        Rubrique rubrique = tbvRubrique.getSelectionModel().getSelectedItem();

                        if (rubrique != null) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation Dialog");
                            alert.setHeaderText("Confirm Deletion");
                            alert.setContentText("Are you sure you want to delete this Rubrique?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                rubriqueService.deleteRubrique(rubrique);
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

        Action_Col.setCellFactory(cellFactory);

        // Récupération de la liste de Rubrique
        ObservableList<Rubrique> rubriquesList = FXCollections.observableArrayList(rubriqueService.listerRubrique());

        // Ajout de chaque Rubrique dans la table
        tbvRubrique.setItems(rubriquesList);
    }







    public void onSelected(MouseEvent mouseEvent) throws SQLException {

        Rubrique selectedRubrique = tbvRubrique.getSelectionModel().getSelectedItem(); // Get the selected Rubrique from the table view
        if (selectedRubrique != null) {
            ServiceUtilisateur u = new ServiceUtilisateur();

            lblAuteur.setText(u.trouverParId(selectedRubrique.getAuteur_id()).toString());
            // Update the Rubrique object with the new data
            lbldate.setText(selectedRubrique.getDateCréation().toString());
            txtcontenu.setText(selectedRubrique.getContenu());
            txttitle.setText(selectedRubrique.getTitre());


        }

    }

    public void btnAdd(ActionEvent actionEvent) {
        String title = txttitle.getText().trim(); // Get the title from the text field and trim any leading or trailing whitespaces
        String contenu = txtcontenu.getText().trim(); // Get the content from the text area and trim any leading or trailing whitespaces
        int auteurId = Main.userid; // Assuming you have the user ID stored in MainApplication.userid

        // Check if the title is empty
        if (title.isEmpty()) {
            // Display an error message if the title is empty
            showAlert("Error", "Empty Title", "Please enter a title for the Rubrique.");
            return;
        }

        // Check if the content is empty
        if (contenu.isEmpty()) {
            // Display an error message if the content is empty
            showAlert("Error", "Empty Content", "Please enter content for the Rubrique.");
            return;
        }

        // Create a new Rubrique object with the provided data
        Rubrique newRubrique = new Rubrique(auteurId, title, contenu, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), "Published");

        // Call the RubriqueService method to add the new Rubrique to the database
        rubriqueService.ajouterRubrique(newRubrique);

        // Refresh the table view to display the new Rubrique
        ShowBLs();
    }

    public void btnmodifier(ActionEvent actionEvent) {
        Rubrique selectedRubrique = tbvRubrique.getSelectionModel().getSelectedItem(); // Get the selected Rubrique from the table view
        if (selectedRubrique != null) {
            String title = txttitle.getText().trim(); // Get the updated title from the text field and trim any leading or trailing whitespaces
            String contenu = txtcontenu.getText().trim(); // Get the updated content from the text area and trim any leading or trailing whitespaces
            ////////////Controle de saisie

            // Check if the title is empty
            if (title.isEmpty()) {
                // Display an error message if the title is empty
                showAlert("Error", "Empty Title", "Please enter a title for the Rubrique.");
                return;
            }

            // Check if the content is empty
            if (contenu.isEmpty()) {
                // Display an error message if the content is empty
                showAlert("Error", "Empty Content", "Please enter content for the Rubrique.");
                return;
            }

            // Update the Rubrique object with the new data
            selectedRubrique.setTitre(title); // Update the title
            selectedRubrique.setContenu(contenu); // Update the content

            // Call the RubriqueService method to update the Rubrique in the database
            rubriqueService.modifierRubrique(selectedRubrique);

            // Refresh the table view to reflect the changes
            ShowBLs();
        } else {
            // No Rubrique selected, display an error message or handle it appropriately
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

}
