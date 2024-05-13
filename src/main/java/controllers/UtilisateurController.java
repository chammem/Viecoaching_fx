package controllers;


import com.cloudinary.utils.ObjectUtils;
import entities.Utilisateur;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceUtilisateur;
import utils.MyDatabase;
import utils.SessionManager;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Optional;
import com.cloudinary.Cloudinary;

import javax.imageio.ImageIO;
import java.util.Map;



public class UtilisateurController implements Initializable {

    @FXML
    public ImageView logoId;
    @FXML
    public Label nomCompteId;
    @FXML
    public TextField nomId;
    @FXML
    public TextField prenomId;
    @FXML
    public TextField emailId;
    @FXML
    public TextField numeroId;
    @FXML
    public TextField VilleId;
    @FXML
    public PasswordField mdpId;
    @FXML
    public PasswordField confId;
    @FXML
    public RadioButton hommeId;
    @FXML
    public RadioButton femmeId;
    @FXML
    public ComboBox RoleId;
    @FXML
    public Button insertId;
    @FXML
    public Button ajouterId;
    @FXML
    public Button ModifierId;
    @FXML
    public Button supprimerId;
    @FXML
    public ImageView imageId;
    @FXML
    public TableView<Utilisateur> tableView;
    @FXML
    public TableColumn<Utilisateur, String> imageColumn;
    @FXML
    public Button décnxId;
    @FXML
    public Button profil;
    @FXML
    private TableColumn<Utilisateur, String> emailColumn;
    @FXML
    public TableColumn<Utilisateur, String> nomColumn;
    @FXML
    public TableColumn<Utilisateur, String> prenomColumn;
    @FXML
    public TableColumn<Utilisateur, String> villeColumn;
    @FXML
    public TableColumn<Utilisateur, String> numColumn;
    @FXML
    public TableColumn<Utilisateur, String> genreColumn;
    @FXML
    public TableColumn<Utilisateur, String> roleColumn;
    @FXML
    private TableColumn<Utilisateur, ?> etatColumn;

    Connection con;

    private String nomFichierImageSelectionne;

    private Utilisateur utilisateurSelectionne;
    private ServiceUtilisateur serviceUtilisateur;
    private Utilisateur utilisateurInitial;
    private static final String IMAGE_DIRECTORY = "src/main/resources/photos";
    private Cloudinary cloudinary;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MyDatabase myDatabase = MyDatabase.getInstance();
        Connection connection = myDatabase.getConnection();
        this.serviceUtilisateur = new ServiceUtilisateur(connection);
        showUser();
        controleBtnRadio();
        // Initialisation de Cloudinary avec vos identifiants
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dppj3e5cp",
                "api_key", "647876725794588",
                "api_secret", "yQBtTPY_dzeUjCcUHQcmHvJevgg"));
            //ajouter les roles
        RoleId.getItems().add("ROLE_PATIENT");
        RoleId.getItems().add("ROLE_COACH");
        RoleId.getItems().add("ROLE_ADMIN");

        insertId.setOnAction(actionEvent -> selectImage());
        ajouterId.setOnAction(actionEvent -> {
            try {
                // Charger le fichier FXML de la page patient.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/patient.fxml"));
                Parent root = loader.load();

                // Créer la scène avec la nouvelle page
                Scene scene = new Scene(root);

                // Récupérer la fenêtre actuelle
                Stage stage = (Stage) ajouterId.getScene().getWindow();

                // Afficher la nouvelle scène dans la fenêtre
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        supprimerId.setOnAction(actionEvent -> handleSupprimerUtilisateur());
        ModifierId.setOnAction(actionEvent -> handleModifierUtilisateur());
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                handleUserSelection();
            }
        });

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


    // Méthode pour sélectionner et mettre à jour l'image
    private String selectAndUpdateImage() {
        selectImage();
        String imageUrl = null;
        if (nomFichierImageSelectionne != null) {
            try {
                InputStream imageStream = new FileInputStream(nomFichierImageSelectionne);
                String nomFichierImageSansExtension = "profile_" + System.currentTimeMillis();
                imageUrl = uploadImage(imageStream, nomFichierImageSansExtension);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'enregistrement de l'image", "Une erreur s'est produite lors de l'enregistrement de l'image.");
            }
        }
        return imageUrl;
    }

    @FXML
    private void handleModifierUtilisateur() {
        Utilisateur utilisateurSelectionne = tableView.getSelectionModel().getSelectedItem();
        if (utilisateurSelectionne != null) {
            // Récupérer les nouvelles valeurs des champs du formulaire
            int idnom = utilisateurSelectionne.getId();
            String newEmail = emailId.getText();
            String newNom = nomId.getText();
            String newPrenom = prenomId.getText();
            String newVille = VilleId.getText();
            String newNumero = numeroId.getText();
            String genre = hommeId.isSelected() ? "Homme" : (femmeId.isSelected() ? "Femme" : "");
            String roleString = RoleId.getValue().toString();
            int roleId = mapRoleToInt(roleString);

            // Téléverser une nouvelle image vers Cloudinary et obtenir l'URL de l'image
            String imageUrl = selectAndUpdateImage();

            // Mettre à jour les données de l'utilisateur avec l'URL de l'image
            utilisateurSelectionne.setId(idnom);
            utilisateurSelectionne.setEmail(newEmail);
            utilisateurSelectionne.setNom(newNom);
            utilisateurSelectionne.setPrenom(newPrenom);
            utilisateurSelectionne.setVille(newVille);
            utilisateurSelectionne.setTel(newNumero);
            utilisateurSelectionne.setGenre(genre);
            utilisateurSelectionne.setRole_id(roleId);
            utilisateurSelectionne.setImage(imageUrl);

            // Mettre à jour l'utilisateur dans la base de données
            try {
                serviceUtilisateur.modifier(utilisateurSelectionne, utilisateurSelectionne.getEmail());
                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Utilisateur modifié", "Les données de l'utilisateur ont été modifiées avec succès.");
                clearFields(); // Effacer les champs du formulaire après la modification
                loadData(); // Rafraîchir la table pour afficher les modifications
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification", "Une erreur s'est produite lors de la modification de l'utilisateur.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun utilisateur sélectionné", "Veuillez sélectionner un utilisateur à modifier.");
        }
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
    private void loadData() {
        try {
            MyDatabase myDatabase = MyDatabase.getInstance();
            Connection connection = myDatabase.getConnection();

            // Initialisez ServiceUtilisateur avec la connexion à la base de données
            ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur(connection);

            // Chargez les utilisateurs à partir du service
            ObservableList<Utilisateur> utilisateurs = FXCollections.observableList(serviceUtilisateur.afficher());

            // Définissez les utilisateurs dans la tableView
            tableView.setItems(utilisateurs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Cette méthode est appelée lorsque vous cliquez sur le bouton pour modifier l'utilisateur

    @FXML
    private void showUser() {
        try {
            ObservableList<Utilisateur> utilisateurs = FXCollections.observableList(serviceUtilisateur.afficher());

            // Configuration des PropertyValueFactory pour les autres colonnes (email, nom, prénom, etc.)
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            villeColumn.setCellValueFactory(new PropertyValueFactory<>("ville"));
            numColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));
            genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
            roleColumn.setCellValueFactory(cellData -> {
                String role = "";
                switch (cellData.getValue().getRole_id()) {
                    case 1:
                        role = "PATIENT";
                        break;
                    case 2:
                        role = "COACH";
                        break;
                    case 3:
                        role = "ADMIN";
                        break;
                    default:
                        role = "Autre";
                        break;
                }
                return new SimpleStringProperty(role);
            });
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("image")); // Ajout de la colonne d'image

            // Définition de la cellule personnalisée pour la colonne image
            imageColumn.setCellFactory(col -> new TableCell<Utilisateur, String>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(String image, boolean empty) {
                    super.updateItem(image, empty);
                    if (empty || image == null) {
                        setGraphic(null);
                    } else {
                        try {
                            Image imageUrl;
                            if (image.startsWith("http://") || image.startsWith("https://")) {
                                // Charger l'image à partir de l'URL Cloudinary
                                imageUrl = new Image(image);
                            } else {
                                // Charger l'image à partir du dossier local sur le PC
                                imageUrl = new Image(new File("C:/Users/LENOVO/Desktop/3A55/Pidev/viecoaching/public/images/user/" + image).toURI().toString());
                            }
                            imageView.setImage(imageUrl);
                            imageView.setFitWidth(50);
                            imageView.setFitHeight(50);
                            setGraphic(imageView);
                        } catch (Exception e) {
                            System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
                            setGraphic(null);
                        }
                    }
                }
            });

            tableView.setItems(utilisateurs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private int mapRoleToInt(String role) {
        switch (role) {
            case "ROLE_PATIENT":
                return 1;
            case "ROLE_COACH":
                return 2;
            case "ROLE_ADMIN":
                return 3;
            default:
                return -1;
        }
    }
    @FXML
    private void handleSupprimerUtilisateur() {
        Utilisateur utilisateurSelectionne = tableView.getSelectionModel().getSelectedItem();
        if (utilisateurSelectionne != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Supprimer l'utilisateur");
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    MyDatabase myDatabase = MyDatabase.getInstance();
                    Connection connection = myDatabase.getConnection();
                    ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur(connection);
                    serviceUtilisateur.supprimer(utilisateurSelectionne.getId());
                    tableView.getItems().remove(utilisateurSelectionne);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur supprimé", "L'utilisateur a été supprimé avec succès.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression", "Une erreur s'est produite lors de la suppression de l'utilisateur.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun utilisateur sélectionné", "Veuillez sélectionner un utilisateur à supprimer.");
        }
    }
    @FXML
    private void handleUserSelection() {
        utilisateurSelectionne = tableView.getSelectionModel().getSelectedItem(); // Utiliser la variable de classe
        if (utilisateurSelectionne != null) {
            emailId.setText(utilisateurSelectionne.getEmail());
            nomId.setText(utilisateurSelectionne.getNom());
            prenomId.setText(utilisateurSelectionne.getPrenom());
            VilleId.setText(utilisateurSelectionne.getVille());
            numeroId.setText(utilisateurSelectionne.getTel());
            if (utilisateurSelectionne.getGenre().equals("Homme")) {
                hommeId.setSelected(true);
                femmeId.setSelected(false);
            } else {
                hommeId.setSelected(false);
                femmeId.setSelected(true);
            }
            String roleString = mapIntToRole(utilisateurSelectionne.getRole_id());
            RoleId.getSelectionModel().select(roleString);

            String imagePath = utilisateurSelectionne.getImage();
            Image image;
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                // Charger l'image à partir de l'URL Cloudinary
                image = new Image(imagePath);
            } else {
                // Charger l'image à partir du dossier local sur le PC
                File file = new File("C:/Users/LENOVO/Desktop/3A55/Pidev/viecoaching/public/images/user/" + imagePath);
                if (file.exists()) {
                    image = new Image(file.toURI().toString());
                } else {
                    // Si le fichier local n'existe pas, vous pouvez gérer ce cas selon vos besoins
                    // Par exemple, afficher une image par défaut ou rien du tout
                    // Image imageDefault = new Image("chemin_vers_image_par_defaut.png");
                    // image = imageDefault;
                    image = null;
                }
            }
            imageId.setImage(image);
        }
    }
    private String mapIntToRole(int roleId) {
        switch (roleId) {
            case 1:
                return "ROLE_PATIENT";
            case 2:
                return "ROLE_COACH";
            case 3:
                return "ROLE_ADMIN";
            default:
                // Gérez le cas où aucun rôle valide n'est sélectionné
                return ""; // Ou une autre valeur par défaut appropriée
        }
    }
    private void clearFields() {
        nomId.clear();
        prenomId.clear();
        emailId.clear();
        mdpId.clear();
        confId.clear();
        VilleId.clear();
        numeroId.clear();
        hommeId.setSelected(false);
        femmeId.setSelected(false);
        RoleId.getSelectionModel().clearSelection();
        nomFichierImageSelectionne = null;
        imageId.setImage(null);
    }


    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
