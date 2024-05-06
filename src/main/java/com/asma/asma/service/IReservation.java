package com.asma.asma.service;

import com.asma.asma.entities.Reservation;
import com.asma.asma.entities.typeseance;

import java.util.List;

public interface IReservation<T> {

    void addreservation(Reservation reservation);

    void supprimer(Reservation reservation);

    List<Reservation> afficher();

    void modifier(Reservation reservation);
}
