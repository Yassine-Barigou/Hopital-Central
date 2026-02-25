package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.PatientDAO;
import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.dao.TriageDAO;
import main.java.com.hospital.model.Patient;
import main.java.com.hospital.model.Employees;
import main.java.com.hospital.model.Triage;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TriagePanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private TriageDAO dao = new TriageDAO();
    private PatientDAO patDao = new PatientDAO();
    private EmployeesDAO empDao = new EmployeesDAO();
    private Employees currentUser;

    public TriagePanel(Employees user) {
        this.currentUser = user;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("🚨 File d'Attente des Urgences");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(220, 38, 38)); // Loun 7mer
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Heure d'arrivée", "Patient", "Priorité", "Symptômes", "Infirmier(e)"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    String prio = value.toString();
                    setHorizontalAlignment(SwingConstants.CENTER);
                    setFont(new Font("SansSerif", Font.BOLD, 13));
                    if ("Rouge".equals(prio)) { c.setBackground(new Color(254, 202, 202)); c.setForeground(new Color(153, 27, 27)); }
                    else if ("Orange".equals(prio)) { c.setBackground(new Color(254, 215, 170)); c.setForeground(new Color(154, 52, 18)); }
                    else if ("Jaune".equals(prio)) { c.setBackground(new Color(254, 240, 138)); c.setForeground(new Color(133, 77, 14)); }
                    else if ("Vert".equals(prio)) { c.setBackground(new Color(187, 247, 208)); c.setForeground(new Color(22, 101, 52)); }
                }
                if (isSelected) { c.setBackground(table.getSelectionBackground()); c.setForeground(table.getSelectionForeground()); }
                return c;
            }
        });
        
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        if ("Infirmier".equalsIgnoreCase(user.getRole()) || "Admin".equalsIgnoreCase(user.getRole())) {
            JButton btnAdd = new JButton("🚨 Nouveau Triage");
            btnAdd.setBackground(new Color(220, 38, 38));
            btnAdd.setForeground(Color.WHITE);
            btnPanel.add(btnAdd);
            
            btnAdd.addActionListener(e -> {
                Triage t = new Triage();
                TriageFormDialog dialog = new TriageFormDialog(t, currentUser);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    dao.addTriage(t);
                    refreshTable();
                }
            });
        }

        if ("Médecin".equalsIgnoreCase(user.getRole()) || "Admin".equalsIgnoreCase(user.getRole())) {
            JButton btnTreat = new JButton("✅ Marquer comme Traité");
            btnTreat.setForeground(new Color(22, 163, 74));
            btnPanel.add(btnTreat);

            btnTreat.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    try {
                        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                        int confirm = JOptionPane.showConfirmDialog(this, "Avez-vous terminé la consultation de ce patient ?", "Validation", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            if (dao.markAsTreated(id)) {
                                refreshTable();
                            }
                        }
                    } catch (Exception ex) { ex.printStackTrace(); }
                } else {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un patient dans la liste.");
                }
            });
        }
        
        add(btnPanel, BorderLayout.SOUTH);
        refreshTable();
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<Triage> list = dao.getPendingTriages(); 
        List<Patient> patients = patDao.getAllPatients();
        List<Employees> nurses = empDao.getAllEmployees();

        for (Triage t : list) {
            String patName = patients.stream().filter(p -> p.getId() == t.getPatientId()).map(p -> p.getFirstName() + " " + p.getLastName()).findFirst().orElse("Inconnu");
            String nurseName = nurses.stream().filter(n -> n.getId() == t.getNurseId()).map(n -> n.getFirstName() + " " + n.getLastName()).findFirst().orElse("Inconnu");

            model.addRow(new Object[]{
                    t.getId(),
                    t.getCreatedAt().toString().substring(11, 16), // Kanjbdou ghir s-sa3a (Ex: 14:30)
                    patName,
                    t.getPriority(),
                    t.getSymptoms(),
                    nurseName
            });
        }
    }
}