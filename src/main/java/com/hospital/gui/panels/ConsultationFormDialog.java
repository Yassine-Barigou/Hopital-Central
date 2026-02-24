package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.dao.PatientDAO;
import main.java.com.hospital.model.Consultation;
import main.java.com.hospital.model.Employees;
import main.java.com.hospital.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class ConsultationFormDialog extends JDialog {

    private boolean saved = false;
    private Consultation consultation;

    private JComboBox<ComboItem> cbPatients;
    private JComboBox<ComboItem> cbDoctors;
    private JTextField txtDate; // YYYY-MM-DD
    private JTextField txtTime; // HH:MM
    private JTextField txtReason;
    private JTextArea txtDiagnosis;
    private JTextArea txtPrescription;
    private JComboBox<String> cbStatus;

    public ConsultationFormDialog(Consultation c) {
        this.consultation = c;
        setTitle(c.getId() == 0 ? "Nouvelle Consultation" : "Modifier Consultation");
        setModal(true);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Initialisation Dropdowns
        cbPatients = new JComboBox<>();
        cbDoctors = new JComboBox<>();
        loadDropdowns();

        txtDate = new JTextField();
        txtDate.setToolTipText("Ex: 2026-05-14");
        txtTime = new JTextField();
        txtTime.setToolTipText("Ex: 14:30");
        
        txtReason = new JTextField();
        txtDiagnosis = new JTextArea(3, 20);
        txtPrescription = new JTextArea(3, 20);
        cbStatus = new JComboBox<>(new String[]{"Planifiée", "En cours", "Terminée", "Annulée"});

        formPanel.add(new JLabel("Patient : *"));
        formPanel.add(cbPatients);
        
        formPanel.add(new JLabel("Médecin : *"));
        formPanel.add(cbDoctors);
        
        formPanel.add(new JLabel("Date (YYYY-MM-DD) : *"));
        formPanel.add(txtDate);
        
        formPanel.add(new JLabel("Heure (HH:MM) : *"));
        formPanel.add(txtTime);
        
        formPanel.add(new JLabel("Motif : *"));
        formPanel.add(txtReason);
        
        formPanel.add(new JLabel("Diagnostic :"));
        formPanel.add(new JScrollPane(txtDiagnosis));
        
        formPanel.add(new JLabel("Traitement/Ordonnance :"));
        formPanel.add(new JScrollPane(txtPrescription));
        
        formPanel.add(new JLabel("Statut :"));
        formPanel.add(cbStatus);

        add(formPanel, BorderLayout.CENTER);

        // Boutons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Enregistrer");
        JButton btnCancel = new JButton("Annuler");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        // Pré-remplir si modification
        if (c.getId() != 0) {
            selectItemById(cbPatients, c.getPatientId());
            selectItemById(cbDoctors, c.getDoctorId());
            if (c.getDate() != null) {
                String fullDate = c.getDate().toString(); // format: "YYYY-MM-DD HH:MM:SS.0"
                txtDate.setText(fullDate.substring(0, 10));
                txtTime.setText(fullDate.substring(11, 16));
            }
            txtReason.setText(c.getReason());
            txtDiagnosis.setText(c.getDiagnosis());
            txtPrescription.setText(c.getPrescription());
            cbStatus.setSelectedItem(c.getStatus());
        } else {
            cbStatus.setSelectedItem("Planifiée");
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
            txtDate.getText().trim().isEmpty() || txtTime.getText().trim().isEmpty() || txtReason.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires (*)");
            return;
        }

        try {
            ComboItem selectedPatient = (ComboItem) cbPatients.getSelectedItem();
            ComboItem selectedDoc = (ComboItem) cbDoctors.getSelectedItem();

            consultation.setPatientId(selectedPatient.getId());
            consultation.setDoctorId(selectedDoc.getId());
            
            // Formatage de la date w sa3a l Timestamp dial SQL
            String dateTimeStr = txtDate.getText().trim() + " " + txtTime.getText().trim() + ":00";
            consultation.setDate(Timestamp.valueOf(dateTimeStr)); // ✅ Hada l-Timestamp msa7a7
            
            consultation.setReason(txtReason.getText().trim());
            consultation.setDiagnosis(txtDiagnosis.getText().trim());
            consultation.setPrescription(txtPrescription.getText().trim());
            consultation.setStatus(cbStatus.getSelectedItem().toString());

            saved = true;
            dispose();
            
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Format de date ou d'heure invalide.\nExemple Date: 2024-05-14\nExemple Heure: 14:30", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    // Classe utilitaire pour afficher le Nom mais garder l'ID en background
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