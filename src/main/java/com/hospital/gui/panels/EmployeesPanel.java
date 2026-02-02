package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.model.Employees;
import main.java.com.hospital.gui.panels.HomePanel;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class EmployeesPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private EmployeesDAO dao = new EmployeesDAO();

    public EmployeesPanel() {
        
        
        setLayout(new BorderLayout(10, 10));
        JLabel title = new JLabel("Gestion des Employés");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Prénom", "Nom", "Email", "Rôle", "Département", "Téléphone", "Date embauche"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // Boutons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Ajouter");
        JButton btnEdit = new JButton("Modifier");
        JButton btnDelete = new JButton("Supprimer");
        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        add(btnPanel, BorderLayout.SOUTH);

        refreshTable();

        // Ajouter
        btnAdd.addActionListener(e -> {
            Employees emp = new Employees();
            EmployeeFormDialog dialog = new EmployeeFormDialog(emp);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                dao.addEmployee(emp);
                refreshTable();
            }
        });

        // Modifier
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                Employees emp = dao.getAllEmployees().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
                if (emp != null) {
                    EmployeeFormDialog dialog = new EmployeeFormDialog(emp);
                    dialog.setVisible(true);
                    if (dialog.isSaved()) {
                        dao.updateEmployee(emp);
                        refreshTable();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un employé.");
            }
        });

        // Supprimer
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cet employé ?", "Confirmer", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dao.deleteEmployee(id);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un employé.");
            }
        });
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<Employees> list = dao.getAllEmployees();
        for (Employees e : list) {
            model.addRow(new Object[]{
                    e.getId(),
                    e.getFirstName(),
                    e.getLastName(),
                    e.getEmail(),
                    e.getRole(),
                    e.getDepartment(),
                    e.getPhone(),
                    e.getHireDate()
            });
        }
    }
}
