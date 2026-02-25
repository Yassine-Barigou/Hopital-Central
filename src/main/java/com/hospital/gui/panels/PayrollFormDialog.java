package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.model.Employees;
import main.java.com.hospital.model.Payroll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PayrollFormDialog extends JDialog {

    private boolean saved = false;
    private Payroll payroll;
    private Map<Integer, Double> employeeSalaries = new HashMap<>(); // Bach n7efdo salaire d kolla wa7d

    private JComboBox<ComboItem> cbEmployees;
    private JComboBox<String> cbMonth;
    private JTextField txtYear;
    private JTextField txtBaseSalary;
    private JTextField txtBonus;
    private JTextField txtDeductions;
    private JTextField txtNetSalary;

    public PayrollFormDialog(Payroll p) {
        this.payroll = p;
        setTitle("Nouveau Paiement (Fiche de Paie)");
        setModal(true);
        setSize(450, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cbEmployees = new JComboBox<>();
        loadEmployees();

        String[] months = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        cbMonth = new JComboBox<>(months);
        
        // Par défaut, l-3am l-7ali
        txtYear = new JTextField(String.valueOf(LocalDate.now().getYear()));
        
        txtBaseSalary = new JTextField("0.0");
        txtBaseSalary.setEditable(false); // Kay-t3mer bo7do
        
        txtBonus = new JTextField("0.0");
        txtDeductions = new JTextField("0.0");
        
        txtNetSalary = new JTextField("0.0");
        txtNetSalary.setEditable(false);
        txtNetSalary.setFont(new Font("SansSerif", Font.BOLD, 14));
        txtNetSalary.setForeground(new Color(16, 185, 129)); // Loun Khder

        formPanel.add(new JLabel("Employé : *"));
        formPanel.add(cbEmployees);
        
        formPanel.add(new JLabel("Mois : *"));
        formPanel.add(cbMonth);
        
        formPanel.add(new JLabel("Année : *"));
        formPanel.add(txtYear);
        
        formPanel.add(new JLabel("Salaire de Base (MAD) :"));
        formPanel.add(txtBaseSalary);
        
        formPanel.add(new JLabel("Primes / Heures supp. :"));
        formPanel.add(txtBonus);
        
        formPanel.add(new JLabel("Déductions / Retards :"));
        formPanel.add(txtDeductions);
        
        formPanel.add(new JLabel("Salaire NET (MAD) :"));
        formPanel.add(txtNetSalary);

        add(formPanel, BorderLayout.CENTER);

        // --- ACTIONS DYNAMIQUES ---
        // Mli n-bdel l-khddam, y-tbeddel salaire de base w y-t7seb Net
        cbEmployees.addActionListener(e -> updateCalculations());
        
        // Mli n-kteb chi prime wla deduction, y-t7seb Net f l-blassa
        KeyAdapter calcAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { updateCalculations(); }
        };
        txtBonus.addKeyListener(calcAdapter);
        txtDeductions.addKeyListener(calcAdapter);

        // Boutons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Enregistrer le Paiement");
        JButton btnCancel = new JButton("Annuler");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> save());

        // Lancement initial des calculs
        updateCalculations();
    }

    private void loadEmployees() {
        EmployeesDAO eDao = new EmployeesDAO();
        for (Employees e : eDao.getAllEmployees()) {
            cbEmployees.addItem(new ComboItem(e.getId(), e.getFirstName() + " " + e.getLastName()));
            employeeSalaries.put(e.getId(), e.getBaseSalary());
        }
    }

    private void updateCalculations() {
        try {
            ComboItem selectedEmp = (ComboItem) cbEmployees.getSelectedItem();
            if (selectedEmp != null) {
                double base = employeeSalaries.getOrDefault(selectedEmp.getId(), 0.0);
                txtBaseSalary.setText(String.valueOf(base));

                double bonus = txtBonus.getText().isEmpty() ? 0.0 : Double.parseDouble(txtBonus.getText());
                double deductions = txtDeductions.getText().isEmpty() ? 0.0 : Double.parseDouble(txtDeductions.getText());
                
                double net = base + bonus - deductions;
                txtNetSalary.setText(String.format("%.2f", net).replace(",", "."));
            }
        } catch (NumberFormatException ex) {
            txtNetSalary.setText("Erreur");
        }
    }

    private void save() {
        if (cbEmployees.getSelectedItem() == null || txtYear.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir les champs obligatoires (*)");
            return;
        }

        try {
            ComboItem selectedEmp = (ComboItem) cbEmployees.getSelectedItem();
            payroll.setEmployeeId(selectedEmp.getId());
            payroll.setMonth(cbMonth.getSelectedItem().toString());
            payroll.setYear(Integer.parseInt(txtYear.getText().trim()));
            payroll.setBaseSalary(Double.parseDouble(txtBaseSalary.getText()));
            payroll.setBonus(txtBonus.getText().isEmpty() ? 0.0 : Double.parseDouble(txtBonus.getText()));
            payroll.setDeductions(txtDeductions.getText().isEmpty() ? 0.0 : Double.parseDouble(txtDeductions.getText()));
            
            payroll.calculateNetSalary(); // Method mn l-Model
            payroll.setPaymentDate(Date.valueOf(LocalDate.now())); // Date dyal l-youm
            
            saved = true;
            dispose();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Format de nombre invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() { return saved; }

    private class ComboItem {
        private int id;
        private String label;
        public ComboItem(int id, String label) { this.id = id; this.label = label; }
        public int getId() { return id; }
        @Override public String toString() { return label; } 
    }
}