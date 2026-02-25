package main.java.com.hospital.dao;

import main.java.com.hospital.model.Appointment;
import main.java.com.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY date ASC"; 

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setId(rs.getInt("id"));
                a.setPatientId(rs.getInt("patient_id"));
                a.setDoctorId(rs.getInt("doctor_id"));
                a.setDate(rs.getTimestamp("date"));
                a.setDurationMinutes(rs.getInt("duration_minutes"));
                a.setType(rs.getString("type"));
                a.setStatus(rs.getString("status"));
                a.setNotes(rs.getString("notes"));
                list.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL (getAllAppointments) : " + e.getMessage());
        }
        return list;
    }

    public boolean addAppointment(Appointment a) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, date, duration_minutes, type, status, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, a.getPatientId());
            pstmt.setInt(2, a.getDoctorId());
            pstmt.setTimestamp(3, a.getDate());
            pstmt.setInt(4, a.getDurationMinutes());
            pstmt.setString(5, a.getType());
            pstmt.setString(6, a.getStatus());
            pstmt.setString(7, a.getNotes());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL (addAppointment) : " + e.getMessage());
            return false;
        }
    }

    public boolean updateAppointment(Appointment a) {
        String sql = "UPDATE appointments SET patient_id=?, doctor_id=?, date=?, duration_minutes=?, type=?, status=?, notes=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, a.getPatientId());
            pstmt.setInt(2, a.getDoctorId());
            pstmt.setTimestamp(3, a.getDate());
            pstmt.setInt(4, a.getDurationMinutes());
            pstmt.setString(5, a.getType());
            pstmt.setString(6, a.getStatus());
            pstmt.setString(7, a.getNotes());
            pstmt.setInt(8, a.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL (updateAppointment) : " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAppointment(int id) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL (deleteAppointment) : " + e.getMessage());
            return false;
        }
    }
}