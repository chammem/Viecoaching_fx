package controllers;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.ServiceProfil;
import services.ServiceUtilisateur;
import utils.MyDatabase;
import utils.SessionManager;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class ProfilController implements Initializable {
    public TextField nomId;
    public TextField prenomId;
    public TextField emailId;
    public TextField numeroId;
    public TextField VilleId;
    public RadioButton hommeId;
    public RadioButton femmeId;
    public ImageView imageId;
    public Button modifLogo;
    public Button ajouterId;
    public Button modifier;
    public Button ModifierId;
    public Label nomCompteId;
    private static final String IMAGE_DIRECTORY = "src/main/resources/photos";
    public Button sauvegarder;
    public Button Retour;
    private ServiceProfil serviceProfil;
    private String nomFichierImageSelectionne;

    private Cloudinary cloudinary;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MyDatabase myDatabase = MyDatabase.getInstance();
        Connection connection = myDatabase.getConnection();
        this.serviceProfil = new ServiceProfil(connection);
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dppj3e5cp",
                "api_key", "647876725794588",
                "api_secret", "yQBtTPY_dzeUjCcUHQcmHvJevgg"));

        afficherDonneesUtilisateur();

        // Configure les boutons
        modifLogo.setOnAction(actionEvent -> selectImage());
        modifier.setOnAction(event -> activerEdition());
        ModifierId.setOnAction(event -> {
            // Charger le fichier FXML pour la modification du mot de passe
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mdp.fxml"));
            Parent root;
            try {
                root = loader.load();
                // Créer une nouvelle fenêtre de dialogue modale
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier le mot de passe");
                // Rendre la fenêtre modale (bloque l'interaction avec les autres fenêtres)
                stage.initModality(Modality.APPLICATION_MODAL);
                // Ne pas permettre de redimensionner la fenêtre
                stage.setResizable(false);
                // Afficher la fenêtre
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Retour.setOnAction(event -> {
            // Fermer la fenêtre actuelle
            Stage stage = (Stage) Retour.getScene().getWindow();
            stage.close();

            // Charger et afficher la nouvelle fenêtre (utilisateur.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
            Parent root;
            try {
                root = loader.load();
                Stage utilisateurStage = new Stage();
                utilisateurStage.setScene(new Scene(root));
                utilisateurStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sauvegarder.setOnAction(event -> sauvegarderModification());
        sauvegarder.setVisible(false); // Cache le bouton de sauvegarde par défaut
    }
    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Créer un InputStream à partir du fichier sélectionné
                InputStream imageStream = new FileInputStream(selectedFile);

                // Enregistrer le chemin du fichier sélectionné
                nomFichierImageSelectionne = selectedFile.getAbsolutePath();

                // Afficher l'image sélectionnée dans l'interface utilisateur
                Image image = new Image(selectedFile.toURI().toString());
                imageId.setImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Fichier introuvable", "Le fichier sélectionné est introuvable.");
            }
        }
    }
    private String uploadImage(InputStream imageStream, String nomFichierImage) throws IOException {
        // Créer un tableau de bytes pour contenir le contenu du flux d'entrée
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = imageStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        byte[] imageData = byteArrayOutputStream.toByteArray();

        // Charger l'image vers Cloudinary
        Map<String, Object> uploadResult = cloudinary.uploader().upload(imageData, ObjectUtils.asMap(
                "folder", "votre_dossier_images",
                "public_id", nomFichierImage));

        // Retourner l'URL de l'image téléchargée
        return (String) uploadResult.get("url");
    }
    private void controleBtnRadio() {
        ToggleGroup genreToggleGroup = new ToggleGroup();
        hommeId.setToggleGroup(genreToggleGroup);
        femmeId.setToggleGroup(genreToggleGroup);

        hommeId.setOnAction(event -> {
            if (hommeId.isSelected()) {
                femmeId.setSelected(false);
            }
        });

        femmeId.setOnAction(event -> {
            if (femmeId.isSelected()) {
                hommeId.setSelected(false);
            }
        });    }

    private void afficherDonneesUtilisateur() {
        Utilisateur utilisateurConnecte = SessionManager.getUtilisateurConnecte();
        if (utilisateurConnecte != null) {
            emailId.setText(utilisateurConnecte.getEmail());
            nomId.setText(utilisateurConnecte.getNom());
            prenomId.setText(utilisateurConnecte.getPrenom());
            VilleId.setText(utilisateurConnecte.getVille());
            numeroId.setText(utilisateurConnecte.getTel());

            if (utilisateurConnecte.getGenre() != null) {
                if (utilisateurConnecte.getGenre().equals("Homme")) {
                    hommeId.setSelected(true);
                    femmeId.setSelected(false);
                } else {
                    hommeId.setSelected(false);
                    femmeId.setSelected(true);
                }
            }

            try {
                String imagePath = utilisateurConnecte.getImage();
                Image imageUrl;
                if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                    // Charger l'image à partir de l'URL Cloudinary
                    imageUrl = new Image(imagePath);
                } else {
                    // Charger l'image à partir du dossier local sur le PC
                    imageUrl = new Image(new File("C:/Users/LENOVO/Desktop/3A55/Pidev/viecoaching/public/images/user/" + imagePath).toURI().toString());
                }
                imageId.setImage(imageUrl);
                imageId.setFitWidth(150);
                imageId.setFitHeight(150);
                // Afficher l'image dans imageId (ou tout autre ImageView que vous utilisez)
                imageId.setImage(imageUrl);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
                imageId.setImage(null); // Assurez-vous de définir l'image sur null en cas d'erreur
            }
        } else {
            // Gérer le cas où aucun utilisateur n'est connecté
            // Peut-être afficher un message d'erreur ou rediriger vers la page de connexion
        }
    }




    private void sauvegarderModification() {
        // Récupère les nouvelles valeurs des champs
        String nouveauNom = nomId.getText();
        String nouveauPrenom = prenomId.getText();
        String nouvelleVille = VilleId.getText();
        String nouveauNumero = numeroId.getText();
        boolean hommeSelected = hommeId.isSelected();
        boolean femmeSelected = femmeId.isSelected();

        // Contrôles de saisie
        if (nouveauNom.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Nom invalide", "Le nom doit comporter au moins 6 caractères.");
            return;
        }

        // Contrôle de saisie pour le prénom
        if (nouveauPrenom.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Prénom invalide", "Le prénom doit comporter au moins 6 caractères.");
            return;
        }

        // Contrôle de saisie pour la ville
        if (nouvelleVille.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Ville invalide", "La ville doit comporter au moins 6 caractères.");
            return;
        }

        // Contrôle de saisie pour le numéro de téléphone
        if (!nouveauNumero.matches("\\d{8}")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Numéro de téléphone invalide", "Le numéro de téléphone doit comporter exactement 8 chiffres.");
            return;
        }

        // Contrôle de saisie pour le genre
        if ((hommeSelected && femmeSelected) || (!hommeSelected && !femmeSelected)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Sélection de genre invalide", "Veuillez sélectionner uniquement un genre (homme ou femme).");
            return;
        }

        // Afficher une alerte de confirmation pour sauvegarder les modifications
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir sauvegarder les modifications ?");
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Mettre à jour les données de l'utilisateur
                Utilisateur utilisateurConnecte = SessionManager.getUtilisateurConnecte();
                if (utilisateurConnecte != null) {
                    utilisateurConnecte.setNom(nouveauNom);
                    utilisateurConnecte.setPrenom(nouveauPrenom);
                    utilisateurConnecte.setVille(nouvelleVille);
                    utilisateurConnecte.setTel(nouveauNumero);
                    utilisateurConnecte.setGenre(hommeSelected ? "Homme" : "Femme"); // Met à jour le genre de l'utilisateur en fonction de la sélection

                    // Mettre à jour l'image si une nouvelle image est sélectionnée
                    String imageUrl = selectAndUpdateImage();
                    if (imageUrl != null) {
                        utilisateurConnecte.setImage(imageUrl);
                    }

                    // Mettre à jour les données dans la base de données
                    try {
                        this.serviceProfil.modifier(utilisateurConnecte, utilisateurConnecte.getEmail());
                        afficherDonneesUtilisateur(); // Rafraîchit les données affichées après la modification
                        sauvegarder.setVisible(false); // Cache le bouton de sauvegarde après la modification
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Gérer l'erreur lors de la sauvegarde
                    }
                }
            }
        });
    }
    private String selectAndUpdateImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                // Créer un InputStream à partir du fichier sélectionné
                InputStream imageStream = new FileInputStream(selectedFile);

                // Générer un nom de fichier unique
                String nomFichierImageSansExtension = "profile_" + System.currentTimeMillis();

                // Téléverser l'image vers Cloudinary et obtenir l'URL de l'image téléchargée
                String imageUrl = uploadImage(imageStream, nomFichierImageSansExtension);

                // Retourner l'URL de l'image téléchargée
                return imageUrl;
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la sélection de l'image", "Une erreur s'est produite lors de la sélection de l'image.");
            }
        }

        return null; // Retourner null si aucun fichier sélectionné ou en cas d'erreur
    }

    private void activerEdition() {
        // Active l'édition des champs et affiche le bouton de sauvegarde
        nomId.setEditable(true);
        prenomId.setEditable(true);
        VilleId.setEditable(true);
        numeroId.setEditable(true);
        hommeId.setDisable(false);
        femmeId.setDisable(false);
        sauvegarder.setVisible(true);
    }
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}