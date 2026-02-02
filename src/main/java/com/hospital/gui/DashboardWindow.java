package main.java.com.hospital.gui;

import main.java.com.hospital.gui.panels.EmployeesPanel;
import main.java.com.hospital.gui.panels.HomePanel;
import main.java.com.hospital.gui.panels.TechnicianDashboard;
import main.java.com.hospital.model.Employees;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private List<JButton> menuButtons = new ArrayList<>();

    public DashboardWindow(Employees user) {
        if (user == null) {
            System.err.println("Utilisateur non identifié !");
            return;
        }

        setTitle("Dashboard - " + user.getFirstName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        mainContentPanel.add(new HomePanel(user), "HOME");
        mainContentPanel.add(new EmployeesPanel(), "EMPLOYES");
        mainContentPanel.add(new TechnicianDashboard(user), "TECHNICIEN");

        add(createSidebar(user), BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
        
        if("Technicien".equalsIgnoreCase(user.getRole())){
            cardLayout.show(mainContentPanel, "TECHNICIEN");
        }else{
        
        cardLayout.show(mainContentPanel, "HOME");
        }
    }

    private JPanel createSidebar(Employees user) {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(30, 41, 59));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);

        JLabel logo = new JLabel("Hôpital Central");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 20));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(Box.createVerticalStrut(20));
        topPanel.add(logo);
        topPanel.add(Box.createVerticalStrut(30));

        JButton homeBtn = createSidebarButton("🏠 Dashboard", true);
        homeBtn.addActionListener(e -> {
            cardLayout.show(mainContentPanel, "HOME");
            updateMenuStyles(homeBtn);
        });
        topPanel.add(homeBtn);
        topPanel.add(Box.createVerticalStrut(10));

        if ("RH".equalsIgnoreCase(user.getRole()) || "Admin".equalsIgnoreCase(user.getRole())) {
            JButton empBtn = createSidebarButton("👥 Employés", false);
            empBtn.addActionListener(e -> {
                cardLayout.show(mainContentPanel, "EMPLOYES");
                updateMenuStyles(empBtn);
            });
            topPanel.add(empBtn);

        }
        if("Technicien".equalsIgnoreCase(user.getRole())){
            JButton eqButton =  createSidebarButton("Equipement",false);
            

        }

        sidebar.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);

        JLabel nameLbl = new JLabel(user.getFirstName() + " " + user.getLastName());
        nameLbl.setForeground(Color.WHITE);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutBtn = new JButton("↪ Déconnexion");
        logoutBtn.setForeground(Color.RED);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.addActionListener(e -> {
            new LoginWindow().setVisible(true);
            this.dispose();
        });

        bottomPanel.add(Box.createVerticalGlue());
        bottomPanel.add(nameLbl);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(logoutBtn);
        bottomPanel.add(Box.createVerticalStrut(20));

        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createSidebarButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        applyStyle(btn, active);
        menuButtons.add(btn);

        return btn;
    }

    private void updateMenuStyles(JButton activeBtn) {
        for (JButton btn : menuButtons) {
            applyStyle(btn, btn == activeBtn);
        }
    }

    private void applyStyle(JButton btn, boolean active) {
        if (active) {
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        } else {
            btn.setForeground(new Color(180, 180, 180));
            btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        }
        btn.setBackground(new Color(30, 41, 59));
    }
}
