package main.java.com.hospital.gui;

import main.java.com.hospital.gui.panels.EmployeesPanel;
import main.java.com.hospital.gui.panels.HomePanel;
import main.java.com.hospital.gui.panels.TechnicianDashboard;
import main.java.com.hospital.gui.panels.EquipmentPanel;
import main.java.com.hospital.gui.panels.PatientsPanel; 
import main.java.com.hospital.gui.panels.ConsultationsPanel; // ✨ ZEDNA CONSULTATIONS HNA
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

        setTitle("Dashboard Hospitalier - " + user.getFirstName() + " (" + user.getRole() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 750); // Keberna chwiya l-fenêtre bach yban l-jadwal mzyan
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        // 1. Ajouter tous les panels au CardLayout
        mainContentPanel.add(new HomePanel(user), "HOME");
        mainContentPanel.add(new EmployeesPanel(), "EMPLOYES");
        mainContentPanel.add(new TechnicianDashboard(user), "TECHNICIEN");
        mainContentPanel.add(new EquipmentPanel(), "EQUIPEMENTS");
        mainContentPanel.add(new PatientsPanel(), "PATIENTS"); 
        mainContentPanel.add(new ConsultationsPanel(), "CONSULTATIONS"); // ✨ ZEDNA L-PANEL HNA

        // 2. Ajouter la sidebar et le contenu principal
        add(createSidebar(user), BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
        
        // 3. Affichage par défaut selon le rôle
        String role = user.getRole();
        if ("Technicien".equalsIgnoreCase(role)) {
            cardLayout.show(mainContentPanel, "TECHNICIEN");
        } else if ("Médecin".equalsIgnoreCase(role)) {
            cardLayout.show(mainContentPanel, "PATIENTS"); // Le médecin voit les patients par défaut
        } else {
            cardLayout.show(mainContentPanel, "HOME");
        }
    }

    private JPanel createSidebar(Employees user) {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(230, 0));
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

        boolean isTech = "Technicien".equalsIgnoreCase(user.getRole());
        boolean isMed = "Médecin".equalsIgnoreCase(user.getRole());
        boolean isDefault = !isTech && !isMed; 

        // 1. Bouton Dashboard (Home)
        JButton homeBtn = createSidebarButton("🏠 Dashboard", isDefault);
        homeBtn.addActionListener(e -> {
            if ("Technicien".equalsIgnoreCase(user.getRole())) {
                cardLayout.show(mainContentPanel, "TECHNICIEN");
            } else {
                cardLayout.show(mainContentPanel, "HOME");
            }
            updateMenuStyles(homeBtn);
        });
        topPanel.add(homeBtn);
        topPanel.add(Box.createVerticalStrut(10));

        // 2. Boutons Médecin & Admin (Patients + Consultations)
        if ("Médecin".equalsIgnoreCase(user.getRole()) || "Admin".equalsIgnoreCase(user.getRole())) {
            
            // Bouton Patients
            JButton patientsBtn = createSidebarButton("🩺 Patients", isMed);
            patientsBtn.addActionListener(e -> {
                cardLayout.show(mainContentPanel, "PATIENTS");
                updateMenuStyles(patientsBtn);
            });
            topPanel.add(patientsBtn);
            topPanel.add(Box.createVerticalStrut(10));

            // ✨ NOUVEAU: Bouton Consultations
            JButton consBtn = createSidebarButton("📋 Consultations", false);
            consBtn.addActionListener(e -> {
                cardLayout.show(mainContentPanel, "CONSULTATIONS");
                updateMenuStyles(consBtn);
            });
            topPanel.add(consBtn);
            topPanel.add(Box.createVerticalStrut(10));
        }

        // 3. Bouton Employés (RH & Admin)
        if ("RH".equalsIgnoreCase(user.getRole()) || "Admin".equalsIgnoreCase(user.getRole())) {
            JButton empBtn = createSidebarButton("👥 Employés", false);
            empBtn.addActionListener(e -> {
                cardLayout.show(mainContentPanel, "EMPLOYES");
                updateMenuStyles(empBtn);
            });
            topPanel.add(empBtn);
            topPanel.add(Box.createVerticalStrut(10));
        }

        // 4. Bouton Équipements (Technicien & Admin)
        if ("Technicien".equalsIgnoreCase(user.getRole()) || "Admin".equalsIgnoreCase(user.getRole())) {
            JButton eqButton = createSidebarButton("🔧 Équipements", isTech);
            eqButton.addActionListener(e -> {
                cardLayout.show(mainContentPanel, "EQUIPEMENTS");
                updateMenuStyles(eqButton);
            });
            topPanel.add(eqButton);
            topPanel.add(Box.createVerticalStrut(10));
        }

        sidebar.add(topPanel, BorderLayout.NORTH);

        // --- SECTION BAS (Profil) ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);

        JLabel nameLbl = new JLabel(user.getFirstName() + " " + user.getLastName());
        nameLbl.setForeground(Color.WHITE);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel roleLbl = new JLabel(user.getRole()); 
        roleLbl.setForeground(new Color(150, 150, 150));
        roleLbl.setFont(new Font("SansSerif", Font.ITALIC, 12));
        roleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutBtn = new JButton("↪ Déconnexion");
        logoutBtn.setForeground(new Color(255, 100, 100)); 
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.addActionListener(e -> {
            new LoginWindow().setVisible(true);
            this.dispose(); // Kat-sed l-fenêtre dyal Dashboard
        });

        bottomPanel.add(Box.createVerticalGlue());
        bottomPanel.add(nameLbl);
        bottomPanel.add(roleLbl); 
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
        
        btn.setHorizontalAlignment(SwingConstants.LEFT); 
        btn.setMargin(new Insets(0, 20, 0, 0)); 

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
            btn.setFont(new Font("SansSerif", Font.BOLD, 15));
            btn.setBackground(new Color(51, 65, 85)); 
            btn.setOpaque(true);
        } else {
            btn.setForeground(new Color(180, 180, 180));
            btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
            btn.setBackground(new Color(30, 41, 59)); 
            btn.setOpaque(false);
        }
    }
}