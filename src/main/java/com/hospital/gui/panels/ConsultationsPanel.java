package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.ConsultationDAO;
import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.dao.PatientDAO;
import main.java.com.hospital.model.Consultation;
import main.java.com.hospital.model.Employees;
import main.java.com.hospital.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ConsultationsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private ConsultationDAO dao = new ConsultationDAO();
    private PatientDAO patientDAO = new PatientDAO();
    private EmployeesDAO empDAO = new EmployeesDAO();

    public ConsultationsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("🩺 Gestion des Consultations");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Patient", "Médecin", "Date & Heure", "Motif", "Statut"};
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
            Consultation c = new Consultation();
            ConsultationFormDialog dialog = new ConsultationFormDialog(c);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                dao.addConsultation(c);
                refreshTable();
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                Consultation c = dao.getAllConsultations().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
                if (c != null) {
                    ConsultationFormDialog dialog = new ConsultationFormDialog(c);
                    dialog.setVisible(true);
                    if (dialog.isSaved()) {
                        dao.updateConsultation(c);
                        refreshTable();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une consultation.");
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cette consultation ?", "Confirmer", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dao.deleteConsultation(id);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une consultation.");
            }
        });
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<Consultation> list = dao.getAllConsultations();
        List<Patient> patients = patientDAO.getAllPatients();
        List<Employees> employees = empDAO.getAllEmployees();

        for (Consultation c : list) {
            String patientName = patients.stream()
                .filter(p -> p.getId() == c.getPatientId())
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .findFirst().orElse("Patient Inconnu");
                                
            String doctorName = employees.stream()
                .filter(e -> e.getId() == c.getDoctorId())
                .map(e -> "Dr. " + e.getFirstName() + " " + e.getLastName())
                .findFirst().orElse("Médecin Inconnu");

            model.addRow(new Object[]{
                    c.getId(),
                    patientName,
                    doctorName,
                    c.getFormattedDate(), 
                    c.getReason(),
                    c.getStatus()
            });
        }
    }
}