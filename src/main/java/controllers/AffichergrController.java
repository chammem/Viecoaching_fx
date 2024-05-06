package controllers;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.nio.file.*;
import java.awt.Desktop;

import entities.Groupe;
import entities.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image ;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.ServiceGroupe;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AffichergrController implements Initializable {
    // Utilisation de l'image de JavaFX

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

    @FXML
    private void nextPage(ActionEvent event) throws SQLException {
        int totalGroupes = service.countGroupes(); // Nombre total de groupes
        int lastPageIndex = (totalGroupes - 1) / pageSize; // Dernier index de page possible

        if (pageIndex < lastPageIndex) {
            pageIndex++; // Passer à la page suivante si ce n'est pas la dernière page
            loadResources(); // Recharger les ressources avec la nouvelle plage
        }
    }

    @FXML
    private void previousPage(ActionEvent event) {
        if (pageIndex > 0) {
            pageIndex--; // Passer à la page précédente si ce n'est pas la première page
            loadResources(); // Recharger les ressources avec la nouvelle plage
        }
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Groupe.fxml"));
            Stage stage = (Stage) searchField.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }
    @FXML
    void NavBarCat(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Affichegr.fxml"));
            Stage stage = (Stage)searchField.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }
    }

    @FXML
    void NavBarRes(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AfficheType.fxml"));
            Stage stage = (Stage) searchField.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de afficheRessource.fxml : " + e.getMessage());
        }}

    @FXML
    private void searchAction(KeyEvent event) {
        searchText = searchField.getText();
        // Recharger les ressources en fonction du texte de recherche
        loadResources();
    }

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

        Label actionLabel = new Label("Actions");
        actionLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(actionLabel, 5, 0);

        Label updateLabel = new Label("Modifier");
        updateLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(updateLabel, 6, 0);

        Label utilisateursLabel = new Label("Utilisateurs");
        utilisateursLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(utilisateursLabel, 7, 0);
        Label pdf = new Label("Generer PDF");
        pdf.setStyle("-fx-font-weight: bold;");
        gridPane.add(pdf, 8, 0);
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

        // Ajouter les données au PieChart
        pieChart.setData(pieChartData);
    }
    public void loadResources() {
        try {
            gridPane.getChildren().clear(); // Effacer le contenu actuel du GridPane

            final int[] row = {1}; // Commence à partir de la première ligne (index 0)
            service.afficherPagination(pageIndex, pageSize).stream()
                    .filter(groupe -> groupe.getNom().toLowerCase().contains((searchText != null ? searchText : "").toLowerCase()))
                    .forEach(groupe -> {
                        Label typeLabel = new Label(groupe.getTypegroupe_id().getNomtype());
                        gridPane.add(typeLabel, 0, row[0]);

                        Label titleLabel = new Label(groupe.getNom());
                        gridPane.add(titleLabel, 1, row[0]);

                        Label descriptionLabel = new Label(groupe.getDescription());
                        gridPane.add(descriptionLabel, 2, row[0]);

                        Label datecreationLabel = new Label(groupe.getDatecreation().toString());
                        gridPane.add(datecreationLabel, 3, row[0]);

                        if (groupe.getImage() != null && !groupe.getImage().isEmpty()) {
                            try {
                                Image image = new Image(groupe.getImage());
                                ImageView imageView = new ImageView(image);
                                imageView.setFitWidth(100);
                                imageView.setFitHeight(100);
                                gridPane.add(imageView, 4, row[0]);
                            } catch (Exception e) {
                                handleImageLoadException(e);
                            }
                        } else {
                            System.out.println("L'URL de l'image est vide ou null pour la ressource : " + groupe.getImage());
                        }

                        Button deleteButton = new Button("Delete");
                        deleteButton.setOnAction(event -> deletegroupe(groupe));
                        deleteButton.setStyle("-fx-background-color: #c40000;");
                        gridPane.add(deleteButton, 5, row[0]);

                        Button updateButton = new Button("Update");
                        updateButton.setOnAction(event -> updaategroupe(groupe));
                        updateButton.setStyle(" -fx-background-color: green");

                        ;
                        gridPane.add(updateButton, 6, row[0]);
                        Button pdfButton = new Button("Generate PDF");
                        pdfButton.setOnAction(event -> generatePDF(groupe));
                        pdfButton.setStyle(" -fx-background-color: orange");
                        gridPane.add(pdfButton, 8, row[0]);

                        StringBuilder utilisateursText = new StringBuilder();
                        groupe.getUtilisateurs().forEach(utilisateur -> utilisateursText.append(utilisateur.getNom()).append(", "));
                        Label utilisateursLabel = new Label(utilisateursText.toString());
                        gridPane.add(utilisateursLabel, 7, row[0]);

                        row[0]++;
                    });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleImageLoadException(Exception e) {
    }


    private void updaategroupe(Groupe groupe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifiergroupe.fxml"));
            Parent modif = loader.load();
            ModifiergroupeController controller = loader.getController();
            controller.initData(groupe);

            // Utilisez un nœud réel de votre scène actuelle
            Scene currentScene = gridPane.getScene();
            if (currentScene != null) {
                Stage stage = (Stage) currentScene.getWindow();
                stage.setScene(new Scene(modif));
            } else {
                System.err.println("Impossible de récupérer la scène actuelle. Veuillez utiliser un nœud réel de votre interface utilisateur.");
            }
            Platform.runLater(this::loadResources);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlertGroupeModifie() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Groupe modifié");
        alert.setHeaderText(null);
        alert.setContentText("Le groupe a été modifié avec succès !");
        alert.showAndWait();

        // Fermer la fenêtre de mise à jour
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.close();
    }

    private void deletegroupe(Groupe groupe) {
        // Créer une alerte de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer ce groupe ?");

        // Ajouter les boutons "OK" et "Annuler"
        ButtonType okButton = new ButtonType("OUI", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, cancelButton);

        // Afficher l'alerte et attendre la réponse de l'utilisateur
        Optional<ButtonType> result = alert.showAndWait();

        // Vérifier la réponse de l'utilisateur
        if (result.isPresent() && result.get() == okButton) {
            // Si l'utilisateur a cliqué sur "OK", supprimer le groupe
            try {
                service.supprimer(groupe.getId());
                showAlertGroupeSupprime(); // Afficher l'alerte après la suppression réussie
                loadResources();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private void loadPieChartData() {
        try {
            int nombreGroupes = service.countGroupes(); // Méthode à implémenter dans votre service pour compter le nombre de groupes
            int reste = 100 - nombreGroupes;

            PieChart.Data groupesData = new PieChart.Data("Groupes", nombreGroupes);
            PieChart.Data resteData = new PieChart.Data("Reste", reste);

            pieChart.getData().add(groupesData);
            pieChart.getData().add(resteData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Groupe getGroupeAtIndex(int rowIndex) {
        try {
            // Obtenez tous les groupes affichés dans la GridPane
            List<Groupe> groupes = service.afficher();

            // Assurez-vous que l'index de ligne est valide
            if (rowIndex >= 0 && rowIndex < groupes.size()) {
                // Récupérer le groupe à l'index spécifié
                return groupes.get(rowIndex);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
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
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
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
            // Ajouter ici votre code pour ajouter l'image

            // Ajouter la liste des utilisateurs du groupe au document
            StringBuilder utilisateursText = new StringBuilder("Utilisateurs : ");
            for (Utilisateur utilisateur : groupe.getUtilisateurs()) {
                utilisateursText.append(utilisateur.getNom()).append(", ");
            }
            document.add(new Paragraph(utilisateursText.toString(), infoFont));

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
