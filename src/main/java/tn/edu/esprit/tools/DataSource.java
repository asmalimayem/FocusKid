package tn.edu.esprit.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    private static final String URL      = "jdbc:mysql://localhost:3306/tdah"; // ← nom de votre BDD
    private static final String USER     = "root";
    private static final String PASSWORD = "";                                      // ← votre mot de passe

    private static DataSource instance;
    public Connection cnx;

    private DataSource() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connexion MySQL réussie !");
        } catch (SQLException e) {
            System.err.println("❌ Échec connexion MySQL : " + e.getMessage());
            System.err.println("   → Vérifiez que MySQL est démarré (XAMPP/WAMP)");
            System.err.println("   → URL : " + URL);
            cnx = null;
        }
    }

    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }

    // ✅ Méthode principale avec reconnexion automatique
    public Connection getConnection() {
        try {
            if (cnx == null || cnx.isClosed()) {
                System.out.println("🔄 Reconnexion MySQL...");
                cnx = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Reconnexion réussie !");
            }
        } catch (SQLException e) {
            System.err.println("❌ Reconnexion échouée : " + e.getMessage());
            cnx = null;
        }
        return cnx;
    }

    // ✅ Alias pour les classes qui appellent getCnx()
 // ✅ NOUVELLE version — méthode, pas champ
    private Connection getCnx() {
        return DataSource.getInstance().getConnection();
    }
}