package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.EquipementDAO;
import main.java.com.hospital.model.Equipment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MaintenancePanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable maintenanceTable;
    private EquipementDAO equipmentDAO;

    // Labels pour les compteurs dynamiques en haut
    private JLabel retardValueLbl;
    private JLabel urgentValueLbl;
    private JLabel avenirValueLbl;

    public MaintenancePanel() {
        this.equipmentDAO = new EquipementDAO();

        // Layout principal
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 247, 250)); // Fond gris clair
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- 1. HEADER (Titre) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Planning de Maintenance");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        JLabel subtitleLabel = new JLabel("Suivez les maintenances préventives des équipements");
        subtitleLabel.setForeground(Color.GRAY);
        
        titleBox.add(titleLabel);
        titleBox.add(Box.createRigidArea(new Dimension(0, 5)));
        titleBox.add(subtitleLabel);
        headerPanel.add(titleBox, BorderLayout.WEST);

        // --- 2. CARTES STATISTIQUES (En retard, Urgent, À venir) ---
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0)); // 3 colonnes espacées
        cardsPanel.setOpaque(false);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Initialisation des labels
        retardValueLbl = new JLabel("0");
        urgentValueLbl = new JLabel("0");
        avenirValueLbl = new JLabel("0");

        // Création des 3 cartes avec leurs couleurs spécifiques
        JPanel retardCard = createMaintenanceCard("En retard", retardValueLbl, new Color(220, 20, 60), "❌");
        JPanel urgentCard = createMaintenanceCard("Urgent (< 7 jours)", urgentValueLbl, new Color(218, 165, 32), "⚠️");
        JPanel avenirCard = createMaintenanceCard("À venir (< 30 jours)", avenirValueLbl, new Color(65, 105, 225), "🕒");

        cardsPanel.add(retardCard);
        cardsPanel.add(urgentCard);
        cardsPanel.add(avenirCard);

        // --- 3. TABLEAU DES MAINTENANCES ---
        JPanel tableContainer = new JPanel(new BorderLayout(0, 15));
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // En-tête du tableau
        JPanel tableHeaderPanel = new JPanel();
        tableHeaderPanel.setLayout(new BoxLayout(tableHeaderPanel, BoxLayout.Y_AXIS));
        tableHeaderPanel.setOpaque(false);
        JLabel tableTitle = new JLabel("Calendrier des maintenances");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel tableSubtitle = new JLabel("Liste des équipements triés par date de maintenance");
        tableSubtitle.setForeground(Color.GRAY);
        tableHeaderPanel.add(tableTitle);
        tableHeaderPanel.add(tableSubtitle);
        tableHeaderPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Configuration du tableau
        String[] columnNames = {"Équipement", "Département", "Status actuel", "Date de maintenance", "Délai", "Priorité"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        maintenanceTable = new JTable(tableModel);
        maintenanceTable.setRowHeight(40);
        maintenanceTable.setShowGrid(false);
        
        JScrollPane tableScrollPane = new JScrollPane(maintenanceTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

        tableContainer.add(tableHeaderPanel, BorderLayout.NORTH);
        tableContainer.add(tableScrollPane, BorderLayout.CENTER);

        // --- ASSEMBLAGE ---
        add(headerPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(cardsPanel);
        add(Box.createRigidArea(new Dimension(0, 25)));
        add(tableContainer);

        // --- 4. CHARGEMENT DES DONNÉES ---
        refreshMaintenanceData();
        //refresh a chaque fois qu on affiche maintance pannel
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                
                refreshMaintenanceData();
            }
        });
    }

    /**
     * Méthode utilitaire pour créer les cartes colorées avec bordures adaptées
     */
    private JPanel createMaintenanceCard(String title, JLabel valueLbl, Color themeColor, String icon) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        
        // Bordure colorée si c'est "En retard" (rouge), sinon bordure grise standard
        if (title.contains("retard")) {
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeColor, 1, true),
                new EmptyBorder(15, 20, 15, 20)
            ));
        } else {
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 20, 15, 20)
            ));
        }

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        titleLbl.setForeground(Color.GRAY);

        valueLbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        valueLbl.setForeground(themeColor);

        textPanel.add(titleLbl);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(valueLbl);

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("SansSerif", Font.PLAIN, 24));
        iconLbl.setForeground(themeColor);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(iconLbl, BorderLayout.EAST);

        return card;
    }

    /**
     * Interroge la base de données, calcule les délais et remplit le tableau
     */
    public void refreshMaintenanceData() {
        // 1. Mettre à jour les compteurs en haut via le DAO
        retardValueLbl.setText(String.valueOf(equipmentDAO.countMaintenanceEnRetard()));
        urgentValueLbl.setText(String.valueOf(equipmentDAO.countMaintenanceUrgente()));
        avenirValueLbl.setText(String.valueOf(equipmentDAO.countMaintenanceAVenir()));

        // 2. Vider le tableau
        tableModel.setRowCount(0);

        // 3. Récupérer la liste triée
        List<Equipment> equipements = equipmentDAO.getListePourPlanningMaintenance();
        LocalDate aujourdHui = LocalDate.now();

        // 4. Remplir le tableau avec le calcul des jours
        for (Equipment eq : equipements) {
            
            // Éviter les erreurs si la date est nulle
            if (eq.getNext_maintenance_date() == null) continue;
            
            LocalDate dateMaintenance = eq.getNext_maintenance_date().toLocalDate();
            
            // Calculer la différence en jours
            long joursDeDifference = ChronoUnit.DAYS.between(aujourdHui, dateMaintenance);
            
            String delaiTexte = "";
            String prioriteTexte = "";
            
            // Logique de classification comme sur votre image
            if (joursDeDifference < 0) {
                delaiTexte = Math.abs(joursDeDifference) + " jours de retard";
                prioriteTexte = "❌ En retard";
            } else if (joursDeDifference <= 7) {
                delaiTexte = "Dans " + joursDeDifference + " jours";
                prioriteTexte = "⚠️ Urgent";
            } else if (joursDeDifference <= 30) {
                delaiTexte = "Dans " + joursDeDifference + " jours";
                prioriteTexte = "🕒 Bientôt";
            } else {
                delaiTexte = "Dans " + joursDeDifference + " jours";
                prioriteTexte = "✅ OK";
            }

            // Mettre des émojis pour simuler les badges colorés du "Status actuel"
            String statusActuel = eq.getStatus();
            if ("Fonctionnel".equals(statusActuel)) statusActuel = "🟢 " + statusActuel;
            else if ("En maintenance".equals(statusActuel)) statusActuel = "🟡 " + statusActuel;
            else if ("Hors service".equals(statusActuel)) statusActuel = "🔴 " + statusActuel;

            Object[] row = {
                eq.getName(),
                eq.getLocation(),
                statusActuel,
                eq.getNext_maintenance_date(),
                delaiTexte,     // Le calcul dynamique !
                prioriteTexte   // L'étiquette dynamique !
            };
            tableModel.addRow(row);
        }
    }
}