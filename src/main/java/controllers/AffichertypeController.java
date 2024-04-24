package controllers;

import entities.Typegroupe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.ServiceTypegroupe;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AffichertypeController implements Initializable {

    @FXML
    private GridPane gridPane;

    private final ServiceTypegroupe service = new ServiceTypegroupe();
    @FXML
    private TextField hh;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadResources();
    }
    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Typeg.fxml"));
            Parent ajoutGroupeView = loader.load();

            // Obtenez le contrôleur de la vue Ajoutgroupe.fxml si nécessaire
            // AjoutgroupeController controller = loader.getController();

            Scene scene = new Scene(ajoutGroupeView);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadResources() {
        gridPane.getChildren().clear(); // Effacer le contenu actuel du GridPane

        List<Typegroupe> typeGroupes;
        try {
            typeGroupes = service.afficher();
            int rowIndex = 1; // Commence à partir de la deuxième ligne (index 1)

            // Titres de colonnes en gras
            Label titleLabel1 = new Label("type Groupe");
            gridPane.add(titleLabel1, 0, 0);
            titleLabel1.setStyle("-fx-font-weight: bold;");

            Label titleLabel2 = new Label("Supprimer");
            titleLabel2.setStyle("-fx-font-weight: bold;");
            gridPane.add(titleLabel2, 1, 0);

            Label titleLabel3 = new Label("Modifier");
            titleLabel3.setStyle("-fx-font-weight: bold;");
            gridPane.add(titleLabel3, 2, 0);

            for (Typegroupe typegroupe : typeGroupes) {
                Label typeLabel = new Label(typegroupe.getNomtype());
                gridPane.add(typeLabel, 0, rowIndex);

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(event -> deleteTypegroupe(typegroupe));
                deleteButton.setStyle("-fx-background-color: red;");
                gridPane.add(deleteButton, 1, rowIndex);

                Button updateButton = new Button("Update");
                updateButton.setOnAction(event -> updateTypegroupe(typegroupe));
                updateButton.setStyle("-fx-background-color: green;");
                gridPane.add(updateButton, 2, rowIndex);

                rowIndex++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void NavBarCat(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Affichegr.fxml"));
            Stage stage = (Stage) gridPane.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }

    @FXML
    void NavBarRes(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AfficheType.fxml"));
            Stage stage = (Stage) gridPane.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }}

    private void deleteTypegroupe(Typegroupe typegroupe) {
        try {
            service.supprimer(typegroupe.getId());
            showAlertGroupeSupprime();
            loadResources();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTypegroupe(Typegroupe typegroupe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Modifiertype.fxml"));
            Parent modif = loader.load();
            ModifiertypeController controller = loader.getController();
            controller.initData(typegroupe);

            Stage updateStage = new Stage();
            updateStage.setScene(new Scene(modif));
            updateStage.setTitle("Update Resource");
            updateStage.showAndWait();

            loadResources();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlertGroupeSupprime() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Groupe supprimé");
        alert.setHeaderText(null);
        alert.setContentText("Le groupe a été supprimé avec succès !");
        alert.showAndWait();
    }
}
