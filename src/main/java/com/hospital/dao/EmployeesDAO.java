package main.java.com.hospital.dao;

import main.java.com.hospital.model.Employees;
import main.java.com.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeesDAO {

    // Vérifier login
    public Employees checkLogin(String email, String password) {
        String sql = "SELECT * FROM employees WHERE email=? AND password_hash=?";
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
                    user.setPhone(rs.getString("phone"));
                    user.setHireDate(rs.getDate("hire_date"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lister tous les employés
    public List<Employees> getAllEmployees() {
        List<Employees> list = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employees e = new Employees();
                e.setId(rs.getInt("id"));
                e.setEmail(rs.getString("email"));
                e.setFirstName(rs.getString("first_name"));
                e.setLastName(rs.getString("last_name"));
                e.setRole(rs.getString("role"));
                e.setDepartment(rs.getString("department"));
                e.setPhone(rs.getString("phone"));
                e.setHireDate(rs.getDate("hire_date"));
                e.setPasswordHash(rs.getString("password_hash"));
                list.add(e);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Ajouter un employé
    public void addEmployee(Employees e) {
        String sql = "INSERT INTO employees (email, password_hash, first_name, last_name, role, department, phone, hire_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, e.getEmail());
            stmt.setString(2, e.getPasswordHash());
            stmt.setString(3, e.getFirstName());
            stmt.setString(4, e.getLastName());
            stmt.setString(5, e.getRole());
            stmt.setString(6, e.getDepartment());
            stmt.setString(7, e.getPhone());
            stmt.setDate(8, e.getHireDate());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    e.setId(rs.getInt(1));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Modifier un employé
    public void updateEmployee(Employees e) {
        String sql = "UPDATE employees SET email=?, password_hash=?, first_name=?, last_name=?, role=?, department=?, phone=?, hire_date=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getEmail());
            stmt.setString(2, e.getPasswordHash());
            stmt.setString(3, e.getFirstName());
            stmt.setString(4, e.getLastName());
            stmt.setString(5, e.getRole());
            stmt.setString(6, e.getDepartment());
            stmt.setString(7, e.getPhone());
            stmt.setDate(8, e.getHireDate());
            stmt.setInt(9, e.getId());

            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Supprimer un employé
    public void deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
