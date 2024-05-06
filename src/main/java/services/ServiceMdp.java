package services;

import entities.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServiceMdp {
    Connection connection;

    public ServiceMdp(Connection connection) {
        this.connection = connection;
    }
    public void modifier(Utilisateur utilisateur , String email) throws SQLException {
        System.out.println(utilisateur);
        String req = "UPDATE utilisateur SET  mdp = ? WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            // Utilisez le nouveau mot de passe haché
            preparedStatement.setString(1, utilisateur.getMdp());
            preparedStatement.setString(2, email);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("mdp modifié avec succès.");
            } else {
                System.out.println("Aucune modification n'a été effectuée pour cet utilisateur.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'utilisateur : " + e.getMessage());
            throw e;
        }
    }
}
