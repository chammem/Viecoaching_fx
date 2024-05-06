package com.asma.asma.service;

import com.asma.asma.entities.Reservation;
import com.asma.asma.entities.typeseance;
import com.asma.asma.utils.ConnectionBd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IReservation<Reservation>{



    Connection cnx = ConnectionBd.getInstance().getCnx();
    @Override
    public void addreservation(Reservation reservation) {
        try {
            String req = "INSERT INTO reservation (sujet, description,background_color,border_color,text_color) VALUES ( ?,?,?,?,?)";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, reservation.getSujet());
            pst.setString(2, reservation.getDescription());
            pst.setString(3, reservation.getBorder_color());
            pst.setString(4, reservation.getBackground_color());
            pst.setString(5, reservation.getText_color());

            pst.executeUpdate();
            System.out.println("reservation ajoutée !");
        } catch (SQLException e) {
            System.out.println("Error adding reservation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Reservation reservation) {
        try {
            String req = "DELETE FROM reservation WHERE id=?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, reservation.getId());
            pst.executeUpdate();
            System.out.println("reservation suprimée !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<Reservation> afficher() {
        List<Reservation> list = new ArrayList<>();

        try {
            String req = "SELECT * FROM reservation";
            PreparedStatement pst = cnx.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {

                list.add(new Reservation(  rs.getString(1),rs.getString(2), rs.getString(3),rs.getString(4),rs.getString(5)));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return list;
    }


    @Override
    public void modifier(Reservation reservation) {
        try {
            String req = "UPDATE reservation SET sujet=? ,description=? ,border_color=?,background_color=?,text_color=? WHERE id=?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, reservation.getSujet());
            pst.setString(2, reservation.getDescription());
            pst.setString(3, reservation.getBorder_color());
            pst.setString(4, reservation.getBackground_color());
            pst.setString(5, reservation.getText_color());
            pst.setInt(6, reservation.getId());
            pst.executeUpdate();
            System.out.println("reservation " + reservation.getSujet() + " Modifiée !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
