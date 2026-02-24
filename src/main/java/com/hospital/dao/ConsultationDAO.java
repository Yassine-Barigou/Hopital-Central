package main.java.com.hospital.dao;

import main.java.com.hospital.model.Consultation;
import main.java.com.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultationDAO {

    public List<Consultation> getAllConsultations() {
        List<Consultation> list = new ArrayList<>();
        String sql = "SELECT * FROM consultations ORDER BY date DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Consultation c = new Consultation();
                c.setId(rs.getInt("id"));
                c.setPatientId(rs.getInt("patient_id"));
                c.setDoctorId(rs.getInt("doctor_id"));
                c.setDate(rs.getTimestamp("date"));
                c.setReason(rs.getString("reason"));
                c.setDiagnosis(rs.getString("diagnosis"));
                c.setPrescription(rs.getString("prescription"));
                c.setNotes(rs.getString("notes"));
                c.setStatus(rs.getString("status"));
                list.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL (getAllConsultations) : " + e.getMessage());
        }
        return list;
    }

    public boolean addConsultation(Consultation c) {
        String sql = "INSERT INTO consultations (patient_id, doctor_id, date, reason, diagnosis, prescription, notes, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, c.getPatientId());
            pstmt.setInt(2, c.getDoctorId());
            // ✅ Fix appliqué ici
            pstmt.setTimestamp(3, c.getDate());
            pstmt.setString(4, c.getReason());
            pstmt.setString(5, c.getDiagnosis());
            pstmt.setString(6, c.getPrescription());
            pstmt.setString(7, c.getNotes());
            pstmt.setString(8, c.getStatus());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL (addConsultation) : " + e.getMessage());
            return false;
        }
    }

    public boolean updateConsultation(Consultation c) {
        String sql = "UPDATE consultations SET patient_id=?, doctor_id=?, date=?, reason=?, diagnosis=?, prescription=?, notes=?, status=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, c.getPatientId());
            pstmt.setInt(2, c.getDoctorId());
            // ✅ Fix appliqué ici aussi bach ma y3tikch erreur f modification
            pstmt.setTimestamp(3, new java.sql.Timestamp(c.getDate().getTime()));
            pstmt.setString(4, c.getReason());
            pstmt.setString(5, c.getDiagnosis());
            pstmt.setString(6, c.getPrescription());
            pstmt.setString(7, c.getNotes());
            pstmt.setString(8, c.getStatus());
            pstmt.setInt(9, c.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL (updateConsultation) : " + e.getMessage());
            return false;
        }
    }

    public boolean deleteConsultation(int id) {
        String sql = "DELETE FROM consultations WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL (deleteConsultation) : " + e.getMessage());
            return false;
        }
    }
}