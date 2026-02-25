package main.java.com.hospital.dao;

import main.java.com.hospital.model.VitalSign;
import main.java.com.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VitalSignDAO {

    public boolean addVitalSign(VitalSign vs) {
        String sql = "INSERT INTO vital_signs (patient_id, nurse_id, temperature, blood_pressure, heart_rate, weight, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, vs.getPatientId());
            pstmt.setInt(2, vs.getNurseId());
            pstmt.setDouble(3, vs.getTemperature());
            pstmt.setString(4, vs.getBloodPressure());
            pstmt.setInt(5, vs.getHeartRate());
            pstmt.setDouble(6, vs.getWeight());
            pstmt.setString(7, vs.getNotes());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL (addVitalSign) : " + e.getMessage());
            return false;
        }
    }

    public List<VitalSign> getAllVitalSigns() {
        List<VitalSign> list = new ArrayList<>();
        String sql = "SELECT * FROM vital_signs ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                VitalSign vs = new VitalSign();
                vs.setId(rs.getInt("id"));
                vs.setPatientId(rs.getInt("patient_id"));
                vs.setNurseId(rs.getInt("nurse_id"));
                vs.setTemperature(rs.getDouble("temperature"));
                vs.setBloodPressure(rs.getString("blood_pressure"));
                vs.setHeartRate(rs.getInt("heart_rate"));
                vs.setWeight(rs.getDouble("weight"));
                vs.setNotes(rs.getString("notes"));
                vs.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(vs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL (getAllVitalSigns) : " + e.getMessage());
        }
        return list;
    }

    public boolean deleteVitalSign(int id) {
        String sql = "DELETE FROM vital_signs WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur SQL (deleteVitalSign) : " + e.getMessage());
            return false;
        }
    }
}