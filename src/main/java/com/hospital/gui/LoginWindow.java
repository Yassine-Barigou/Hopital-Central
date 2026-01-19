package main.java.com.hospital.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import main.java.com.hospital.dao.EmployeesDAO;
import main.java.com.hospital.gui.panels.TechnicianDashboard;
import main.java.com.hospital.model.Employees;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class  LoginWindow extends JFrame{
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginWindow(){
        setTitle("Hôpital Central - Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,700);
        setLocationRelativeTo(null);

        //background
        getContentPane().setBackground(new Color(240,244,248));
        setLayout(new GridBagLayout());
        

        //hadik l3yba li kayna wsst 
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(450,550));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40,40,40,40));


        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(card, gbc);
         // nbadw les componet 
         // icone  
        JPanel iconCircle = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Couleur du cercle (le bleu très clair de l'image)
        g2.setColor(new Color(232, 241, 242)); 
        g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
        g2.dispose();
        }
        };

       
iconCircle.setLayout(new GridBagLayout()); // Pour centrer l'icône parfaitement
iconCircle.setPreferredSize(new Dimension(100, 100));
iconCircle.setMaximumSize(new Dimension(100, 100)); // Important pour BoxLayout

iconCircle.setAlignmentX(Component.CENTER_ALIGNMENT);

// 3.nzido icon wasts circle
JLabel iconLabel = new JLabel("🏥");
iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
iconLabel.setForeground(new Color(0, 102, 110)); // Couleur sombre de l'image
iconCircle.add(iconLabel);

card.add(iconCircle);

// 3iwd mn dir boarder hadi khdma m3a l3yba dyal position vertival deja fait pour que  pas repeter a chaque fois hadak border
card.add(Box.createRigidArea(new Dimension(0, 20)));

//titre details
JLabel titleLabel = new JLabel("Hôpital Central"); 
titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
card.add(titleLabel);
card.add(Box.createRigidArea(new Dimension(0, 5)));

// sous titre
JLabel subTitleLabel = new JLabel("Connectez-vous à votre espace de gestion");
subTitleLabel.setForeground(Color.GRAY);
subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
card.add(subTitleLabel);
card.add(Box.createRigidArea(new Dimension(0, 40)));
// email

JPanel emailGroup = new JPanel();
emailGroup.setLayout(new BoxLayout(emailGroup, BoxLayout.Y_AXIS));
emailGroup.setOpaque(false);
emailGroup.setMaximumSize(new Dimension(350, 80));
emailGroup.setAlignmentX(Component.CENTER_ALIGNMENT); 

JLabel emailLabel = createLabel("Email");
emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT); 
emailGroup.add(emailLabel);

emailGroup.add(Box.createRigidArea(new Dimension(0, 8))); 

// Ajout du Champ dans le groupe
emailField = new JTextField();
styleField(emailField);
emailField.setAlignmentX(Component.LEFT_ALIGNMENT); 
emailGroup.add(emailField);

card.add(emailGroup); 
card.add(Box.createRigidArea(new Dimension(0, 20)));

// password
JPanel passwordGroup = new JPanel();
passwordGroup.setLayout(new BoxLayout(passwordGroup, BoxLayout.Y_AXIS));
passwordGroup.setOpaque(false);
passwordGroup.setMaximumSize(new Dimension(350, 80)); 
passwordGroup.setAlignmentX(Component.CENTER_ALIGNMENT); 


JLabel passwordLabel = createLabel("Mot de passe");
passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT); 
passwordGroup.add(passwordLabel);

passwordGroup.add(Box.createRigidArea(new Dimension(0, 8))); 


passwordField = new JPasswordField();
styleField(passwordField); 
passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
passwordGroup.add(passwordField);

card.add(passwordGroup);


card.add(Box.createRigidArea(new Dimension(0, 30)));

//button 

JPanel buttonGroup = new JPanel();
buttonGroup.setLayout(new BoxLayout(buttonGroup, BoxLayout.Y_AXIS));
buttonGroup.setOpaque(false);
buttonGroup.setMaximumSize(new Dimension(350, 50)); 
buttonGroup.setAlignmentX(Component.CENTER_ALIGNMENT);

loginButton = new JButton("Se connecter");
styleButton(loginButton);
buttonGroup.add(loginButton);

card.add(buttonGroup);

loginButton.addActionListener(e -> {
    String email = emailField.getText();
    String password = new String(passwordField.getPassword());

    EmployeesDAO dao = new EmployeesDAO();
    Employees user = dao.checkLogin(email, password);
    System.out.print(user);

    if (user != null) {
        String role = user.getRole(); 
        
        // 1. Redirection vers la fenêtre principale (DashboardWindow)
        // On passe l'objet 'user' à la fenêtre pour qu'elle sache qui est connecté
        DashboardWindow dashboard = new DashboardWindow(user); 
        dashboard.setVisible(true); 
        
        this.dispose(); // Ferme le login
    } else {
        JOptionPane.showMessageDialog(this, "Email ou mot de passe incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
});




        
    }
    //HHH

private JLabel createLabel(String text) {
    JLabel label = new JLabel(text);
    label.setFont(new Font("Segoe UI", Font.BOLD, 14));
    label.setForeground(new Color(33, 37, 41));
    return label;
}

private void styleField(JTextField field) {
    field.setBorder(new RoundedBorder(new Color(206, 212, 218), 15));
    field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
    field.setBackground(Color.WHITE);
    
    // On force la largeur pour qu'il remplisse le emailGroup
    field.setPreferredSize(new Dimension(350, 45));
    field.setMaximumSize(new Dimension(350, 45));
}

    // --- Classe pour la bordure arrondie ---
    class RoundedBorder extends javax.swing.border.AbstractBorder {
        private Color color;
        private int radius;

        RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }
        @Override
        public Insets getBorderInsets(Component c) {
        // top, left, bottom, right
        return new Insets(8, 15, 8, 15); 
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.right = 15;
        insets.top = insets.bottom = 8;
        return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
            g2.dispose();
        }       
}
private void styleButton(JButton button) {
    Color originalColor = new Color(0, 102, 110);
    Color hoverColor = new Color(0, 125, 135); // Un bleu un peu plus clair pour le hover

    button.setBackground(originalColor);
    button.setForeground(Color.WHITE);
    button.setFont(new Font("Segoe UI", Font.BOLD, 16));
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setMaximumSize(new Dimension(350, 50));
    button.setPreferredSize(new Dimension(350, 50));
    button.setAlignmentX(Component.CENTER_ALIGNMENT);

    // --- AJOUT DE L'EFFET HOVER ---
    button.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            button.setBackground(hoverColor); // Change vers la couleur claire
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
            button.setBackground(originalColor); // Revient à la couleur d'origine
        }
    });

    button.setContentAreaFilled(false);
    button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Cette ligne va maintenant utiliser la couleur modifiée par le MouseListener
            g2.setColor(c.getBackground()); 
            
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 15, 15);
            g2.dispose();
            super.paint(g, c);
        }
    });
}       
}