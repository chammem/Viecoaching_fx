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
import java.util.stream.Collectors;

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

    private final int pageSize = 3;



    private ObservableList<Typegroupe> displayedTypeGroupes = FXCollections.observableArrayList();
    @FXML
    private void nextPage(ActionEvent event) {
        int maxPageIndex = Math.max(0, displayedTypeGroupes.size() / pageSize); // Calculer le nombre maximal de pages
        pageIndex = Math.min(pageIndex + 1, maxPageIndex); // Augmenter l'index de page tout en s'assurant qu'il ne dépasse pas la valeur maximale
        loadResources2();
    }


    @FXML
    private void previousPage(ActionEvent event) {
        pageIndex = Math.max(0, pageIndex - pageSize);
        loadResources2();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

            loadResources2(); // Appel de loadResources2()


        // Ajouter un écouteur de changement pour le champ de texte de recherche


    }






    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Typeg.fxml"));
            Stage stage = (Stage) gridPane.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }
    private void loadResources2() {
        try {
            ObservableList<Typegroupe> typegroupes;
            if (pageIndex == 0) {
                // Si c'est la première page, charger uniquement trois lignes
                typegroupes = FXCollections.observableArrayList(service.afficherg(pageIndex, pageSize));
            } else {
                // Sinon, charger les données normalement avec le pageSize complet
                typegroupes = FXCollections.observableArrayList(service.afficherg(pageIndex, pageSize));
            }

            gridPane.getChildren().clear();

            // Mettre à jour la liste affichée avec les éléments à afficher sur la page actuelle
            displayedTypeGroupes = FXCollections.observableArrayList(typegroupes);

            AtomicInteger row = new AtomicInteger(1);

            // Charger les ressources pour la page actuelle
            displayedTypeGroupes.forEach(typegroupe -> loadResources(typegroupe, row.getAndIncrement()));
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception de manière appropriée
        }
    }


    @FXML
    private void filterResources(String searchText) {
        try {
            List<Typegroupe> filteredList = service.getAllTypegroupe().stream()
                    .filter(typegroupe -> typegroupe.getNomtype().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());

            displayedTypeGroupes = FXCollections.observableArrayList(filteredList);

            pageIndex = 0; // Réinitialiser l'index de la page à 0 après la recherche
            loadResources2(); // Recharger les ressources en fonction de la recherche et de la pagination
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            Stage stage = (Stage) gridPane.getScene().getWindow();
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
