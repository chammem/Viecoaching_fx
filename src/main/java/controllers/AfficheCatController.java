package controllers;

import entities.Categorie;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.ServiceCategorie;
import services.ServiceRessource;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AfficheCatController implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField tText;



    private ServiceCategorie serviceCategorie;
    private ServiceRessource serviceRessource;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceCategorie = new ServiceCategorie();
        serviceRessource = new ServiceRessource();
        loadCategories();
       // displayCategoryStatistics();
        tText.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                filterCategories(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        Label typeLabel = new Label("Nom Catégorie");
        typeLabel.setStyle("-fx-font-weight: bold;"); // Appliquer le style en gras
        gridPane.add(typeLabel, 0, 0);

        Label titleLabel = new Label("Ressource");
        titleLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(titleLabel, 1, 0);

        Label descriptionLabel = new Label("Description");
        descriptionLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(descriptionLabel, 2, 0);



        Label imageLabel = new Label("Image");
        imageLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(imageLabel, 3, 0);

        Label actionLabel = new Label("Supprimer");
        actionLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(actionLabel, 4, 0);

        Label updateLabel = new Label("Modifier");
        updateLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(updateLabel, 5, 0);

    }

    private void loadCategories() {
        try {
            ObservableList<Categorie> categories = FXCollections.observableArrayList(serviceCategorie.afficher());
            gridPane.getChildren().clear();
            AtomicInteger row = new AtomicInteger(1);
            categories.forEach(categorie -> addCategoryToGrid(categorie, row.getAndIncrement()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCategoryToGrid(Categorie categorie, int row) {
        // Ajouter les détails de la catégorie dans les colonnes du GridPane
        Label nameLabel = new Label(categorie.getNom_categorie());
        gridPane.add(nameLabel, 0, row);

        Label titleLabel = new Label(categorie.getRessource_id().getType_r());
        gridPane.add(titleLabel, 1, row);

        Label descriptionLabel = new Label(categorie.getDescription());
        gridPane.add(descriptionLabel, 2, row);

        String imageUrl = categorie.getImage(); // Obtenez l'URL de l'image à partir de l'objet Categorie

        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Créer un objet Image à partir de l'URL
            Image image = new Image("file:///C:/chemin/vers/le/dossier/images/" + imageUrl); // Remplacez le chemin par le chemin réel de votre dossier d'images

            // Créer un objet ImageView pour afficher l'image
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100); // Définir la largeur de l'ImageView
            imageView.setFitHeight(100); // Définir la hauteur de l'ImageView

            // Ajouter l'ImageView au GridPane à l'emplacement spécifié
            gridPane.add(imageView, 3, row);
        } else {
            // Gérer le cas où l'URL de l'image est vide ou null
            // Vous pouvez afficher un message ou une image par défaut
            System.out.println("L'URL de l'image est vide ou null.");
        }

        // Boutons d'action (Supprimer et Mettre à jour)
        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(event -> deleteCategory(categorie));
        gridPane.add(deleteButton, 4, row);

        Button updateButton = new Button("Mettre à jour");
        updateButton.setOnAction(event -> updateCategory(categorie));
        gridPane.add(updateButton, 5, row);
    }

    private void deleteCategory(Categorie categorie) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous supprimer ce catégorie ?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    // Supprime la ressource en utilisant le service
                    serviceCategorie.supprimer(categorie.getId());

                    // Actualise la liste des ressources après la suppression
                    Platform.runLater(this::loadCategories);
                } catch (SQLException e) {
                    e.printStackTrace(); // Gérer l'exception de manière appropriée
                }
            }
        });

    }

    private void updateCategory(Categorie categorie) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifierCat.fxml"));

            Parent modif = loader.load();
            ModifierCatController controller = loader.getController();
            controller.initData(categorie);
            Stage stage = (Stage) tText.getScene().getWindow();
            stage.setScene(new Scene(modif));
            Platform.runLater(this::loadCategories);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void filterCategories(String searchText) throws SQLException {
        ObservableList<Categorie> categories = FXCollections.observableArrayList(serviceCategorie.afficher());
        gridPane.getChildren().clear();
        AtomicInteger row = new AtomicInteger(1);

        categories.stream()
                .filter(categorie -> categorie.getNom_categorie().toLowerCase().contains(searchText.toLowerCase()))
                .forEach(categorie -> addCategoryToGrid(categorie, row.getAndIncrement()));
    }
    @FXML
    void NavBarCat(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/afficheCategorie.fxml"));
            Stage stage = (Stage)tText.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }

    @FXML
    void NavBarRes(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/afficheRessource.fxml"));
            Stage stage = (Stage) tText.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }
    @FXML
    void AjoutCat(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AjoutCat.fxml"));
            Stage stage = (Stage) tText.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }
    @FXML
    void stats(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/sats.fxml"));
            Stage stage = (Stage) tText.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }
}
