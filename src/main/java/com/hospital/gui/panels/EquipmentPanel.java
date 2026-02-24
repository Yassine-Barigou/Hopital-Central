package main.java.com.hospital.gui.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import main.java.com.hospital.dao.EquipementDAO;
import main.java.com.hospital.model.Equipment;

public class EquipmentPanel extends JPanel {

    private JTable equipmentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    private EquipementDAO equipmentDAO;

    // 1. Declare the value labels globally so we can update them later
    private JLabel totalValueLbl = new JLabel("0");
    private JLabel functionalValueLbl = new JLabel("0");
    private JLabel maintenanceValueLbl = new JLabel("0");
    private JLabel brokenValueLbl = new JLabel("0");
    private JLabel subtitleLabel = new JLabel("0 équipements enregistrés");

    public EquipmentPanel() {
        this.equipmentDAO = new EquipementDAO();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 247, 250)); 
        setBorder(new EmptyBorder(20, 20, 20, 20)); 

        // --- 1. TOP HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false); 
        
        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Gestion des Équipements");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        subtitleLabel.setForeground(Color.GRAY);
        
        titleBox.add(titleLabel);
        titleBox.add(Box.createRigidArea(new Dimension(0, 5)));
        titleBox.add(subtitleLabel);

        // --- Création du groupe de boutons (Modifier, Supprimer, Ajouter) ---
        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionButtonsPanel.setOpaque(false);

        JButton editBtn = new JButton("✏️ Modifier");
        editBtn.setBackground(new Color(255, 193, 7)); // Jaune
        editBtn.setFocusPainted(false);
        editBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton deleteBtn = new JButton("🗑️ Supprimer");
        deleteBtn.setBackground(new Color(220, 53, 69)); // Rouge
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton addBtn = new JButton("+ Ajouter un équipement");
        addBtn.setBackground(new Color(0, 102, 102)); // Vert canard
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Actions des boutons
        addBtn.addActionListener(e -> showAddEquipmentDialog());

        editBtn.addActionListener(e -> {
            int selectedRow = equipmentTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez d'abord sélectionner un équipement dans le tableau.", "Avertissement", JOptionPane.WARNING_MESSAGE);
                return;
            }
            showEditEquipmentDialog(selectedRow);
        });

        deleteBtn.addActionListener(e -> {
            int selectedRow = equipmentTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez d'abord sélectionner un équipement dans le tableau.", "Avertissement", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int modelRow = equipmentTable.convertRowIndexToModel(selectedRow);
            int id = (int) tableModel.getValueAt(modelRow, 0); // L'ID caché
            String equipName = String.valueOf(tableModel.getValueAt(modelRow, 1));

            int confirm = JOptionPane.showConfirmDialog(this, 
                "Êtes-vous sûr de vouloir supprimer définitivement l'équipement : " + equipName + " ?", 
                "Confirmer la suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (equipmentDAO.deleteEquipement(id)) {
                    JOptionPane.showMessageDialog(this, "L'équipement a été supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    refreshAllData(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ajout au panel
        actionButtonsPanel.add(editBtn);
        actionButtonsPanel.add(deleteBtn);
        actionButtonsPanel.add(addBtn);

        headerPanel.add(titleBox, BorderLayout.WEST);
        headerPanel.add(actionButtonsPanel, BorderLayout.EAST);

        // --- 2. STATISTIC CARDS (Tickets) ---
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0)); 
        cardsPanel.setOpaque(false);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); 

        // 2. Pass the global labels into our helper method
        JPanel totalCard = createStatCard("Total", totalValueLbl, Color.DARK_GRAY);
        JPanel functionalCard = createStatCard("Fonctionnels", functionalValueLbl, new Color(34, 139, 34)); 
        JPanel maintenanceCard = createStatCard("En maintenance", maintenanceValueLbl, new Color(218, 165, 32)); 
        JPanel brokenCard = createStatCard("Hors service", brokenValueLbl, new Color(220, 20, 60)); 

        cardsPanel.add(totalCard);
        cardsPanel.add(functionalCard);
        cardsPanel.add(maintenanceCard);
        cardsPanel.add(brokenCard);

        // --- 3. TABLE SECTION ---
        JPanel tableContainer = new JPanel(new BorderLayout(0, 15));
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel tableHeaderPanel = new JPanel(new BorderLayout());
        tableHeaderPanel.setOpaque(false);
        
        JPanel tableTitleBox = new JPanel();
        tableTitleBox.setLayout(new BoxLayout(tableTitleBox, BoxLayout.Y_AXIS));
        tableTitleBox.setOpaque(false);
        JLabel tableTitle = new JLabel("Liste des équipements");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel tableSubtitle = new JLabel("Gérez les équipements médicaux de l'hôpital");
        tableSubtitle.setForeground(Color.GRAY);
        tableTitleBox.add(tableTitle);
        tableTitleBox.add(tableSubtitle);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setOpaque(false);
        
        searchField = new JTextField(15);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String keyword = searchField.getText().trim();
                tableModel.setRowCount(0); // Vider le tableau visuel
                
                List<Equipment> searchedList;
                
                
                if (keyword.isEmpty()) {
                    searchedList = equipmentDAO.getAllEquipements();
                } else {
                    
                    searchedList = equipmentDAO.searchEquipements(keyword);
                }
                
                
                for (Equipment eq : searchedList) {
                    Object[] row = {
                        eq.getId(), 
                        eq.getName(),
                        eq.getType(),
                        eq.getLocation(),
                        eq.getStatus(),
                        eq.getPurchase_date(),
                        eq.getNext_maintenance_date()
                    };
                    tableModel.addRow(row);
                }
            }
        });
        
        
        String[] statuses = {"Tous les statuts", "Fonctionnel", "En maintenance", "Hors service"};
        statusFilter = new JComboBox<>(statuses);
        statusFilter.setPreferredSize(new Dimension(150, 30));
        statusFilter.addActionListener(e -> {
            // 1. Récupérer le choix de l'utilisateur
            String selectedStatus = (String) statusFilter.getSelectedItem();
            
            // 2. Vider complètement les lignes actuelles du tableau
            tableModel.setRowCount(0);
            
            // 3. Préparer une liste vide
            List<Equipment> filteredList;
            
            // 4. Interroger la BDD selon le choix
            if ("Tous les statuts".equals(selectedStatus)) {
                filteredList = equipmentDAO.getAllEquipements(); // On prend tout
            } else {
                filteredList = equipmentDAO.getEquipementsByStatus(selectedStatus); // On filtre en BDD
            }
            
            // 5. Remplir le tableau avec la nouvelle liste récupérée de la BDD
            for (Equipment eq : filteredList) {
                Object[] row = {
                    eq.getId(), // Toujours garder l'ID caché en première colonne
                    eq.getName(),
                    eq.getType(),
                    eq.getLocation(),
                    eq.getStatus(),
                    eq.getPurchase_date(),
                    eq.getNext_maintenance_date()
                };
                tableModel.addRow(row);
            }
        });
        
        filterPanel.add(new JLabel("🔍"));
        filterPanel.add(searchField);
        filterPanel.add(statusFilter);

        tableHeaderPanel.add(tableTitleBox, BorderLayout.WEST);
        tableHeaderPanel.add(filterPanel, BorderLayout.SOUTH);

        
        String[] columnNames = {"ID", "Nom", "Type", "Département", "Status", "Date d'achat", "Prochaine maint."};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        equipmentTable = new JTable(tableModel);
        equipmentTable.setRowHeight(40);
        equipmentTable.setShowGrid(false);
        equipmentTable.setIntercellSpacing(new Dimension(0, 0));
        
        // LA LIGNE MAGIQUE POUR CACHER L'ID À L'ÉCRAN :
        equipmentTable.removeColumn(equipmentTable.getColumnModel().getColumn(0));
        
        JScrollPane tableScrollPane = new JScrollPane(equipmentTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

        tableContainer.add(tableHeaderPanel, BorderLayout.NORTH);
        tableContainer.add(tableScrollPane, BorderLayout.CENTER);

        // --- 4. ASSEMBLE EVERYTHING ---
        add(headerPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(cardsPanel);
        add(Box.createRigidArea(new Dimension(0, 25)));
        add(tableContainer);

        // 3. Load initial table data AND statistics
        refreshAllData();
    }

    /**
     * Helper method updated to accept an existing JLabel
     */
    private JPanel createStatCard(String title, JLabel valueLbl, Color valueColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        titleLbl.setForeground(Color.GRAY);

        // Configure the dynamically updated label
        valueLbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        valueLbl.setForeground(valueColor);

        card.add(titleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLbl);

        return card;
    }

    /**
     * Call this whenever data is added, edited, or deleted
     */
    public void refreshAllData() {
        refreshTableData();
        refreshStatistics();
    }

    public void refreshTableData() {
        tableModel.setRowCount(0);
        List<Equipment> equipmentList = equipmentDAO.getAllEquipements();

        for (Equipment eq : equipmentList) {
            Object[] row = {
                eq.getId(),
                eq.getName(),
                eq.getType(),
                eq.getLocation(),
                eq.getStatus(),
                eq.getPurchase_date(),
                eq.getNext_maintenance_date()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddEquipmentDialog() {
        // 1. Setup the Dialog
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, "Ajouter un équipement", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setLayout(new BorderLayout());

        // 2. Build the Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Name
        formPanel.add(new JLabel("Nom de l'équipement *"));
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(15));

        // Type & Department (Row 2)
        JPanel row2 = new JPanel(new GridLayout(1, 2, 15, 0));
        
        JPanel typePanel = new JPanel();
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.Y_AXIS));
        typePanel.add(new JLabel("Type *"));
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Sélectionner", "Imagerie", "Cardiologie", "Laboratoire", "Chirurgie"});
        typePanel.add(typeCombo);
        
        JPanel depPanel = new JPanel();
        depPanel.setLayout(new BoxLayout(depPanel, BoxLayout.Y_AXIS));
        depPanel.add(new JLabel("Département *"));
        JComboBox<String> departementCombo = new JComboBox<>(new String[]{"Sélectionner", "Radiologie", "Cardiologie", "Urgences", "Bloc Opératoire"});
        depPanel.add(departementCombo);

        row2.add(typePanel);
        row2.add(depPanel);
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        formPanel.add(row2);
        formPanel.add(Box.createVerticalStrut(15));

        // Status
        formPanel.add(new JLabel("Status *"));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Fonctionnel", "En maintenance", "Hors service"});
        statusCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(statusCombo);
        formPanel.add(Box.createVerticalStrut(15));

        // Dates (Row 4)
        JPanel row4 = new JPanel(new GridLayout(1, 2, 15, 0));
        
        JPanel purchasePanel = new JPanel();
        purchasePanel.setLayout(new BoxLayout(purchasePanel, BoxLayout.Y_AXIS));
        purchasePanel.add(new JLabel("Date d'achat (YYYY-MM-DD) *"));
        JTextField purchaseDateField = new JTextField();
        purchasePanel.add(purchaseDateField);
        
        JPanel maintPanel = new JPanel();
        maintPanel.setLayout(new BoxLayout(maintPanel, BoxLayout.Y_AXIS));
        maintPanel.add(new JLabel("Prochaine maintenance *"));
        JTextField nextMaintenanceDateField = new JTextField();
        maintPanel.add(nextMaintenanceDateField);

        row4.add(purchasePanel);
        row4.add(maintPanel);
        row4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        formPanel.add(row4);

        dialog.add(formPanel, BorderLayout.CENTER);

        // 3. Build the Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelBtn = new JButton("Annuler");
        JButton confirmAddBtn = new JButton("Ajouter");
        confirmAddBtn.setBackground(new Color(0, 102, 102));
        confirmAddBtn.setForeground(Color.WHITE);

        cancelBtn.addActionListener(e -> dialog.dispose());

        // 4. Handle Validation and Database Insertion
        confirmAddBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            String departement = (String) departementCombo.getSelectedItem();
            String status = (String) statusCombo.getSelectedItem();
            String purchaseStr = purchaseDateField.getText().trim();
            String maintStr = nextMaintenanceDateField.getText().trim();

            // Constraints: Check empty fields
            if (name.isEmpty() || purchaseStr.isEmpty() || maintStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir tous les champs texte.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Constraints: Check dropdowns
            if ("Sélectionner".equals(type) || "Sélectionner".equals(departement)) {
                JOptionPane.showMessageDialog(dialog, "Veuillez sélectionner un Type et un Département.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Constraints: Check Date Format
            java.sql.Date purchaseDate;
            java.sql.Date maintDate;
            try {
                purchaseDate = java.sql.Date.valueOf(purchaseStr); 
                maintDate = java.sql.Date.valueOf(maintStr);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, "Format de date invalide. Utilisez YYYY-MM-DD (ex: 2024-05-20).", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create Object and Insert into DB
            Equipment eq = new Equipment();
            eq.setName(name);
            eq.setType(type);
            eq.setLocation(departement); 
            eq.setStatus(status);
            eq.setPurchase_date(purchaseDate);
            eq.setNext_maintenance_date(maintDate);

            if (equipmentDAO.addEquipement(eq)) {
                JOptionPane.showMessageDialog(dialog, "Équipement ajouté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                refreshAllData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Erreur lors de l'enregistrement dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelBtn);
        buttonPanel.add(confirmAddBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Show the dialog
        dialog.setVisible(true);
    }

    /**
     * 4. Fetches real counts from the database and updates the JLabels
     */
    public void refreshStatistics() {
        int total = equipmentDAO.getTotalEquipmentCount();
        int functional = equipmentDAO.getEquipmentCountByStatus("Fonctionnel");
        int maintenance = equipmentDAO.getEquipmentCountByStatus("En maintenance");
        int broken = equipmentDAO.getEquipmentCountByStatus("Hors service");

        // Update the UI labels
        totalValueLbl.setText(String.valueOf(total));
        functionalValueLbl.setText(String.valueOf(functional));
        maintenanceValueLbl.setText(String.valueOf(maintenance));
        brokenValueLbl.setText(String.valueOf(broken));
        
        // Update the subtitle text
        subtitleLabel.setText(total + " équipements enregistrés");
    }

    /**
     * Ouvre la fenêtre pour modifier un équipement existant
     */
    private void showEditEquipmentDialog(int viewRow) {
        int modelRow = equipmentTable.convertRowIndexToModel(viewRow);
        
        // Récupération sécurisée des données de la ligne sélectionnée
        int id = (int) tableModel.getValueAt(modelRow, 0); 
        String currentName = String.valueOf(tableModel.getValueAt(modelRow, 1));
        String currentType = String.valueOf(tableModel.getValueAt(modelRow, 2));
        String currentDep = String.valueOf(tableModel.getValueAt(modelRow, 3));
        String currentStatus = String.valueOf(tableModel.getValueAt(modelRow, 4));
        
        Object purchaseObj = tableModel.getValueAt(modelRow, 5);
        String currentPurchase = (purchaseObj != null) ? purchaseObj.toString() : "";
        
        Object maintObj = tableModel.getValueAt(modelRow, 6);
        String currentMaint = (maintObj != null) ? maintObj.toString() : "";

        // Construction de la fenêtre
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, "Modifier l'équipement", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Nom
        formPanel.add(new JLabel("Nom de l'équipement *"));
        JTextField nameField = new JTextField(currentName);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(15));

        // Type & Département 
        JPanel row2 = new JPanel(new GridLayout(1, 2, 15, 0));
        
        JPanel typePanel = new JPanel();
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.Y_AXIS));
        typePanel.add(new JLabel("Type *"));
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Sélectionner", "Imagerie", "Cardiologie", "Laboratoire", "Chirurgie"});
        typeCombo.setSelectedItem(currentType);
        typePanel.add(typeCombo);
        
        JPanel depPanel = new JPanel();
        depPanel.setLayout(new BoxLayout(depPanel, BoxLayout.Y_AXIS));
        depPanel.add(new JLabel("Département *"));
        JComboBox<String> departementCombo = new JComboBox<>(new String[]{"Sélectionner", "Radiologie", "Cardiologie", "Urgences", "Bloc Opératoire"});
        departementCombo.setSelectedItem(currentDep);
        depPanel.add(departementCombo);

        row2.add(typePanel);
        row2.add(depPanel);
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        formPanel.add(row2);
        formPanel.add(Box.createVerticalStrut(15));

        // Status
        formPanel.add(new JLabel("Status *"));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Fonctionnel", "En maintenance", "Hors service"});
        statusCombo.setSelectedItem(currentStatus);
        statusCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(statusCombo);
        formPanel.add(Box.createVerticalStrut(15));

        // Dates 
        JPanel row4 = new JPanel(new GridLayout(1, 2, 15, 0));
        
        JPanel purchasePanel = new JPanel();
        purchasePanel.setLayout(new BoxLayout(purchasePanel, BoxLayout.Y_AXIS));
        purchasePanel.add(new JLabel("Date d'achat (YYYY-MM-DD) *"));
        JTextField purchaseDateField = new JTextField(currentPurchase);
        purchasePanel.add(purchaseDateField);
        
        JPanel maintPanel = new JPanel();
        maintPanel.setLayout(new BoxLayout(maintPanel, BoxLayout.Y_AXIS));
        maintPanel.add(new JLabel("Prochaine maintenance *"));
        JTextField nextMaintenanceDateField = new JTextField(currentMaint);
        maintPanel.add(nextMaintenanceDateField);

        row4.add(purchasePanel);
        row4.add(maintPanel);
        row4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        formPanel.add(row4);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Boutons du bas
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelBtn = new JButton("Annuler");
        JButton saveBtn = new JButton("Enregistrer");
        saveBtn.setBackground(new Color(0, 102, 102));
        saveBtn.setForeground(Color.WHITE);

        cancelBtn.addActionListener(e -> dialog.dispose());

        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            String departement = (String) departementCombo.getSelectedItem();
            String status = (String) statusCombo.getSelectedItem();
            String purchaseStr = purchaseDateField.getText().trim();
            String maintStr = nextMaintenanceDateField.getText().trim();

            if (name.isEmpty() || purchaseStr.isEmpty() || maintStr.isEmpty() || "Sélectionner".equals(type) || "Sélectionner".equals(departement)) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir tous les champs correctement.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.sql.Date purchaseDate;
            java.sql.Date maintDate;
            try {
                purchaseDate = java.sql.Date.valueOf(purchaseStr); 
                maintDate = java.sql.Date.valueOf(maintStr);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, "Format de date invalide (YYYY-MM-DD).", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Equipment eq = new Equipment();
            eq.setId(id); // L'ID pour modifier la bonne ligne
            eq.setName(name);
            eq.setType(type);
            eq.setLocation(departement); 
            eq.setStatus(status);
            eq.setPurchase_date(purchaseDate);
            eq.setNext_maintenance_date(maintDate);

            if (equipmentDAO.updateEquipement(eq)) {
                JOptionPane.showMessageDialog(dialog, "Équipement modifié avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                refreshAllData(); 
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Erreur lors de la modification en base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

}