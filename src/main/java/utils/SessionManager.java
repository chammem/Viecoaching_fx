package utils;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import entities.Utilisateur;

public class SessionManager {
    private static Utilisateur utilisateurConnecte;
    private static LocalDateTime lastLogin;

    // Méthode pour démarrer une nouvelle session après la connexion de l'utilisateur
    public static void startSession(Utilisateur utilisateur) {
        utilisateurConnecte = utilisateur;
        lastLogin = LocalDateTime.now();
    }

    // Méthode pour vérifier si une session est active
    public static boolean isSessionActive() {
        if (utilisateurConnecte == null) {
            return false;
        }
        long daysSinceLastLogin = ChronoUnit.DAYS.between(lastLogin, LocalDateTime.now());
        if (daysSinceLastLogin >= 3) {
            endSession();
            return false;
        }
        return true;
    }

    // Méthode pour récupérer l'utilisateur connecté
    public static Utilisateur getUtilisateurConnecte() {
        if (isSessionActive()) {
            Utilisateur utilisateur = utilisateurConnecte;
            System.out.println("Utilisateur connecté : " + utilisateur);
            return utilisateur;
        }
        return null;
    }

    // Méthode pour mettre fin à la session
    public static void endSession() {
        utilisateurConnecte = null;
        lastLogin = null;
        System.out.println("Utilisateur déconnecté : " + (utilisateurConnecte == null));

    }
}