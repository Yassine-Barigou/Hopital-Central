package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.dao.PayrollDAO;
import main.java.com.hospital.model.Employees;
import main.java.com.hospital.model.Payroll;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PayrollPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private PayrollDAO dao = new PayrollDAO();
    private EmployeesDAO empDAO = new EmployeesDAO();

    public PayrollPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("💰 Gestion de la Paie");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Employé", "Mois", "Année", "Base (MAD)", "Primes", "Déductions", "NET (MAD)", "Date Paiement"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // Boutons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("➕ Nouveau Paiement");
        JButton btnDelete = new JButton("🗑️ Annuler Paiement");
        btnDelete.setForeground(Color.RED);
        
        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);
        add(btnPanel, BorderLayout.SOUTH);

        refreshTable();

        btnAdd.addActionListener(e -> {
            Payroll p = new Payroll();
            PayrollFormDialog dialog = new PayrollFormDialog(p);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                dao.addPayroll(p);
                refreshTable();
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous annuler cette fiche de paie ?", "Confirmer", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dao.deletePayroll(id);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez un paiement à annuler.");
            }
        });
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<Payroll> list = dao.getAllPayrolls();
        List<Employees> employees = empDAO.getAllEmployees();

        for (Payroll p : list) {
            String empName = employees.stream()
                .filter(e -> e.getId() == p.getEmployeeId())
                .map(e -> e.getFirstName() + " " + e.getLastName())
                .findFirst().orElse("Inconnu");

            model.addRow(new Object[]{
                    p.getId(),
                    empName,
                    p.getMonth(),
                    p.getYear(),
                    p.getBaseSalary(),
                    p.getBonus(),
                    p.getDeductions(),
                    p.getNetSalary(),
                    p.getPaymentDate()
            });
        }
    }
}