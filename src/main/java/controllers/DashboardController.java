package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import services.ServiceCategorie;
import services.ServiceGroupe;
import services.ServiceRessource;
import services.ServiceTypegroupe;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Button NavBarCat;

    @FXML
    private Button NavBarGroupe;

    @FXML
    private Button NavBarRes;

    @FXML
    private Button NavBarRub;

    @FXML
    private Button NavBarSeance;

    @FXML
    private Button NavBarUser;

    @FXML
    private VBox vbox;

    @FXML
    private WebView webView;
    private WebEngine webEngine;
    private ServiceCategorie serviceCategorie;
    private ServiceRessource serviceRessource;
    @FXML
    private PieChart pieChart ;
    @FXML
    private PieChart piechart2;

    @FXML
    private PieChart piechart3;

    @FXML
    private PieChart piechart1;

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
    @FXML
    void NavBarC(ActionEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/afficheCategorie.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vbox.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vbox.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }
    }

    @FXML
    void NavBarR(ActionEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/afficheRessource.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vbox.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vbox.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }
    }

    @FXML
    void stats(ActionEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sats.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vbox.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vbox.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }
    }
    @FXML
    void NavBarRub(ActionEvent event) {

    }
    private ServiceTypegroupe serviceTypegroupe;
    private ServiceGroupe serviceGroupe;

    @FXML
    void NavBarSeance(ActionEvent event) {

    }

    @FXML
    void NavBarUser(ActionEvent event) {

    }
    @FXML
    void NavBarGroupe(ActionEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Affichegr.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vbox.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vbox.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }
    }
    @FXML
    void NavBarType(ActionEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficheType.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vbox.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vbox.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (webView != null) {
            webEngine = webView.getEngine();
            webView.setContextMenuEnabled(false);

            // Load the Leaflet map
            loadLeafletMap();
        } else {
            System.err.println("WebView is not injected by FXMLLoader");
        }
        serviceTypegroupe = new ServiceTypegroupe();
        serviceGroupe = new ServiceGroupe();

        serviceCategorie = new ServiceCategorie();
        serviceRessource = new ServiceRessource();
        displayCategoryStatistics();
        try {
            configurePieChartDataa();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            configurePieChartDataGroupe();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            configurePieChartData();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        //displayCategoryStatistics();


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
    private void configurePieChartDataa() throws SQLException {
        // Obtenir le nombre de groupes depuis le service ou la méthode de service appropriée
        int nombreGroupes = serviceTypegroupe.countGroupes();

        // Créer une liste observable pour stocker les données du PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Types Groupes", nombreGroupes),
                new PieChart.Data("Reste", 100 - nombreGroupes) // Vous pouvez ajuster cette valeur en fonction de vos besoins
        );

        // Ajouter les données au PieChart
        piechart3.setData(pieChartData);
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
    private void configurePieChartDataGroupe() throws SQLException {
        // Obtenir le nombre de groupes depuis le service ou la méthode de service appropriée
        int nombreGroupes = serviceGroupe.countGroupes();

        // Créer une liste observable pour stocker les données du PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Groupes", nombreGroupes),
                new PieChart.Data("Reste", 100 - nombreGroupes) // Vous pouvez ajuster cette valeur en fonction de vos besoins
        );

        // Ajouter les données au PieChart
        piechart2.setData(pieChartData);
    }
}

