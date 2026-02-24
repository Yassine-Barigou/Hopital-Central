package main.java.com.hospital.gui.panels;

import main.java.com.hospital.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class PatientFormDialog extends JDialog {

    private boolean saved = false;
    private Patient patient;

    // Les champs du formulaire
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtDateBirth; // Format YYYY-MM-DD
    private JComboBox<String> cbGender;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JComboBox<String> cbBloodType;
    private JTextArea txtAllergies;
    private JTextArea txtMedicalNotes;

    public PatientFormDialog(Patient p) {
        this.patient = p;
        
        // Configuration de la fenêtre Pop-up
        setTitle(p.getId() == 0 ? "Ajouter un Patient" : "Modifier le Patient");
        setModal(true); // Bach t-bloquer l-fenêtre li wraha
        setSize(450, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PANEL DU FORMULAIRE ---
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Initialisation des champs
        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtDateBirth = new JTextField();
        txtDateBirth.setToolTipText("Format: YYYY-MM-DD");
        
        cbGender = new JComboBox<>(new String[]{"Homme", "Femme"});
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextField();
        cbBloodType = new JComboBox<>(new String[]{"O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"});
        
        txtAllergies = new JTextArea(2, 20);
        txtMedicalNotes = new JTextArea(2, 20);

        // Ajout au formPanel
        formPanel.add(new JLabel("Prénom : *"));
        formPanel.add(txtFirstName);
        
        formPanel.add(new JLabel("Nom : *"));
        formPanel.add(txtLastName);
        
        formPanel.add(new JLabel("Date Naissance (YYYY-MM-DD): *"));
        formPanel.add(txtDateBirth);
        
        formPanel.add(new JLabel("Sexe :"));
        formPanel.add(cbGender);
        
        formPanel.add(new JLabel("Téléphone :"));
        formPanel.add(txtPhone);
        
        formPanel.add(new JLabel("Email :"));
        formPanel.add(txtEmail);
        
        formPanel.add(new JLabel("Adresse :"));
        formPanel.add(txtAddress);
        
        formPanel.add(new JLabel("Groupe Sanguin :"));
        formPanel.add(cbBloodType);
        
        formPanel.add(new JLabel("Allergies :"));
        formPanel.add(new JScrollPane(txtAllergies));
        
        formPanel.add(new JLabel("Notes Médicales :"));
        formPanel.add(new JScrollPane(txtMedicalNotes));

        add(formPanel, BorderLayout.CENTER);

        // --- PANEL DES BOUTONS ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Enregistrer");
        JButton btnCancel = new JButton("Annuler");

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        // --- REMPLIR LES CHAMPS SI C'EST UNE MODIFICATION ---
        if (p.getId() != 0) {
            txtFirstName.setText(p.getFirstName());
            txtLastName.setText(p.getLastName());
            if(p.getDateBirth() != null) txtDateBirth.setText(p.getDateBirth().toString());
            cbGender.setSelectedItem(p.getGende());
            txtPhone.setText(String.valueOf(p.getPhone()));
            txtEmail.setText(p.getEmail());
            txtAddress.setText(p.getAdress());
            cbBloodType.setSelectedItem(p.getBloodType());
            txtAllergies.setText(p.getAllergies());
            txtMedicalNotes.setText(p.getMedicalNotes());
        }

        // --- ACTIONS DES BOUTONS ---
        btnCancel.addActionListener(e -> dispose());

        btnSave.addActionListener(e -> savePatient());
    }

    private void savePatient() {
        // 1. Vérification dyal les champs obligatoires
        if (txtFirstName.getText().trim().isEmpty() || txtLastName.getText().trim().isEmpty() || txtDateBirth.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir les champs obligatoires (*).", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 2. Récupérer et convertir les valeurs
            patient.setFirstName(txtFirstName.getText().trim());
            patient.setLastName(txtLastName.getText().trim());
            
            // Convertir String to java.sql.Date
            patient.setDateBirth(Date.valueOf(txtDateBirth.getText().trim())); 
            
            patient.setGende(cbGender.getSelectedItem().toString());
            
            // Convertir String to int (Téléphone)
            String phoneStr = txtPhone.getText().trim();
            if(!phoneStr.isEmpty()) {
                patient.setPhone(Integer.parseInt(phoneStr));
            } else {
                patient.setPhone(0);
            }
            
            patient.setEmail(txtEmail.getText().trim());
            patient.setAdress(txtAddress.getText().trim());
            patient.setBloodType(cbBloodType.getSelectedItem().toString());
            patient.setAllergies(txtAllergies.getText().trim());
            patient.setMedicalNotes(txtMedicalNotes.getText().trim());

            // 3. Valider o nseddo l-fenêtre
            saved = true;
            dispose();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez YYYY-MM-DD (ex: 1990-05-20).", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode li kat 3lem l-Panel wach cliquina 3la Save wla Cancel
    public boolean isSaved() {
        return saved;
    }
}