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
        // Ensure the WebView is instantiated and ready
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
}
