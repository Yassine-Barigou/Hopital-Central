package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.dao.PayrollDAO;
import main.java.com.hospital.model.Employees;
import main.java.com.hospital.model.Payroll;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PayrollPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private PayrollDAO dao = new PayrollDAO();
    private EmployeesDAO empDAO = new EmployeesDAO();

    public PayrollPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("💰 Gestion de la Paie");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(30, 41, 59));
        
        JLabel subtitle = new JLabel("Historique des paiements et gestion des salaires nets");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        String[] cols = {"ID", "Employé", "Mois", "Année", "Base (MAD)", "Primes", "Déductions", "NET (MAD)", "Date Paiement"};
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

        JButton btnAdd = createStyledButton("➕ Nouveau Paiement", new Color(37, 99, 235), Color.WHITE);
        JButton btnDelete = createStyledButton("🗑️ Annuler Paiement", new Color(244, 63, 94), Color.WHITE);

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

    private void setupTableStyle() {
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setGridColor(new Color(241, 245, 249));
        table.setShowVerticalLines(false);
        
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // Styling l-colonne dial l-khlass NET (Index 7)
        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setForeground(new Color(22, 163, 74)); // Loun khder dial l-flous
                setFont(getFont().deriveFont(Font.BOLD));
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
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
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
                    p.getId(), empName, p.getMonth(), p.getYear(),
                    String.format("%.2f", p.getBaseSalary()), 
                    String.format("%.2f", p.getBonus()), 
                    String.format("%.2f", p.getDeductions()), 
                    String.format("%.2f", p.getNetSalary()), 
                    p.getPaymentDate()
            });
        }
    }
}