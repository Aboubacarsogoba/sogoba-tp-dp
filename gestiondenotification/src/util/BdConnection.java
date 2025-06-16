package util;

import java.sql.*;

public class BdConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/entreprise?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Remplacez par le mot de passe correct

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//            System.out.println("Connexion à la base de données établie avec succès.");
            return conn;
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
            throw e;
        }
    }
}
