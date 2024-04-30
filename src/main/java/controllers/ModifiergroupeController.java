package controllers;

import entities.Groupe;
import entities.Typegroupe;
import entities.Utilisateur;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import services.ServiceGroupe;
import services.ServiceTypegroupe;
import services.ServiceUtilisateur;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ModifiergroupeController implements Initializable {

    @FXML
    private ListView<Utilisateur> utilisateursListView;

    @FXML
    private Button btnModifier;

    @FXML
    private DatePicker dateCreationField;

    @FXML
    private TextField descriptionField;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField nomField;

    @FXML
    private ChoiceBox<String> typeGroupeField;

    private Groupe groupe;
    private ServiceUtilisateur serviceUtilisateur;
    private ServiceGroupe serviceGroupe;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation des services
        utilisateursListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        serviceUtilisateur = new ServiceUtilisateur();
        serviceGroupe = new ServiceGroupe();
        List<Utilisateur> utilisateurs = new ArrayList<>();

        try {
            utilisateurs = serviceGroupe.afficherUtilisateurs();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (utilisateurs != null) {
            for (Utilisateur utilisateur : utilisateurs) {
                utilisateursListView.getItems().add(utilisateur);
            }

            // Activer la sélection multiple

            utilisateursListView.setCellFactory(param -> new ListCell<Utilisateur>() {
                @Override
                protected void updateItem(Utilisateur utilisateur, boolean empty) {
                    super.updateItem(utilisateur, empty);

                    if (empty || utilisateur == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        UtilisateurListItem utilisateurListItem = new UtilisateurListItem(utilisateur);
                        setGraphic(utilisateurListItem);
                    }
                }
            });
        }
    }

    public void initData(Groupe groupe) {
        this.groupe = groupe;

        // Pré-remplissage des champs avec les valeurs du groupe à modifier
        typeGroupeField.setValue(groupe.getTypegroupe_id().getNomtype());
        nomField.setText(groupe.getNom());
        descriptionField.setText(groupe.getDescription());
        imageView.setImage(new Image(groupe.getImage()));
    }

    @FXML
    void modifierGroupe() throws SQLException {
        // Validate that the 'nom' field is not empty
        String nom = nomField.getText();
        if (nom == null || nom.trim().isEmpty()) {
            showAlert("Le nom du groupe ne peut pas être vide !");
            return;
        }

        // Mise à jour des informations du groupe
        groupe.setNom(nom);
        groupe.setDescription(descriptionField.getText());
        groupe.setImage(imageView.getImage().getUrl());

        // Récupération de l'utilisateur sélectionné
        ObservableList<Utilisateur> utilisateursSelectionnes = utilisateursListView.getSelectionModel().getSelectedItems();

        if (utilisateursSelectionnes != null) {
            // Mise à jour de la liste des utilisateurs dans le groupe
            groupe.getUtilisateurs().clear();
            groupe.getUtilisateurs().addAll(utilisateursSelectionnes);
        } else {
            System.out.println("Aucun utilisateur sélectionné !");
            return;
        }

        // Appel du service pour modifier le groupe
        serviceGroupe.modifier(groupe);

        // Affichage d'une notification de succès
        showAlert("Groupe modifié avec succès !");
    }

    @FXML
    private void selectImage() {
        // Sélection d'une image pour le groupe
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Chargement et affichage de l'image sélectionnée
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
        }
    }

    private void showAlert(String message) {
        // Affichage d'une boîte de dialogue d'alerte avec le message spécifié
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
