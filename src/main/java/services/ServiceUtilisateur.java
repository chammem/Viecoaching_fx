package services;

import controllers.UtilisateurController;
import entities.Utilisateur;
import javafx.scene.image.Image;
import utils.MyDatabase;
import utils.PasswordHasher;

import java.io.File;
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

    public ServiceUtilisateur(Connection connection) {
        this.connection = connection;
    }


    private static final String IMAGE_DIRECTORY = "src/main/resources/photos";

    private UtilisateurController utilisateurController;

    @Override
    public void ajouter(Utilisateur utilisateur) throws SQLException {
        if (!validateFields(utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getEmail(), utilisateur.getMdp(), utilisateur.getVille(), utilisateur.getTel(), utilisateur.getGenre())) {
            throw new IllegalArgumentException("Échec de la validation des champs.");
        }
        // Préparez la requête SQL d'insertion
        String sql = "INSERT INTO utilisateur (nom, prenom, email, mdp, ville, tel, genre, image, role_id, active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Créez une PreparedStatement pour exécuter la requête
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Remplacez les paramètres de la requête par les valeurs de l'utilisateur
            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getPrenom());
            statement.setString(3, utilisateur.getEmail());
            statement.setString(4, PasswordHasher.hashPassword(utilisateur.getMdp())); // Assurez-vous de hasher le mot de passe avant de l'insérer
            statement.setString(5, utilisateur.getVille());
            statement.setString(6, utilisateur.getTel());
            statement.setString(7, utilisateur.getGenre());
            statement.setString(8, utilisateur.getImage()); // Assurez-vous que l'image est correctement gérée
            statement.setInt(9, utilisateur.getRole_id());
            statement.setBoolean(10, true); // Vous pouvez définir l'activité de l'utilisateur ici

            // Exécutez la requête SQL d'insertion
            int rowsAffected = statement.executeUpdate();

            // Vérifiez si des lignes ont été affectées (c'est-à-dire si l'insertion a réussi)
            if (rowsAffected > 0) {
                System.out.println("Utilisateur ajouté avec succès.");
            } else {
                System.out.println("Échec de l'ajout de l'utilisateur.");
            }
        } catch (SQLException e) {
            // Gérez les exceptions SQL
            e.printStackTrace();
            throw e; // Vous pouvez choisir de gérer ou de propager l'exception
        }
    }

    @Override
    public void modifier(Utilisateur utilisateur , String email) throws SQLException {
        System.out.println(utilisateur);
        String req = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ?, mdp = ?, ville = ?, tel = ?," +
                " genre = ?, image = ?, role_id = ?, active = ? WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, utilisateur.getNom());
            preparedStatement.setString(2, utilisateur.getPrenom());
            preparedStatement.setString(3, utilisateur.getEmail());
            preparedStatement.setString(4, utilisateur.getMdp());
            preparedStatement.setString(5, utilisateur.getVille());
            preparedStatement.setString(6, utilisateur.getTel());
            preparedStatement.setString(7, utilisateur.getGenre());
            preparedStatement.setString(8, utilisateur.getImage());
            preparedStatement.setInt(9, utilisateur.getRole_id());
            preparedStatement.setBoolean(10, utilisateur.isActive());
            preparedStatement.setString(11, email);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Utilisateur modifié avec succès.");
            } else {
                System.out.println("Aucune modification n'a été effectuée pour cet utilisateur.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'utilisateur : " + e.getMessage());
            throw e;
        }
    }

    public void supprimer(int userId) throws SQLException {
        String req = "DELETE FROM utilisateur WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Utilisateur supprimé avec succès.");
            } else {
                System.out.println("Aucun utilisateur n'a été supprimé.");
            }
        }
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
            utilisateur.setNom(rs.getString("nom"));
            utilisateur.setPrenom(rs.getString("prenom"));
            utilisateur.setEmail(rs.getString("email"));
            utilisateur.setTel(rs.getString("tel"));
            utilisateur.setMdp(rs.getString("mdp"));
            utilisateur.setGenre(rs.getString("genre"));
            utilisateur.setVille(rs.getString("ville"));
            utilisateur.setActive(true); // Définit active à true par défaut
            utilisateur.setRole_id(rs.getInt("role_id"));
            String imageFileName = rs.getString("image");
            utilisateur.setImage(imageFileName);
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
    private Image loadImage(String imageName) {
        String imagePath = System.getProperty("user.dir") + "/" + IMAGE_DIRECTORY + "/" + imageName;
        return new Image(new File(imagePath).toURI().toString());
    }


    private boolean validateFields(String nom, String prenom, String email, String mdp, String ville, String tel, String genre) throws IllegalArgumentException {
        boolean isValid = true;

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty() || ville.isEmpty() || tel.isEmpty() || genre.isEmpty()) {
            throw new IllegalArgumentException("Tous les champs obligatoires doivent être remplis.");
        }

        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Adresse email invalide.");
        }

        if (emailExists(email)) {
            throw new IllegalArgumentException("Adresse email déjà utilisée.");
        }

        if (mdp.length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères.");
        }



        if (!isValidPhoneNumber(tel)) {
            throw new IllegalArgumentException("Numéro de téléphone doit contenir au moins 8 chiffres.");
        }

        return isValid;
    }
    public boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private boolean emailExists(String email) {
        try {
            String sql = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }}

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() >= 8 && phoneNumber.matches("[0-9]+");
    }

}
>>>>>>> 400a8ba329c716088c97e277c00b2f54dbc8e483
