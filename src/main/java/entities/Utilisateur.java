package entities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Utilisateur {
    private int id,age;
    private String nom,prenom,email,tel,mdp,genre,ville;
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

    public Utilisateur(int id, int age, String nom, String prenom, String email, String tel, String mdp, String genre, String ville) {
        this.id = id;
        this.age = age;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.mdp = mdp;
        this.genre = genre;
        this.ville = ville;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", age=" + age +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", mdp='" + mdp + '\'' +
                ", genre='" + genre + '\'' +
                ", ville='" + ville + '\'' +
                '}';
    }


}
