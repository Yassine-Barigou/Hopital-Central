package main.java.com.hospital.gui.panels;

import main.java.com.hospital.model.Employees;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;

public class EmployeeFormDialog extends JDialog {

    private JTextField tfFirstName;
    private JTextField tfLastName;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JTextField tfRole;
    private JTextField tfDepartment;
    private JTextField tfPhone;
    private JTextField tfHireDate; // format yyyy-MM-dd

    private boolean saved = false;
    private Employees employee;

    public EmployeeFormDialog(Employees employee) {
        this.employee = employee;

        setTitle("Formulaire Employé");
        setModal(true);
        setSize(400, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(8, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        form.add(new JLabel("Prénom :"));
        tfFirstName = new JTextField(employee.getFirstName() != null ? employee.getFirstName() : "");
        form.add(tfFirstName);

        form.add(new JLabel("Nom :"));
        tfLastName = new JTextField(employee.getLastName() != null ? employee.getLastName() : "");
        form.add(tfLastName);

        form.add(new JLabel("Email :"));
        tfEmail = new JTextField(employee.getEmail() != null ? employee.getEmail() : "");
        form.add(tfEmail);

        form.add(new JLabel("Mot de passe :"));
        pfPassword = new JPasswordField();
        form.add(pfPassword);

        form.add(new JLabel("Rôle :"));
        tfRole = new JTextField(employee.getRole() != null ? employee.getRole() : "");
        form.add(tfRole);

        form.add(new JLabel("Département :"));
        tfDepartment = new JTextField(employee.getDepartment() != null ? employee.getDepartment() : "");
        form.add(tfDepartment);

        form.add(new JLabel("Téléphone :"));
        tfPhone = new JTextField(employee.getPhone() != null ? employee.getPhone() : "");
        form.add(tfPhone);

        form.add(new JLabel("Date d'embauche (yyyy-MM-dd) :"));
        tfHireDate = new JTextField(employee.getHireDate() != null ? employee.getHireDate().toString() : LocalDate.now().toString());
        form.add(tfHireDate);

        add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Enregistrer");
        JButton btnCancel = new JButton("Annuler");

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            if (saveEmployee()) {
                saved = true;
                dispose();
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }

    private boolean saveEmployee() {
        try {
            employee.setFirstName(tfFirstName.getText().trim());
            employee.setLastName(tfLastName.getText().trim());
            employee.setEmail(tfEmail.getText().trim());
            employee.setPasswordHash(new String(pfPassword.getPassword())); // ⚡ mot de passe
            employee.setRole(tfRole.getText().trim());
            employee.setDepartment(tfDepartment.getText().trim());
            employee.setPhone(tfPhone.getText().trim());
            employee.setHireDate(Date.valueOf(tfHireDate.getText().trim())); // java.sql.Date
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            return false;
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public Employees getEmployee() {
        return employee;
    }
}
