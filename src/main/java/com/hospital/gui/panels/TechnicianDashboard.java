package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.EquipementDAO;
import main.java.com.hospital.model.Employees;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;
import java.util.List;

public class TechnicianDashboard extends JPanel {
    private EquipementDAO dao = new EquipementDAO();
    
    public TechnicianDashboard(Employees user) {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE); // Background abyad clean
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- Header (T-ter7ib) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JLabel welcome = new JLabel("🛠️ Espace Technique - Bienvenue, " + user.getFirstName());
        welcome.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcome.setForeground(new Color(30, 41, 59));
        
        JLabel subtitle = new JLabel("Surveillance de l'état des équipements en temps réel");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        
        header.add(welcome, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // --- Stats Section ---
        Map<String, Integer> stats = dao.getEquipmentStats();
        int okCount = stats.getOrDefault("Fonctionnel", 0);
        int alertCount = stats.getOrDefault("Hors service", 0) + stats.getOrDefault("En maintenance", 0);

        JPanel mainContent = new JPanel(new BorderLayout(0, 30));
        mainContent.setOpaque(false);

        JPanel topCards = new JPanel(new GridLayout(1, 2, 20, 0));
        topCards.setOpaque(false);

        topCards.add(createKpiCard("Équipements opérationnels", String.valueOf(okCount), new Color(22, 163, 74), "✔"));
        topCards.add(createKpiCard("Alertes Maintenance / HS", String.valueOf(alertCount), new Color(220, 38, 38), "⚠"));

        mainContent.add(topCards, BorderLayout.NORTH);
        mainContent.add(createRecentActivitySection(), BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createKpiCard(String title, String value, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(0, 120));
        
        // Border r9iq b-style modern
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(20, 25, 20, 25)
        ));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setForeground(new Color(100, 116, 139));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblValue.setForeground(new Color(30, 41, 59));

        textPanel.add(lblTitle);
        textPanel.add(lblValue);

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("SansSerif", Font.BOLD, 40));
        lblIcon.setForeground(color);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(lblIcon, BorderLayout.EAST);

        return card;
    }

    private JPanel createRecentActivitySection() {
        JPanel container = new JPanel(new BorderLayout(0, 15));
        container.setOpaque(false);

        JLabel title = new JLabel("📋 Activité récente sur le parc matériel");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(30, 41, 59));
        container.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        
        // Container dyal l'activité
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setBackground(Color.WHITE);
        listWrapper.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true));
        
        listPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        List<String> activities = dao.getRecentActivity(); 
        
        if (activities.isEmpty()) {
            JLabel empty = new JLabel("Aucun mouvement récent enregistré.");
            empty.setBorder(new EmptyBorder(20, 10, 20, 10));
            empty.setForeground(Color.GRAY);
            listPanel.add(empty);
        } else {
            for (int i = 0; i < activities.size(); i++) {
                JPanel row = new JPanel(new BorderLayout(10, 0));
                row.setOpaque(false);
                row.setBorder(new EmptyBorder(15, 5, 15, 5));

                JLabel bullet = new JLabel("•");
                bullet.setFont(new Font("SansSerif", Font.BOLD, 20));
                bullet.setForeground(new Color(37, 99, 235)); // Blue bullet

                JLabel text = new JLabel(activities.get(i));
                text.setFont(new Font("SansSerif", Font.PLAIN, 15));
                text.setForeground(new Color(51, 65, 85));

                row.add(bullet, BorderLayout.WEST);
                row.add(text, BorderLayout.CENTER);
                
                listPanel.add(row);
                
                if (i < activities.size() - 1) {
                    JSeparator sep = new JSeparator();
                    sep.setForeground(new Color(241, 245, 249));
                    listPanel.add(sep);
                }
            }
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null); 
        listWrapper.add(scroll, BorderLayout.CENTER);
        
        container.add(listWrapper, BorderLayout.CENTER);
        return container;
    }
}