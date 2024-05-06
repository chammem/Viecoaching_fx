package controllers;

import entities.Utilisateur;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class UtilisateurListItem extends HBox {
    private Utilisateur utilisateur;

    public UtilisateurListItem(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        Label label = new Label(utilisateur.getPrenom() + " " + utilisateur.getNom()); // Vous pouvez personnaliser cela selon vos besoins

        // Ajoutez l'étiquette à la disposition HBox
        this.getChildren().add(label);
    }

    // Méthode pour obtenir l'utilisateur associé à cet élément de liste
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
}
