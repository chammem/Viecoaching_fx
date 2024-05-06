package services;

import entities.Typegroupe;
import entities.Utilisateur;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTypegroupe implements IService<Typegroupe> {
    private Connection connection;

    public ServiceTypegroupe() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Typegroupe typegroupe) throws SQLException {
        String req = "INSERT INTO typegroupe (nomtype) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, typegroupe.getNomtype());
        preparedStatement.executeUpdate();
    }

    @Override
    public void modifier(Typegroupe typegroupe, String a) throws SQLException {

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM typegroupe WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        System.out.println("Type de groupe supprimé");
    }

    @Override
    public List<Typegroupe> afficher() throws SQLException {
        return null;
    }

    public Typegroupe getTypegroupebynom(String nom) throws SQLException {
        String sql = "SELECT * FROM typegroupe WHERE nomtype = ?";
        Typegroupe typegroupe = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nom);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nomtype = resultSet.getString("nomtype");

                    typegroupe = new Typegroupe(id, nomtype);
                }
            }
        } catch (SQLException e) {
            // Gérer les exceptions SQL ici selon vos besoins
            System.out.println("Erreur lors de la récupération du type de groupe par nom : " + e.getMessage());
            throw e; // Lancer l'exception pour la traiter au niveau supérieur si nécessaire
        }

        return typegroupe;
    }

    public List<Typegroupe> afficherg(int pageIndex, int pageSize) throws SQLException {
        List<Typegroupe> typegroupes = new ArrayList<>();
        String req = "SELECT * FROM typegroupe LIMIT ? OFFSET ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, pageSize);
            preparedStatement.setInt(2, pageIndex * pageSize);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nomtype = rs.getString("nomtype");

                    Typegroupe typegroupe = new Typegroupe(id, nomtype);
                    typegroupes.add(typegroupe);
                }
            }
        }
        return typegroupes;
    }

    public List<Typegroupe> getAllTypegroupe() throws SQLException {
        List<Typegroupe> typegroupeList = new ArrayList<>();
        String sql = "SELECT * FROM typegroupe";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nomtype = resultSet.getString("nomtype");

                Typegroupe typegroupe = new Typegroupe(id, nomtype);
                typegroupeList.add(typegroupe);
            }
        } catch (SQLException e) {
            // Gérer les exceptions SQL ici selon vos besoins
            System.out.println("Erreur lors de la récupération des types de groupe : " + e.getMessage());
            throw e; // Lancer l'exception pour la traiter au niveau supérieur si nécessaire
        }

        return typegroupeList;
    }



    @Override
    public void modifier(Typegroupe typegroupe) throws SQLException {
        String sql = "UPDATE typegroupe SET nomtype=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, typegroupe.getNomtype());
        preparedStatement.setInt(2, typegroupe.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void ajouterAvecUtilisateurs(Typegroupe typegroupe, List<Utilisateur> utilisateursSelectionnes) throws SQLException {

    }

    @Override
    public void modifierg(Typegroupe typegroupe, List<Utilisateur> utilisateursSelectionnes) throws SQLException {

    }
    public int countGroupes() throws SQLException {
        // Déclaration des variables
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int count = 0;

        try {
            // Requête SQL pour compter le nombre de groupes
            String query = "SELECT COUNT(*) FROM typegroupe";

            // Préparation de la requête
            statement = connection.prepareStatement(query);

            // Exécution de la requête et récupération du résultat
            resultSet = statement.executeQuery();

            // Si des lignes sont retournées, récupérer le nombre de groupes
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } finally {
            // Fermeture des ressources
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            // La connexion n'est pas fermée ici pour éviter de rompre la connexion pour les autres opérations dans la classe.
        }

        return count;
    }



}
