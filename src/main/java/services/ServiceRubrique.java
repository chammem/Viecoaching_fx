package services;

import entities.Rubrique;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceRubrique implements IService<Rubrique>{
    Connection connection;
    public ServiceRubrique(){
        connection= MyDatabase.getInstance().getConnection();
    }
    @Override
    public void ajouter(Rubrique rubrique) throws SQLException {
        String req = "INSERT INTO rubrique (titre, contenu, dateCreation, datePublication,etat) " +
                "VALUES ('" + rubrique.getTitre() + "', '" + rubrique.getContenu() + "', " +
                rubrique.getDateCreation() + ", '" + rubrique.getDatePublication() + "', '" +
                rubrique.getEtat() + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(req);
        System.out.println("Rubrique ajouté");

    }

    @Override
    public void modifier(Rubrique rubrique) throws SQLException {
        String req = "UPDATE rubrique SET titre=?, contenu=?, dateCreation=?, datePublication=?, etat=?" +
                "WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, rubrique.getTitre());
        preparedStatement.setString(2, rubrique.getContenu());
        preparedStatement.setObject(3, rubrique.getDateCreation());
        preparedStatement.setObject(4, rubrique.getDatePublication());
        preparedStatement.setObject(5, rubrique.getEtat());
        preparedStatement.executeUpdate();
        System.out.println("Rubrique modifié");

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM rubrique WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        System.out.println("Rubrique supprimé");
    }

    @Override
    public List<Rubrique> afficher() throws SQLException {
        List<Rubrique> Rubrique = new ArrayList<>();
        String req = "SELECT * FROM rubrique";
        Statement statement = connection.createStatement();
        ResultSet rub = statement.executeQuery(req);
        while (rub.next()) {
            Rubrique rubrique = new Rubrique();
            rubrique.setId(rub.getInt("id"));
            rubrique.setTitre(rub.getString("titre"));
            rubrique.setContenu(rub.getString("contenu"));
            rubrique.setDateCreation(rub.getObject("dateCreation", LocalDateTime.class));
            rubrique.setDatePublication(rub.getObject("datePublication", LocalDateTime.class));
        }
        return Rubrique;
    }

    @Override
    public Rubrique trouverParId(int id) throws SQLException {
        return null;
    }



}
