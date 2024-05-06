package controllers;
import entities.Utilisateur;
import services.ServiceMdp;
import services.ServiceProfil;
import utils.MyDatabase;
import utils.SessionManager;
import utils.PasswordHasher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MdpController implements Initializable {
    @FXML
    public PasswordField mdpId;
    @FXML
    public PasswordField confId;
    @FXML
    public Button ajouterId;
    private ServiceMdp serviceMdp;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MyDatabase myDatabase = MyDatabase.getInstance();
        Connection connection = myDatabase.getConnection();
        this.serviceMdp = new ServiceMdp(connection);
        ajouterId.setOnAction(event -> ajouterUtilisateur());}
    private void ajouterUtilisateur() {
        // Récupérer le mot de passe et sa confirmation depuis les champs de texte
        String motDePasse = mdpId.getText();
        String confirmationMotDePasse = confId.getText();

        // Vérifier si les champs de mot de passe et de confirmation sont vides
        if (motDePasse.isEmpty() || confirmationMotDePasse.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Vérifier si les champs de mot de passe correspondent
        if (!motDePasse.equals(confirmationMotDePasse)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Mots de passe différents", "Les champs de mot de passe et de confirmation ne correspondent pas.");
            return;
        }

        // Hacher le mot de passe avant de le stocker dans la base de données
        String motDePasseHache = PasswordHasher.hashPassword(motDePasse);

        // Mettre à jour le mot de passe haché dans la base de données
        Utilisateur utilisateurConnecte = SessionManager.getUtilisateurConnecte();
        if (utilisateurConnecte != null) {
            try {
                // Définir le nouveau mot de passe haché pour l'utilisateur connecté
                utilisateurConnecte.setMdp(motDePasseHache);
                // Mettre à jour le mot de passe dans la base de données
                serviceMdp.modifier(utilisateurConnecte, utilisateurConnecte.getEmail());
                // Afficher une alerte pour confirmer la modification du mot de passe
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Mot de passe modifié", "Le mot de passe a été modifié avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification du mot de passe", "Une erreur s'est produite lors de la modification du mot de passe.");
                e.printStackTrace();
            }
        }
    }
            private void showAlert(Alert.AlertType type, String title, String header, String content) {
                Alert alert = new Alert(type);
                alert.setTitle(title);
                alert.setHeaderText(header);
                alert.setContentText(content);
                alert.showAndWait();


            }
}
