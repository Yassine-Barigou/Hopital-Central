package main.java.com.hospital.gui.panels;

import main.java.com.hospital.model.Employees;
import main.java.com.hospital.dao.TriageDAO;
import main.java.com.hospital.dao.PatientDAO;
import main.java.com.hospital.dao.VitalSignDAO;

import javax.swing.*;
import java.awt.*;

public class NurseHomePanel extends JPanel {

    private Employees currentUser;

    public NurseHomePanel(Employees user) {
        this.currentUser = user;
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("👋 Bienvenue, Infirmier(e) " + user.getLastName());
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(30, 41, 59));
        
        JLabel subtitle = new JLabel("Voici un résumé de l'activité d'aujourd'hui :");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(Color.GRAY);
        
        headerPanel.add(welcomeLabel, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

       
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        
      
        TriageDAO tDao = new TriageDAO();
        int pendingUrgences = tDao.getPendingTriages().size();
        
        VitalSignDAO vDao = new VitalSignDAO();
        int totalVitals = vDao.getAllVitalSigns().size();
        
        PatientDAO pDao = new PatientDAO();
        int totalPatients = pDao.getAllPatients().size();

        statsPanel.add(createStatCard("🚨 Urgences en attente", String.valueOf(pendingUrgences), new Color(254, 226, 226), new Color(220, 38, 38)));
        statsPanel.add(createStatCard("📉 Constantes Prises", String.valueOf(totalVitals), new Color(219, 234, 254), new Color(37, 99, 235)));
        statsPanel.add(createStatCard("🩺 Total Patients", String.valueOf(totalPatients), new Color(220, 252, 231), new Color(22, 163, 74)));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.add(statsPanel, BorderLayout.NORTH);
        
        JLabel infoLabel = new JLabel("Sélectionnez une option dans le menu de gauche pour commencer.", SwingConstants.CENTER);
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        infoLabel.setForeground(Color.GRAY);
        centerContainer.add(infoLabel, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, Color bgColor, Color fgColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fgColor, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitle.setForeground(fgColor);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 48));
        lblValue.setForeground(fgColor);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        
        return card;
    }
}