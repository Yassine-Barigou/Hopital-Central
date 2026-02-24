package main.java.com.hospital.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection{
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_management?characterEncoding=utf8";
    private static final String LOGIN = "root";
    private static final String MOTDEPASSE = "Admin123!";
    
    public static Connection getConnection() throws SQLException{
        try{
            //we load the driver
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(URL, LOGIN, MOTDEPASSE);

        }catch(ClassNotFoundException e){
            throw new SQLException("Le Driver MySQL est introuvable dans le projet.", e);
        }


    }

}
