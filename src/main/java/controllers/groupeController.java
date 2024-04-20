package controllers;

import com.google.protobuf.BoolValue;
import entities.Groupe;
import entities.Typegroupe;
import entities.Utilisateur;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import services.ServiceGroupe;
import services.ServiceTypegroupe;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class groupeController implements Initializable {
        @FXML
        private TextField nomField;
        @FXML
        private ChoiceBox<String> typeGroupeField;
        @FXML
        private DatePicker dateCreationField;

        @FXML
        private ImageView imageView;


        @FXML
        private TextField descriptionField;



        private String nomFichierurlSelectionne;
        @FXML
        private ListView<Utilisateur> utilisateursListView;

        public void ajouter(ActionEvent event) {
                String nom = nomField.getText();
                String description = descriptionField.getText();
                String type = typeGroupeField.getValue();
                String image = (imageView.getImage() != null) ? imageView.getImage().getUrl() : null;
                ObservableList<Utilisateur> utilisateursSelectionnes = utilisateursListView.getSelectionModel().getSelectedItems();

                ServiceGroupe serviceGroupe = new ServiceGroupe();

                try {
                        LocalDate localDate = dateCreationField.getValue();
                        java.sql.Date date = java.sql.Date.valueOf(localDate);

                        ServiceTypegroupe serviceTypegroupe = new ServiceTypegroupe();
                        Typegroupe typegroupe = serviceTypegroupe.getTypegroupebynom(type);

                        if (typegroupe == null) {
                                showAlert("Le type de groupe spécifié n'existe pas.");
                                return;
                        }

                        Groupe groupe = new Groupe(nom, typegroupe, date, image, description);
                        groupe.setUtilisateurs(utilisateursSelectionnes);

                        serviceGroupe.ajouter(groupe);
                        showAlert("Groupe ajouté avec succès !");
                } catch (SQLException e) {
                        System.out.println("Erreur lors de l'ajout du groupe : " + e.getMessage());
                        e.printStackTrace();
                }
        }



        private void showAlert(String message) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
        }
        @FXML
        private void selectImage() {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choisir une url");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("urls", "*.png", "*.jpg")
                );

                File selectedFile = fileChooser.showOpenDialog(null);
                if (selectedFile != null) {
                        // Charger l'url sélectionnée
                        Image image = new Image(selectedFile.toURI().toString());

                        // Afficher l'url dans l'urlView approprié
                        imageView.setImage(image);

                        // Stocker le nom du fichier sélectionné
                        nomFichierurlSelectionne = selectedFile.getName();
                        System.out.println("Nom du fichier sélectionné : " + nomFichierurlSelectionne);
                }
        }


        // Méthode pour charger une url dans l'urlView
        public void setImage(Image image) {
                imageView.setImage(image);
        }

        // Méthode getter pour obtenir l'ImageView
        public ImageView getImageView() {
                return imageView;
        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
                // Initialisez votre ChoiceBox de type de groupe avec des options
                ServiceTypegroupe serviceTypegroupe = new ServiceTypegroupe();
                List<Typegroupe> typegroupeList;

                try {
                        typegroupeList = serviceTypegroupe.getAllTypegroupe();

                        // Remplissez la ChoiceBox avec les noms des types de groupe
                        typeGroupeField.getItems().clear();
                        for (Typegroupe typegroupe : typegroupeList) {
                                typeGroupeField.getItems().add(typegroupe.getNomtype());
                        }
                } catch (SQLException e) {
                        throw new RuntimeException(e);
                }

                // Charger la liste des utilisateurs dans la ListView avec des boutons radio
                ServiceGroupe serviceGroupe = new ServiceGroupe();
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
                        utilisateursListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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



}
