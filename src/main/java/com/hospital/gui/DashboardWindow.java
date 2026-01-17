package main.java.com.hospital.gui;

import main.java.com.hospital.gui.panels.TechnicianDashboard;
import main.java.com.hospital.model.Employees;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardWindow extends JFrame {

    public DashboardWindow(Employees user) {
        // 1. Sécurité : Si pas de user, on arrête tout
        if (user == null) {
            System.err.println("Erreur: Utilisateur non identifié.");
            return;
        }

        // 2. CONFIGURATION PLEIN ÉCRAN (FORCE)
        setUndecorated(true); // Supprime la barre Windows (pas de bouton agrandir)
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 3. ROUTING & SÉCURITÉ DU CONTENU
        String role = user.getRole();

        if (role.equalsIgnoreCase("Technicien")) {
            // --- MODE TECHNICIEN ---
            add(createSidebar(user), BorderLayout.WEST);
            add(new TechnicianDashboard(user), BorderLayout.CENTER);
        } 
        else {
            // --- MODE SÉCURITÉ (RH, Admin, etc.) ---
            // On affiche un écran noir/bleu pro pour dire que c'est pas encore prêt
            add(createSidebar(user), BorderLayout.WEST); // Sidebar limitée
            add(createRestrictedAccessPanel(user), BorderLayout.CENTER);
        }
    }

    // --- SIDEBAR INTELLIGENTE (Filtre les boutons) ---
    private JPanel createSidebar(Employees user) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setBackground(new Color(15, 23, 42)); 
        sidebar.setPreferredSize(new Dimension(260, 0));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(40, 20, 20, 20));

        JLabel logo = new JLabel("Hôpital Central");
        logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        logo.setForeground(Color.WHITE);
        topPanel.add(logo);
        topPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        // --- FILTRAGE DES BOUTONS SELON LE RÔLE ---
        topPanel.add(createSidebarButton("📊  Tableau de bord", true));
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // On n'affiche ces boutons QUE si c'est un technicien
        if (user.getRole().equalsIgnoreCase("Technicien")) {
            topPanel.add(createSidebarButton("🔧  Équipements", false));
            topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            topPanel.add(createSidebarButton("📈  Maintenance", false));
        }

        sidebar.add(topPanel, BorderLayout.NORTH);

        // --- PARTIE BASSE ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(20, 20, 30, 20));

        JLabel userName = new JLabel(user.getFirstName() + " " + user.getLastName());
        userName.setForeground(Color.WHITE);
        userName.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JLabel userRole = new JLabel(user.getRole());
        userRole.setForeground(new Color(148, 163, 184));

        JButton logoutBtn = new JButton("↪  Déconnexion");
        logoutBtn.setForeground(new Color(239, 68, 68));
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            new LoginWindow().setVisible(true);
            this.dispose();
        });

        bottomPanel.add(userName);
        bottomPanel.add(userRole);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        bottomPanel.add(logoutBtn);

        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    // --- PANEL D'ERREUR POUR RH / ADMIN ---
    private JPanel createRestrictedAccessPanel(Employees user) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(248, 250, 252));

        JLabel message = new JLabel("<html><div style='text-align: center;'>"
            + "<h1 style='color: #1E293B;'>Accès Restreint</h1>"
            + "<p style='font-size: 14px; color: #64748B;'>Désolé " + user.getFirstName() + ", l'interface <b>" + user.getRole() + "</b><br>"
            + "est actuellement en cours de développement.</p></div></html>");
        
        panel.add(message);
        return panel;
    }

    private JButton createSidebarButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 14));
        btn.setForeground(active ? Color.WHITE : new Color(148, 163, 184));
        btn.setBackground(active ? new Color(30, 41, 59) : new Color(15, 23, 42));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}