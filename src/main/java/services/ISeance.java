package services;

import entities.Seance;

import java.util.List;

public interface ISeance <T>{

    void addSeance(Seance seance);



    void supprimer(Seance seance);

    List<Seance> afficher();

    void modifier(Seance seance);

    void AffichageAssocierseanceTypeseance();
}
