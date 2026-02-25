package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.PatientDAO;
import main.java.com.hospital.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private PatientDAO dao = new PatientDAO();

    public PatientsPanel() {
        
        setLayout(new BorderLayout(10, 10));
        
        
        JLabel title = new JLabel("🩺 Gestion des Patients");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Prénom", "Nom", "Date de Naissance", "Sexe", "Téléphone", "Groupe Sanguin"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Ajouter");
        JButton btnEdit = new JButton("Modifier");
        JButton btnDelete = new JButton("Supprimer");
        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        add(btnPanel, BorderLayout.SOUTH);

        refreshTable();

     
        btnAdd.addActionListener(e -> {
            Patient p = new Patient();
            PatientFormDialog dialog = new PatientFormDialog(p);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                dao.addPatient(p);
                refreshTable();
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                // Njibo l-patient mn la liste dyal base de données
                Patient p = dao.getAllPatients().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
                
                if (p != null) {
                    PatientFormDialog dialog = new PatientFormDialog(p);
                    dialog.setVisible(true);
                    if (dialog.isSaved()) {
                        dao.updatePatient(p);
                        refreshTable();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un patient.");
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer ce patient ?", "Confirmer", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dao.deletePatient(id);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un patient.");
            }
        });
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<Patient> list = dao.getAllPatients();
        for (Patient p : list) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getFirstName(),
                    p.getLastName(),
                    p.getDateBirth(),
                    p.getGende(),
                    p.getPhone(),
                    p.getBloodType()
            });
        }
    }
}