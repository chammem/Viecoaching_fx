package entities;


import java.util.Date;

public class Rubrique {
        private int id;
        private Date dateCréation, datePublication;
        private String titre, contenu;
        private String etat;
        private int auteur_id;

        public Rubrique() {

        }

    public Rubrique(int id, Date dateCréation, Date datePublication, String titre, String contenu, String etat, int auteur_id) {
        this.id = id;
        this.dateCréation = dateCréation;
        this.datePublication = datePublication;
        this.titre = titre;
        this.contenu = contenu;
        this.etat = etat;
        this.auteur_id = auteur_id;
    }

    public Rubrique(int id, int auteurId, String title, String contenu, java.sql.Date dateCréation, java.sql.Date datePublication, String etat) {
    }

    public Rubrique(int auteurId, String title, String contenu, java.sql.Date date, java.sql.Date date1, String published) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateCréation() {
        return dateCréation;
    }

    public void setDateCréation(Date dateCréation) {
        this.dateCréation = dateCréation;
    }

    public Date getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Date datePublication) {
        this.datePublication = datePublication;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getAuteur_id() {
        return auteur_id;
    }

    public void setAuteur_id(int auteur_id) {
        this.auteur_id = auteur_id;
    }

    @Override
    public String toString() {
        return "Rubrique{" +
                "id=" + id +
                ", dateCréation=" + dateCréation +
                ", datePublication=" + datePublication +
                ", titre='" + titre + '\'' +
                ", contenu='" + contenu + '\'' +
                ", etat='" + etat + '\'' +
                ", auteur_id=" + auteur_id +
                '}';
    }
}


