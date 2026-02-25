package main.java.com.hospital.dao;

import main.java.com.hospital.model.Triage;
import main.java.com.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TriageDAO {

    public boolean addTriage(Triage t) {
        String sql = "INSERT INTO triage (patient_id, nurse_id, priority, symptoms, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, t.getPatientId());
            pstmt.setInt(2, t.getNurseId());
            pstmt.setString(3, t.getPriority());
            pstmt.setString(4, t.getSymptoms());
            pstmt.setString(5, "En attente"); 

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL (addTriage) : " + e.getMessage());
            return false;
        }
    }

    public List<Triage> getPendingTriages() {
        List<Triage> list = new ArrayList<>();
        String sql = "SELECT * FROM triage WHERE status = 'En attente' ORDER BY " +
                     "FIELD(priority, 'Rouge', 'Orange', 'Jaune', 'Vert'), created_at ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Triage t = new Triage();
                t.setId(rs.getInt("id"));
                t.setPatientId(rs.getInt("patient_id"));
                t.setNurseId(rs.getInt("nurse_id"));
                t.setPriority(rs.getString("priority"));
                t.setSymptoms(rs.getString("symptoms"));
                t.setStatus(rs.getString("status"));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL (getPendingTriages) : " + e.getMessage());
        }
        return list;
    }

    public boolean markAsTreated(int id) {
        String sql = "UPDATE triage SET status = 'Traité' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur SQL (markAsTreated) : " + e.getMessage());
            return false;
        }
    }
}