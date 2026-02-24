package main.java.com.hospital.dao;

import main.java.com.hospital.model.Patient;
import main.java.com.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // 1. Tjib ga3 les patients mn MySQL
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY id DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Patient p = new Patient();
                p.setId(rs.getInt("id"));
                p.setFirstName(rs.getString("first_name"));
                p.setLastName(rs.getString("last_name"));
                p.setDateBirth(rs.getDate("date_of_birth"));
                p.setGende(rs.getString("gender"));
                
                // Gestion dyal telephone (Ila kenti derti int f model o Varchar f DB)
                String phoneStr = rs.getString("phone");
                if (phoneStr != null && !phoneStr.isEmpty() && phoneStr.matches("\\d+")) {
                    p.setPhone(Integer.parseInt(phoneStr));
                } else {
                    p.setPhone(0);
                }

                p.setEmail(rs.getString("email"));
                p.setAdress(rs.getString("address"));
                p.setBloodType(rs.getString("blood_type"));
                p.setAllergies(rs.getString("allergies"));
                p.setMedicalNotes(rs.getString("medical_notes"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setUpdatedAt(rs.getTimestamp("updated_at"));

                patients.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL (getAllPatients) : " + e.getMessage());
        }
        return patients;
    }

    // 2. Tzid patient jdid f la base de données
    public boolean addPatient(Patient p) {
        String sql = "INSERT INTO patients (first_name, last_name, date_of_birth, gender, phone, email, address, blood_type, allergies, medical_notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getFirstName());
            pstmt.setString(2, p.getLastName());
            pstmt.setDate(3, p.getDateBirth());
            pstmt.setString(4, p.getGende());
            pstmt.setString(5, String.valueOf(p.getPhone())); // N7ewlo int l String l DB
            pstmt.setString(6, p.getEmail());
            pstmt.setString(7, p.getAdress());
            pstmt.setString(8, p.getBloodType());
            pstmt.setString(9, p.getAllergies());
            pstmt.setString(10, p.getMedicalNotes());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur SQL (addPatient) : " + e.getMessage());
            return false;
        }
    }

    // 3. Modifier les informations dyal wa7ed l-patient
    public boolean updatePatient(Patient p) {
        String sql = "UPDATE patients SET first_name=?, last_name=?, date_of_birth=?, gender=?, phone=?, email=?, address=?, blood_type=?, allergies=?, medical_notes=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getFirstName());
            pstmt.setString(2, p.getLastName());
            pstmt.setDate(3, p.getDateBirth());
            pstmt.setString(4, p.getGende());
            pstmt.setString(5, String.valueOf(p.getPhone()));
            pstmt.setString(6, p.getEmail());
            pstmt.setString(7, p.getAdress());
            pstmt.setString(8, p.getBloodType());
            pstmt.setString(9, p.getAllergies());
            pstmt.setString(10, p.getMedicalNotes());
            pstmt.setInt(11, p.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur SQL (updatePatient) : " + e.getMessage());
            return false;
        }
    }

    // 4. Supprimer un patient
    public boolean deletePatient(int id) {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur SQL (deletePatient) : " + e.getMessage());
            return false;
        }
    }
}