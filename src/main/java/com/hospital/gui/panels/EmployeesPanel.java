package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.model.Employees;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeesPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private EmployeesDAO dao = new EmployeesDAO();

    public EmployeesPanel() {
        // 1. الإعدادات الأساسية (Layout & Padding)
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // 2. Header (العنوان)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("👥 Gestion du Personnel");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(30, 41, 59)); // Slate 800
        
        JLabel subtitle = new JLabel("Liste de tous les employés travaillant dans l'hôpital");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // 3. الجدول (Table Customization)
        String[] cols = {"ID", "Prénom", "Nom", "Email", "Rôle", "Département", "Téléphone", "Embauche"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(model);
        setupTableStyle(); // دالة لتحسين شكل الجدول
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240))); // Border خفيف
        add(scroll, BorderLayout.CENTER);

        // 4. الأزرار (Buttons Style)
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnAdd = createStyledButton("Ajouter Employé", new Color(37, 99, 235), Color.WHITE);
        JButton btnEdit = createStyledButton("Modifier", new Color(71, 85, 105), Color.WHITE);
        JButton btnDelete = createStyledButton("Supprimer", new Color(220, 38, 38), Color.WHITE);

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        add(btnPanel, BorderLayout.SOUTH);

        // Events
        refreshTable();
        
        btnAdd.addActionListener(e -> {
            Employees emp = new Employees();
            EmployeeFormDialog dialog = new EmployeeFormDialog(emp);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                dao.addEmployee(emp);
                refreshTable();
            }
        });

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
                showWarning("Veuillez sélectionner un employé à modifier.");
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cet employé ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dao.deleteEmployee(id);
                    refreshTable();
                }
            } else {
                showWarning("Veuillez sélectionner un employé à supprimer.");
            }
        });
    }

    private void setupTableStyle() {
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setSelectionForeground(new Color(30, 41, 59));
        table.setGridColor(new Color(241, 245, 249));
        table.setShowVerticalLines(false); 
        
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.LEFT);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Attention", JOptionPane.WARNING_MESSAGE);
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<Employees> list = dao.getAllEmployees();
        for (Employees e : list) {
            model.addRow(new Object[]{
                    e.getId(), e.getFirstName(), e.getLastName(), e.getEmail(),
                    e.getRole(), e.getDepartment(), e.getPhone(), e.getHireDate()
            });
        }
    }
}