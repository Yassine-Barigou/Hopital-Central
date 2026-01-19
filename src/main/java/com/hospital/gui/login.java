package 
main.java.com.hospital.gui;
import java.awt.*;

import javax.sound.sampled.Line;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class login extends JFrame {
    private JButton connecter ;
    private JTextField textField ;
    private JPasswordField passwordField;
    public login(){
        setTitle("hospitale");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(240,244,248));
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(450,550));
        card.setBackground(Color.white);
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS ));
        LineBorder lineBorder = new LineBorder(new Color(240,244,248),16,true);
        EmptyBorder emptyBorder = new EmptyBorder(40,40,40,40);
        card.setBorder(new CompoundBorder(lineBorder,emptyBorder));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx =gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = gbc.weighty = 1;

        JPanel iconCircle = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Couleur du cercle (le bleu très clair de l'image)
                g2.setColor(new Color(232, 241, 242)); 
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                }
        };
        iconCircle.setLayout(new GridBagLayout());
        iconCircle.setPreferredSize(new Dimension(100,100));
        iconCircle.setMaximumSize(new Dimension(100,100));
        iconCircle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emoji = new JLabel("🏥");
        emoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
        emoji.setForeground(new Color(0, 102, 110));
        iconCircle.add(emoji);
        card.add(iconCircle);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel titleLabel = new JLabel("Hôpital Central"); 
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0,5)));

        JLabel subtitle = new JLabel("Connectez-vous à votre espace de gestion");
        subtitle.setForeground(Color.gray);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0,40)));

            



        add(card,gbc);

    }
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new login().setVisible(true);
    });
}
}

/*login (extends JFrame)
│
├── constructor login()
│     - إعدادات النافذة (title, size, close, location)
│     - إعداد Layout (GridBagLayout)
│     - إنشاء JPanel card
│     - إنشاء المكونات (labels, fields, buttons)
│     - ترتيب المكونات داخل card
│     - إضافة card للنافذة
│
├── Methods خارج constructor
│     - أي وظائف مساعدة (مثلاً styleButton(), styleField())
│     - أي action listeners
│
└── main()
      - إنشاء نافذة وتشغيلها
      */