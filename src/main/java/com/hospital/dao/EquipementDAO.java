package main.java.com.hospital.dao;

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
         
                String sql = "SELECT name, status, updated_at FROM equipment ORDER BY updated_at DESC LIMIT 3";
                try (Connection conn = DBConnection.getConnection();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {
                  while (rs.next()) {
                    activities.add(rs.getString("name") + " mis à jour (" + rs.getString("status") + ")");
                 }
                } catch (SQLException e) { e.printStackTrace(); }
                return activities;
               }


                    
                
            }

            
        
    
          
         





       
 