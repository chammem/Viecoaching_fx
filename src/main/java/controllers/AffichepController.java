package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import entities.Groupe;
import entities.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import services.ServiceGroupe;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;
public class AffichepController implements Initializable {
    @FXML
    private GridPane gridPane;
    @FXML
    private TextField searchField;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button nextPageButton;

    private String searchText = "";
    private ServiceGroupe service;
    private int pageIndex = 0;
    private final int pageSize = 3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        service = new ServiceGroupe();
        loadResources();
    }

    @FXML
    private void goToPreviousPage(ActionEvent event) {
        if (pageIndex > 0) {
            pageIndex--;
            refreshGridPane();
        }
    }

    @FXML
    private void goToNextPage(ActionEvent event) throws SQLException {
        int totalGroups = service.countGroupes();
        if ((pageIndex + 1) * pageSize < totalGroups) {
            pageIndex++;
            refreshGridPane();
        }
    }

    @FXML
    private void search(KeyEvent event) {
        searchText = searchField.getText().toLowerCase();
        pageIndex = 0;
        refreshGridPane();
    }

    @FXML
    private void loadResources() {
        try {
            gridPane.getChildren().clear();
            int startIndex = pageIndex * pageSize;
            int endIndex = Math.min(startIndex + pageSize, service.countGroupes());

            final int[] row = {1};
            service.afficher().stream()
                    .filter(groupe -> {
                        String nom = groupe.getNom();
                        return nom != null && nom.toLowerCase().contains(searchText);
                    })
                    .skip(startIndex)
                    .limit(endIndex - startIndex)
                    .forEach(groupe -> {
                        // Créer les éléments d'interface utilisateur pour afficher les données du groupe
                        ImageView imageView = new ImageView(new Image(groupe.getImage()));
                        imageView.setFitWidth(250);
                        imageView.setFitHeight(250);
                        gridPane.add(imageView, 0, row[0]);

                        VBox vbox = new VBox(); // Créer une VBox pour le titre, la description et le type de groupe
                        Label titleLabel = new Label(groupe.getNom());
                        titleLabel.setStyle("-fx-font-weight: bold");
                        vbox.getChildren().add(titleLabel);

                        Label descriptionLabel = new Label(groupe.getDescription());
                        vbox.getChildren().add(descriptionLabel);

                        Label typeLabel = new Label("Type de groupe : " + groupe.getTypegroupe_id().getNomtype());
                        vbox.getChildren().add(typeLabel);

                        gridPane.add(vbox, 1, row[0]); // Ajouter la VBox à la deuxième colonne

                        Button pdfButton = new Button("Generate PDF");
                        pdfButton.setOnAction(event -> generatePDF(groupe));
                        pdfButton.setStyle("-fx-background-color: orange");
                        gridPane.add(pdfButton, 2, row[0]); // Ajouter le bouton à la troisième colonne

                        row[0]++;
                    });

            // Désactiver le bouton "Page précédente" si nous sommes sur la première page
            prevPageButton.setDisable(pageIndex == 0);
            // Désactiver le bouton "Page suivante" si nous sommes sur la dernière page
            nextPageButton.setDisable((pageIndex + 1) * pageSize >= service.countGroupes());
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
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));

            // Ouvrir le document
            document.open();

            // Ajouter le contenu au document
            // Titre du document avec une police personnalisée et en gras
            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph(new Chunk("Informations sur le groupe :", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.RED)));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Informations sur le groupe avec un espacement et une police différente
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Paragraph info = new Paragraph();
            info.add(new Chunk("Nom du groupe : ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            info.add(new Chunk(groupe.getNom(), infoFont));
            info.add(Chunk.NEWLINE);
            info.add(new Chunk("Description : ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            info.add(new Chunk(groupe.getDescription(), infoFont));
            info.add(Chunk.NEWLINE);
            info.add(new Chunk("Date de création : ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            info.add(new Chunk(groupe.getDatecreation().toString(), infoFont));
            document.add(info);

            // Ajouter l'image du groupe au document
            if (groupe.getImage() != null && !groupe.getImage().isEmpty()) {
                try {
                    Image image = new Image(groupe.getImage());
                    document.add((Element) image);
                } catch (Exception e) {
                    // Gérer les erreurs lors du chargement de l'image
                    e.printStackTrace();
                }
            }

            // Ajouter un pied de page avec le numéro de page
            PdfContentByte canvas = writer.getDirectContent();
            Phrase footer = new Phrase("Page " + document.getPageNumber(), infoFont);
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER,
                    footer,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 10, 0);

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
        gridPane.getChildren().clear();
        loadResources();
    }
}
