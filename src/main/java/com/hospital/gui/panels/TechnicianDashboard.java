package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.EquipementDAO;
import main.java.com.hospital.model.Employees;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

import java.util.List;



    

        


public class  TechnicianDashboard extends JPanel{
    private EquipementDAO dao = new EquipementDAO();
    
    public TechnicianDashboard(Employees user){
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252)); // Gris très clair
        setBorder(new EmptyBorder(30, 30, 30, 30));
        // prendre les nombres du BDD
        Map<String , Integer> stats = dao.getEquipmentStats();
        int  okCount = stats.getOrDefault("Fonctionnel", 0);
        int alertCount = stats.getOrDefault("Hors service", 0)+ stats.getOrDefault("En maintenance", 0);
        
        //ZONE 1 : les CARDS (North)

        JPanel topCards = new JPanel(new GridLayout(1,2,20,0));
        topCards.setOpaque(false);

        topCards.add(createKpiCard("Équipements fonctionnels", String.valueOf(okCount), new Color(34, 197, 94), "✔"));
        topCards.add(createKpiCard("En maintenance / HS", String.valueOf(alertCount), new Color(239, 68, 68), "⚠"));

        add(topCards, BorderLayout.NORTH);

        //zone 2
        add(createRecentActivitySection(), BorderLayout.CENTER);


    }


    private JPanel createKpiCard(String title, String value, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(0, 140));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.GRAY);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 30));
        
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("SansSerif", Font.PLAIN, 26));
        lblIcon.setForeground(color);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        card.add(lblIcon, BorderLayout.EAST);

        return card;
        

        
        
    }
    private JPanel createRecentActivitySection() {
        JPanel container = new JPanel(new BorderLayout(0, 15));
        container.setOpaque(false);

        JLabel title = new JLabel("Activité récente");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        container.add(title, BorderLayout.NORTH);

        //boarder
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        
        List<String> activities = dao.getRecentActivity(); // Kat-jbed mn MySQL
        
        if (activities.isEmpty()) {
            listPanel.add(new JLabel("Aucune activité récente."));
        } else {
            for (String activity : activities) {
                // Créer une ligne pour chaque activité
                JLabel row = new JLabel("<html><b>•</b> " + activity + "</html>");
                row.setBorder(new EmptyBorder(10, 0, 10, 0));
                row.setFont(new Font("SansSerif", Font.PLAIN, 14));
                listPanel.add(row);
                
                // Zid wa7ed l-khet sghir (Separator)
                listPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
        }

        container.add(new JScrollPane(listPanel), BorderLayout.CENTER);
        return container;
    }

}

    
    