package services;

import entities.Commentaire;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCommentaire{
    static Connection connection;
    public ServiceCommentaire(){
        connection= MyDatabase.getInstance().getConnection();
    }
    public void ajouterCommentaire(Commentaire c){

        try {
            String requete="INSERT INTO Commentaire(rubrique_id, auteur_id, contenu, date_creation, likes, dislikes)Values(?,?,?,?,?,?)";
            PreparedStatement pst= MyDatabase.getInstance().getConnection().prepareStatement(requete);
            pst.setInt(1, c.getRubrique_id());
            pst.setInt(2, c.getAuteur_id());
            pst.setString(3, c.getContenu());
            pst.setDate(4, c.getDateCréation());
            pst.setInt(5, c.getLikes());
            pst.setInt(6, c.getDislikes());

            pst.executeUpdate();
            System.out.println("Commentaire Ajouté avec succés!!!");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<Commentaire>listerCommentaire(){
        List<Commentaire>mylist=new ArrayList();
        try {
            String requete="SELECT *FROM Commentaire";
            Statement st=MyDatabase.getInstance().getConnection().createStatement();
            ResultSet rs= st.executeQuery(requete);
            while(rs.next()){

                Commentaire P=new Commentaire();

                P.setId(rs.getInt(1));
                P.setContenu(rs.getString("contenu"));
                P.setDateCréation(rs.getDate("date_creation"));
                P.setLikes(rs.getInt("likes"));
                P.setDislikes(rs.getInt("dislikes"));
                mylist.add(P);

            }


        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return mylist;}
    public void deleteCommentaire(Commentaire C) {
        String requete = "DELETE FROM Commentaire WHERE id=?";

        try {
            PreparedStatement statement = MyDatabase.getInstance().getConnection().prepareStatement(requete);
            statement.setInt(1,C.getId());
            statement.executeUpdate();
            System.out.println("Commentaire supprimé avec succés!!!");
        } catch (SQLException ex) {
            //Logger.getLogger(CommentaireService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public void modifierCommentaire(Commentaire c){
        try {
            String requete =  "UPDATE Commentaire SET rubrique_id = ?, auteur_id = ? , contenu =? , date_creation = ?, likes = ?, dislikes = ? WHERE id=?";

            PreparedStatement pst= MyDatabase.getInstance().getConnection().prepareStatement(requete);
            pst.setInt(1, c.getRubrique_id());
            pst.setInt(2, c.getAuteur_id());
            pst.setString(3, c.getContenu());
            pst.setDate(4, c.getDateCréation());
            pst.setInt(5, c.getId());
            pst.setInt(6, c.getLikes());
            pst.setInt(7, c.getDislikes());

            pst.executeUpdate();
            System.out.println("Commentaire modifiée!");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Commentaire getCommentaireById(int CommentaireId) {
        Commentaire Commentaire = null;
        try {
            String query = "SELECT * FROM Commentaire WHERE id = ?";
            PreparedStatement statement = MyDatabase.getInstance().getConnection().prepareStatement(query);
            statement.setInt(1, CommentaireId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Commentaire = new Commentaire(resultSet.getInt("id"), resultSet.getInt("rubrique_id"), resultSet.getInt("auteur_id"), resultSet.getString("contenu"),resultSet.getDate("date_creation"),resultSet.getInt("likes"),resultSet.getInt("dislikes"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return Commentaire;
    }
    public List<Commentaire> listerCommentaireByrubrique(int rubriqueid){
        List<Commentaire>mylist=new ArrayList();
        try {
            String query = "SELECT * FROM Commentaire WHERE rubrique_id = ?";
            PreparedStatement statement = MyDatabase.getInstance().getConnection().prepareStatement(query);
            statement.setInt(1, rubriqueid);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){

                Commentaire P=new Commentaire();

                P.setId(rs.getInt(1));
                P.setRubrique_id(rs.getInt("rubrique_id"));
                P.setAuteur_id(rs.getInt("auteur_id"));
                P.setContenu(rs.getString("contenu"));
                P.setDateCréation(rs.getDate("date_creation"));
                mylist.add(P);

            }


        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return mylist;}
        public static void likeCommentaire(Commentaire c) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE Commentaire SET likes = likes + 1 WHERE id =?")) {
                statement.setInt(1, c.getId());
                statement.executeUpdate();
                System.out.println("Commentaire liked!");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        public static void dislikeCommentaire(Commentaire c) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE Commentaire SET dislikes = dislikes + 1 WHERE id =?")) {
                statement.setInt(1, c.getId());
                statement.executeUpdate();
                System.out.println("Commentaire disliked!");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    private void updateCommentaireCount(Commentaire c, String column) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE Commentaire SET " + column + " = " + column + " + 1 WHERE id =?")) {
            statement.setInt(1, c.getId());
            statement.executeUpdate();
            System.out.println("Commentaire " + column + "!");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


}








