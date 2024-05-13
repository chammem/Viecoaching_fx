package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import entities.Groupe;
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
import java.io.*;
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
                        try {
                            String imageUrl = groupe.getImage();
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                File file = new File("C:/Users/LENOVO/Desktop/3A55/Pidev/viecoaching/public/uploads/" + imageUrl);

                                if (file.exists()) {
                                    InputStream inputStream = new FileInputStream(file);
                                    Image image = new Image(inputStream);
                                    inputStream.close();

                                    ImageView imageView = new ImageView(image);
                                    imageView.setFitWidth(100);
                                    imageView.setFitHeight(100);
                                    gridPane.add(imageView, 4, row[0]);
                                } else if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                                    URL url = new URL(imageUrl);
                                    InputStream inputStream = url.openStream();
                                    Image image = new Image(inputStream);
                                    inputStream.close();

                                    ImageView imageView = new ImageView(image);
                                    imageView.setFitWidth(100);
                                    imageView.setFitHeight(100);
                                    gridPane.add(imageView, 4, row[0]);
                                } else {
                                    System.err.println("Invalid image URL: " + imageUrl);
                                }
                            }

                            VBox vbox = new VBox();
                            Label titleLabel = new Label(groupe.getNom());
                            titleLabel.setStyle("-fx-font-weight: bold");
                            vbox.getChildren().add(titleLabel);

                            Label descriptionLabel = new Label(groupe.getDescription());
                            vbox.getChildren().add(descriptionLabel);

                            Label typeLabel = new Label("Type de groupe : " + groupe.getTypegroupe_id().getNomtype());
                            vbox.getChildren().add(typeLabel);

                            gridPane.add(vbox, 1, row[0]);

                            Button pdfButton = new Button("Generate PDF");
                            pdfButton.setOnAction(event -> generatePDF(groupe));
                            pdfButton.setStyle("-fx-background-color: orange");
                            gridPane.add(pdfButton, 2, row[0]);

                            row[0]++;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            prevPageButton.setDisable(pageIndex == 0);
            nextPageButton.setDisable((pageIndex + 1) * pageSize >= service.countGroupes());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void generatePDF(Groupe groupe) {
        Document document = new Document();

        try {
            String userHomeDir = System.getProperty("user.home");
            String filePath = userHomeDir + "/Downloads/Groupe_" + groupe.getId() + ".pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));

            document.open();

            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph(new Chunk("Informations sur le groupe :", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.RED)));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

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

            if (groupe.getImage() != null && !groupe.getImage().isEmpty()) {
                try {
                    Image image = new Image(groupe.getImage());
                    document.add((Element) image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            PdfContentByte canvas = writer.getDirectContent();
            Phrase footer = new Phrase("Page " + document.getPageNumber(), infoFont);
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER,
                    footer,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 10, 0);

            document.close();

            showAlertPDFGenerated();

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