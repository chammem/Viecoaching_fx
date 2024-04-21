package entities;

import java.time.LocalDateTime;

public class Rubrique {
        private int id;
        private LocalDateTime dateCreation, datePublication;
        private String titre, contenu;
        private Etat etat;

        public enum Etat {
            ACTIF, INACTIF
        }

        public Rubrique() {

        }

        public Rubrique(int id, LocalDateTime dateCreation, LocalDateTime datePublication, String titre, String contenu, Etat etat) {
            this.id = id;
            this.dateCreation = dateCreation;
            this.datePublication = datePublication;
            this.titre = titre;
            this.contenu = contenu;
            this.etat = etat;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public LocalDateTime getDateCreation() {
            return dateCreation;
        }

        public void setDateCreation(LocalDateTime dateCreation) {
            this.dateCreation = dateCreation;
        }

        public LocalDateTime getDatePublication() {
            return datePublication;
        }

        public void setDatePublication(LocalDateTime datePublication) {
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

        public Etat getEtat() {
            return etat;
        }

        public void setEtat(Etat etat) {
            this.etat = etat;
        }

        @Override
        public String toString() {
            return "Rubrique{" +
                    "id=" + id +
                    ", dateCreation=" + dateCreation +
                    ", datePublication=" + datePublication +
                    ", titre='" + titre + '\'' +
                    ", contenu='" + contenu + '\'' +
                    ", etat=" + etat +
                    '}';
        }
    }


