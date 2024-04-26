package entities;

import java.sql.Date;

public class Commentaire {
    private int id;
    private int rubrique_id;
    private int auteur_id;
    private String contenu;
    private Date DateCréation;

    public Commentaire() {
    }

    public Commentaire(int rubrique_id, int auteur_id, String contenu, Date DateCréation) {
        this.rubrique_id = rubrique_id;
        this.auteur_id = auteur_id;
        this.contenu = contenu;
        this.DateCréation = DateCréation;
    }

    public Commentaire(int id, int rubrique_id, int auteur_id, String contenu, Date DateCréation) {
        this.id = id;
        this.rubrique_id = rubrique_id;
        this.auteur_id = auteur_id;
        this.contenu = contenu;
        this.DateCréation = DateCréation;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", rubrique_id=" + rubrique_id +
                ", auteur_id=" + auteur_id +
                ", contenu='" + contenu + '\'' +
                ", DateCréation=" + DateCréation +
                '}';
    }

    public int getId() {
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
        return DateCréation;
    }

    public void setDateCréation(Date DateCréation) {
        this.DateCréation = DateCréation;
    }
}
