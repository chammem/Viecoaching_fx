package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import services.ServiceCategorie;
import services.ServiceRessource;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StatsController implements Initializable {
    private ServiceCategorie serviceCategorie;
    private ServiceRessource serviceRessource;
    @FXML
    private PieChart pieChart ;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceCategorie = new ServiceCategorie();
        serviceRessource = new ServiceRessource();
        // loadCategories();
        displayCategoryStatistics();
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
    @FXML
    void NavBarC(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/afficheCategorie.fxml"));
            Stage stage = (Stage)pieChart.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }

    @FXML
    void NavBarR(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/afficheRessource.fxml"));
            Stage stage = (Stage) pieChart.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }
    @FXML
    void stats(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/sats.fxml"));
            Stage stage = (Stage) pieChart.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }
}
