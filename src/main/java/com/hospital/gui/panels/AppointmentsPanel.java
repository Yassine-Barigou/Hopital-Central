package main.java.com.hospital.gui.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import javax.swing.AbstractCellEditor;

public class AppointmentsPanel extends JPanel {

    public AppointmentsPanel() {
        // 1. CONFIGURATION GLOBALE
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // 2. HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setOpaque(false);

        JLabel title = new JLabel("Gestion des Rendez-vous");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(33, 37, 41));

        JLabel subtitle = new JLabel("3 rendez-vous enregistrés");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);

        titleContainer.add(title);
        titleContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        titleContainer.add(subtitle);

        JButton addButton = new JButton("+  Nouveau rendez-vous");
        styleButton(addButton, new Color(16, 185, 129));

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // 3. CONTENU (Carte Blanche)
        JPanel contentCard = new JPanel(new BorderLayout());
        contentCard.setBackground(Color.WHITE);
        contentCard.setBorder(new LineBorder(new Color(230, 230, 230)));

        JPanel innerContent = new JPanel(new BorderLayout());
        innerContent.setOpaque(false);
        innerContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchPanel.setOpaque(false);
        JTextField searchField = new JTextField("Rechercher un rendez-vous...");
        searchField.setPreferredSize(new Dimension(300, 40));
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)));
        searchPanel.add(searchField);

        innerContent.add(searchPanel, BorderLayout.NORTH);

        // --- TABLEAU DES RENDEZ-VOUS ---
        String[] columnNames = {"Patient", "Médecin", "Date", "Heure", "Motif", "Status", "Actions"};
        
        Object[][] data = {
            {"Pierre Moreau", "Dr. Jean Martin", "2026-01-15", "09:00", "Suivi cardiologique", "Confirmé", ""},
            {"Isabelle Roux", "Dr. Claire Leroy", "2026-01-16", "11:00", "Contrôle neurologique", "En attente", ""},
            {"Michel Fournier", "Dr. Jean Martin", "2026-01-17", "15:30", "Examen de routine", "Confirmé", ""},
            {"Sarah Connor", "Dr. Sophie Bernard", "2026-01-18", "10:00", "Urgence", "Annulé", ""}
        };

        // Modèle : Seule la colonne Actions (Index 6) est éditable
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; 
            }
        };

        JTable table = new JTable(model);
        
        // Style Table
        table.setRowHeight(55);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(240, 240, 240));
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(232, 245, 254));
        table.setSelectionForeground(Color.BLACK);
        table.setFocusable(false);

        // Header Style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(100, 116, 139));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));

        // ✨ APPEL DES RENDERERS SPECIFIQUES

        // 1. Status Badge (Colonne 5)
        table.getColumnModel().getColumn(5).setCellRenderer(new StatusBadgeRenderer());

        // 2. Actions Buttons (Colonne 6)
        table.getColumnModel().getColumn(6).setCellRenderer(new ActionRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ActionEditor());

        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setOpaque(false);
        tableContainer.setBorder(new EmptyBorder(20, 0, 0, 0));
        tableContainer.add(scrollPane);

        innerContent.add(tableContainer, BorderLayout.CENTER);
        contentCard.add(innerContent);

        // Assemblage final
        add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.CENTER);
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setOpaque(false);
        mainContainer.add(headerPanel, BorderLayout.NORTH);

        JPanel spacer = new JPanel(new BorderLayout());
        spacer.setOpaque(false);
        spacer.setBorder(new EmptyBorder(20, 0, 0, 0));
        spacer.add(contentCard);

        mainContainer.add(spacer, BorderLayout.CENTER);
        add(mainContainer);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(220, 40));
    }

    // ==========================================================
    //  CLASSES INTERNES (INNER CLASSES)
    // ==========================================================

    // --- 1. Panel Actions ---
    class ActionPanel extends JPanel {
        JButton editBtn = new JButton("✏️");
        JButton deleteBtn = new JButton("🗑️");

        public ActionPanel() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
            setBackground(Color.WHITE);
            
            editBtn.setBorder(null); editBtn.setBackground(null); editBtn.setForeground(new Color(245, 158, 11));
            editBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            deleteBtn.setBorder(null); deleteBtn.setBackground(null); deleteBtn.setForeground(new Color(239, 68, 68));
            deleteBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            add(editBtn);
            add(deleteBtn);
        }
    }

    // --- 2. Action Renderer ---
    class ActionRenderer extends DefaultTableCellRenderer {
        private ActionPanel panel = new ActionPanel();
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return panel;
        }
    }

    // --- 3. Action Editor ---
    class ActionEditor extends AbstractCellEditor implements TableCellEditor {
        private ActionPanel panel = new ActionPanel();

        public ActionEditor() {
            panel.editBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, "Modifier le rendez-vous !");
                stopCellEditing();
            });
            panel.deleteBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, "Supprimer le rendez-vous !");
                stopCellEditing();
            });
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }
        @Override
        public Object getCellEditorValue() { return ""; }
    }

    // --- 4. Status Badge Renderer (Spécial pour RDV) ---
    class StatusBadgeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = (String) value;
            label.setText("  " + status + "  ");
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setOpaque(true);

            // Couleurs selon le Status
            if (status.equalsIgnoreCase("Confirmé")) {
                label.setBackground(new Color(209, 250, 229)); // Vert clair
                label.setForeground(new Color(6, 95, 70));     // Vert foncé
            } else if (status.equalsIgnoreCase("En attente")) {
                label.setBackground(new Color(254, 243, 199)); // Jaune/Orange clair
                label.setForeground(new Color(180, 83, 9));    // Orange foncé
            } else if (status.equalsIgnoreCase("Annulé")) {
                label.setBackground(new Color(254, 226, 226)); // Rouge clair
                label.setForeground(new Color(153, 27, 27));   // Rouge foncé
            } else {
                label.setBackground(new Color(241, 245, 249)); // Gris
                label.setForeground(new Color(71, 85, 105));
            }
            
            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
                label.setForeground(table.getSelectionForeground());
            }
            return label;
        }
    }
}