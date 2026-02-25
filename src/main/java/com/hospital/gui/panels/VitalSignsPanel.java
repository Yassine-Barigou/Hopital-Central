package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.PatientDAO;
import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.dao.VitalSignDAO;
import main.java.com.hospital.model.Patient;
import main.java.com.hospital.model.Employees;
import main.java.com.hospital.model.VitalSign;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VitalSignsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private VitalSignDAO dao = new VitalSignDAO();
    private PatientDAO patDao = new PatientDAO();
    private EmployeesDAO empDao = new EmployeesDAO();
    private Employees currentUser;

    public VitalSignsPanel(Employees user) {
        this.currentUser = user;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("🩺 Constantes Vitales");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Date", "Patient", "Temp. (°C)", "Tension", "Rythme (bpm)", "Poids (kg)", "Notes", "Infirmier"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        if ("Infirmier".equalsIgnoreCase(user.getRole()) || "Admin".equalsIgnoreCase(user.getRole())) {
            JButton btnAdd = new JButton("➕ Prendre Constantes");
            JButton btnDelete = new JButton("🗑️ Supprimer");
            btnDelete.setForeground(Color.RED);
            
            btnPanel.add(btnAdd);
            btnPanel.add(btnDelete);
            
            btnAdd.addActionListener(e -> {
                VitalSign vs = new VitalSign();
                VitalSignFormDialog dialog = new VitalSignFormDialog(vs, currentUser);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    dao.addVitalSign(vs);
                    refreshTable();
                }
            });

            btnDelete.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    try {
                        // Jbid l-ID mn l-jadwal
                        Object value = table.getValueAt(row, 0);
                        int id = Integer.parseInt(value.toString()); 
                        
                        int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cet enregistrement ?", "Confirmer", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            
                            boolean success = dao.deleteVitalSign(id);
                            
                            if (success) {
                                refreshTable();
                            } else {
                                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erreur : Impossible de lire l'ID de la ligne.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne d'abord.", "Attention", JOptionPane.WARNING_MESSAGE);
                }
            });
        }
        
        add(btnPanel, BorderLayout.SOUTH);
        refreshTable();
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<VitalSign> list = dao.getAllVitalSigns();
        List<Patient> patients = patDao.getAllPatients();
        List<Employees> nurses = empDao.getAllEmployees();

        for (VitalSign vs : list) {
            String patName = patients.stream().filter(p -> p.getId() == vs.getPatientId()).map(p -> p.getFirstName() + " " + p.getLastName()).findFirst().orElse("Inconnu");
            String nurseName = nurses.stream().filter(n -> n.getId() == vs.getNurseId()).map(n -> n.getFirstName() + " " + n.getLastName()).findFirst().orElse("Inconnu");

            model.addRow(new Object[]{
                    vs.getId(),
                    vs.getCreatedAt(),
                    patName,
                    vs.getTemperature(),
                    vs.getBloodPressure(),
                    vs.getHeartRate(),
                    vs.getWeight(),
                    vs.getNotes(),
                    nurseName
            });
        }
    }
}