package services;

import entities.Utilisateur;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
public class ServiceUtilisateur implements IService<Utilisateur> {
=======
public class  ServiceUtilisateur implements IService<Utilisateur>{
>>>>>>> 400a8ba329c716088c97e277c00b2f54dbc8e483
    Connection connection;

    public ServiceUtilisateur() {
        connection = MyDatabase.getInstance().getConnection();

    }

    @Override
    public void ajouter(Utilisateur utilisateur) throws SQLException {
        String req = "INSERT INTO utilisateur (nom, prenom, email, tel, mdp, genre, ville, active, role_id) " +
                "VALUES ('" + utilisateur.getNom() + "', '" + utilisateur.getPrenom() + "', '" + utilisateur.getEmail() + "', '" +
                utilisateur.getTel() + "', '" + utilisateur.getMdp() + "', '" + utilisateur.getGenre() + "', '" +
                utilisateur.getVille() + "', " + utilisateur.isActive() + ", " + utilisateur.getRole_id() + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(req);
        System.out.println("Utilisateur ajouté");
    }

    @Override
    public void modifier(Utilisateur utilisateur) throws SQLException {
        String req = "UPDATE utilisateur SET email=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, utilisateur.getEmail());
        preparedStatement.setInt(2, utilisateur.getId());
        preparedStatement.executeUpdate();
        System.out.println("Utilisateur modifié");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM utilisateur WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        System.out.println("Utilisateur supprimé");
    }
    public List<Utilisateur> getAllUtilisateurs() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateur";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                // Récupérez d'autres attributs de l'utilisateur de la base de données

                Utilisateur utilisateur = new Utilisateur(id, nom);
                utilisateurs.add(utilisateur);
            }
        }

        return utilisateurs;
    }

    @Override
    public List<Utilisateur> afficher() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String req = "SELECT * FROM utilisateur";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(req);
        while (rs.next()) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(rs.getInt("id"));
            utilisateur.setNom(rs.getString("nom"));
            utilisateur.setPrenom(rs.getString("prenom"));
            utilisateur.setEmail(rs.getString("email"));
            utilisateur.setTel(rs.getString("tel"));
            utilisateur.setMdp(rs.getString("mdp"));
            utilisateur.setGenre(rs.getString("genre"));
            utilisateur.setVille(rs.getString("ville"));
            utilisateur.setActive(true); // Définit active à true par défaut
            utilisateur.setRole_id(rs.getInt("role_id"));
            utilisateurs.add(utilisateur);
        }
        return utilisateurs;
    }

<<<<<<< HEAD

    public Utilisateur getUtilisateurByNom(String nomUtilisateur) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;

        try {
            // Préparer la requête SQL pour récupérer l'utilisateur par son nom
            String query = "SELECT * FROM utilisateur WHERE nom = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nomUtilisateur);

            // Exécuter la requête SQL
            resultSet = preparedStatement.executeQuery();

            // Vérifier s'il y a un résultat
            if (resultSet.next()) {
                // Créer un nouvel objet Utilisateur avec les données récupérées de la base de données
                utilisateur = new Utilisateur();
                utilisateur.setId(resultSet.getInt("id"));
                utilisateur.setNom(resultSet.getString("nom"));
                utilisateur.setPrenom(resultSet.getString("prenom"));
                utilisateur.setAge(resultSet.getInt("age"));
                utilisateur.setEmail(resultSet.getString("email"));
                utilisateur.setTel(resultSet.getString("tel"));
                utilisateur.setMdp(resultSet.getString("mdp"));
                utilisateur.setGenre(resultSet.getString("genre"));
                utilisateur.setVille(resultSet.getString("ville"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception SQL
        } finally {
            // Fermer les ressources JDBC
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }

        // Retourner l'utilisateur trouvé ou null s'il n'est pas trouvé
        return utilisateur;
    }

}
=======
    public Utilisateur trouverParId(int id) throws SQLException {
        String req = "SELECT * FROM utilisateur WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(rs.getInt("id"));
            utilisateur.setNom(rs.getString("nom"));
            utilisateur.setPrenom(rs.getString("prenom"));
            utilisateur.setEmail(rs.getString("email"));
            utilisateur.setTel(rs.getString("tel"));
            utilisateur.setMdp(rs.getString("mdp"));
            utilisateur.setGenre(rs.getString("genre"));
            utilisateur.setVille(rs.getString("ville"));
            utilisateur.setActive(true);
            return utilisateur;
        } else {
            return null; // Aucun utilisateur trouvé avec cet ID
        }
    }
}
>>>>>>> 400a8ba329c716088c97e277c00b2f54dbc8e483
