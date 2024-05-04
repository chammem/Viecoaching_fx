package controllers;

import entities.Groupe;
import entities.Typegroupe;
import entities.Utilisateur;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceGroupe;
import services.ServiceTypegroupe;
import services.ServiceUtilisateur;

import java.io.IOException;
import java.time.LocalDate;
import javafx.scene.layout.GridPane;

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
    private Scene scene;

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
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    @FXML
    void modifierGroupe() throws SQLException {
        // Validation du champ nom
        String nom = nomField.getText();
        if (nom == null || nom.trim().isEmpty()) {
            showAlert("Le nom du groupe ne peut pas être vide !");
            return;
        }

        // Validation du champ type de groupe
        String type = typeGroupeField.getValue();
        if (type == null || type.isEmpty()) {
            showAlert("Veuillez sélectionner un type de groupe.");
            return;
        }

        // Validation du champ date de création
        LocalDate dateCreation = dateCreationField.getValue();
        if (dateCreation == null) {
            showAlert("Veuillez sélectionner une date de création.");
            return;
        }

        // Validation du champ description
        String description = descriptionField.getText();
        if (description == null || description.trim().isEmpty()) {
            showAlert("La description du groupe ne peut pas être vide !");
            return;
        }

        // Validation de la sélection d'au moins un utilisateur
        ObservableList<Utilisateur> utilisateursSelectionnes = utilisateursListView.getSelectionModel().getSelectedItems();
        if (utilisateursSelectionnes == null || utilisateursSelectionnes.isEmpty()) {
            showAlert("Veuillez sélectionner au moins un utilisateur.");
            return;
        }

        // Mise à jour des informations du groupe
        groupe.setNom(nom);
        groupe.setDescription(description);
        groupe.setImage(imageView.getImage().getUrl());

        // Récupération de la date de création et mise à jour du groupe
        groupe.setDatecreation(java.sql.Date.valueOf(dateCreation));

        // Mise à jour de la liste des utilisateurs dans le groupe
        groupe.getUtilisateurs().clear();
        groupe.getUtilisateurs().addAll(utilisateursSelectionnes);

        // Appel du service pour modifier le groupe
        serviceGroupe.modifierg(groupe, utilisateursSelectionnes);
        showAlert("Le groupe a été modifié avec succès.");
        // Fermeture de la scène actuelle
        loadAfficheCategorieView();

        // Affichage d'une notification de succès
    }
    private void loadAfficheCategorieView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Affichegr.fxml"));
            Stage stage = (Stage) nomField.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheCategorie.fxml : " + e.getMessage());
        }
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
