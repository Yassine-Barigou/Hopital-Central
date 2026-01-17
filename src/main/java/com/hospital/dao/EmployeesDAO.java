package main.java.com.hospital.dao;

import main.java.com.hospital.model.Employees;
import main.java.com.hospital.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployeesDAO {

    /**
     * Vérifie si l'utilisateur existe dans la base de données
     * @return un objet Employees si trouvé, sinon null
     */
    public Employees checkLogin(String email, String password) {
        
        String sql = "SELECT * FROM employees WHERE email = ? AND password_hash = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            
            stmt.setString(1, email);
            stmt.setString(2, password); 
            
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                   
                    Employees user = new Employees();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setRole(rs.getString("role"));
                    user.setDepartment(rs.getString("department"));
                    
                    System.out.println("✅ Login réussi pour : " + user.getFirstName());
                    return user;
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du login : " + e.getMessage());
            e.printStackTrace();
        }
        
        // Si rien n'est trouvé ou erreur
        return null;
    }
}

