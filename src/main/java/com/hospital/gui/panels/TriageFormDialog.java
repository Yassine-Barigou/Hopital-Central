package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.PatientDAO;
import main.java.com.hospital.model.Patient;
import main.java.com.hospital.model.Triage;
import main.java.com.hospital.model.Employees;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TriageFormDialog extends JDialog {

    private boolean saved = false;
    private Triage triage;
    private Employees currentNurse;

    private JComboBox<ComboItem> cbPatients;
    private JComboBox<String> cbPriority;
    private JTextArea txtSymptoms;

    public TriageFormDialog(Triage t, Employees nurse) {
        this.triage = t;
        this.currentNurse = nurse;
        
        setTitle("Nouveau Triage (Urgences)");
        setModal(true);
        setSize(450, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 20));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cbPatients = new JComboBox<>();
        loadPatients();

        String[] priorities = {"Rouge", "Orange", "Jaune", "Vert"};
        cbPriority = new JComboBox<>(priorities);
        
        txtSymptoms = new JTextArea(4, 20);

        formPanel.add(new JLabel("Patient : *"));
        formPanel.add(cbPatients);
        
        formPanel.add(new JLabel("Niveau d'Urgence : *"));
        formPanel.add(cbPriority);
        
        formPanel.add(new JLabel("Symptômes  : *"));
        formPanel.add(new JScrollPane(txtSymptoms));

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Enregistrer");
        JButton btnCancel = new JButton("Annuler");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> save());
    }

    private void loadPatients() {
        PatientDAO pDao = new PatientDAO();
        for (Patient p : pDao.getAllPatients()) {
            cbPatients.addItem(new ComboItem(p.getId(), p.getFirstName() + " " + p.getLastName()));
        }
    }

    private void save() {
        if (cbPatients.getSelectedItem() == null || txtSymptoms.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs (*)");
            return;
        }
        
        ComboItem selectedPat = (ComboItem) cbPatients.getSelectedItem();
        triage.setPatientId(selectedPat.getId());
        triage.setNurseId(currentNurse.getId());
        triage.setPriority(cbPriority.getSelectedItem().toString());
        triage.setSymptoms(txtSymptoms.getText().trim());
        
        saved = true;
        dispose();
    }

    public boolean isSaved() { return saved; }

    private class ComboItem {
        private int id;
        private String label;
        public ComboItem(int id, String label) { this.id = id; this.label = label; }
        public int getId() { return id; }
        @Override public String toString() { return label; } 
    }
}