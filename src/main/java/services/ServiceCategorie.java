package services;

import entities.Categorie;
import entities.Ressources;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategorie implements IService<Categorie> {
    public Connection connection;

    public ServiceCategorie() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Categorie categorie) throws SQLException {
        String req = "INSERT INTO categorie (nom_categorie, description, image, ressource_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, categorie.getNom_categorie());
            preparedStatement.setString(2, categorie.getDescription());
            preparedStatement.setString(3, categorie.getImage());
            Ressources ressource = categorie.getRessource_id();
            if (ressource != null) {
                preparedStatement.setInt(4, ressource.getId());
            } else {
                preparedStatement.setNull(4, Types.INTEGER); // If ressource_id is null
            }
            preparedStatement.executeUpdate();
        }
    }

    @Override
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
                preparedStatement.setNull(4, Types.INTEGER); // If ressource_id is null
            }
            preparedStatement.setInt(5, categorie.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM categorie WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
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

                Ressources ressource = new Ressources();
                ressource.setId(ressource_id);

                Categorie categorie = new Categorie(id, nom, description, image, ressource);
                categories.add(categorie);
            }
        }

        return categories;
    }

    public int getCount() throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM categorie";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        }

        return 0;
    }

    public List<String> getCategoryTypes() throws SQLException {
        List<String> categoryTypes = new ArrayList<>();

        String query = "SELECT DISTINCT nom_categorie FROM categorie"; // Assuming 'type_r' is the category type column
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String categoryType = rs.getString("nom_categorie");
                categoryTypes.add(categoryType);
            }
        }

        return categoryTypes;
    }

    public int getCountByCategoryType(String categoryType) throws SQLException {
        int count = 0;

        String query = "SELECT COUNT(*) AS category_count FROM categorie WHERE nom_categorie = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, categoryType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("category_count");
                }
            }
        }

        return count;
    }
}
