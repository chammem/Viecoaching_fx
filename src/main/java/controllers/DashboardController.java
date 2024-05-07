package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import services.ServiceCategorie;
import services.ServiceRessource;
import services.ServiceUtilisateur;
import utils.MyDatabase;
import utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    public Button décnxId;
    public Button profil;
    public Label labelTotalUsers;
    public Label labelTotalCoaches;
    public Label labelTotalPatients;
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
    private PieChart piechart1;
    private ServiceUtilisateur serviceUtilisateur;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Vérifier si webView est injecté
        if (webView != null) {
            webEngine = webView.getEngine();
            webView.setContextMenuEnabled(false);

            // Charger la carte Leaflet
            loadLeafletMap();
        } else {
            System.err.println("WebView is not injected by FXMLLoader");
        }

        // Initialiser le service de catégorie et de ressource
        serviceCategorie = new ServiceCategorie();
        serviceRessource = new ServiceRessource();

        // Afficher les statistiques de catégorie
        displayCategoryStatistics();

        // Configurer les données du PieChart
        try {
            configurePieChartData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Initialiser le service utilisateur et mettre à jour les labels
        MyDatabase myDatabase = MyDatabase.getInstance();
        Connection connection = myDatabase.getConnection();
        this.serviceUtilisateur = new ServiceUtilisateur(connection);
        try {
            // Récupérer le nombre total d'utilisateurs
            int totalPatients = serviceUtilisateur.countPatients();
            int totalCoaches = serviceUtilisateur.countCoaches();
            int totalUsers = serviceUtilisateur.countAllUsers();

            // Mettre à jour le label correspondant avec le nombre total d'utilisateurs
            labelTotalUsers.setText(String.valueOf(totalUsers));
            labelTotalPatients.setText(String.valueOf(totalPatients));
            labelTotalCoaches.setText(String.valueOf(totalCoaches));
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    @FXML
    void NavBarSeance(ActionEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/seance.fxml"));
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
    void NavBarUser(ActionEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/utilisateur.fxml"));
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
    void NavBarGroupe(ActionEvent event) {

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
    private void handleDeconnexion() {
        SessionManager.endSession();

        // Fermer la fenêtre utilisateur.fxml
        Stage stage = (Stage) décnxId.getScene().getWindow();
        stage.close();

        // Charger et afficher la fenêtre login.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void navigateToProfil() {
        try {
            MyDatabase myDatabase = MyDatabase.getInstance();
            Connection connection = myDatabase.getConnection();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profil.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) profil.getScene().getWindow(); // Récupère la fenêtre actuelle
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}

