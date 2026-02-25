package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.PatientDAO;
import main.java.com.hospital.model.Patient;
import main.java.com.hospital.model.VitalSign;
import main.java.com.hospital.model.Employees;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VitalSignFormDialog extends JDialog {

    private boolean saved = false;
    private VitalSign vitalSign;
    private Employees currentNurse;

    private JComboBox<ComboItem> cbPatients;
    private JTextField txtTemp;
    private JTextField txtBP;
    private JTextField txtHeart;
    private JTextField txtWeight;
    private JTextArea txtNotes;

    public VitalSignFormDialog(VitalSign vs, Employees nurse) {
        this.vitalSign = vs;
        this.currentNurse = nurse;
        
        setTitle("Prendre les Constantes Vitales");
        setModal(true);
        setSize(450, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cbPatients = new JComboBox<>();
        loadPatients();

        txtTemp = new JTextField();
        txtTemp.setToolTipText("Ex: 37.5");
        
        txtBP = new JTextField();
        txtBP.setToolTipText("Ex: 120/80");
        
        txtHeart = new JTextField();
        txtHeart.setToolTipText("Ex: 75");
        
        txtWeight = new JTextField();
        txtWeight.setToolTipText("Ex: 70.5");
        
        txtNotes = new JTextArea(3, 20);

        formPanel.add(new JLabel("Patient : *"));
        formPanel.add(cbPatients);
        formPanel.add(new JLabel("Température (°C) : *"));
        formPanel.add(txtTemp);
        formPanel.add(new JLabel("Tension (mmHg) : *"));
        formPanel.add(txtBP);
        formPanel.add(new JLabel("Rythme Cardiaque (bpm) : *"));
        formPanel.add(txtHeart);
        formPanel.add(new JLabel("Poids (Kg) :"));
        formPanel.add(txtWeight);
        formPanel.add(new JLabel("Notes / Symptômes :"));
        formPanel.add(new JScrollPane(txtNotes));

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
        List<Patient> patients = pDao.getAllPatients();
        for (Patient p : patients) {
            cbPatients.addItem(new ComboItem(p.getId(), p.getFirstName() + " " + p.getLastName()));
        }
    }

    private void save() {
        if (cbPatients.getSelectedItem() == null || txtTemp.getText().isEmpty() || txtBP.getText().isEmpty() || txtHeart.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir les champs obligatoires (*)", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ComboItem selectedPat = (ComboItem) cbPatients.getSelectedItem();
            vitalSign.setPatientId(selectedPat.getId());
            vitalSign.setNurseId(currentNurse.getId()); 
            vitalSign.setTemperature(Double.parseDouble(txtTemp.getText().trim()));
            vitalSign.setBloodPressure(txtBP.getText().trim());
            vitalSign.setHeartRate(Integer.parseInt(txtHeart.getText().trim()));
            vitalSign.setWeight(txtWeight.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(txtWeight.getText().trim()));
            vitalSign.setNotes(txtNotes.getText().trim());
            
            saved = true;
            dispose();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Format de nombre invalide. Utilisez des chiffres (ex: 37.5).", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
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