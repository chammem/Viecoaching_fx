package com.asma.asma.entities;

import java.sql.Time;

public class seanceDTO {

    private int idseance;
    private String titre;
    private Time duree;
    private String lien;
    private String motDePasse;
    private int typeSeanceId;
    private String nom_type;


    public seanceDTO(int idseance, String titre, Time duree, String lien, String motDePasse, int typeSeanceId, String nom_type) {
        this.idseance = idseance;
        this.titre = titre;
        this.duree = duree;
        this.lien = lien;
        this.motDePasse = motDePasse;
        this.typeSeanceId = typeSeanceId;
        this.nom_type = nom_type;

    }

    public int getIdseance() {
        return idseance;
    }

    public void setIdseance(int idseance) {
        this.idseance = idseance;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Time getDuree() {
        return duree;
    }

    public void setDuree(Time duree) {
        this.duree = duree;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public int getTypeSeanceId() {
        return typeSeanceId;
    }

    public void setTypeSeanceId(int typeSeanceId) {
        this.typeSeanceId = typeSeanceId;
    }

    public String getNom_type() {
        return nom_type;
    }

    public void setNom_type(String nom_type) {
        this.nom_type = nom_type;
    }



    @Override
    public String toString() {
        return "seanceDTO{" +
                "idseance=" + idseance +
                ", titre='" + titre + '\'' +
                ", duree=" + duree +
                ", lien='" + lien + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", typeSeanceId=" + typeSeanceId +
                ", nom_type='" + nom_type + '\'' +

                '}';
    }
}
