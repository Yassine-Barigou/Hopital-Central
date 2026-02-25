package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.model.Leave;
import main.java.com.hospital.model.Employees;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class LeaveFormDialog extends JDialog {

    private boolean saved = false;
    private Leave leave;

    private JComboBox<ComboItem> cbEmployees;
    private JTextField txtStartDate; 
    private JTextField txtEndDate;   
    private JComboBox<String> cbType;
    private JTextArea txtReason;

    public LeaveFormDialog(Leave l) {
        this.leave = l;
        setTitle("Nouvelle Demande de Congé");
        setModal(true);
        setSize(450, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cbEmployees = new JComboBox<>();
        loadEmployees();

        txtStartDate = new JTextField();
        txtStartDate.setToolTipText("Ex: 2026-06-01");
        
        txtEndDate = new JTextField();
        txtEndDate.setToolTipText("Ex: 2026-06-15");

        cbType = new JComboBox<>(new String[]{"Annuel", "Maladie", "Maternité", "Sans solde", "Autre"});
        txtReason = new JTextArea(3, 20);

        formPanel.add(new JLabel("Employé : *"));
        formPanel.add(cbEmployees);
        
        formPanel.add(new JLabel("Date de début (YYYY-MM-DD) : *"));
        formPanel.add(txtStartDate);
        
        formPanel.add(new JLabel("Date de fin (YYYY-MM-DD) : *"));
        formPanel.add(txtEndDate);
        
        formPanel.add(new JLabel("Type de congé : *"));
        formPanel.add(cbType);
        
        formPanel.add(new JLabel("Motif / Raison :"));
        formPanel.add(new JScrollPane(txtReason));

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Soumettre");
        JButton btnCancel = new JButton("Annuler");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> save());
    }

    private void loadEmployees() {
        EmployeesDAO eDao = new EmployeesDAO();
        for (Employees e : eDao.getAllEmployees()) {
            cbEmployees.addItem(new ComboItem(e.getId(), e.getFirstName() + " " + e.getLastName() + " (" + e.getRole() + ")"));
        }
    }

    private void save() {
        if (cbEmployees.getSelectedItem() == null || txtStartDate.getText().trim().isEmpty() || txtEndDate.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir les champs obligatoires (*)", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
       
            Date start = Date.valueOf(txtStartDate.getText().trim());
            Date end = Date.valueOf(txtEndDate.getText().trim());

            if (end.before(start)) {
                JOptionPane.showMessageDialog(this, 
                    "Erreur : La date de fin ne peut pas être avant la date de début !", 
                    "Date invalide", 
                    JOptionPane.WARNING_MESSAGE);
                return; 
            }

            ComboItem selectedEmp = (ComboItem) cbEmployees.getSelectedItem();
            leave.setEmployeeId(selectedEmp.getId());
            leave.setStartDate(start);
            leave.setEndDate(end);
            leave.setType(cbType.getSelectedItem().toString());
            leave.setReason(txtReason.getText().trim());
            
            saved = true;
            dispose();
            
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez YYYY-MM-DD.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    private class ComboItem {
        private int id;
        private String label;
        public ComboItem(int id, String label) { this.id = id; this.label = label; }
        public int getId() { return id; }
        @Override public String toString() { return label; } 
    }
}