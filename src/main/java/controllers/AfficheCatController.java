package controllers;

import entities.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.ServiceCategorie;
import services.ServiceRessource;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AfficheCatController implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField tText;

    @FXML
    private PieChart pieChart;

    private ServiceCategorie serviceCategorie;
    private ServiceRessource serviceRessource;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceCategorie = new ServiceCategorie();
        serviceRessource = new ServiceRessource();
        loadCategories();
        displayCategoryStatistics();
        tText.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                filterCategories(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
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

        // Afficher une image si besoin
        ImageView imageView = new ImageView(new Image(categorie.getImage()));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        gridPane.add(imageView, 3, row);

        // Boutons d'action (Supprimer et Mettre à jour)
        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(event -> deleteCategory(categorie));
        gridPane.add(deleteButton, 4, row);

        Button updateButton = new Button("Mettre à jour");
        updateButton.setOnAction(event -> updateCategory(categorie));
        gridPane.add(updateButton, 5, row);
    }

    private void deleteCategory(Categorie categorie) {
        try {
            serviceCategorie.supprimer(categorie.getId());
            loadCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCategory(Categorie categorie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifierCat.fxml"));
            Stage updateStage = new Stage();
            updateStage.setScene(new Scene(loader.load()));

            ModifierCatController controller = loader.getController();
            controller.initData(categorie);

            updateStage.setTitle("Modifier Catégorie");
            updateStage.showAndWait();

            loadCategories();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayCategoryStatistics() {
        try {
            ObservableList<String> categoryTypes = FXCollections.observableArrayList(serviceCategorie.getCategoryTypes());
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            for (String categoryType : categoryTypes) {
                int count = serviceCategorie.getCountByCategoryType(categoryType);
                pieChartData.add(new PieChart.Data(categoryType, count));
            }

            pieChart.setData(pieChartData);
        } catch (SQLException e) {
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
}
