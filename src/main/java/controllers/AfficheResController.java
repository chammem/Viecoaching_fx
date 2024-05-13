package controllers;

import entities.Categorie;
import entities.Ressources;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceRessource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class AfficheResController implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField tText;
    @FXML
    private Button NavBarCat;

    @FXML
    private Button NavBarRes;
    @FXML
    private VBox contentVBox;


    private ServiceRessource service;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        service = new ServiceRessource();
        loadResources();

        // Add listener to the TextField for search functionality
        tText.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                filterResources(newValue);
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception gracefully
            }
        });


        Label typeLabel = new Label("Titre Ressource");
        typeLabel.setStyle("-fx-font-weight: bold;"); // Appliquer le style en gras
        gridPane.add(typeLabel, 0, 0);

        Label titleLabel = new Label("Type");
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
        gridPane.add(actionLabel, 5, 0);

        Label updateLabel = new Label("Modifier");
        updateLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(updateLabel, 6, 0);



    }

    private void loadResources() {
        try {
            ObservableList<Ressources> resources = FXCollections.observableArrayList(service.afficher());
            gridPane.getChildren().clear(); // Clear existing items in GridPane
            AtomicInteger row = new AtomicInteger(1); // Start from the second row (index 1)
            resources.forEach(ressource -> {
                try {
                    addResourceToGrid(ressource, row.getAndIncrement());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception gracefully
        }
    }

    private void addResourceToGrid(Ressources ressources, int row) throws IOException {
        // Titre
        Label typeLabel = new Label(ressources.getType_r());
        gridPane.add(typeLabel, 0, row);

        Label titleLabel = new Label(ressources.getTitre_r());
        gridPane.add(titleLabel, 1, row);

        Label descriptionLabel = new Label(ressources.getDescription());
        gridPane.add(descriptionLabel, 2, row);

        String imageUrl = ressources.getUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                File file = new File("C:/Users/LENOVO/Desktop/3A55/Pidev/viecoaching/public/uploads/" + imageUrl);

                if (file.exists()) {
                    // Local file path exists, load image from local file
                    InputStream inputStream = new FileInputStream(file);
                    Image image = new Image(inputStream);
                    inputStream.close();

                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    gridPane.add(imageView, 3, row);
                } else if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                    // Web URL, load image from web
                    URL url = new URL(imageUrl);
                    InputStream inputStream = url.openStream();
                    Image image = new Image(inputStream);
                    inputStream.close();

                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    gridPane.add(imageView, 3, row);
                } else {
                    System.err.println("Invalid image URL: " + imageUrl);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle image loading errors
            }
        }





        //Actions (Supprimer et Mettre à jour)
        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(event -> deleteResource(ressources));
        gridPane.add(deleteButton, 5, row);

        Button updateButton = new Button("Mettre à jour");
        updateButton.setOnAction(event -> updateResource(ressources));
        gridPane.add(updateButton, 6, row);
    }


    private void deleteResource(Ressources ressources) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous supprimer cette ressource ?");

        // Affiche une boîte de dialogue de confirmation avec des boutons Oui et Non
        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    // Supprime la ressource en utilisant le service
                    service.supprimer(ressources.getId());

                    // Actualise la liste des ressources après la suppression
                    Platform.runLater(this::loadResources);
                } catch (SQLException e) {
                    e.printStackTrace(); // Gérer l'exception de manière appropriée
                }
            }
        });
    }
    @FXML
    void AjoutR(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ressource.fxml"));
            Stage stage = (Stage) tText.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }
    private void updateResource(Ressources ressources) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifierRes.fxml"));
            Parent modif = loader.load();
            ModifierResController controller = loader.getController();
            controller.initData(ressources);
            Stage stage = (Stage) tText.getScene().getWindow();
            stage.setScene(new Scene(modif));
            Platform.runLater(this::loadResources);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void filterResources(String searchText) throws SQLException {
        ObservableList<Ressources> resources = FXCollections.observableArrayList(service.afficher());
        gridPane.getChildren().clear(); // Clear existing items in GridPane
        AtomicInteger row = new AtomicInteger(1); // Start from the second row (index 1)

        resources.stream()
                .filter(ressource -> ressource.getType_r().toLowerCase().contains(searchText.toLowerCase()))
                .forEach(ressource -> {
                    try {
                        addResourceToGrid(ressource, row.getAndIncrement());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
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
    void statsAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/sats.fxml"));
            Stage stage = (Stage) tText.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }







}