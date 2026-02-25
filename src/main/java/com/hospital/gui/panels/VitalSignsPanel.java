package main.java.com.hospital.gui.panels;

import main.java.com.hospital.dao.PatientDAO;
import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.dao.VitalSignDAO;
import main.java.com.hospital.model.Patient;
import main.java.com.hospital.model.Employees;
import main.java.com.hospital.model.VitalSign;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
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
        
        // Style Layout
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("🩺 Constantes Vitales");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(30, 41, 59));
        
        JLabel subtitle = new JLabel("Suivi clinique : Température, Tension, Rythme cardiaque et Poids");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Date", "Patient", "Temp. (°C)", "Tension", "Rythme (bpm)", "Poids (kg)", "Notes", "Infirmier"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(model);
        setupTableStyle();
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        add(scroll, BorderLayout.CENTER);

        // Boutons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        
        if ("Infirmier".equalsIgnoreCase(user.getRole()) || "Admin".equalsIgnoreCase(user.getRole())) {
            JButton btnAdd = createStyledButton("➕ Prendre Constantes", new Color(37, 99, 235), Color.WHITE);
            JButton btnDelete = createStyledButton("🗑️ Supprimer", new Color(220, 38, 38), Color.WHITE);
            
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
                        int id = Integer.parseInt(table.getValueAt(row, 0).toString()); 
                        int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cet enregistrement ?", "Confirmer", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            if (dao.deleteVitalSign(id)) {
                                refreshTable();
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne.");
                }
            });
        }
        
        add(btnPanel, BorderLayout.SOUTH);
        refreshTable();
    }

    private void setupTableStyle() {
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setGridColor(new Color(241, 245, 249));
        table.setShowVerticalLines(false);
        
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // Styling spécifique pour les colonnes numériques (Temp, Tension, Rythme)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(190, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
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
                    vs.getTemperature() + " °C",
                    vs.getBloodPressure(),
                    vs.getHeartRate() + " bpm",
                    vs.getWeight() + " kg",
                    vs.getNotes(),
                    nurseName
            });
        }
    }
}