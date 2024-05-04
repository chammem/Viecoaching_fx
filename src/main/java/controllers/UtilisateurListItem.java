package controllers;
import entities.Utilisateur;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;

public class UtilisateurListItem extends HBox {
    private Utilisateur utilisateur;
    private RadioButton radioButton;

    public UtilisateurListItem(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        radioButton = new RadioButton();
        Label label = new Label(utilisateur.getPrenom() + " " + utilisateur.getNom()); // Vous pouvez personnaliser cela selon vos besoins
        radioButton.selectedProperty().bindBidirectional(utilisateur.selectedProperty());

        // Ajoutez le bouton radio et l'étiquette à la disposition HBox
        this.getChildren().addAll(radioButton, label);

        // Associez un gestionnaire d'événements pour mettre à jour l'attribut selected de l'utilisateur lorsque le bouton radio est sélectionné ou désélectionné
        radioButton.setOnAction(event -> utilisateur.setSelected(radioButton.isSelected()));
    }

    // Méthode pour obtenir l'utilisateur associé à cet élément de liste
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
}

