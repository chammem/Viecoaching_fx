package services;

        import entities.Groupe;
        import entities.Typegroupe;
        import entities.Utilisateur;
import services.IService;
import utils.MyDatabase;

        import javax.mail.*;
        import javax.mail.internet.InternetAddress;
        import javax.mail.internet.MimeMessage;
        import java.sql.Date;

        import java.sql.*;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Properties;

public class ServiceGroupe implements IService<Groupe> {
    Connection connection;

    public ServiceGroupe() {
        connection = MyDatabase.getInstance().getConnection();
    }


    @Override
    public void ajouterAvecUtilisateurs(Groupe groupe, List<Utilisateur> utilisateursSelectionnes) throws SQLException {
        String reqGroupe = "INSERT INTO groupe (nom, typegroupe_id, datecreation, image, description) VALUES (?, ?, ?, ?, ?)";
        String reqGroupeUtilisateur = "INSERT INTO groupe_utilisateur (groupe_id, utilisateur_id) VALUES (?, ?)";

        try (PreparedStatement preparedStatementGroupe = connection.prepareStatement(reqGroupe, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement preparedStatementGroupeUtilisateur = connection.prepareStatement(reqGroupeUtilisateur)) {
            // Ajouter le groupe
            preparedStatementGroupe.setString(1, groupe.getNom());

            Typegroupe typegroupe = groupe.getTypegroupe_id();
            if (typegroupe != null) {
                preparedStatementGroupe.setInt(2, typegroupe.getId());
            } else {
                preparedStatementGroupe.setNull(2, Types.INTEGER); // Si typegroupe est null
            }

            preparedStatementGroupe.setDate(3, new java.sql.Date(groupe.getDatecreation().getTime()));
            preparedStatementGroupe.setString(4, groupe.getImage());
            preparedStatementGroupe.setString(5, groupe.getDescription());

            preparedStatementGroupe.executeUpdate();
            ResultSet rs = preparedStatementGroupe.getGeneratedKeys();
            int groupeId;
            if (rs.next()) {
                groupeId = rs.getInt(1);
            } else {
                throw new SQLException("Erreur lors de l'ajout du groupe, aucun ID généré.");
            }

            // Ajouter les utilisateurs au groupe
            for (Utilisateur utilisateur : groupe.getUtilisateurs()) {
                preparedStatementGroupeUtilisateur.setInt(1, groupeId);
                preparedStatementGroupeUtilisateur.setInt(2, utilisateur.getId());
                preparedStatementGroupeUtilisateur.executeUpdate(); // Exécuter pour chaque utilisateur
            }
            for (Utilisateur utilisateur : utilisateursSelectionnes) {
                sendEmail(utilisateur.getEmail(), "Nouveau groupe ajouté", "Vous avez été ajouté au groupe : " + groupe.getNom());
            }

            System.out.println("Groupe ajouté avec succès.");
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de l'ajout du groupe : " + e.getMessage(), e);
        }
    }
    public static void sendEmail(String recipientEmail, String subject, String content) {
        final String username = "mariem.dghaies@gmail.com";
        final String password = "nkxn otmj jasy tprd";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Utilisateur> afficherUtilisateurs() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String req = "SELECT id, age, nom, prenom, email, tel, mdp, genre, ville FROM utilisateur";


        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int age = rs.getInt("age");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String tel = rs.getString("tel");
                String mdp = rs.getString("mdp");
                String genre = rs.getString("genre");
                String ville = rs.getString("ville");

                Utilisateur utilisateur = new Utilisateur(id, age, null, prenom, email, tel, mdp, genre, ville);
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de l'affichage des utilisateurs : " + e.getMessage(), e);
        }

        return utilisateurs;
    }



    @Override
    public List<Groupe> afficher() throws SQLException {
        List<Groupe> groupes = new ArrayList<>();
        String req = "SELECT * FROM groupe";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int typegroupe_id = rs.getInt("typegroupe_id");
                String nom = rs.getString("nom");
                String description = rs.getString("description");
                java.sql.Date datecreation = rs.getDate("datecreation");
                String image = rs.getString("image");

                // Récupérer les informations de Typegroupe associées à l'ID de Typegroupe
                Typegroupe typegroupe = getTypegroupeById(typegroupe_id);

                // Récupérer les utilisateurs associés à ce groupe
                List<Utilisateur> utilisateurs = getUtilisateursByGroupeId(id);

                // Créer un objet Groupe avec les données récupérées
                Groupe groupe = new Groupe(id, typegroupe, nom, description, datecreation, image);

                // Associer les utilisateurs récupérés au groupe
                groupe.setUtilisateurs(utilisateurs);

                groupes.add(groupe);
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de l'affichage des catégories : " + e.getMessage(), e);
        }

        return groupes;
    }
    private List<Utilisateur> getUtilisateursByGroupeId(int groupeId) throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String req = "SELECT * FROM groupe_utilisateur WHERE groupe_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, groupeId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int utilisateurId = rs.getInt("utilisateur_id");

                // Récupérer les informations de l'utilisateur associé à cet ID
                Utilisateur utilisateur = getUtilisateurById(utilisateurId);

                if (utilisateur != null) {
                    utilisateurs.add(utilisateur);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la récupération des utilisateurs du groupe : " + e.getMessage(), e);
        }

        return utilisateurs;
    }
    private Utilisateur getUtilisateurById(int utilisateurId) throws SQLException {
        Utilisateur utilisateur = null;
        String req = "SELECT * FROM utilisateur WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, utilisateurId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                int age = rs.getInt("age");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String tel = rs.getString("tel");
                String mdp = rs.getString("mdp");
                String genre = rs.getString("genre");
                String ville = rs.getString("ville");
                // Ajoutez d'autres colonnes si nécessaire

                // Créer un objet Utilisateur avec les données récupérées
                utilisateur = new Utilisateur(id, age, nom, prenom, email, tel, mdp, genre, ville);
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la récupération de l'utilisateur : " + e.getMessage(), e);
        }

        return utilisateur;
    }



    // Méthode pour récupérer les informations de Typegroupe à partir de l'ID
    private Typegroupe getTypegroupeById(int typegroupe_id) throws SQLException {
        Typegroupe typegroupe=new Typegroupe();
        String req = "SELECT * FROM typegroupe WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, typegroupe_id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    // Créer un objet Typegroupe avec les données récupérées de la base de données
                    int typeId = rs.getInt("id");
                    String typeNom = rs.getString("nomtype"); // Assurez-vous que la colonne 'nom' est sélectionnée
                    typegroupe = new Typegroupe(typeId, typeNom);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la récupération du Typegroupe : " + e.getMessage(), e);
        }

        return typegroupe;
    }

    public void modifier(Groupe groupe) throws SQLException {
        String reqGroupe = "UPDATE groupe SET nom=?, datecreation=NOW(), image=?, description=? WHERE id=?";
        String reqGroupeUtilisateurSupprimer = "DELETE FROM groupe_utilisateur WHERE groupe_id=?";
        String reqGroupeUtilisateurInserer = "INSERT INTO groupe_utilisateur (groupe_id, utilisateur_id) VALUES (?, ?)";

        try (PreparedStatement preparedStatementGroupe = connection.prepareStatement(reqGroupe);
             PreparedStatement preparedStatementGroupeUtilisateurSupprimer = connection.prepareStatement(reqGroupeUtilisateurSupprimer);
             PreparedStatement preparedStatementGroupeUtilisateurInserer = connection.prepareStatement(reqGroupeUtilisateurInserer)) {

            // Modifier le groupe
            preparedStatementGroupe.setString(1, groupe.getNom());
            preparedStatementGroupe.setString(2, groupe.getImage());
            preparedStatementGroupe.setString(3, groupe.getDescription());
            preparedStatementGroupe.setInt(4, groupe.getId());

            preparedStatementGroupe.executeUpdate();

            // Supprimer tous les utilisateurs associés au groupe
            preparedStatementGroupeUtilisateurSupprimer.setInt(1, groupe.getId());
            preparedStatementGroupeUtilisateurSupprimer.executeUpdate();

            // Ajouter les nouveaux utilisateurs sélectionnés au groupe
            for (Utilisateur utilisateur : groupe.getUtilisateurs()) {
                preparedStatementGroupeUtilisateurInserer.setInt(1, groupe.getId());
                preparedStatementGroupeUtilisateurInserer.setInt(2, utilisateur.getId());
                preparedStatementGroupeUtilisateurInserer.executeUpdate(); // Exécuter pour chaque utilisateur
            }

            System.out.println("Groupe modifié avec succès.");
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la mise à jour du groupe : " + e.getMessage(), e);
        }
    }


    @Override
    public void ajouter(Groupe groupe) throws SQLException {

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM groupe WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        System.out.println("Groupe supprimé");
    }


    public int countGroupes() throws SQLException {
        // Déclaration des variables
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int count = 0;

        try {
            // Requête SQL pour compter le nombre de groupes
            String query = "SELECT COUNT(*) FROM groupe";

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
