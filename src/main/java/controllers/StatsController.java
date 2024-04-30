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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import services.ServiceCategorie;
import services.ServiceRessource;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StatsController implements Initializable {
    private ServiceCategorie serviceCategorie;
    private ServiceRessource serviceRessource;
    @FXML
    private PieChart pieChart ;
    @FXML
    private PieChart piechart1;
    @FXML
    private WebView webView;

    private WebEngine webEngine;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceCategorie = new ServiceCategorie();
        serviceRessource = new ServiceRessource();
        displayCategoryStatistics();
        try {
            configurePieChartData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //displayCategoryStatistics();

        if (webView != null) {
            webEngine = webView.getEngine();
            webView.setContextMenuEnabled(false);

            // Load the Leaflet map
            loadLeafletMap();
        } else {
            System.err.println("WebView is not injected by FXMLLoader");
        }
    }
    private void loadLeafletMap() {
        // Load the HTML content containing the Leaflet map
        String htmlContent = "<html>"
                + "<head>"
                + "    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet/dist/leaflet.css\" />"
                + "    <script src=\"https://unpkg.com/leaflet/dist/leaflet.js\"></script>"
                + "    <style>"
                + "        #map { height: 100%; }"
                + "    </style>"
                + "</head>"
                + "<body>"
                + "    <div id=\"map\" style=\"height: 100%;\"></div>"  // Ensure map container fills WebView
                + "    <script>"
                + "        var map = L.map('map').setView([51.505, -0.09], 13);"
                + "        L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {"
                + "            attribution: '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors'"
                + "        }).addTo(map);"
                + "        L.marker([51.5, -0.09]).addTo(map)"
                + "            .bindPopup('A pretty CSS popup.<br> Easily customizable.')"
                + "            .openPopup();"
                + "    </script>"
                + "</body>"
                + "</html>";

        // Load the HTML content into the WebEngine
        webEngine.loadContent(htmlContent);
    }
    private void displayCategoryStatistics() {
        try {
            ObservableList<String> categoryTypes = FXCollections.observableArrayList(serviceRessource.getCategoryTypes());
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            for (String categoryType : categoryTypes) {
                int count = serviceRessource.getCountByCategoryType(categoryType);
                pieChartData.add(new PieChart.Data(categoryType, count));
            }

            piechart1.setData(pieChartData);
        } catch (SQLException e) {
            e.printStackTrace();
        }

}

    private void configurePieChartData() throws SQLException {
        // Obtenir le nombre de groupes depuis le service ou la méthode de service appropriée
        int nbr = serviceCategorie.countCategorie();

        // Créer une liste observable pour stocker les données du PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Catégorie", nbr),
                new PieChart.Data("Reste", 100 - nbr) // Vous pouvez ajuster cette valeur en fonction de vos besoins
        );

        // Ajouter les données au PieChart
        pieChart.setData(pieChartData);
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
