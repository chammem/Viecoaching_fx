package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class MyDatabase {
        private static MyDatabase instance;
        private Connection connection;

        private MyDatabase() {
            // Initialisation de la connexion à la base de données
            try {
                String URL="jdbc:mysql://localhost:3306/viecoaching";
                String USERNAME="root";
                String PASSWORD="";
                this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la connexion à la base de données: " + e.getMessage());
                e.printStackTrace();
            }
        }

        public static MyDatabase getInstance() {
            if (instance == null) {
                instance = new MyDatabase();
            }
            return instance;
        }

        public Connection getConnection() {
            return connection;
        }
    }