/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import utils.MyDatabase;
import entities.Rubrique;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class ServiceRubrique {
    public void ajouterRubrique(Rubrique c){
        try {
            String requete="INSERT INTO Rubrique(auteur_id,titre,contenu,date_creation,date_publication,etat)Values(?,?,?,?,?,?)";
            PreparedStatement pst= MyDatabase.getInstance().getConnection().prepareStatement(requete);
            pst.setInt(1, c.getAuteur_id());
            pst.setString(2, c.getTitre());
            pst.setString(3, c.getContenu());
            pst.setDate(4, new java.sql.Date(c.getDateCréation().getTime()));
            pst.setDate(5, new java.sql.Date(c.getDatePublication().getTime()));
            pst.setString(6, c.getEtat());


            pst.executeUpdate();
            System.out.println("elemenT AJOUTEEEEE");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<Rubrique>listerRubrique(){
        List<Rubrique>mylist=new ArrayList();
        try {

            String requete="SELECT *FROM Rubrique";
            Statement st=MyDatabase.getInstance().getConnection().createStatement();
            ResultSet rs= st.executeQuery(requete);
            while(rs.next()){

                Rubrique P=new Rubrique();

                P.setId(rs.getInt(1));
                P.setAuteur_id(rs.getInt("auteur_id"));
                P.setTitre(rs.getString("titre"));
                P.setContenu(rs.getString("contenu"));
                P.setEtat(rs.getString("etat"));
                P.setDateCréation(rs.getDate("date_creation"));
                P.setDatePublication(rs.getDate("date_publication"));



                mylist.add(P);

            }


        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return mylist;}
    public void deleteRubrique(Rubrique rubrique) {
        // Delete comments associated with the Rubrique
        deleteCommentsForRubrique(rubrique.getId());

        // Delete the Rubrique itself
        String requete = "DELETE FROM Rubrique WHERE id=?";

        try {
            PreparedStatement statement = MyDatabase.getInstance().getConnection().prepareStatement(requete);
            statement.setInt(1, rubrique.getId());
            statement.executeUpdate();
            System.out.println("Rubrique supprimé!");
        } catch (SQLException ex) {
            // Handle the exception appropriately
            ex.printStackTrace();
        }
    }

    private void deleteCommentsForRubrique(int rubriqueId) {
        String requete = "DELETE FROM Commentaire WHERE rubrique_id=?";

        try {
            PreparedStatement statement = MyDatabase.getInstance().getConnection().prepareStatement(requete);
            statement.setInt(1, rubriqueId);
            statement.executeUpdate();
            System.out.println("Comments for Rubrique with ID " + rubriqueId + " deleted!");
        } catch (SQLException ex) {
            // Handle the exception appropriately
            ex.printStackTrace();
        }
    }




    public void modifierRubrique(Rubrique c){
        try {
            String requete =  "UPDATE Rubrique SET auteur_id = ?, titre = ? , contenu =? ,date_creation = ?, date_publication = ?, etat = ? WHERE id=?";

            PreparedStatement pst= MyDatabase.getInstance().getConnection().prepareStatement(requete);
            pst.setInt(7, c.getId());
            pst.setInt(1, c.getAuteur_id());
            pst.setString(2, c.getTitre());
            pst.setString(3, c.getContenu());
            pst.setDate(4, new java.sql.Date(c.getDateCréation().getTime()));
            pst.setDate(5, new java.sql.Date(c.getDatePublication().getTime()));
            pst.setString(6, c.getEtat());

            pst.executeUpdate();
            System.out.println("Rubrique modifiée!");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Rubrique getRubriqueById(int RubriqueId) {
        Rubrique Rubrique = null;
        try {
            String query = "SELECT * FROM Rubrique WHERE id = ?";
            PreparedStatement statement = MyDatabase.getInstance().getConnection().prepareStatement(query);
            statement.setInt(1, RubriqueId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Rubrique = new Rubrique(resultSet.getInt("id"), resultSet.getInt("auteur_id"), resultSet.getString("title"), resultSet.getString("contenu"),resultSet.getDate("date_creation"),resultSet.getDate("date_publication"),resultSet.getString("etat"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return Rubrique;
    }

}
