package com.foodwaste.ui;

import com.foodwaste.util.DBManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private String selectedRole = "Donor";
    private boolean isLoginMode = true;

    // Design Colors
    private final Color COLOR_BG = new Color(0xFAF6F1);
    private final Color COLOR_TEXT_PRIMARY = new Color(0x3B2F2F);
    private final Color COLOR_TEXT_SECONDARY = new Color(0x7A6A5A);
    private final Color COLOR_DONOR = new Color(0x7BAE7F);
    private final Color COLOR_NGO = new Color(0x6A9AB0);
    private final Color COLOR_HIGHLIGHT = new Color(0xD4956A);
    private final Color COLOR_WHEAT = new Color(0xE8C99A);

    public LoginFrame() {
        setTitle("Khaana");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(COLOR_BG);

        mainContainer.add(createLandingScreen(), "SELECT");
        mainContainer.add(createFormScreen(), "FORM");

        add(mainContainer);
    }

    private JPanel createLandingScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BG);

        // Header
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(60, 0, 40, 0));
        GridBagConstraints gbcH = new GridBagConstraints();
        gbcH.gridx = 0;

        JLabel logo = new JLabel("🌾 Khaana");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        logo.setForeground(COLOR_TEXT_PRIMARY);
        gbcH.gridy = 0; header.add(logo, gbcH);

        JLabel tagline = new JLabel("Reduce Waste. Restore Hope.");
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        tagline.setForeground(COLOR_TEXT_SECONDARY);
        gbcH.gridy = 1; header.add(tagline, gbcH);
        
        panel.add(header, BorderLayout.NORTH);

        // Selection Cards
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.insets = new Insets(0, 12, 0, 12);

        JPanel donorCard = createRoleCard("I'm a Donor", "Share surplus food with those who need it.", "🥘", COLOR_DONOR, COLOR_WHEAT, "Donor");
        JPanel ngoCard = createRoleCard("I'm an NGO", "Find and collect food donations near you.", "📦", COLOR_NGO, COLOR_WHEAT, "NGO");

        gbcC.gridx = 0; center.add(donorCard, gbcC);
        gbcC.gridx = 1; center.add(ngoCard, gbcC);
        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRoleCard(String title, String desc, String icon, Color accent, Color highlight, String role) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(340, 380));
        card.setBackground(Color.WHITE);

        // Top Gradient Strip
        JPanel strip = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, accent, getWidth(), 0, highlight));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        strip.setPreferredSize(new Dimension(0, 6));
        card.add(strip, BorderLayout.NORTH);

        // Card Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(40, 30, 40, 30));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI", Font.PLAIN, 52));
        iconLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLbl.setForeground(COLOR_TEXT_PRIMARY);
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLbl = new JLabel("<html><center>" + desc + "</center></html>");
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLbl.setForeground(COLOR_TEXT_SECONDARY);
        descLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLbl.setMaximumSize(new Dimension(240, 60));

        JButton btn = new JButton("Continue as " + role + " →");
        btn.setBackground(accent);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> showRoleForm(role));

        // Hover Effect on card
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accent, 2, true),
                    new EmptyBorder(0, 0, 0, 0)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0xEDE3D8), 1, true),
                    new EmptyBorder(1, 1, 1, 1)
                ));
            }
        });

        content.add(iconLbl);
        content.add(Box.createRigidArea(new Dimension(0, 25)));
        content.add(titleLbl);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(descLbl);
        content.add(Box.createVerticalGlue());
        content.add(btn);

        card.add(content, BorderLayout.CENTER);
        
        // Custom Rounded Border with Warm Shadow equivalent
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xEDE3D8), 1, true),
            new EmptyBorder(1, 1, 1, 1)
        ));

        return card;
    }

    private void showRoleForm(String role) {
        this.selectedRole = role;
        isLoginMode = true;
        updateFormUI();
        cardLayout.show(mainContainer, "FORM");
    }

    private JPanel formCard;
    private JTextField idField, nameField, contactField, cityField;

    private JPanel createFormScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG);

        formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setPreferredSize(new Dimension(450, 550));
        formCard.setBorder(BorderFactory.createLineBorder(new Color(0xEDE3D8), 1, true));

        panel.add(formCard);
        return panel;
    }

    private void updateFormUI() {
        formCard.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 40, 10, 40);
        gbc.gridx = 0;

        Color accent = selectedRole.equals("Donor") ? COLOR_DONOR : COLOR_NGO;

        // Tab Switcher - Fixed layout for side-by-side view with more space
        JPanel tabs = new JPanel(new GridLayout(1, 2));
        tabs.setOpaque(false);
        tabs.setPreferredSize(new Dimension(380, 50));
        
        JButton loginTab = createTabButton("Login", isLoginMode, accent);
        JButton signupTab = createTabButton("Sign Up", !isLoginMode, accent);
        
        loginTab.addActionListener(e -> { isLoginMode = true; updateFormUI(); });
        signupTab.addActionListener(e -> { isLoginMode = false; updateFormUI(); });

        tabs.add(loginTab);
        tabs.add(signupTab);
        
        gbc.gridy = 0; 
        gbc.insets = new Insets(10, 30, 20, 30);
        formCard.add(tabs, gbc);

        if (isLoginMode) {
            addFormField(formCard, "Your Unique ID 🔑", gbc, 1);
            idField = new JTextField();
            idField.setPreferredSize(new Dimension(0, 40));
            gbc.gridy = 2; formCard.add(idField, gbc);

            JButton btn = new JButton("Login →");
            btn.setBackground(accent);
            btn.setForeground(Color.WHITE);
            btn.setPreferredSize(new Dimension(0, 45));
            gbc.gridy = 3; gbc.insets = new Insets(30, 40, 10, 40);
            formCard.add(btn, gbc);
            btn.addActionListener(e -> handleLogin());
        } else {
            addFormField(formCard, "Full Name / Organization", gbc, 1);
            nameField = new JTextField(); gbc.gridy = 2; formCard.add(nameField, gbc);
            
            addFormField(formCard, "Contact Number", gbc, 3);
            contactField = new JTextField(); gbc.gridy = 4; formCard.add(contactField, gbc);
            
            addFormField(formCard, "City", gbc, 5);
            cityField = new JTextField(); gbc.gridy = 6; formCard.add(cityField, gbc);

            JButton btn = new JButton("Create Account 🌿");
            btn.setBackground(accent);
            btn.setForeground(Color.WHITE);
            btn.setPreferredSize(new Dimension(0, 45));
            gbc.gridy = 7; gbc.insets = new Insets(20, 40, 10, 40);
            formCard.add(btn, gbc);
            btn.addActionListener(e -> handleSignup());
        }

        JButton back = new JButton("← Back");
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.setForeground(COLOR_TEXT_SECONDARY);
        back.addActionListener(e -> cardLayout.show(mainContainer, "SELECT"));
        gbc.gridy = 10; gbc.insets = new Insets(10, 40, 10, 40);
        formCard.add(back, gbc);

        formCard.revalidate();
        formCard.repaint();
    }

    private JButton createTabButton(String text, boolean active, Color accent) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 16));
        btn.setForeground(active ? COLOR_TEXT_PRIMARY : COLOR_TEXT_SECONDARY);
        btn.setBorder(active ? BorderFactory.createMatteBorder(0, 0, 3, 0, COLOR_HIGHLIGHT) : BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xEDE3D8)));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(0, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void addFormField(JPanel p, String label, GridBagConstraints g, int y) {
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(COLOR_TEXT_SECONDARY);
        g.gridy = y; g.insets = new Insets(10, 40, 2, 40);
        p.add(l, g);
        g.insets = new Insets(2, 40, 5, 40);
    }

    private void handleLogin() {
        try {
            int id = Integer.parseInt(idField.getText());
            boolean success = selectedRole.equals("Donor") ? DBManager.validateDonor(id) : DBManager.validateNGO(id);
            if (success) {
                if (selectedRole.equals("Donor")) new DonorDashboard(id).setVisible(true);
                else new NGODashboard(id).setVisible(true);
                this.dispose();
            } else JOptionPane.showMessageDialog(this, "Invalid ID");
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Check your ID"); }
    }

    private void handleSignup() {
        try {
            int id = selectedRole.equals("Donor") ? 
                DBManager.registerDonor(nameField.getText(), cityField.getText(), contactField.getText()) :
                DBManager.registerNGO(nameField.getText(), cityField.getText(), contactField.getText());
            
            // Warm Toast / Welcome Message
            JPanel toast = new JPanel(new FlowLayout(FlowLayout.CENTER));
            toast.setBackground(new Color(0xD6EAD7));
            toast.add(new JLabel("🌾 Welcome! Your Unique ID is: " + id));
            JOptionPane.showMessageDialog(this, toast, "Account Created", JOptionPane.PLAIN_MESSAGE);
            isLoginMode = true;
            updateFormUI();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }
}
