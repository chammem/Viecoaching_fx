package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class Map implements Initializable {

    @FXML
    private WebView webView;

    private WebEngine webEngine;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = webView.getEngine();
        webView.setContextMenuEnabled(false);

        // Chargement de la carte Leaflet
        loadLeafletMap();
    }

    private void loadLeafletMap() {
        // Chargement de la page HTML contenant la carte Leaflet
        webEngine.loadContent(
                "<html>"
                        + "<head>"
                        + "    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet/dist/leaflet.css\" />"
                        + "    <script src=\"https://unpkg.com/leaflet/dist/leaflet.js\"></script>"
                        + "    <style>"
                        + "        #map { height: 100%; }"
                        + "    </style>"
                        + "</head>"
                        + "<body>"
                        + "    <div id=\"map\"></div>"
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
                        + "</html>"
        );
    }
}