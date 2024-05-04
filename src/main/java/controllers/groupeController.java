package controllers;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import com.google.protobuf.BoolValue;
import entities.Groupe;
import entities.Typegroupe;
import entities.Utilisateur;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceGroupe;
import services.ServiceTypegroupe;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

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

        public void ajouter(ActionEvent event) throws IOException {
                // Validation du champ Image
                String image = nomFichierurlSelectionne; // Utilisez le nom du fichier sélectionné

                // Validation du champ Nom
                String nom = nomField.getText();
                if (nom.isEmpty()) {
                        showAlert("Veuillez saisir un nom.");
                        return;
                }

                // Validation du champ Type de groupe
                String type = typeGroupeField.getValue();
                if (type == null) {
                        showAlert("Veuillez sélectionner un type de groupe.");
                        return;
                }
                // Validation du champ Description
                String description = descriptionField.getText();
                if (description.isEmpty()) {
                        showAlert("Veuillez saisir une description.");
                        return;
                }

                // Validation du champ Date de création
                LocalDate dateCreation = dateCreationField.getValue();
                if (dateCreation == null || dateCreation.isAfter(LocalDate.now())) {
                        showAlert("Veuillez sélectionner une date de création valide (antérieure à la date actuelle).");
                        return;
                }

                // Autres validations pour les champs facultatifs, comme l'image et la description
                 image = (imageView.getImage() != null) ? imageView.getImage().getUrl() : null;
                // Ajoutez des validations supplémentaires au besoin pour les champs facultatifs

                // Validation de la sélection d'utilisateurs
                ObservableList<Utilisateur> utilisateursSelectionnes = utilisateursListView.getSelectionModel().getSelectedItems();
                if (utilisateursSelectionnes.isEmpty()) {
                        showAlert("Veuillez sélectionner au moins un utilisateur.");
                        return;
                }

                // Si toutes les validations passent, ajoutez le groupe
                ServiceGroupe serviceGroupe = new ServiceGroupe();

                try {
                        ServiceTypegroupe serviceTypegroupe = new ServiceTypegroupe();
                        Typegroupe typegroupe = serviceTypegroupe.getTypegroupebynom(type);

                        if (typegroupe == null) {
                                showAlert("Le type de groupe spécifié n'existe pas.");
                                return;
                        }

                        Groupe groupe = new Groupe(nom, typegroupe, Date.valueOf(dateCreation), image, description);
                        groupe.setUtilisateurs(utilisateursSelectionnes);

                        // Ajouter le groupe avec les utilisateurs
                        serviceGroupe.ajouterAvecUtilisateurs(groupe, utilisateursSelectionnes);

                        // Afficher une confirmation
                        showAlert("Groupe ajouté avec succès !");
                        loadAfficheCategorieVieww();                } catch (SQLException e) {
                        System.out.println("Erreur lors de l'ajout du groupe : " + e.getMessage());
                        e.printStackTrace();
                        showAleert("Groupe ajouté avec succès !", event);


                }


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
        private void loadAffichergr() {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Affichergr.fxml"));
                        Parent root = loader.load();
                        AffichergrController controller = loader.getController();
                        controller.loadResources(); // Appelez la méthode pour recharger les données
                        Stage stage = (Stage) nomField.getScene().getWindow();
                        stage.setScene(new Scene(root));
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
        private void loadPreviousPage(ActionEvent event) {
                // Récupérer le nœud source de l'événement (le bouton "Ajouter" dans ce cas)
                Node sourceNode = (Node) event.getSource();

                // Récupérer la scène actuelle
                Scene scene = sourceNode.getScene();

                // Récupérer le stage actuel
                Stage stage = (Stage) scene.getWindow();

                // Fermer le stage actuel pour revenir à la page précédente
                stage.close();
        }
        private void loadAfficheCategorieVieww() {
                try {
                        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Affichegr.fxml"));
                        Stage stage = (Stage) nomField.getScene().getWindow(); // Récupérer la fenêtre actuelle
                        stage.setScene(new Scene(root));
                } catch (IOException e) {
                        System.out.println("Erreur lors du chargement de afficheCategorie.fxml : " + e.getMessage());
                }
        }

        private void showAleert(String message, ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();}

        private void showAlert(String message) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
        }
        @FXML
        private void selectImage() {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choisir une image");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
                );

                File selectedFile = fileChooser.showOpenDialog(null);
                if (selectedFile != null) {
                        try {
                                // Charger l'image à partir du fichier sélectionné
                                Image image = new Image(selectedFile.toURI().toString());
                                imageView.setImage(image);

                                // Stocker le nom du fichier sélectionné (sans le chemin d'accès)
                                nomFichierurlSelectionne = selectedFile.getName();
                                System.out.println("Nom du fichier sélectionné : " + nomFichierurlSelectionne);
                        } catch (Exception e) {
                                e.printStackTrace();
                                showAlert("Erreur lors du chargement de l'image : " + e.getMessage());
                        }
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
