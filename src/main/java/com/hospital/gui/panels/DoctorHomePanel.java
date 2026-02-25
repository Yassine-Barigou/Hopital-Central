package main.java.com.hospital.gui.panels;

import main.java.com.hospital.model.Employees;
import main.java.com.hospital.model.Appointment;
import main.java.com.hospital.model.Consultation;
import main.java.com.hospital.model.Patient;
import main.java.com.hospital.dao.AppointmentDAO;
import main.java.com.hospital.dao.ConsultationDAO;
import main.java.com.hospital.dao.PatientDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorHomePanel extends JPanel {

    private AppointmentDAO apptDao = new AppointmentDAO();
    private ConsultationDAO consDao = new ConsultationDAO();
    private PatientDAO patDao = new PatientDAO();

    public DoctorHomePanel(Employees doctor) {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252)); 
        
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 250, 252));
        mainContent.setBorder(new EmptyBorder(30, 30, 30, 30)); 

        
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Bonjour, Dr. " + doctor.getLastName());
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(33, 37, 41));

        JLabel subtitle = new JLabel(doctor.getDepartment() + " - Espace Médical");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setForeground(Color.GRAY);

        headerPanel.add(title);
        headerPanel.add(subtitle);
        
        mainContent.add(headerPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

    
        int myId = doctor.getId(); 
        List<Patient> allPatients = patDao.getAllPatients();

        List<Appointment> myAppts = apptDao.getAllAppointments().stream()
                .filter(a -> a.getDoctorId() == myId)
                .collect(Collectors.toList());

        List<Consultation> myCons = consDao.getAllConsultations().stream()
                .filter(c -> c.getDoctorId() == myId)
                .collect(Collectors.toList());

        long mesRdvAVenir = myAppts.stream()
                .filter(a -> "En attente".equalsIgnoreCase(a.getStatus()) || "Confirmé".equalsIgnoreCase(a.getStatus()))
                .count();

        long mesConsultations = myCons.size();

        
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 25, 0)); 
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        statsPanel.add(createStatCard("Mes RDV à venir", String.valueOf(mesRdvAVenir), new Color(245, 158, 11), "📅")); 
        
        statsPanel.add(createStatCard("Consultations effectuées", String.valueOf(mesConsultations), new Color(59, 130, 246), "🩺")); // Loun zre9

        mainContent.add(statsPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

    
        JPanel bottomSection = new JPanel(new GridLayout(1, 2, 25, 0)); 
        bottomSection.setOpaque(false);
        bottomSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel rdvCard = createSectionCard("Mon programme", "Vos prochains patients à consulter");
        
        List<Appointment> upcomingAppts = myAppts.stream()
                .filter(a -> "En attente".equalsIgnoreCase(a.getStatus()) || "Confirmé".equalsIgnoreCase(a.getStatus()))
                .limit(5)
                .collect(Collectors.toList());

        for (Appointment a : upcomingAppts) {
            String patName = allPatients.stream()
                .filter(p -> p.getId() == a.getPatientId())
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .findFirst().orElse("Patient Inconnu");

            String[] dateParts = a.getFormattedDate().split(" ");
            String dateStr = dateParts.length > 0 ? dateParts[0] : "";
            String timeStr = dateParts.length > 1 ? dateParts[1] : "";

            addRdvItem(rdvCard, patName, a.getType(), dateStr, timeStr);
        }
        if (upcomingAppts.isEmpty()) addRdvItem(rdvCard, "Aucun patient prévu", "-", "-", "-");

        rdvCard.add(Box.createVerticalGlue());
        bottomSection.add(rdvCard);

        JPanel activityCard = createSectionCard("Historique récent", "Vos derniers diagnostics");
        
        int actLimit = Math.min(5, myCons.size());
        for (int i = 0; i < actLimit; i++) {
            Consultation c = myCons.get(i);
            String patName = allPatients.stream()
                .filter(p -> p.getId() == c.getPatientId())
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .findFirst().orElse("Patient Inconnu");

            String motif = c.getReason();
            if (motif.length() > 20) motif = motif.substring(0, 20) + "...";
            
            addActivityItem(activityCard, patName + " - " + motif, c.getFormattedDate());
        }
        if (actLimit == 0) addActivityItem(activityCard, "Aucune consultation récente", "");

        activityCard.add(Box.createVerticalGlue());
        bottomSection.add(activityCard);

        mainContent.add(bottomSection);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    
    
    private JPanel createStatCard(String title, String value, Color iconColor, String iconTxt) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(25, 30, 25, 30) 
        ));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblTitle.setForeground(new Color(100, 116, 139)); 
        JLabel icon = new JLabel(iconTxt); 
        icon.setForeground(iconColor);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        top.add(lblTitle, BorderLayout.WEST);
        top.add(icon, BorderLayout.EAST);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblValue.setForeground(new Color(33, 37, 41));
        lblValue.setBorder(new EmptyBorder(15, 0, 0, 0)); 

        card.add(top, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    private JPanel createSectionCard(String title, String subtitle) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(new Color(33, 37, 41));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(Color.GRAY);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblSub);
        card.add(Box.createRigidArea(new Dimension(0, 25))); 
        return card;
    }

    private void addActivityItem(JPanel container, String text, String time) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); 
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblText = new JLabel(text);
        lblText.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblText.setForeground(new Color(51, 65, 85));

        JLabel lblTime = new JLabel(time);
        lblTime.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTime.setForeground(new Color(148, 163, 184)); 

        row.add(lblText, BorderLayout.WEST);
        row.add(lblTime, BorderLayout.EAST);
        
        container.add(row);
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(240, 240, 240));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(sep);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void addRdvItem(JPanel container, String name, String motif, String date, String heure) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel left = new JPanel(new GridLayout(2, 1));
        left.setOpaque(false);
        
        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblName.setForeground(new Color(33, 37, 41));
        
        JLabel lblMotif = new JLabel(motif);
        lblMotif.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblMotif.setForeground(Color.GRAY);
        
        left.add(lblName);
        left.add(lblMotif);

        JPanel right = new JPanel(new GridLayout(2, 1));
        right.setOpaque(false);
        
        JLabel lblDate = new JLabel(date);
        lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDate.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblDate.setForeground(new Color(51, 65, 85));
        
        JLabel lblHeure = new JLabel(heure);
        lblHeure.setHorizontalAlignment(SwingConstants.RIGHT);
        lblHeure.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblHeure.setForeground(new Color(100, 116, 139));

        right.add(lblDate);
        right.add(lblHeure);

        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);

        container.add(row);
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(240, 240, 240));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(sep);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
    }
}