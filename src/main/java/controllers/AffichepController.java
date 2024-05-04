package controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import entities.Groupe;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.ServiceGroupe;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AffichepController  implements Initializable {
    @FXML
    private PieChart pieChart;
    @FXML
    private GridPane gridPane;

    @FXML
    private TextField searchField;

    private String searchText;
    private ServiceGroupe service;
    private int pageIndex = 0; // Index de la première ligne à afficher
    private final int pageSize = 3; // Taille de la page (nombre de lignes à afficher)











    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        service = new ServiceGroupe();
        loadResources();
        try {
            configurePieChartData();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Ajouter des labels pour définir les champs au-dessus de chaque colonne
        Label typeLabel = new Label("Type de groupe");
        typeLabel.setStyle("-fx-font-weight: bold;"); // Appliquer le style en gras
        gridPane.add(typeLabel, 0, 0);

        Label titleLabel = new Label("Nom de groupe");
        titleLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(titleLabel, 1, 0);

        Label descriptionLabel = new Label("Description");
        descriptionLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(descriptionLabel, 2, 0);

        Label dateCreationLabel = new Label("Date de création");
        dateCreationLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(dateCreationLabel, 3, 0);

        Label imageLabel = new Label("Image");
        imageLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(imageLabel, 4, 0);




        Label pdf = new Label("Generer PDF");
        pdf.setStyle("-fx-font-weight: bold;");
        gridPane.add(pdf, 5, 0);
    }

    private void showAlertGroupeSupprime() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Groupe supprimé");
        alert.setHeaderText(null);
        alert.setContentText("Le groupe a été supprimé avec succès !");
        alert.showAndWait();
    }

    private void configurePieChartData() throws SQLException {
        // Obtenir le nombre de groupes depuis le service ou la méthode de service appropriée
        int nombreGroupes = service.countGroupes();

        // Créer une liste observable pour stocker les données du PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Groupes", nombreGroupes),
                new PieChart.Data("Reste", 100 - nombreGroupes) // Vous pouvez ajuster cette valeur en fonction de vos besoins
        );
    }
        // Ajouter les données au PieChart

    public void loadResources() {
        try {
            gridPane.getChildren().clear(); // Effacer le contenu actuel du GridPane
            int startIndex = Math.min(pageIndex, service.countGroupes());
            int endIndex = Math.min(startIndex + pageSize, service.countGroupes());

            final int[] row = {1}; // Commence à partir de la première ligne (index 0)
            service.afficher().stream()
                    .filter(groupe -> groupe.getNom().toLowerCase().contains((searchText != null ? searchText : "").toLowerCase()))
                    .skip(pageIndex) // Ignorer les lignes avant l'index de la première ligne à afficher
                    .limit(pageSize) // Limiter le nombre de lignes à afficher par page
                    .forEach(groupe -> {
                        Label typeLabel = new Label(groupe.getTypegroupe_id().getNomtype());
                        gridPane.add(typeLabel, 0, row[0]);

                        Label titleLabel = new Label(groupe.getNom());
                        gridPane.add(titleLabel, 1, row[0]);

                        Label descriptionLabel = new Label(groupe.getDescription());
                        gridPane.add(descriptionLabel, 2, row[0]);

                        Label datecreationLabel = new Label(groupe.getDatecreation().toString());
                        gridPane.add(datecreationLabel, 3, row[0]);

                        ImageView imageView = new ImageView(new Image(groupe.getImage()));
                        imageView.setFitWidth(100);
                        imageView.setFitHeight(100);
                        gridPane.add(imageView, 4, row[0]);


                        Button pdfButton = new Button("Generate PDF");
                        pdfButton.setOnAction(event -> generatePDF(groupe)); // Ajouter le gestionnaire d'événements pour générer le PDF
                        gridPane.add(pdfButton, 5, row[0]);



                        row[0]++;
                    });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }







    private void generatePDF(Groupe groupe) {
        // Créer un nouveau document PDF
        Document document = new Document();

        try {
            // Spécifier le chemin du fichier PDF à créer
            String userHomeDir = System.getProperty("user.home"); // Obtenir le répertoire de l'utilisateur
            String filePath = userHomeDir + "/Downloads/Groupe_" + groupe.getId() + ".pdf"; // Chemin complet pour enregistrer le fichier dans le répertoire de téléchargement de l'utilisateur
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            // Ouvrir le document
            document.open();

            // Ajouter le contenu au document
            document.add(new Paragraph("Informations sur le groupe :"));
            document.add(new Paragraph("Nom du groupe : " + groupe.getNom()));
            document.add(new Paragraph("Description : " + groupe.getDescription()));
            document.add(new Paragraph("Date de création : " + groupe.getDatecreation()));

            // Ajouter l'image du groupe au document


            // Ajouter la liste des utilisateurs du groupe au document
            StringBuilder utilisateursText = new StringBuilder("Utilisateurs : ");
            groupe.getUtilisateurs().forEach(utilisateur -> utilisateursText.append(utilisateur.getNom()).append(", "));
            document.add(new Paragraph(utilisateursText.toString()));

            // Fermer le document
            document.close();

            // Afficher un message de succès
            showAlertPDFGenerated();

            // Ouvrir le fichier PDF dans le visualiseur PDF par défaut de l'utilisateur
            openPDFFile(filePath);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void openPDFFile(String filePath) {
        try {
            // Vérifier si le support de bureau est pris en charge et le fichier PDF existe
            if (Desktop.isDesktopSupported() && Files.exists(Paths.get(filePath))) {
                Desktop.getDesktop().open(new File(filePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlertPDFGenerated() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PDF généré");
        alert.setHeaderText(null);
        alert.setContentText("Le PDF a été généré avec succès !");
        alert.showAndWait();
    }



    private void refreshGridPane() {
        gridPane.getChildren().clear(); // Effacer le contenu actuel du GridPane
        loadResources(); // Recharger les ressources et mettre à jour le GridPane
    }
}

