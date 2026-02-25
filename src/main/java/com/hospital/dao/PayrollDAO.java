package main.java.com.hospital.dao;

import main.java.com.hospital.model.Payroll;
import main.java.com.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAO {

    public List<Payroll> getAllPayrolls() {
        List<Payroll> list = new ArrayList<>();
        String sql = "SELECT * FROM payroll ORDER BY payment_date DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Payroll p = new Payroll();
                p.setId(rs.getInt("id"));
                p.setEmployeeId(rs.getInt("employee_id"));
                p.setMonth(rs.getString("month"));
                p.setYear(rs.getInt("year"));
                p.setBaseSalary(rs.getDouble("base_salary"));
                p.setBonus(rs.getDouble("bonus"));
                p.setDeductions(rs.getDouble("deductions"));
                p.setNetSalary(rs.getDouble("net_salary"));
                p.setPaymentDate(rs.getDate("payment_date"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL (getAllPayrolls) : " + e.getMessage());
        }
        return list;
    }

    public boolean addPayroll(Payroll p) {
        String sql = "INSERT INTO payroll (employee_id, month, year, base_salary, bonus, deductions, net_salary, payment_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, p.getEmployeeId());
            pstmt.setString(2, p.getMonth());
            pstmt.setInt(3, p.getYear());
            pstmt.setDouble(4, p.getBaseSalary());
            pstmt.setDouble(5, p.getBonus());
            pstmt.setDouble(6, p.getDeductions());
            pstmt.setDouble(7, p.getNetSalary());
            pstmt.setDate(8, p.getPaymentDate());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL (addPayroll) : " + e.getMessage());
            return false;
        }
    }

    public boolean deletePayroll(int id) {
        String sql = "DELETE FROM payroll WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur SQL (deletePayroll) : " + e.getMessage());
            return false;
        }
    }
}