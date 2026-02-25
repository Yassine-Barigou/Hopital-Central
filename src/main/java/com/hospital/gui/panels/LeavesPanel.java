package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.dao.LeaveDAO;
import main.java.com.hospital.model.Employees;
import main.java.com.hospital.model.Leave;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeavesPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private LeaveDAO dao = new LeaveDAO();
    private EmployeesDAO empDAO = new EmployeesDAO();

    public LeavesPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("🌴 Gestion des Congés");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Employé", "Du", "Au", "Type", "Statut", "Motif"};
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
        JButton btnAdd = new JButton("➕ Nouvelle Demande");
        JButton btnApprove = new JButton("✅ Approuver");
        btnApprove.setForeground(new Color(16, 185, 129)); // Vert
        JButton btnReject = new JButton("❌ Refuser");
        btnReject.setForeground(Color.RED);
        JButton btnDelete = new JButton("🗑️ Supprimer");
        
        btnPanel.add(btnAdd);
        btnPanel.add(btnApprove);
        btnPanel.add(btnReject);
        btnPanel.add(btnDelete);
        add(btnPanel, BorderLayout.SOUTH);

        refreshTable();


        btnAdd.addActionListener(e -> {
            Leave l = new Leave();
            LeaveFormDialog dialog = new LeaveFormDialog(l);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                dao.addLeave(l);
                refreshTable();
            }
        });

        btnApprove.addActionListener(e -> changerStatut("Approuvée"));
        btnReject.addActionListener(e -> changerStatut("Refusée"));

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cet historique ?", "Confirmer", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dao.deleteLeave(id);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez un congé.");
            }
        });
    }

    private void changerStatut(String nouveauStatut) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) model.getValueAt(row, 0);
            dao.updateLeaveStatus(id, nouveauStatut);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une demande de congé dans le tableau.");
        }
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<Leave> list = dao.getAllLeaves();
        List<Employees> employees = empDAO.getAllEmployees();

        for (Leave l : list) {
            String empName = employees.stream()
                .filter(e -> e.getId() == l.getEmployeeId())
                .map(e -> e.getFirstName() + " " + e.getLastName())
                .findFirst().orElse("Inconnu");

            model.addRow(new Object[]{
                    l.getId(),
                    empName,
                    l.getStartDate(),
                    l.getEndDate(),
                    l.getType(),
                    l.getStatus(),
                    l.getReason()
            });
        }
    }
}