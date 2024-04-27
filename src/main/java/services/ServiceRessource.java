package services;

import entities.Ressources;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRessource implements IService<Ressources>{
    public Connection connection; // Ajout de la référence à la connexion

    public ServiceRessource() {
        connection = MyDatabase.getInstance().getConnection();
    }


    @Override
    public void ajouter(Ressources ressource) throws SQLException {
        String req = "INSERT INTO ressources (titre_r, type_r, url, description) VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, ressource.getTitre_r());
        preparedStatement.setString(2, ressource.getType_r());
        preparedStatement.setString(3, ressource.getUrl());
        preparedStatement.setString(4, ressource.getDescription());

        preparedStatement.executeUpdate();

    }

    @Override
    public void modifier(Ressources ressources) throws SQLException {
        // Define the SQL update query
        String sql = "UPDATE ressources SET titre_r=?, type_r=?, description=?, url=? WHERE id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Set the parameters for the prepared statement
            preparedStatement.setString(1, ressources.getTitre_r());
            preparedStatement.setString(2, ressources.getType_r());
            preparedStatement.setString(3, ressources.getDescription());
            preparedStatement.setString(4, ressources.getUrl());
            preparedStatement.setInt(5, ressources.getId());

            // Execute the update query
        preparedStatement.executeUpdate();

    }


    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM ressources WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        int rowsDeleted = preparedStatement.executeUpdate();

        if (rowsDeleted > 0) {
            System.out.println("Ressource avec l'ID " + id + " supprimée avec succès.");
        } else {
            System.out.println("Aucune ressource trouvée avec l'ID " + id + ". Aucune suppression effectuée.");
        }
    }

    @Override
    public List<Ressources> afficher() throws SQLException {
        List<Ressources> ressources = new ArrayList<>();
        String req = "SELECT * FROM ressources";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre_r");
                String type = rs.getString("type_r");
                String image = rs.getString("url");
                String description = rs.getString("description");

                Ressources ressource = new Ressources(id, titre, type, image, description);
                ressources.add(ressource);
            }
        }
        return ressources;
    }

    public Ressources getRessourceByNom(String nom) throws SQLException {
        String sql = "SELECT * FROM ressources WHERE titre_r = ?";
        Ressources ressource = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nom);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String titre = resultSet.getString("titre_r");
                    String type = resultSet.getString("type_r");
                    String description = resultSet.getString("description");
                    String url = resultSet.getString("url");

                    ressource = new Ressources(id, titre, type, url, description);
                }
            }
        }

        return ressource;
    }

    public List<Ressources> getAllRessources() throws SQLException {
        List<Ressources> ressourcesList = new ArrayList<>();
        String sql = "SELECT * FROM ressources";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titre = resultSet.getString("titre_r");
                String type = resultSet.getString("type_r");
                String url = resultSet.getString("url");
                String description = resultSet.getString("description");

                Ressources ressource = new Ressources(id, titre, type, url, description);
                ressourcesList.add(ressource);
            }
        } catch (SQLException e) {
            // Gérer les exceptions SQL ici selon vos besoins
            System.out.println("Erreur lors de la récupération des ressources : " + e.getMessage());
            throw e; // Lancer l'exception pour la traiter au niveau supérieur si nécessaire
        }

        return ressourcesList;
    }


    public void updateRating(int resourceId, int newRatingValue) throws SQLException {
        // Prepare SQL update statement
        String sql = "UPDATE ressources SET rating = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set parameters for the prepared statement
            statement.setInt(1, newRatingValue); // Set the new rating value
            statement.setInt(2, resourceId); // Set the resource ID

            // Execute the update statement
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Rating updated successfully for resource with ID: " + resourceId);
            } else {
                System.out.println("Failed to update rating for resource with ID: " + resourceId);
            }
        } catch (SQLException e) {
            System.err.println("Error updating rating for resource with ID: " + resourceId);
            throw e; // Rethrow the exception to handle at the caller level
        }
    }

}