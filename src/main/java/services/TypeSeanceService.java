package services;

import entities.typeseance;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeSeanceService implements ITypeSeance<typeseance>{


    Connection cnx = MyDatabase.getInstance().getConnection();


    @Override
    public void addtypeseance(typeseance typeseance) {
        try {
            String req = "INSERT INTO type_seance (nom_type, id) VALUES (?, ?)";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, typeseance.getNomtype());
            pst.setInt(2, typeseance.getTypeSeanceId());

            pst.executeUpdate();
            System.out.println("Seance ajoutée !");
        } catch (SQLException e) {
            System.out.println("Error adding Seance: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(typeseance typeseance) {
        try {
            String req = "DELETE FROM type_seance WHERE id=?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, typeseance.getTypeSeanceId());
            pst.executeUpdate();
            System.out.println("typeseance suprimée !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<typeseance> afficher() {

            List<typeseance> list = new ArrayList<>();

            try {
                String req = "SELECT * FROM type_seance";
                PreparedStatement pst = cnx.prepareStatement(req);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {

                    list.add(new typeseance( rs.getInt(1),rs.getString(2)));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            return list;
        }

    @Override
    public void modifier(typeseance typeseance) {
        try {
            String req = "UPDATE type_seance SET nom_type=? WHERE id=?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, typeseance.getNomtype());

            pst.setInt(2, typeseance.getTypeSeanceId()); // Assuming 'id' is the correct column name for the ID
            pst.executeUpdate();
            System.out.println("typeseance " + typeseance.getNomtype() + " Modifiée !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
