package main.java.com.hospital.main; // Keep your package name

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        // --- CONFIGURATION ---
        // Change "test_db" to the name of your database in phpMyAdmin/MySQL
        String url = "jdbc:mysql://localhost:3306/hospital_management?characterEncoding=utf8"; 
        String user = "root"; // Default for XAMPP is "root"
        String password = "Admin123!"; // Default for XAMPP is empty ("")

        System.out.println("⏳ Connecting to database...");

        try {
            // 1. Load Driver
            Class.forName("com.mysql.jdbc.Driver");
            
            // 2. Attempt Connection
            Connection conn = DriverManager.getConnection(url, user, password);
            
            if (conn != null) {
                System.out.println("✅ CONNECTION SUCCESSFUL!");
                System.out.println("You are connected to: " + url);
            }
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Error: MySQL Driver not found in 'lib' folder.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Error: Could not connect to MySQL.");
            System.out.println("1. Is XAMPP/MySQL running?");
            System.out.println("2. Is the database name correct?");
            e.printStackTrace();
        }
    }
}