package services;

import entities.Categorie;
import entities.Ressources;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategorie implements IService<Categorie> {
    public Connection connection; // Ajout de la référence à la connexion

    public ServiceCategorie() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Categorie categories) throws SQLException {
        String req = "INSERT INTO categorie (nom_categorie, description, image, ressource_id) VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, categories.getNom_categorie());
        preparedStatement.setString(2, categories.getDescription());
        preparedStatement.setString(3, categories.getImage());
        Ressources ressource = categories.getRessource_id();
        if (ressource != null) {
            preparedStatement.setInt(4, ressource.getId());
        } else {
            preparedStatement.setNull(4, Types.INTEGER); // Si ressource_id est null
        }
        preparedStatement.executeUpdate();
    }

    public void modifier(Categorie categorie) throws SQLException {
        String req = "UPDATE categorie SET nom_categorie=?, description=?, image=?, ressource_id=? WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, categorie.getNom_categorie());
            preparedStatement.setString(2, categorie.getDescription());
            preparedStatement.setString(3, categorie.getImage());

            Ressources ressource = categorie.getRessource_id();
            if (ressource != null) {
                preparedStatement.setInt(4, ressource.getId());
            } else {
                preparedStatement.setNull(4, Types.INTEGER); // Si ressource_id est null
            }

            preparedStatement.setInt(5, categorie.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la modification de la catégorie : " + e.getMessage(), e);
        }
    }
    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM categorie WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la suppression de la catégorie : " + e.getMessage(), e);
        }
    }

    @Override
    public List<Categorie> afficher() throws SQLException {
        List<Categorie> categories = new ArrayList<>();
        String req = "SELECT * FROM categorie";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom_categorie");
                String description = rs.getString("description");
                String image = rs.getString("image");
                int ressource_id = rs.getInt("ressource_id");

                // Créer un objet Ressources à partir de l'identifiant récupéré
                Ressources ressource = new Ressources();
                ressource.setId(ressource_id);

                // Créer un objet Categorie avec les données récupérées
                Categorie categorie = new Categorie(id, nom, description, image, ressource);
                categories.add(categorie);
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de l'affichage des catégories : " + e.getMessage(), e);
        }

        return categories;
    }

}
