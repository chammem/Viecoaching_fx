package entities;

import java.sql.Time;
public class Seance {
    private int idseance;
    private String titre;
    private Time duree;
    private String lien;
    private String motDePasse;
    private int typeSeanceId;
    private int utilisateurs_id;
private String qr;
    public Seance() {
    }

	public Seance(String titre, Time duree, String lien, String motDePasse, int typeSeanceId, int utilisateurs_id, String qr) {
		this.titre = titre;
		this.duree = duree;
		this.lien = lien;
		this.motDePasse = motDePasse;
		this.typeSeanceId = typeSeanceId;
		this.utilisateurs_id = utilisateurs_id;
		this.qr = qr;
	}

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}

	public Seance(int idseance) {
        this.idseance = idseance;
    }

    public Seance(int idseance, String titre, Time duree, String lien, String motDePasse, int typeSeanceId, int utilisateurs_id) {
        this.idseance = idseance;
        this.titre = titre;
        this.duree = duree;
        this.lien = lien;
        this.motDePasse = motDePasse;
        this.typeSeanceId = typeSeanceId;
        this.utilisateurs_id = utilisateurs_id;
    }

    public Seance(String titre, Time duree, String lien, String motDePasse, int typeSeanceId, int utilisateurs_id) {
        this.titre = titre;
        this.duree = duree;
        this.lien = lien;
        this.motDePasse = motDePasse;
        this.typeSeanceId = typeSeanceId;
        this.utilisateurs_id = utilisateurs_id;
    }


	public Seance(int idseance, String titre, Time duree, String lien, String motDePasse, int typeSeanceId, int utilisateurs_id, String qr) {
		this.idseance = idseance;
		this.titre = titre;
		this.duree = duree;
		this.lien = lien;
		this.motDePasse = motDePasse;
		this.typeSeanceId = typeSeanceId;
		this.utilisateurs_id = utilisateurs_id;
		this.qr = qr;
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

    public int getUtilisateursId() {
        return utilisateurs_id;
    }

    public void setUtilisateursId(int utilisateursId) {
        this.utilisateurs_id = utilisateursId;
    }


    @Override
	public String toString() {
		return "Seance{" +

				", titre='" + titre + '\'' +
				", duree=" + duree +
				", lien='" + lien + '\'' +
				", motDePasse='" + motDePasse + '\'' +
				", typeSeanceId=" + typeSeanceId +
				", utilisateurs_id=" + utilisateurs_id +
	", qr='" + qr + '\'' +
				'}';
	}
}


