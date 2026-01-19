package main.java.com.hospital.gui.panels;

import main.java.com.hospital.model.Employees;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel(Employees user) {
        // 1. CONFIGURATION DE BASE
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252)); // Gris très clair (Background global)
        
        // On utilise un JScrollPane au cas où l'écran est petit
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 250, 252));
        mainContent.setBorder(new EmptyBorder(30, 30, 30, 30)); // Marges externes

        // =================================================================
        // 2. HEADER (Bonjour + Rôle)
        // =================================================================
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Bonjour, " + user.getFirstName() + " " + user.getLastName());
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(33, 37, 41));

        JLabel subtitle = new JLabel(user.getDepartment() + " - Tableau de bord");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setForeground(Color.GRAY);

        headerPanel.add(title);
        headerPanel.add(subtitle);
        
        mainContent.add(headerPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 30))); // Espace vide

        // =================================================================
        // 3. STATISTIQUES (Les cartes du haut)
        // =================================================================
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 25, 0)); // 1 ligne, 2 colonnes, espace 25px
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Carte 1 : Employés (Avec icône verte style unicode)
        statsPanel.add(createStatCard("Employés", "5", new Color(16, 185, 129), "👥")); 
        
        // Carte 2 : RDV en attente (Avec icône orange)
        statsPanel.add(createStatCard("RDV en attente", "1", new Color(245, 158, 11), "📅")); 

        mainContent.add(statsPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 30))); // Espace vide

        // =================================================================
        // 4. SECTION BASSE (Activités & RDV)
        // =================================================================
        JPanel bottomSection = new JPanel(new GridLayout(1, 2, 25, 0)); // Split écran en 2 colonnes
        bottomSection.setOpaque(false);
        bottomSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // -- Colonne Gauche : Activité Récente --
        JPanel activityCard = createSectionCard("Activité récente", "Les dernières actions effectuées");
        addActivityItem(activityCard, "Nouvelle consultation créée", "Il y a 10 min");
        addActivityItem(activityCard, "Équipement mis à jour", "Il y a 25 min");
        addActivityItem(activityCard, "Rendez-vous confirmé", "Il y a 1 heure");
        // On ajoute un composant flexible pour pousser le contenu vers le haut
        activityCard.add(Box.createVerticalGlue());
        bottomSection.add(activityCard);

        // -- Colonne Droite : Prochains RDV --
        JPanel rdvCard = createSectionCard("Prochains rendez-vous", "Les rendez-vous à venir");
        addRdvItem(rdvCard, "Pierre Moreau", "Suivi cardiologique", "2026-01-15", "09:00");
        addRdvItem(rdvCard, "Isabelle Roux", "Contrôle neurologique", "2026-01-16", "11:00");
        addRdvItem(rdvCard, "Michel Fournier", "Examen de routine", "2026-01-17", "15:30");
        rdvCard.add(Box.createVerticalGlue());
        bottomSection.add(rdvCard);

        mainContent.add(bottomSection);

        // Ajout du ScrollPane propre
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll plus fluide
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    // =================================================================
    // HELPER METHODS (POUR LE DESIGN)
    // =================================================================

    // --- Créer une carte de statistique (Carré blanc en haut) ---
    private JPanel createStatCard(String title, String value, Color iconColor, String iconTxt) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true), // Bordure grise fine
            new EmptyBorder(25, 30, 25, 30) // Padding interne
        ));

        // Haut : Titre + Icone
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblTitle.setForeground(new Color(100, 116, 139)); // Gris
        
        JLabel icon = new JLabel(iconTxt); 
        icon.setForeground(iconColor);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        top.add(lblTitle, BorderLayout.WEST);
        top.add(icon, BorderLayout.EAST);

        // Bas : La Valeur (Gros Chiffre)
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblValue.setForeground(new Color(33, 37, 41));
        lblValue.setBorder(new EmptyBorder(15, 0, 0, 0)); // Espace au dessus du chiffre

        card.add(top, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        
        return card;
    }

    // --- Créer le conteneur blanc pour les listes (Activité / RDV) ---
    private JPanel createSectionCard(String title, String subtitle) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(new Color(33, 37, 41));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(Color.GRAY);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblSub);
        card.add(Box.createRigidArea(new Dimension(0, 25))); // Espace avant la liste
        
        return card;
    }

    // --- Ajouter une ligne dans "Activité récente" ---
    private void addActivityItem(JPanel container, String text, String time) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); // Hauteur fixe
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblText = new JLabel(text);
        lblText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblText.setForeground(new Color(51, 65, 85));

        JLabel lblTime = new JLabel(time);
        lblTime.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTime.setForeground(new Color(148, 163, 184)); // Gris clair

        row.add(lblText, BorderLayout.WEST);
        row.add(lblTime, BorderLayout.EAST);
        
        container.add(row);
        
        // Ligne de séparation fine
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(240, 240, 240));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(sep);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    // --- Ajouter une ligne dans "Prochains RDV" ---
    private void addRdvItem(JPanel container, String name, String motif, String date, String heure) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel left = new JPanel(new GridLayout(2, 1));
        left.setOpaque(false);
        
        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblName.setForeground(new Color(33, 37, 41));
        
        JLabel lblMotif = new JLabel(motif);
        lblMotif.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblMotif.setForeground(Color.GRAY);
        
        left.add(lblName);
        left.add(lblMotif);

        // Date et Heure à droite
        JPanel right = new JPanel(new GridLayout(2, 1));
        right.setOpaque(false);
        
        JLabel lblDate = new JLabel(date);
        lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDate.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblDate.setForeground(new Color(51, 65, 85));
        
        JLabel lblHeure = new JLabel(heure);
        lblHeure.setHorizontalAlignment(SwingConstants.RIGHT);
        lblHeure.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblHeure.setForeground(new Color(100, 116, 139));

        right.add(lblDate);
        right.add(lblHeure);

        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);

        container.add(row);
        
        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(240, 240, 240));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(sep);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
    }
}