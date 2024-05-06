package services;
import entities.Utilisateur;
import utils.MyDatabase;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceProfil {
    Connection connection;

    public ServiceProfil(Connection connection) {
        this.connection = connection;
    }
    public void modifier(Utilisateur utilisateur , String email) throws SQLException {
        System.out.println(utilisateur);
        String req = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ?, mdp = ?, ville = ?, tel = ?," +
                " genre = ?, image = ? WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, utilisateur.getNom());
            preparedStatement.setString(2, utilisateur.getPrenom());
            preparedStatement.setString(3, utilisateur.getEmail());
            preparedStatement.setString(4, utilisateur.getMdp());
            preparedStatement.setString(5, utilisateur.getVille());
            preparedStatement.setString(6, utilisateur.getTel());
            preparedStatement.setString(7, utilisateur.getGenre());
            preparedStatement.setString(8, utilisateur.getImage());
            preparedStatement.setString(9, email);

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
    public void modifierImage(Utilisateur utilisateur, InputStream nouvelleImageStream, String email) throws SQLException {
        String req = "UPDATE utilisateur SET image = ? WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setBlob(1, nouvelleImageStream);
            preparedStatement.setString(2, email);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Image de l'utilisateur modifiée avec succès.");
            } else {
                System.out.println("Aucune modification n'a été effectuée pour l'image de cet utilisateur.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'image de l'utilisateur : " + e.getMessage());
            throw e;
        }
    }
}

