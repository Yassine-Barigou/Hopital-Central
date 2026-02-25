package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.dao.PatientDAO;
import main.java.com.hospital.model.Appointment;
import main.java.com.hospital.model.Employees;
import main.java.com.hospital.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class AppointmentFormDialog extends JDialog {

    private boolean saved = false;
    private Appointment appointment;

    private JComboBox<ComboItem> cbPatients;
    private JComboBox<ComboItem> cbDoctors;
    private JTextField txtDate; 
    private JTextField txtTime;
    private JTextField txtDuration; 
    private JTextField txtType; 
    private JComboBox<String> cbStatus;
    private JTextArea txtNotes;

    public AppointmentFormDialog(Appointment a) {
        this.appointment = a;
        setTitle(a.getId() == 0 ? "Nouveau Rendez-vous" : "Modifier Rendez-vous");
        setModal(true);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cbPatients = new JComboBox<>();
        cbDoctors = new JComboBox<>();
        loadDropdowns();

        txtDate = new JTextField();
        txtTime = new JTextField();
        txtDuration = new JTextField("30"); 
        txtType = new JTextField("Consultation standard");
        cbStatus = new JComboBox<>(new String[]{"En attente", "Confirmé", "Annulé", "Terminé"});
        txtNotes = new JTextArea(3, 20);

        formPanel.add(new JLabel("Patient : *"));
        formPanel.add(cbPatients);
        
        formPanel.add(new JLabel("Médecin : *"));
        formPanel.add(cbDoctors);
        
        formPanel.add(new JLabel("Date (YYYY-MM-DD) : *"));
        formPanel.add(txtDate);
        
        formPanel.add(new JLabel("Heure (HH:MM) : *"));
        formPanel.add(txtTime);
        
        formPanel.add(new JLabel("Durée (minutes) : *"));
        formPanel.add(txtDuration);

        formPanel.add(new JLabel("Type de visite : *"));
        formPanel.add(txtType);
        
        formPanel.add(new JLabel("Statut :"));
        formPanel.add(cbStatus);
        
        formPanel.add(new JLabel("Notes :"));
        formPanel.add(new JScrollPane(txtNotes));

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Enregistrer");
        JButton btnCancel = new JButton("Annuler");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        if (a.getId() != 0) {
            selectItemById(cbPatients, a.getPatientId());
            selectItemById(cbDoctors, a.getDoctorId());
            if (a.getDate() != null) {
                String fullDate = a.getDate().toString();
                txtDate.setText(fullDate.substring(0, 10));
                txtTime.setText(fullDate.substring(11, 16));
            }
            txtDuration.setText(String.valueOf(a.getDurationMinutes()));
            txtType.setText(a.getType());
            cbStatus.setSelectedItem(a.getStatus());
            txtNotes.setText(a.getNotes());
        }

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> save());
    }

    private void loadDropdowns() {
        PatientDAO pDao = new PatientDAO();
        for (Patient p : pDao.getAllPatients()) {
            cbPatients.addItem(new ComboItem(p.getId(), p.getFirstName() + " " + p.getLastName()));
        }

        EmployeesDAO eDao = new EmployeesDAO();
        for (Employees e : eDao.getAllEmployees()) {
            if ("Médecin".equalsIgnoreCase(e.getRole())) {
                cbDoctors.addItem(new ComboItem(e.getId(), "Dr. " + e.getFirstName() + " " + e.getLastName()));
            }
        }
    }

    private void selectItemById(JComboBox<ComboItem> cb, int id) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (cb.getItemAt(i).getId() == id) {
                cb.setSelectedIndex(i);
                break;
            }
        }
    }

    private void save() {
        if (cbPatients.getSelectedItem() == null || cbDoctors.getSelectedItem() == null ||
            txtDate.getText().trim().isEmpty() || txtTime.getText().trim().isEmpty() || txtType.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires (*)");
            return;
        }

        try {
            ComboItem selectedPatient = (ComboItem) cbPatients.getSelectedItem();
            ComboItem selectedDoc = (ComboItem) cbDoctors.getSelectedItem();

            appointment.setPatientId(selectedPatient.getId());
            appointment.setDoctorId(selectedDoc.getId());
            
            String dateTimeStr = txtDate.getText().trim() + " " + txtTime.getText().trim() + ":00";
            appointment.setDate(Timestamp.valueOf(dateTimeStr));
            
            appointment.setDurationMinutes(Integer.parseInt(txtDuration.getText().trim()));
            appointment.setType(txtType.getText().trim());
            appointment.setStatus(cbStatus.getSelectedItem().toString());
            appointment.setNotes(txtNotes.getText().trim());

            saved = true;
            dispose();
            
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Format invalide.\nDate: 2024-05-14\nHeure: 14:30\nDurée: 30", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    private class ComboItem {
        private int id;
        private String label;

        public ComboItem(int id, String label) {
            this.id = id;
            this.label = label;
        }
        public int getId() { return id; }
        @Override
        public String toString() { return label; } 
    }
}