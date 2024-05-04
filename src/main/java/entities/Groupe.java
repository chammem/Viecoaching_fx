package entities;

import java.util.Date;
import java.util.List;

public class Groupe {
    private int id;
    private Typegroupe typegroupe_id;
    private String nom;
    private String description;
    private Date datecreation; // Changement du type de la date de création
    private String image;
    private List<Utilisateur> utilisateurs; // Relation avec les utilisateurs


    public Groupe(String nom,   Typegroupe typegroupe_id, Date datecreation, String image, String description) {
        this.nom = nom;
        this.typegroupe_id = typegroupe_id;

        this.datecreation = datecreation;
        this.image = image;
        this.description = description;
    }

    public Groupe(String nom,   Typegroupe typegroupe_id, String image, String description) {
        this.nom = nom;
        this.typegroupe_id = typegroupe_id;
        this.image = image;
        this.description = description;
    }
    public Groupe() {

    }
    public Groupe(String nom, Typegroupe typegroupe_id, Date datecreation, String image, String description, List<Utilisateur> utilisateurs) {
        this.nom = nom;
        this.typegroupe_id = typegroupe_id;
        this.datecreation = datecreation;
        this.image = image;
        this.description = description;
        this.utilisateurs = utilisateurs;
    }

    public Groupe(int id, Typegroupe typegroupe_id, String nom, String description, Date datecreation, String image) {
        this.id = id;
        this.typegroupe_id = typegroupe_id;
        this.nom = nom;
        this.description = description;
        this.datecreation = datecreation;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Typegroupe getTypegroupe_id() {
        return typegroupe_id;
    }

    public void setTypegroupe_id(Typegroupe typegroupe_id) {
        this.typegroupe_id = typegroupe_id;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    @Override
    public String toString() {
        return "Groupe{" +
                "id=" + id +
                ", typegroupe_id='" + typegroupe_id + '\'' +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", datecreation=" + datecreation +
                ", image='" + image + '\'' +
                '}';
    }

    public Typegroupe getTypegroupe() {
        return typegroupe_id;
    }
}

