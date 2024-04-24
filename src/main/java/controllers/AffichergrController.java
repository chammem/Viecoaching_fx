package controllers;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import entities.Groupe;
import entities.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.ServiceGroupe;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AffichergrController implements Initializable {
    @FXML
    private PieChart pieChart;
    @FXML
    private GridPane gridPane;

    @FXML
    private TextField searchField;

    private String searchText;
    private ServiceGroupe service;
    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Groupe.fxml"));
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
    @FXML
    void NavBarCat(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Affichegr.fxml"));
            Stage stage = (Stage)searchField.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }

    @FXML
    void NavBarRes(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AfficheType.fxml"));
            Stage stage = (Stage) searchField.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }}

    @FXML
    private void searchAction(KeyEvent event) {
        searchText = searchField.getText();
        // Recharger les ressources en fonction du texte de recherche
        loadResources();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        service = new ServiceGroupe();
        loadResources();
        try {
            configurePieChartData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Ajouter des labels pour définir les champs au-dessus de chaque colonne
        Label typeLabel = new Label("Type de groupe");
        typeLabel.setStyle("-fx-font-weight: bold;"); // Appliquer le style en gras
        gridPane.add(typeLabel, 0, 0);

        Label titleLabel = new Label("Nom de groupe");
        titleLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(titleLabel, 1, 0);

        Label descriptionLabel = new Label("Description");
        descriptionLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(descriptionLabel, 2, 0);

        Label dateCreationLabel = new Label("Date de création");
        dateCreationLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(dateCreationLabel, 3, 0);

        Label imageLabel = new Label("Image");
        imageLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(imageLabel, 4, 0);

        Label actionLabel = new Label("Actions");
        actionLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(actionLabel, 5, 0);

        Label updateLabel = new Label("Modifier");
        updateLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(updateLabel, 6, 0);

        Label utilisateursLabel = new Label("Utilisateurs");
        utilisateursLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(utilisateursLabel, 7, 0);
    }

    private void showAlertGroupeSupprime() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Groupe supprimé");
        alert.setHeaderText(null);
        alert.setContentText("Le groupe a été supprimé avec succès !");
        alert.showAndWait();
    }

    private void configurePieChartData() throws SQLException {
        // Obtenir le nombre de groupes depuis le service ou la méthode de service appropriée
        int nombreGroupes = service.countGroupes();

        // Créer une liste observable pour stocker les données du PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Groupes", nombreGroupes),
                new PieChart.Data("Reste", 100 - nombreGroupes) // Vous pouvez ajuster cette valeur en fonction de vos besoins
        );

        // Ajouter les données au PieChart
        pieChart.setData(pieChartData);
    }
    private void loadResources() {
        try {
            gridPane.getChildren().clear(); // Effacer le contenu actuel du GridPane

            final int[] row = {1}; // Commence à partir de la première ligne (index 0)
            service.afficher().stream()
                    .filter(groupe -> groupe.getNom().toLowerCase().contains((searchText != null ? searchText : "").toLowerCase()))
                    .forEach(groupe -> {
                        Label typeLabel = new Label(groupe.getTypegroupe_id().getNomtype());
                        gridPane.add(typeLabel, 0, row[0]);

                        Label titleLabel = new Label(groupe.getNom());
                        gridPane.add(titleLabel, 1, row[0]);

                        Label descriptionLabel = new Label(groupe.getDescription());
                        gridPane.add(descriptionLabel, 2, row[0]);

                        Label datecreationLabel = new Label(groupe.getDatecreation().toString());
                        gridPane.add(datecreationLabel, 3, row[0]);

                        ImageView imageView = new ImageView(new Image(groupe.getImage()));
                        imageView.setFitWidth(100);
                        imageView.setFitHeight(100);
                        gridPane.add(imageView, 4, row[0]);

                        Button deleteButton = new Button("Delete");
                        deleteButton.setOnAction(event -> deletegroupe(groupe));
                        deleteButton.setStyle("-fx-background-color: red;");
                        gridPane.add(deleteButton, 5, row[0]);

                        Button updateButton = new Button("Update");
                        updateButton.setOnAction(event -> updaategroupe(groupe));
                        gridPane.add(updateButton, 6, row[0]);

                        StringBuilder utilisateursText = new StringBuilder();
                        groupe.getUtilisateurs().forEach(utilisateur -> utilisateursText.append(utilisateur.getNom()).append(", "));
                        Label utilisateursLabel = new Label(utilisateursText.toString());
                        gridPane.add(utilisateursLabel, 7, row[0]);

                        row[0]++;
                    });
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

            Scene mod= new Scene(modif);
            Stage updateStage = new Stage();
            updateStage.setScene(mod);
            updateStage.showAndWait();

            refreshGridPane(); // Rafraîchir le contenu du GridPane après la mise à jour
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlertGroupeModifie() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Groupe modifié");
        alert.setHeaderText(null);
        alert.setContentText("Le groupe a été modifié avec succès !");
        alert.showAndWait();

        // Fermer la fenêtre de mise à jour
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.close();
    }

    private void deletegroupe(Groupe groupe) {
        try {
            service.supprimer(groupe.getId());
            showAlertGroupeSupprime(); // Afficher l'alerte après la suppression réussie
            loadResources();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPieChartData() {
        try {
            int nombreGroupes = service.countGroupes(); // Méthode à implémenter dans votre service pour compter le nombre de groupes
            int reste = 100 - nombreGroupes;

            PieChart.Data groupesData = new PieChart.Data("Groupes", nombreGroupes);
            PieChart.Data resteData = new PieChart.Data("Reste", reste);

            pieChart.getData().add(groupesData);
            pieChart.getData().add(resteData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshGridPane() {
        gridPane.getChildren().clear(); // Effacer le contenu actuel du GridPane
        loadResources(); // Recharger les ressources et mettre à jour le GridPane
    }
}
