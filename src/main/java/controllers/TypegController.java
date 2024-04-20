package controllers;



import entities.Typegroupe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import services.ServiceTypegroupe;

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
            } catch (SQLException e) {
                // En cas d'erreur lors de l'ajout de la groupe, afficher l'erreur dans la console
                System.out.println("Erreur lors de l'ajout de la groupe : " + e.getMessage());
                e.printStackTrace();
            }
        }




        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            // À compléter selon les besoins
        }

}
