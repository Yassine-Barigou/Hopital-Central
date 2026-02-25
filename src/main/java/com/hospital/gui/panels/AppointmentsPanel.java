package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.AppointmentDAO;
import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.dao.PatientDAO;
import main.java.com.hospital.model.Appointment;
import main.java.com.hospital.model.Employees;
import main.java.com.hospital.model.Patient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private AppointmentDAO dao = new AppointmentDAO();
    private PatientDAO patientDAO = new PatientDAO();
    private EmployeesDAO empDAO = new EmployeesDAO();

    public AppointmentsPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("📅 Gestion des Rendez-vous");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(30, 41, 59));
        
        JLabel subtitle = new JLabel("Planification et suivi des rendez-vous médicaux");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        String[] cols = {"ID", "Patient", "Médecin", "Date & Heure", "Durée (min)", "Type", "Statut"};
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

        JButton btnAdd = createStyledButton("➕ Ajouter", new Color(37, 99, 235), Color.WHITE);
        JButton btnEdit = createStyledButton("✏️ Modifier", new Color(71, 85, 105), Color.WHITE);
        JButton btnDelete = createStyledButton("🗑️ Supprimer", new Color(220, 38, 38), Color.WHITE);

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        add(btnPanel, BorderLayout.SOUTH);

        refreshTable();

        btnAdd.addActionListener(e -> {
            Appointment a = new Appointment();
            AppointmentFormDialog dialog = new AppointmentFormDialog(a);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                dao.addAppointment(a);
                refreshTable();
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                Appointment a = dao.getAllAppointments().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
                if (a != null) {
                    AppointmentFormDialog dialog = new AppointmentFormDialog(a);
                    dialog.setVisible(true);
                    if (dialog.isSaved()) {
                        dao.updateAppointment(a);
                        refreshTable();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un rendez-vous.");
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Annuler ce rendez-vous ?", "Confirmer", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dao.deleteAppointment(id);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un rendez-vous.");
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

        // Styling l-Statut (Index 6)
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    String status = value.toString();
                    if ("Confirmé".equalsIgnoreCase(status)) setForeground(new Color(16, 185, 129));
                    else if ("Annulé".equalsIgnoreCase(status)) setForeground(new Color(239, 68, 68));
                    else if ("Terminé".equalsIgnoreCase(status)) setForeground(new Color(37, 99, 235));
                    else setForeground(new Color(107, 114, 128));
                    setFont(getFont().deriveFont(Font.BOLD));
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
        btn.setPreferredSize(new Dimension(130, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<Appointment> list = dao.getAllAppointments();
        List<Patient> patients = patientDAO.getAllPatients();
        List<Employees> employees = empDAO.getAllEmployees();

        for (Appointment a : list) {
            String patientName = patients.stream()
                .filter(p -> p.getId() == a.getPatientId())
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .findFirst().orElse("Inconnu");
                                 
            String doctorName = employees.stream()
                .filter(e -> e.getId() == a.getDoctorId())
                .map(e -> "Dr. " + e.getFirstName() + " " + e.getLastName())
                .findFirst().orElse("Inconnu");

            model.addRow(new Object[]{
                    a.getId(), patientName, doctorName, a.getFormattedDate(),
                    a.getDurationMinutes(), a.getType(), a.getStatus()
            });
        }
    }
}