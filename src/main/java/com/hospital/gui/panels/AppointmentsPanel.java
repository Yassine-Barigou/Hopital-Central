package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.AppointmentDAO;
import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.dao.PatientDAO;
import main.java.com.hospital.model.Appointment;
import main.java.com.hospital.model.Employees;
import main.java.com.hospital.model.Patient;

import javax.swing.*;
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
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("📅 Gestion des Rendez-vous");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Patient", "Médecin", "Date & Heure", "Durée (min)", "Type", "Statut"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("➕ Ajouter");
        JButton btnEdit = new JButton("✏️ Modifier");
        JButton btnDelete = new JButton("🗑️ Supprimer");
        btnDelete.setForeground(Color.RED);
        
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
                int confirm = JOptionPane.showConfirmDialog(this, "Annuler et supprimer ce rendez-vous ?", "Confirmer", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dao.deleteAppointment(id);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un rendez-vous.");
            }
        });
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
                    a.getId(),
                    patientName,
                    doctorName,
                    a.getFormattedDate(),
                    a.getDurationMinutes(),
                    a.getType(),
                    a.getStatus()
            });
        }
    }
}