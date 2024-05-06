package services;

import entities.Reservation;

import java.util.List;

public interface IReservation<T> {

    void addreservation(Reservation reservation);

    void supprimer(Reservation reservation);

    List<Reservation> afficher();

    void modifier(Reservation reservation);
}
