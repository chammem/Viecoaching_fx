package entities;

import java.sql.Date;

public class Commentaire {
    private static int id;
    private int rubrique_id;
    private int auteur_id;
    private String contenu;
    private Date dateCréation;
    private static int likes;
    private static int dislikes;


    public Commentaire() {
    }

    public Commentaire(int rubrique_id, int auteur_id, String contenu, Date dateCréation, int likes,  int dislikes) {
        this.rubrique_id = rubrique_id;
        this.auteur_id = auteur_id;
        this.contenu = contenu;
        this.dateCréation = dateCréation;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public Commentaire(int id, int rubrique_id, int auteur_id, String contenu, Date dateCréation, int likes,  int dislikes) {
        this.id = id;
        this.rubrique_id = rubrique_id;
        this.auteur_id = auteur_id;
        this.contenu = contenu;
        this.dateCréation = dateCréation;
        this.likes = likes;
        this.dislikes = dislikes;
    }


    public static int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRubrique_id() {
        return rubrique_id;
    }

    public void setRubrique_id(int rubrique_id) {
        this.rubrique_id = rubrique_id;
    }

    public int getAuteur_id() {
        return auteur_id;
    }

    public void setAuteur_id(int auteur_id) {
        this.auteur_id = auteur_id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDateCréation() {
        return dateCréation;
    }

    public void setDateCréation(Date dateCréation) {
        this.dateCréation = dateCréation;
    }

    public static int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public static int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", rubrique_id=" + rubrique_id +
                ", auteur_id=" + auteur_id +
                ", contenu='" + contenu + '\'' +
                ", dateCréation=" + dateCréation +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                '}';
    }
}
