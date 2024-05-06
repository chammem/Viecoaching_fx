package controllers;



import entities.Typegroupe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ServiceTypegroupe;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
public class TypegController  implements Initializable {


    @FXML
    private TextField nomField;
    private String nomFichierurlSelectionne;
        @FXML

        void ajouter(ActionEvent event) {
            String nom =nomField.getText();



            ServiceTypegroupe serviceTypegroupe = new ServiceTypegroupe();

            try {
                // Créer une nouvelle groupe avec les valeurs obtenues
                Typegroupe typegroupe = new Typegroupe(nom);

                // Ajouter la groupe en utilisant le service
                serviceTypegroupe.ajouter(typegroupe);

                // Afficher une boîte de dialogue d'information pour indiquer que la groupe a été ajoutée avec succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("groupe ajoutée avec succès !");
                alert.showAndWait();
                loadAfficheCategorieVieww();

            } catch (SQLException e) {
                // En cas d'erreur lors de l'ajout de la groupe, afficher l'erreur dans la console
                System.out.println("Erreur lors de l'ajout de la groupe : " + e.getMessage());
                e.printStackTrace();
            }


        }
    private void loadAfficheCategorieVieww() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AfficheType.fxml"));
            Stage stage = (Stage) nomField.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheCategorie.fxml : " + e.getMessage());
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
    private void  loadAffichetype() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AfficheType.fxml"));
            Stage stage = (Stage) nomField.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }



        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            // À compléter selon les besoins
        }

}
