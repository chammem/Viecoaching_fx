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
    public void modifier(Categorie categorie, String a) throws SQLException {

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
                int ressourceId = rs.getInt("ressource_id"); // Récupérer l'identifiant de la ressource

                // Récupérer la ressource associée à partir de l'identifiant
                Ressources ressource = getRessourceById(ressourceId);

                // Créer une instance de Categorie avec la ressource associée
                Categorie categorie = new Categorie(id, nom, description, image, ressource);
                categories.add(categorie);
            }
        }

        return categories;
    }
    private Ressources getRessourceById(int ressourceId) throws SQLException {
        Ressources ressource = null;
        String req = "SELECT * FROM ressources WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, ressourceId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre_r");
                String description = rs.getString("description");
                String type = rs.getString("type_r");
                String url = rs.getString("url");

                ressource = new Ressources(id, titre, description, type, url);
            }
        }

        return ressource;
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
    public int getTotalItemCount() throws SQLException {
        int totalCount = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "username", "password");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM items_table");

            if (resultSet.next()) {
                totalCount = resultSet.getInt(1);
            }
        } finally {
            // Fermer les ressources JDBC
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return totalCount;
    }

    public int countCategorie() throws SQLException {
        // Déclaration des variables
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int count = 0;

        try {
            // Requête SQL pour compter le nombre de groupes
            String query = "SELECT COUNT(*) FROM categorie";

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
