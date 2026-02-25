package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.dao.LeaveDAO;
import main.java.com.hospital.model.Employees;
import main.java.com.hospital.model.Leave;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeavesPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private LeaveDAO dao = new LeaveDAO();
    private EmployeesDAO empDAO = new EmployeesDAO();

    public LeavesPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("🌴 Gestion des Congés");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(30, 41, 59));
        
        JLabel subtitle = new JLabel("Suivi des demandes d'absence et congés du personnel");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        String[] cols = {"ID", "Employé", "Du", "Au", "Type", "Statut", "Motif"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(model);
        setupTableStyle();
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnAdd = createStyledButton("➕ Nouvelle Demande", new Color(37, 99, 235), Color.WHITE);
        JButton btnApprove = createStyledButton("✅ Approuver", new Color(16, 185, 129), Color.WHITE);
        JButton btnReject = createStyledButton("❌ Refuser", new Color(244, 63, 94), Color.WHITE);
        JButton btnDelete = createStyledButton("🗑️ Supprimer", new Color(71, 85, 105), Color.WHITE);

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

    private void setupTableStyle() {
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setGridColor(new Color(241, 245, 249));
        table.setShowVerticalLines(false);
        
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    String status = value.toString();
                    if ("Approuvée".equals(status)) { setForeground(new Color(16, 185, 129)); setFont(getFont().deriveFont(Font.BOLD)); }
                    else if ("Refusée".equals(status)) { setForeground(new Color(244, 63, 94)); setFont(getFont().deriveFont(Font.BOLD)); }
                    else if ("En attente".equals(status)) { setForeground(new Color(245, 158, 11)); setFont(getFont().deriveFont(Font.BOLD)); }
                    else { setForeground(Color.BLACK); }
                }
                return c;
            }
        });
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(170, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void changerStatut(String nouveauStatut) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) model.getValueAt(row, 0);
            dao.updateLeaveStatus(id, nouveauStatut);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une demande.");
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
                    l.getId(), empName, l.getStartDate(), l.getEndDate(),
                    l.getType(), l.getStatus(), l.getReason()
            });
        }
    }
}