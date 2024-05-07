package controllers;


import entities.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MyDatabase myDatabase = MyDatabase.getInstance();
        Connection connection = myDatabase.getConnection();
        this.serviceUtilisateur = new ServiceUtilisateur(connection);
        showUser();
        controleBtnRadio();
            //ajouter les roles
        RoleId.getItems().add("ROLE_PATIENT");
        RoleId.getItems().add("ROLE_COACH");
        RoleId.getItems().add("ROLE_ADMIN");

        insertId.setOnAction(actionEvent -> selectImage());
        ajouterId.setOnAction(actionEvent -> handleAjouterUtilisateur());
        supprimerId.setOnAction(actionEvent -> handleSupprimerUtilisateur());
        ModifierId.setOnAction(actionEvent -> handleModifierUtilisateur());
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                handleUserSelection();
            }
        });

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

    private void showUser() {
        try {
            ObservableList<Utilisateur> utilisateurs = FXCollections.observableList(serviceUtilisateur.afficher());
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            villeColumn.setCellValueFactory(new PropertyValueFactory<>("ville"));
            numColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));
            genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
            roleColumn.setCellValueFactory(new PropertyValueFactory<>("role_id"));

            // Définir la cellule personnalisée pour la colonne image
            imageColumn.setCellFactory(col -> new TableCell<Utilisateur, String>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(String nomFichierImage, boolean empty) {
                    super.updateItem(nomFichierImage, empty);
                    if (empty || nomFichierImage == null) {
                        setGraphic(null);
                    } else {
                        // Construire le chemin complet de l'image
                        String imagePath = IMAGE_DIRECTORY + "/" + nomFichierImage;
                        File imageFile = new File(imagePath);

                        // Vérifier si le fichier d'image existe
                        if (imageFile.exists()) {
                            // Charger l'image à partir du fichier
                            Image image = new Image(imageFile.toURI().toString());

                            // Afficher l'image dans la cellule
                            imageView.setImage(image);
                            imageView.setFitWidth(50);
                            imageView.setFitHeight(50);
                            setGraphic(imageView);
                        } else {
                            // Si le fichier d'image n'existe pas, afficher une image par défaut ou laisser la cellule vide
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
    @FXML
    private void handleAjouterUtilisateur() {
            String nom = nomId.getText();
            String prenom = prenomId.getText();
            String email = emailId.getText();
            String mdp = mdpId.getText();
            String ville = VilleId.getText();
            String tel = numeroId.getText();
            String genre = hommeId.isSelected() ? "Homme" : (femmeId.isSelected() ? "Femme" : "");
            String roleString = RoleId.getValue().toString();
            int roleId = mapRoleToInt(roleString);

            // Sélectionner une image
            InputStream imageStream = selectImage();
            String nomFichierImage = null; // Initialisez le nom de fichier d'image à null

            if (imageStream != null) {
                // Si une image est sélectionnée, générez un nom de fichier unique
                String nomFichierImageSansExtension = "profile_" + System.currentTimeMillis();
                try {
                    // Enregistrez l'image et récupérez son nom
                    nomFichierImage = saveImage(nomFichierImageSansExtension, imageStream, "jpg"); // Modifiez le troisième paramètre en "png" si nécessaire
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'enregistrement de l'image", "Une erreur s'est produite lors de l'enregistrement de l'image.");
                    return; // Arrêtez le processus si une erreur se produit lors de l'enregistrement de l'image
                }
            }

            Utilisateur utilisateur = new Utilisateur(nom, prenom, email, mdp, ville, tel, genre, nomFichierImage, roleId);

            try {
                MyDatabase myDatabase = MyDatabase.getInstance();
                Connection connection = myDatabase.getConnection();
                ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur(connection);
                serviceUtilisateur.ajouter(utilisateur);

                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Utilisateur ajouté", "L'utilisateur a été ajouté avec succès.");
                clearFields();
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout", "Une erreur s'est produite lors de l'ajout de l'utilisateur.");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.WARNING, "Erreur de validation", "Erreur", e.getMessage());
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
            RoleId.getSelectionModel().select(roleString);        }
        String imagePath = IMAGE_DIRECTORY + "/" + utilisateurSelectionne.getImage(); // Remplacez "getImageFileName()" par la méthode appropriée pour obtenir le nom de fichier de l'image
        File file = new File(imagePath);
        Image image = new Image(file.toURI().toString());
        imageId.setImage(image);
    }

    // Cette méthode est appelée lorsque vous cliquez sur le bouton pour modifier l'utilisateur
    @FXML
    private void handleModifierUtilisateur() {
        Utilisateur utilisateurSelectionne = tableView.getSelectionModel().getSelectedItem();
        String emailfirst = utilisateurSelectionne.getEmail();
        System.out.println(utilisateurSelectionne);
        System.out.println("//////////////////////");
        if (utilisateurSelectionne != null) {
            // Récupérer les nouvelles valeurs des champs du formulaire
            int idnom  = utilisateurSelectionne.getId();
            String newEmail = emailId.getText();
            String newNom = nomId.getText();
            String newPrenom = prenomId.getText();
            String newVille = VilleId.getText();
            String newNumero = numeroId.getText();
            String genre = hommeId.isSelected() ? "Homme" : (femmeId.isSelected() ? "Femme" : "");
            String roleString = RoleId.getValue().toString();
            int roleId = mapRoleToInt(roleString);

            // Vérifier si une nouvelle image est sélectionnée
            String nomFichierImage = utilisateurSelectionne.getImage(); // Conservez le nom de fichier existant
            InputStream imageStream = selectImage();
            if (imageStream != null) {
                // Générez un nom de fichier unique pour la nouvelle image
                String nomFichierImageSansExtension = "profile_" + System.currentTimeMillis();
                try {
                    // Enregistrez la nouvelle image et récupérez son nom
                    nomFichierImage = saveImage(nomFichierImageSansExtension, imageStream, "jpg"); // Modifiez le troisième paramètre en "png" si nécessaire
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'enregistrement de l'image", "Une erreur s'est produite lors de l'enregistrement de l'image.");
                    return; // Arrêtez le processus si une erreur se produit lors de l'enregistrement de l'image
                }
            }

            // Vérifier si les données ont été modifiées
            if (!utilisateurSelectionne.getEmail().equals(newEmail) ||
                    !utilisateurSelectionne.getNom().equals(newNom) ||
                    !utilisateurSelectionne.getPrenom().equals(newPrenom) ||
                    !utilisateurSelectionne.getVille().equals(newVille) ||
                    !utilisateurSelectionne.getTel().equals(newNumero) ||
                    !utilisateurSelectionne.getGenre().equals(genre) ||
                    utilisateurSelectionne.getRole_id() != roleId ||
                    !utilisateurSelectionne.getImage().equals(nomFichierImage)) {

                // Mettre à jour les données de l'utilisateur
                utilisateurSelectionne.setId(idnom);
                utilisateurSelectionne.setEmail(newEmail);
                utilisateurSelectionne.setNom(newNom);
                utilisateurSelectionne.setPrenom(newPrenom);
                utilisateurSelectionne.setVille(newVille);
                utilisateurSelectionne.setTel(newNumero);
                utilisateurSelectionne.setGenre(genre);
                utilisateurSelectionne.setRole_id(roleId);
                utilisateurSelectionne.setImage(nomFichierImage);

                // Afficher la boîte de dialogue de confirmation avant la modification
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation");
                confirmationAlert.setHeaderText("Modifier l'utilisateur");
                confirmationAlert.setContentText("Êtes-vous sûr de vouloir modifier les données de cet utilisateur ?");
                Optional<ButtonType> result = confirmationAlert.showAndWait();

                // Si l'utilisateur confirme la modification
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        // Mettre à jour l'utilisateur dans la base de données
                        serviceUtilisateur.modifier(utilisateurSelectionne,emailfirst);
                        showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Utilisateur modifié", "Les données de l'utilisateur ont été modifiées avec succès.");
                        clearFields(); // Effacer les champs du formulaire après la modification
                        loadData(); // Rafraîchir la table pour afficher les modifications
                    } catch (SQLException e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification", "Une erreur s'est produite lors de la modification de l'utilisateur.");
                    }
                }
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Information", "Aucune modification détectée", "Aucune modification n'a été apportée aux données de cet utilisateur.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun utilisateur sélectionné", "Veuillez sélectionner un utilisateur à modifier.");
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
