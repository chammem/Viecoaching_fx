package controllers;


import entities.Groupe;
import entities.Typegroupe;
import entities.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.ServiceGroupe;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static java.awt.SystemColor.window;

public class AffichergrController implements Initializable {

    @FXML
    private GridPane gridPane;
    private ServiceGroupe service;
    @FXML
    private TextField searchField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        service = new ServiceGroupe();
        loadResources();
    }

    private void loadResources() {
        try {
            int row = 1; // Commence à partir de la première ligne (index 0)
            for (Groupe groupe : service.afficher()) {
                // Titre
                Label typeLabel = new Label(groupe.getTypegroupe_id().getNomtype()); // Utiliser le nom du type de groupe
                gridPane.add(typeLabel, 0, row);

                Label titleLabel = new Label(groupe.getNom());
                gridPane.add(titleLabel, 1, row);

                Label descriptionLabel = new Label(groupe.getDescription());
                gridPane.add(descriptionLabel, 2, row);

                // Date de création (convertir en chaîne de caractères)
                Label datecreationLabel = new Label(groupe.getDatecreation().toString());
                gridPane.add(datecreationLabel, 3, row);

                // Image (Si besoin d'afficher une image, configurez-la dans ImageView)
                ImageView imageView = new ImageView(new Image(groupe.getImage()));
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                gridPane.add(imageView, 4, row);

                //Actions (Supprimer et Mettre à jour)
                // Ajoutez les boutons de suppression et de mise à jour si nécessaire
                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(event -> deletegroupe(groupe));
                gridPane.add(deleteButton, 5, row);

                Button updateButton = new Button("Update");
                updateButton.setOnAction(event -> updaategroupe(groupe));
                gridPane.add(updateButton, 6, row);
                StringBuilder utilisateursText = new StringBuilder();
                for (Utilisateur utilisateur : groupe.getUtilisateurs()) {
                    utilisateursText.append(utilisateur.getNom()).append(", ");
                }
                Label utilisateursLabel = new Label(utilisateursText.toString());
                gridPane.add(utilisateursLabel, 7, row); // Ajoutez la colonne des utilisateurs à la position 7

                row++; // Passer à la ligne suivante
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
            private void updaategroupe(Groupe groupe) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifiergroupe.fxml"));
                    Parent modif = loader.load();

                    ModifiergroupeController controller = loader.getController();
                    controller.initData(groupe);


                    // Passer le groupe sélectionné au contrôleur ModifiertypeController

                    Scene mod= new Scene(modif);
                    Stage updateStage = new Stage();
                    updateStage.setScene(mod);
                    updateStage.showAndWait();

                    refreshGridPane(); // Rafraîchir le contenu du GridPane après la mise à jour
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    private void deletegroupe(Groupe groupe) {
        try {
            service.supprimer(groupe.getId());
            loadResources();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshGridPane() {
        gridPane.getChildren().clear(); // Effacer le contenu actuel du GridPane
        loadResources(); // Recharger les ressources et mettre à jour le GridPane
    }


}
