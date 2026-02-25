package main.java.com.hospital.dao;

import main.java.com.hospital.model.Leave;
import main.java.com.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaveDAO {

    
    public List<Leave> getAllLeaves() {
        List<Leave> list = new ArrayList<>();
        String sql = "SELECT * FROM leaves ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Leave l = new Leave();
                l.setId(rs.getInt("id"));
                l.setEmployeeId(rs.getInt("employee_id"));
                l.setStartDate(rs.getDate("start_date"));
                l.setEndDate(rs.getDate("end_date"));
                l.setType(rs.getString("type"));
                l.setStatus(rs.getString("status"));
                l.setReason(rs.getString("reason"));
                l.setCreatedAt(rs.getTimestamp("created_at"));
                
                list.add(l);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL (getAllLeaves) : " + e.getMessage());
        }
        return list;
    }

    // 2. Ajouter une demande de congé
    public boolean addLeave(Leave l) {
        String sql = "INSERT INTO leaves (employee_id, start_date, end_date, type, status, reason) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, l.getEmployeeId());
            pstmt.setDate(2, l.getStartDate());
            pstmt.setDate(3, l.getEndDate());
            pstmt.setString(4, l.getType());
            pstmt.setString(5, "En attente");
            pstmt.setString(6, l.getReason());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL (addLeave) : " + e.getMessage());
            return false;
        }
    }

    public boolean updateLeaveStatus(int leaveId, String newStatus) {
        String sql = "UPDATE leaves SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, leaveId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL (updateLeaveStatus) : " + e.getMessage());
            return false;
        }
    }

    public boolean deleteLeave(int id) {
        String sql = "DELETE FROM leaves WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur SQL (deleteLeave) : " + e.getMessage());
            return false;
        }
    }
}