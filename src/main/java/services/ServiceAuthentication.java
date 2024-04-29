package services;

import entities.Utilisateur;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.mindrot.jbcrypt.BCrypt;
import utils.MyDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ServiceAuthentication {
    private final Connection con;

    public ServiceAuthentication() {
        this.con = MyDatabase.getInstance().getConnection();
    }

    public Utilisateur login(String email, String password) throws SQLException {
        if (email.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Tous les champs obligatoires doivent être remplis.");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Adresse email invalide.");
        }

        Utilisateur utilisateur = findUserByEmail(email);
        if (utilisateur != null && verifyPassword(password, utilisateur.getMdp())) {
            return utilisateur;
        }

        return null; // Retourner null si l'utilisateur n'est pas trouvé ou le mot de passe est incorrect
    }

    private Utilisateur findUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM utilisateur WHERE email=?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setString(1, email);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("id"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setEmail(rs.getString("email"));
                    utilisateur.setMdp(rs.getString("mdp"));
                    utilisateur.setVille(rs.getString("ville"));
                    utilisateur.setTel(rs.getString("tel"));
                    utilisateur.setGenre(rs.getString("genre"));
                    utilisateur.setImage(rs.getString("image"));
                    return utilisateur;
                }
            }
        }
        return null;
    }

    public boolean verifyPassword(String password, String hashedPassword) {
        // Vérifie le mot de passe en utilisant la même version de BCrypt que celle utilisée pour le hachage
        if (hashedPassword.startsWith("$2y$")) {
            hashedPassword = "$2a$" + hashedPassword.substring(4);
        }
        return BCrypt.checkpw(password, hashedPassword);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}