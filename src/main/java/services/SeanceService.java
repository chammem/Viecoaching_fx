package services;

import entities.Seance;
import entities.seanceDTO;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeanceService implements ISeance<Seance>{



    Connection cnx = MyDatabase.getInstance().getConnection();


    @Override
    public void addSeance(Seance seance) {
        try {
            // Check if utilisateurs_id exists in utilisateur table
            if (!isUtilisateurExists(seance.getUtilisateursId())) {
                System.out.println("Error adding Seance: utilisateurs_id does not exist");
                return;
            }

            String req = "INSERT INTO seance (titre, duree, lien, mot_de_passe, type_seance_id, utilisateurs_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, seance.getTitre());
            pst.setTime(2, seance.getDuree());
            pst.setString(3, seance.getLien());
            pst.setString(4, seance.getMotDePasse());
            pst.setInt(5, seance.getTypeSeanceId());
            pst.setInt(6, seance.getUtilisateursId());

            pst.executeUpdate();
            System.out.println("Seance ajoutée !");
        } catch (SQLException e) {
            System.out.println("Error adding Seance: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isUtilisateurExists(int utilisateursId) throws SQLException {
        String req = "SELECT COUNT(*) FROM utilisateur WHERE id = ?";
        PreparedStatement pst = cnx.prepareStatement(req);
        pst.setInt(1, utilisateursId);
        ResultSet rs = pst.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }


    @Override
    public void supprimer(Seance seance) {
        try {
            String req = "DELETE FROM seance WHERE id=?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, seance.getIdseance());
            pst.executeUpdate();
            System.out.println("seance suprimée !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<Seance> afficher() {
        List<Seance> list = new ArrayList<>();

        try {
            String req = "SELECT * FROM Seance";
            PreparedStatement pst = cnx.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {

                list.add(new Seance(  rs.getInt(1),rs.getString(2), rs.getTime(3), rs.getString(4),rs.getString(5),rs.getInt(6),rs.getInt(7)));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return list;
    }

    @Override
    public void modifier(Seance seance) {
        try {
            String req = "UPDATE seance SET titre=?, duree=?, lien=?, mot_de_passe=?, type_seance_id=?, utilisateurs_id=? WHERE id=?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, seance.getTitre());
            pst.setTime(2, seance.getDuree());
            pst.setString(3, seance.getLien());
            pst.setString(4, seance.getMotDePasse()); // Assuming 'mot_de_passe' is the correct column name
            pst.setInt(5, seance.getTypeSeanceId()); // Assuming 'type_seance_id' is the correct column name
            pst.setInt(6, seance.getUtilisateursId());
            pst.setInt(7, seance.getIdseance()); // Assuming 'id' is the correct column name for the ID
            pst.executeUpdate();
            System.out.println("seance " + seance.getTitre() + " Modifiée !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }





    @Override
    public void AffichageAssocierseanceTypeseance() {
        List<seanceDTO> list = affichage(); // Call the modified method to retrieve data
        displayList(list);
    }

    public List<seanceDTO> affichage() {
        List<seanceDTO> list = new ArrayList<>();

        try {
            Connection cnx = MyDatabase.getInstance().getConnection();

            String req = "SELECT s.id AS idseance, t.id AS typeId, s.titre, s.duree, s.lien, s.mot_de_passe, t.nom_type FROM Seance s JOIN type_seance t ON s.type_seance_id = t.id;";

            PreparedStatement pst = cnx.prepareStatement(req);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                seanceDTO seanceDTO = new seanceDTO(
                        rs.getInt("idseance"),
                        rs.getString("titre"),
                        rs.getTime("duree"),
                        rs.getString("lien"),
                        rs.getString("mot_de_passe"),
                        rs.getInt("typeId"), // Changed to typeId
                        rs.getString("nom_type") // Fixed column name
                );
                list.add(seanceDTO);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }



    private void displayList(List<seanceDTO> list) {
        for (seanceDTO item : list) {
            System.out.println(item);
        }
    }

}
