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
            int id = (int) tableModel.getValueAt(modelRow, 0); 
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

        actionButtonsPanel.add(editBtn);
        actionButtonsPanel.add(deleteBtn);
        actionButtonsPanel.add(addBtn);

        headerPanel.add(titleBox, BorderLayout.WEST);
        headerPanel.add(actionButtonsPanel, BorderLayout.EAST);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0)); 
        cardsPanel.setOpaque(false);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); 

        JPanel totalCard = createStatCard("Total", totalValueLbl, Color.DARK_GRAY);
        JPanel functionalCard = createStatCard("Fonctionnels", functionalValueLbl, new Color(34, 139, 34)); 
        JPanel maintenanceCard = createStatCard("En maintenance", maintenanceValueLbl, new Color(218, 165, 32)); 
        JPanel brokenCard = createStatCard("Hors service", brokenValueLbl, new Color(220, 20, 60)); 

        cardsPanel.add(totalCard);
        cardsPanel.add(functionalCard);
        cardsPanel.add(maintenanceCard);
        cardsPanel.add(brokenCard);

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
                tableModel.setRowCount(0); 
                
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
          
            String selectedStatus = (String) statusFilter.getSelectedItem();
            
            
            tableModel.setRowCount(0);
            
           
            List<Equipment> filteredList;
            
            if ("Tous les statuts".equals(selectedStatus)) {
                filteredList = equipmentDAO.getAllEquipements(); 
            } else {
                filteredList = equipmentDAO.getEquipementsByStatus(selectedStatus); 
            }
            
            for (Equipment eq : filteredList) {
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

      
        add(headerPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(cardsPanel);
        add(Box.createRigidArea(new Dimension(0, 25)));
        add(tableContainer);

        
        refreshAllData();
    }

    
    
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
      
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, "Ajouter un équipement", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setLayout(new BorderLayout());

      
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Nom de l'équipement *"));
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(15));

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

        formPanel.add(new JLabel("Status *"));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Fonctionnel", "En maintenance", "Hors service"});
        statusCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(statusCombo);
        formPanel.add(Box.createVerticalStrut(15));

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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelBtn = new JButton("Annuler");
        JButton confirmAddBtn = new JButton("Ajouter");
        confirmAddBtn.setBackground(new Color(0, 102, 102));
        confirmAddBtn.setForeground(Color.WHITE);

        cancelBtn.addActionListener(e -> dialog.dispose());

        confirmAddBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            String departement = (String) departementCombo.getSelectedItem();
            String status = (String) statusCombo.getSelectedItem();
            String purchaseStr = purchaseDateField.getText().trim();
            String maintStr = nextMaintenanceDateField.getText().trim();

            if (name.isEmpty() || purchaseStr.isEmpty() || maintStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir tous les champs texte.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("Sélectionner".equals(type) || "Sélectionner".equals(departement)) {
                JOptionPane.showMessageDialog(dialog, "Veuillez sélectionner un Type et un Département.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.sql.Date purchaseDate;
            java.sql.Date maintDate;
            try {
                purchaseDate = java.sql.Date.valueOf(purchaseStr); 
                maintDate = java.sql.Date.valueOf(maintStr);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, "Format de date invalide. Utilisez YYYY-MM-DD (ex: 2024-05-20).", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

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

   
    public void refreshStatistics() {
        int total = equipmentDAO.getTotalEquipmentCount();
        int functional = equipmentDAO.getEquipmentCountByStatus("Fonctionnel");
        int maintenance = equipmentDAO.getEquipmentCountByStatus("En maintenance");
        int broken = equipmentDAO.getEquipmentCountByStatus("Hors service");

        totalValueLbl.setText(String.valueOf(total));
        functionalValueLbl.setText(String.valueOf(functional));
        maintenanceValueLbl.setText(String.valueOf(maintenance));
        brokenValueLbl.setText(String.valueOf(broken));
        
        subtitleLabel.setText(total + " équipements enregistrés");
    }

   
    private void showEditEquipmentDialog(int viewRow) {
        int modelRow = equipmentTable.convertRowIndexToModel(viewRow);
        
        int id = (int) tableModel.getValueAt(modelRow, 0); 
        String currentName = String.valueOf(tableModel.getValueAt(modelRow, 1));
        String currentType = String.valueOf(tableModel.getValueAt(modelRow, 2));
        String currentDep = String.valueOf(tableModel.getValueAt(modelRow, 3));
        String currentStatus = String.valueOf(tableModel.getValueAt(modelRow, 4));
        
        Object purchaseObj = tableModel.getValueAt(modelRow, 5);
        String currentPurchase = (purchaseObj != null) ? purchaseObj.toString() : "";
        
        Object maintObj = tableModel.getValueAt(modelRow, 6);
        String currentMaint = (maintObj != null) ? maintObj.toString() : "";

        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, "Modifier l'équipement", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Nom de l'équipement *"));
        JTextField nameField = new JTextField(currentName);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(15));

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

        formPanel.add(new JLabel("Status *"));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Fonctionnel", "En maintenance", "Hors service"});
        statusCombo.setSelectedItem(currentStatus);
        statusCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(statusCombo);
        formPanel.add(Box.createVerticalStrut(15));

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
            eq.setId(id); 
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