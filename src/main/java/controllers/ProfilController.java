package controllers;
import entities.Utilisateur;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MyDatabase myDatabase = MyDatabase.getInstance();
        Connection connection = myDatabase.getConnection();
        this.serviceProfil = new ServiceProfil(connection);

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/utilisateur.fxml"));
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
    private InputStream selectImage() {
        // Vérifier d'abord si une image a déjà été sélectionnée
        if (nomFichierImageSelectionne != null) {
            try {
                return new FileInputStream(nomFichierImageSelectionne);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        // Si aucune image n'a été sélectionnée précédemment, afficher le dialogue de sélection d'image
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Afficher l'image sélectionnée dans l'interface utilisateur
            Image image = new Image(selectedFile.toURI().toString());
            imageId.setImage(image);

            // Mettre à jour la variable avec le nom du fichier sélectionné
            nomFichierImageSelectionne = selectedFile.getAbsolutePath();

            System.out.println("Nom du fichier sélectionné : " + nomFichierImageSelectionne);

            try {
                return new FileInputStream(selectedFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null; // Retourner null si aucun fichier image n'est sélectionné
    }



    public File loadImage(String imageName) {
        String imagePath = System.getProperty("user.dir") + "/" + IMAGE_DIRECTORY + "/" + imageName;
        return new File(imagePath);
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

            String imagePath = IMAGE_DIRECTORY + "/" + utilisateurConnecte.getImage();
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            imageId.setImage(image);
        } else {
            // Gérer le cas où aucun utilisateur n'est connecté
            // Peut-être afficher un message d'erreur ou rediriger vers la page de connexion
        }
    }


    public String saveImage(String nomFichierImage, InputStream imageStream, String extension) throws IOException {
        // Obtenez le chemin absolu du répertoire d'images
        Path imageDirectoryPath = Paths.get(IMAGE_DIRECTORY);

        // Créez le répertoire s'il n'existe pas déjà
        if (!Files.exists(imageDirectoryPath)) {
            Files.createDirectories(imageDirectoryPath);
        }

        // Construisez le chemin complet du fichier dans le répertoire "photos" avec l'extension appropriée
        Path destinationPath = imageDirectoryPath.resolve(nomFichierImage + "." + extension);

        // Copiez l'image dans le répertoire spécifié
        Files.copy(imageStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);

        // Retournez le nom de l'image enregistrée avec l'extension
        return nomFichierImage + "." + extension;
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

        if (nouveauNumero.length() < 8 || !nouveauNumero.matches("\\d{8}")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Numéro de téléphone invalide", "Le numéro de téléphone doit comporter au moins 8 chiffres.");
            return;
        }

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
                // Récupère la nouvelle image sélectionnée
                InputStream imageStream = selectImage();

                // Met à jour les données de l'utilisateur
                Utilisateur utilisateurConnecte = SessionManager.getUtilisateurConnecte();
                if (utilisateurConnecte != null) {
                    utilisateurConnecte.setNom(nouveauNom);
                    utilisateurConnecte.setPrenom(nouveauPrenom);
                    utilisateurConnecte.setVille(nouvelleVille);
                    utilisateurConnecte.setTel(nouveauNumero);
                    utilisateurConnecte.setGenre(hommeSelected ? "Homme" : "Femme"); // Met à jour le genre de l'utilisateur en fonction de la sélection

                    // Met à jour l'image si une nouvelle image est sélectionnée
                    if (imageStream != null) {
                        try {
                            // Sauvegarde la nouvelle image et récupère son nom
                            String nomFichierImage = saveImage("profile_" + System.currentTimeMillis(), imageStream, "jpg");
                            utilisateurConnecte.setImage(nomFichierImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return; // Arrêtez le processus si une erreur se produit lors de l'enregistrement de l'image
                        }
                    }

                    // Met à jour les données dans la base de données
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