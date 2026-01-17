package main.java.com.hospital.gui.panels;

import main.java.com.hospital.model.Employees;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class  TechnicianDashboard extends JPanel{
        
    
    public TechnicianDashboard(Employees user){
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252)); // Gris très clair
        setBorder(new EmptyBorder(30, 40, 30, 40));
    

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        JLabel welcome = new JLabel("Bonjour, " + user.getFirstName() + " " + user.getLastName());
        welcome.setFont(new Font("SansSerif", Font.BOLD, 28));
        JLabel role = new JLabel(user.getRole() + " - Tableau de bord");
        role.setForeground(Color.GRAY);
        header.add(welcome);
        header.add(role);
        add(header, BorderLayout.NORTH);

}

}