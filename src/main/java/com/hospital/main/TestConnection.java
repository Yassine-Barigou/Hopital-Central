package main.java.com.hospital.main; 

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.SwingUtilities;

import main.java.com.hospital.gui.LoginWindow;
import main.java.com.hospital.util.DBConnection;

public class TestConnection {

    public static void main(String[] args) {
        
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("SUCCÈS : La connexion est établie avec 'hospital_management' !");
                System.out.println("Version du serveur : " + conn.getMetaData().getDatabaseProductVersion());

               
                SwingUtilities.invokeLater(() -> {
                    LoginWindow window = new LoginWindow();
                    window.setVisible(true); 
                });
            }
        } catch (SQLException e) {
            System.err.println(" ÉCHEC : Impossible de se connecter à la base de données.");
            System.err.println("Détail de l'erreur : " + e.getMessage());
            e.printStackTrace();
            
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Erreur de connexion à la base de données :\n" + e.getMessage(), 
                "Erreur Fatale", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
}