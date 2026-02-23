package main.java.com.hospital.dao;

import main.java.com.hospital.model.Equipment;
import main.java.com.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipementDAO {

    public Map<String,Integer> getEquipmentStats(){
        Map<String ,Integer> stats = new HashMap<>();
        String sql ="select status , count(*) as total from equipment GROUP BY status";

        try(Connection conn =  DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(sql)){


                while (res.next()){
                    //exemple  stats.put("Fonctionnel", 3)
                    stats.put(res.getString("status"), res.getInt("total"));
                }

                }catch (SQLException e){
                    e.printStackTrace();
                }
                return stats;
            }
    
            public List<String> getRecentActivity() {
                List<String> activities = new ArrayList<>();
                
                String sql = "SELECT name, status, updated_at FROM equipment ORDER BY updated_at DESC LIMIT 7";
                try (Connection conn = DBConnection.getConnection();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    activities.add(rs.getString("name") + " mis à jour (" + rs.getString("status") + ")");
                }
                } catch (SQLException e) { e.printStackTrace(); }
                return activities;
                }
                
            

            public List<Equipment> getAllEquipements(){
                List<Equipment> equipmentList = new ArrayList<>();

                String req = "SELECT * FROM equipment ORDER BY created_at DESC";

                try(Connection conn = DBConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(req);
                    ResultSet rs=stmt.executeQuery()){
                        while(rs.next()){
                            Equipment eq = new Equipment();
                            eq.setId(rs.getInt("id"));
                            eq.setName(rs.getString("name"));
                            eq.setType(rs.getString("type"));
                            eq.setSerial_number(rs.getString("serial_number"));
                            eq.setLocation(rs.getString("location"));
                            eq.setStatus(rs.getString("status"));
                            eq.setPurchase_date(rs.getDate("purchase_date"));
                            eq.setLast_maintenance_date(rs.getDate("last_maintenance_date"));
                            eq.setNext_maintenance_date(rs.getDate("next_maintenance_date"));
                            eq.setNotes(rs.getString("notes"));
                
                            equipmentList.add(eq);
                        }

                        }catch(SQLException e ){
                            System.err.println("Error : "+e.getMessage());
                            e.printStackTrace();
                        }
                        return equipmentList;

                    }
            
            public int getTotalEquipmentCount() {
                String req = "SELECT COUNT(*) FROM equipment";
            try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(req);
                ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException e) {
                System.err.println("Error counting total equipment: " + e.getMessage());
                e.printStackTrace();
            }
            return 0;
        }


        public int getEquipmentCountByStatus(String status) {
        String req = "SELECT COUNT(*) FROM equipment WHERE status = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(req)) {
            
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting equipment by status: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
        }


        public List<Equipment> getEquipementsByStatus(String status) {
        List<Equipment> equipmentList = new ArrayList<>();
        
        String req = "SELECT * FROM equipment WHERE status = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(req)) {
            
            stmt.setString(1, status); // On injecte 'Fonctionnel', 'En maintenance', etc.
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Equipment eq = new Equipment();
                    eq.setId(rs.getInt("id"));
                    eq.setName(rs.getString("name"));
                    eq.setType(rs.getString("type"));
                    eq.setSerial_number(rs.getString("serial_number"));
                    eq.setLocation(rs.getString("location"));
                    eq.setStatus(rs.getString("status"));
                    eq.setPurchase_date(rs.getDate("purchase_date"));
                    eq.setLast_maintenance_date(rs.getDate("last_maintenance_date"));
                    eq.setNext_maintenance_date(rs.getDate("next_maintenance_date"));
                    eq.setNotes(rs.getString("notes"));
                    
                    equipmentList.add(eq);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error filtering by status: " + e.getMessage());
            e.printStackTrace();
        }
        return equipmentList;
    }

    public List<Equipment> searchEquipements(String keyword) {
        List<Equipment> equipmentList = new ArrayList<>();
        // On cherche le mot-clé dans le nom, le type, ou le numéro de série
        String req = "SELECT * FROM equipment WHERE name LIKE ? OR type LIKE ? OR serial_number LIKE ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(req)) {
            
            // Les % permettent de chercher le mot-clé n'importe où dans le texte
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Equipment eq = new Equipment();
                    eq.setId(rs.getInt("id"));
                    eq.setName(rs.getString("name"));
                    eq.setType(rs.getString("type"));
                    eq.setSerial_number(rs.getString("serial_number"));
                    eq.setLocation(rs.getString("location"));
                    eq.setStatus(rs.getString("status"));
                    eq.setPurchase_date(rs.getDate("purchase_date"));
                    eq.setLast_maintenance_date(rs.getDate("last_maintenance_date"));
                    eq.setNext_maintenance_date(rs.getDate("next_maintenance_date"));
                    eq.setNotes(rs.getString("notes"));
                    
                    equipmentList.add(eq);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching equipment: " + e.getMessage());
            e.printStackTrace();
        }
        return equipmentList;
    }
    public boolean addEquipement(Equipment eq) {
        // La requête SQL d'insertion. On utilise NOW() pour created_at
        String req = "INSERT INTO equipment (name, type, location, status, purchase_date, next_maintenance_date, created_at) VALUES (?, ?, ?, ?, ?, ?, NOW())";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(req)) {
            
            stmt.setString(1, eq.getName());
            stmt.setString(2, eq.getType());
            stmt.setString(3, eq.getLocation()); // location = département
            stmt.setString(4, eq.getStatus());
            stmt.setDate(5, eq.getPurchase_date());
            stmt.setDate(6, eq.getNext_maintenance_date());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retourne true si l'insertion a réussi
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'équipement : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateEquipement(Equipment eq) {
        String req = "UPDATE equipment SET name = ?, type = ?, location = ?, status = ?, purchase_date = ?, next_maintenance_date = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(req)) {
            
            stmt.setString(1, eq.getName());
            stmt.setString(2, eq.getType());
            stmt.setString(3, eq.getLocation());
            stmt.setString(4, eq.getStatus());
            stmt.setDate(5, eq.getPurchase_date());
            stmt.setDate(6, eq.getNext_maintenance_date());
            stmt.setInt(7, eq.getId()); // L'ID caché est crucial ici !
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }




}
                
                

