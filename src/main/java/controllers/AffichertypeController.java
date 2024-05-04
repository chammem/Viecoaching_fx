package controllers;

import entities.Typegroupe;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.ServiceGroupe;
import services.ServiceTypegroupe;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class AffichertypeController implements Initializable {

    @FXML
    private GridPane gridPane;
    @FXML
    private PieChart pieChart;
    private final ServiceTypegroupe service = new ServiceTypegroupe();
    @FXML
    private TextField hh;
    private int currentPage = 1;
    private int pageIndex = 0; // Index de la première ligne à afficher
    private final int pageSize = 5;

    private ObservableList<Typegroupe> displayedTypeGroupes = FXCollections.observableArrayList();
    @FXML
    private void nextPage(ActionEvent event) {
        pageIndex += pageSize; // Passer à la page suivante
        loadResources2(); // Recharger les ressources avec la nouvelle plage
    }

    @FXML
    private void previousPage(ActionEvent event) {
        pageIndex = Math.max(0, pageIndex - pageSize); // Passer à la page précédente (assurez-vous que l'index ne devient pas négatif)
        loadResources2(); // Recharger les ressources avec la nouvelle plage
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadResources2(); // Appel de loadResources2()
            configurePieChartData(); // Appel de configurePieChartData()
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception de manière appropriée
        }

        // Ajouter un écouteur de changement pour le champ de texte de recherche
        hh.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                filterResources(newValue);
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception gracefully
            }
        });
    }



    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Typeg.fxml"));
            Stage stage = (Stage) hh.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }
    private void loadResources2() {
        try {
            ObservableList<Typegroupe> typegroupes = FXCollections.observableArrayList(service.afficher());
            gridPane.getChildren().clear();
            int startIndex = Math.min(pageIndex, service.countGroupes());

            int itemsPerPage = 5;
            int startIdx = 0;
            int endIdx = Math.min(startIdx + itemsPerPage, typegroupes.size());
            AtomicInteger row = new AtomicInteger(1);

            for (int i = startIdx; i < endIdx; i++) {
                loadResources(typegroupes.get(i), row.getAndIncrement());
            } // Start from the second row (index 1)
            typegroupes.forEach(typegroupe ->loadResources(typegroupe,row.getAndIncrement()));
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception gracefully
        }
    }
    private void filterResources(String searchText) throws SQLException {
        ObservableList<Typegroupe> typegroupes = FXCollections.observableArrayList(service.afficher());
        gridPane.getChildren().clear(); // Clear existing items in GridPane
        AtomicInteger row = new AtomicInteger(1); // Start from the second row (index 1)

        typegroupes.stream()
                .filter(typegroupe -> typegroupe.getNomtype().toLowerCase().contains(searchText.toLowerCase()))
                .forEach(typegroupe -> loadResources(typegroupe,row.getAndIncrement()));
    }
    private void loadResources(Typegroupe typegroupe ,int row) {



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

                Label typeLabel = new Label(typegroupe.getNomtype());
                gridPane.add(typeLabel, 0, row);

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(event -> deleteTypegroupe(typegroupe));
                deleteButton.setStyle("-fx-background-color: red;");
                gridPane.add(deleteButton, 1, row);

                Button updateButton = new Button("Update");
                updateButton.setOnAction(event -> updateTypegroupe(typegroupe));
                updateButton.setStyle("-fx-background-color: green;");
                gridPane.add(updateButton, 2, row);



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
    private void configurePieChartData() throws SQLException {
        // Obtenir le nombre de groupes depuis le service ou la méthode de service appropriée
        int nombretype = service.countGroupes();

        // Créer une liste observable pour stocker les données du PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Type Groupe", nombretype),
                new PieChart.Data("Reste", 100 - nombretype) // Vous pouvez ajuster cette valeur en fonction de vos besoins
        );

        // Ajouter les données au PieChart
        pieChart.setData(pieChartData);
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
        // Créer une alerte de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer ce type de groupe ?");

        // Ajouter les boutons "OK" et "Annuler"
        ButtonType okButton = new ButtonType("OUI", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, cancelButton);

        // Afficher l'alerte et attendre la réponse de l'utilisateur
        Optional<ButtonType> result = alert.showAndWait();

        // Vérifier la réponse de l'utilisateur
        if (result.isPresent() && result.get() == okButton) {
            // Si l'utilisateur a cliqué sur "OK", supprimer le type de groupe
            try {
                service.supprimer(typegroupe.getId());
                showAlertGroupeSupprime(); // Afficher l'alerte après la suppression réussie
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private void updateTypegroupe(Typegroupe typegroupe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Modifiertype.fxml"));
            Parent modif = loader.load();
            ModifiertypeController controller = loader.getController();
            controller.initData(typegroupe);
            Stage stage = (Stage) hh.getScene().getWindow();
            stage.setScene(new Scene(modif));
            Platform.runLater(this::loadResources2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void loadAffichergr() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Affichergr.fxml"));
            Parent root = loader.load();
            AffichergrController controller = loader.getController();
            controller.loadResources(); // Appelez la méthode pour recharger les données
            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setScene(new Scene(root));
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
