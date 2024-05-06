package entities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Utilisateur {
    private int id;
    private String nom,prenom,email,tel,mdp,genre,ville,image;
    private boolean active;
    private int role_id;
    private String roleName;
    private boolean selected; // Nouveau champ pour indiquer si l'utilisateur est sélectionné
    private BooleanProperty selectedProperty;

    // Constructeur et autres méthodes

    public BooleanProperty selectedProperty() {
        if (selectedProperty == null) {
            selectedProperty = new SimpleBooleanProperty(this, "selected", false);
        }
        return selectedProperty;
    }

    public Utilisateur() {
    }

    public Utilisateur(String nom, String prenom, String email, String mdp, String ville, String tel, String genre, String image, int role_id) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.ville = ville;
        this.tel = tel;
        this.genre = genre;
        this.image = image;
        this.role_id = role_id;
    }

    public Utilisateur(int id, String nom, String prenom, String email, String tel, String mdp, String genre, String ville, String image) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.mdp = mdp;
        this.genre = genre;
        this.ville = ville;
        this.image = image;
    }

    public Utilisateur(int id, String nom, String prenom, String email, String tel, String mdp, String genre, String ville, String image, boolean active, int role_id, String roleName, boolean selected, BooleanProperty selectedProperty) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.mdp = mdp;
        this.genre = genre;
        this.ville = ville;
        this.image = image;
        this.active = active;
        this.role_id = role_id;
        this.roleName = roleName;
        this.selected = selected;
        this.selectedProperty = selectedProperty;
    }

    public Utilisateur(int id, String nom, String prenom, String email, String tel, String mdp, String genre, String ville) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.mdp = mdp;
        this.genre = genre;
        this.ville = ville;
    }


    public Utilisateur(Utilisateur utilisateurSelectionne) {

        this.selected = false; // Par défaut, l'utilisateur n'est pas sélectionné
    }
    public Utilisateur(int id ,String nom) {
        this.id = id;
        this.nom = nom;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }





    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public boolean isSelected() {
        return selectedProperty != null && selectedProperty.get();
    }

    public void setSelected(boolean selected) {
        selectedProperty().set(selected);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Utilisateur(String nom, String prenom, String email, String tel, String mdp, String genre, String ville, String image, boolean active) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.mdp = mdp;
        this.genre = genre;
        this.ville = ville;
        this.image = image;
        this.active = active;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", mdp='" + mdp + '\'' +
                ", genre='" + genre + '\'' +
                ", ville='" + ville + '\'' +
                ", image='" + image + '\'' +
			",role_id='" + role_id + '\'' +
                '}';
    }


}
