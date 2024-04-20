package entities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Utilisateur {
    private int id;
    private String nom,prenom,email,tel,mdp,genre,ville;
<<<<<<< HEAD
    private boolean selected; // Nouveau champ pour indiquer si l'utilisateur est sélectionné
    private BooleanProperty selectedProperty;

    // Constructeur et autres méthodes

    public BooleanProperty selectedProperty() {
        if (selectedProperty == null) {
            selectedProperty = new SimpleBooleanProperty(this, "selected", false);
        }
        return selectedProperty;
    }
=======
    private boolean active;
    private int role_id;
>>>>>>> 400a8ba329c716088c97e277c00b2f54dbc8e483

    public Utilisateur() {
    }

    public Utilisateur(int id, String nom, String prenom, String email, String tel, String mdp, String genre, String ville, boolean active, int role_id) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.mdp = mdp;
        this.genre = genre;
        this.ville = ville;
        this.active = active;
        this.role_id = role_id;
    }
    public Utilisateur(int id, int age, String nom, String prenom, String email, String tel, String mdp, String genre, String ville,Boolean selected) {
        this.id = id;
        this.age = age;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.mdp = mdp;
        this.genre = genre;
        this.ville = ville;
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

<<<<<<< HEAD
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }



=======
>>>>>>> 400a8ba329c716088c97e277c00b2f54dbc8e483
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
                ", active=" + active +
                '}';
    }

<<<<<<< HEAD

=======
    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
>>>>>>> 400a8ba329c716088c97e277c00b2f54dbc8e483
}
