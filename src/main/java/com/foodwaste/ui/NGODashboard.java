package com.foodwaste.ui;

import com.foodwaste.util.DBManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class NGODashboard extends JFrame {
    private int ngoId;
    private JTable availableTable;
    private JTable claimedTable;
    private DefaultTableModel availableTableModel;
    private DefaultTableModel claimedTableModel;
    private JPanel contentCards;
    private CardLayout cardLayout;

    // Sidebar state
    private JPanel sidebar;
    private java.util.List<JButton> navButtons = new java.util.ArrayList<>();
    private JLabel sidebarNameLbl;

    // KPI Labels
    private JLabel availableLbl = new JLabel("0");
    private JLabel claimedLbl = new JLabel("0");
    private JLabel servedLbl = new JLabel("0");
    private JLabel activeDonorsLbl = new JLabel("0");
    
    // Detailed Stats (Our Impact Tab)
    private JLabel statTotalServedLbl = new JLabel("0");
    private JProgressBar statProgressBar = new JProgressBar(0, 2000);
    
    // Profile Fields
    private JTextField profileNameField;
    private JTextField profileAddressField;
    private JTextField profileContactField;

    // Design Colors
    private final Color COLOR_BG = new Color(0xFAF6F1);
    private final Color COLOR_SIDEBAR = new Color(0x182028);
    private final Color COLOR_ACCENT = new Color(0x6A9AB0);
    private final Color COLOR_WHEAT = new Color(0xE8C99A);
    private final Color COLOR_TEXT_PRIMARY = new Color(0x3B2F2F);
    private final Color COLOR_TEXT_SECONDARY = new Color(0x7A6A5A);
    private final Color COLOR_TERRACOTTA = new Color(0xD4956A);
    private final Color COLOR_SUCCESS_BG = new Color(0xD6EAD7);

    public NGODashboard(int ngoId) {
        this.ngoId = ngoId;
        setTitle("Khaana NGO Impact Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 850);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(COLOR_BG);

        // SIDEBAR
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBackground(COLOR_SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(30, 0, 30, 0));

        JLabel logo = new JLabel("🌾 Khaana");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        logo.setForeground(COLOR_WHEAT);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        logo.setBorder(new EmptyBorder(0, 15, 0, 0));
        sidebar.add(logo);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 50)));

        addNavButton(sidebar, "🏠  Dashboard", "DASHBOARD");
        addNavButton(sidebar, "🗺️  Find Food", "FIND");
        addNavButton(sidebar, "📋  Claimed Items", "CLAIMS");
        addNavButton(sidebar, "⚙️  Settings", "SETTINGS");

        // Profile & Logout Bottom
        sidebar.add(Box.createVerticalGlue());
        
        JPanel profile = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        profile.setBorder(new EmptyBorder(0, 15, 0, 0));
        profile.setAlignmentX(Component.LEFT_ALIGNMENT);
        profile.setOpaque(false);
        JLabel avatar = new JLabel("N", SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(35, 35));
        avatar.setOpaque(true);
        avatar.setBackground(COLOR_ACCENT);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        profile.add(avatar);
        
        sidebarNameLbl = new JLabel("NGO Profile");
        sidebarNameLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sidebarNameLbl.setForeground(COLOR_WHEAT);
        profile.add(sidebarNameLbl);
        sidebar.add(profile);

        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton logoutBtn = new JButton("🚪  Sign Out");
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setMinimumSize(new Dimension(280, 54));
        logoutBtn.setPreferredSize(new Dimension(280, 54));
        logoutBtn.setMaximumSize(new Dimension(280, 54));
        logoutBtn.setForeground(new Color(0xA89880));
        logoutBtn.setBackground(COLOR_SIDEBAR);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });
        sidebar.add(logoutBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // MAIN CONTENT
        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setOpaque(false);
        contentCards.setBorder(new EmptyBorder(40, 50, 40, 50));

        contentCards.add(createDashboardView(), "DASHBOARD");
        contentCards.add(createFindView(), "FIND");
        contentCards.add(createClaimedItemsView(), "CLAIMS");
        contentCards.add(createSettingsView(), "SETTINGS");

        main.add(sidebar, BorderLayout.WEST);
        main.add(contentCards, BorderLayout.CENTER);
        add(main);
        
        updateSidebarSelection("DASHBOARD");
        refreshData();
    }

    private void updateSidebarSelection(String targetCard) {
        for (JButton btn : navButtons) {
            boolean active = btn.getActionCommand().equals(targetCard);
            btn.setForeground(active ? Color.WHITE : new Color(0xEADBC8));
            btn.setBackground(active ? new Color(106, 154, 176, 30) : COLOR_SIDEBAR);
            btn.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 18));
            btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, COLOR_WHEAT), new EmptyBorder(0, 10, 0, 0)) : new EmptyBorder(0, 15, 0, 0));
        }
        cardLayout.show(contentCards, targetCard);
    }

    private JPanel createDashboardView() {
        JPanel panel = new JPanel(new BorderLayout(0, 30));
        panel.setOpaque(false);

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        JLabel welcome = new JLabel("NGO Impact Portal");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 36));
        welcome.setForeground(COLOR_TEXT_PRIMARY);
        JLabel date = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMM dd")));
        date.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        date.setForeground(COLOR_TEXT_SECONDARY);
        header.add(welcome);
        header.add(date);
        panel.add(header, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new BorderLayout(0, 30));
        centerContainer.setOpaque(false);

        JPanel kpiPanel = new JPanel(new GridLayout(1, 4, 25, 0));
        kpiPanel.setOpaque(false);
        kpiPanel.setPreferredSize(new Dimension(0, 170));
        
        availableLbl.setFont(new Font("Segoe UI", Font.BOLD, 48));
        availableLbl.setForeground(COLOR_ACCENT);
        claimedLbl.setFont(new Font("Segoe UI", Font.BOLD, 48));
        claimedLbl.setForeground(COLOR_TERRACOTTA);
        servedLbl.setFont(new Font("Segoe UI", Font.BOLD, 48));
        servedLbl.setForeground(COLOR_WHEAT);
        activeDonorsLbl.setFont(new Font("Segoe UI", Font.BOLD, 48));
        activeDonorsLbl.setForeground(new Color(0x7BAE7F));
        
        kpiPanel.add(buildStatCard("Available Near You", availableLbl, "📦", COLOR_ACCENT));
        kpiPanel.add(buildStatCard("Items Claimed", claimedLbl, "🤝", COLOR_TERRACOTTA));
        kpiPanel.add(buildStatCard("Meals Served", servedLbl, "🥘", COLOR_WHEAT));
        kpiPanel.add(buildStatCard("Active Donors", activeDonorsLbl, "🌿", new Color(0x7BAE7F)));
        centerContainer.add(kpiPanel, BorderLayout.NORTH);

        // Impact Progress Bar
        JPanel statsCard = new JPanel(new GridBagLayout());
        statsCard.setBackground(Color.WHITE);
        statsCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xEDE3D8), 1, true),
            new EmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0; g.insets = new Insets(10, 10, 10, 10);
        g.anchor = GridBagConstraints.CENTER;

        JLabel titleLbl = new JLabel("Total Meals Served to the Community");
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        titleLbl.setForeground(COLOR_TEXT_SECONDARY);
        statsCard.add(titleLbl, g);

        g.gridy = 1;
        statTotalServedLbl.setFont(new Font("Segoe UI", Font.BOLD, 64));
        statTotalServedLbl.setForeground(COLOR_WHEAT);
        statsCard.add(statTotalServedLbl, g);

        g.gridy = 2;
        JLabel goalLbl = new JLabel("Impact Goal: 2,000 Meals Served");
        goalLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        goalLbl.setForeground(COLOR_ACCENT);
        statsCard.add(goalLbl, g);

        g.gridy = 3;
        g.fill = GridBagConstraints.HORIZONTAL;
        statProgressBar.setPreferredSize(new Dimension(600, 35));
        statProgressBar.setForeground(COLOR_WHEAT);
        statProgressBar.setBackground(new Color(0xEDE3D8));
        statProgressBar.setStringPainted(true);
        statProgressBar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statsCard.add(statProgressBar, g);

        centerContainer.add(statsCard, BorderLayout.CENTER);
        panel.add(centerContainer, BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel buildStatCard(String label, JLabel valueLabel, String emoji, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, accent),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        card.add(valueLabel, BorderLayout.CENTER);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(COLOR_TEXT_SECONDARY);
        card.add(lbl, BorderLayout.SOUTH);

        JLabel em = new JLabel(emoji);
        em.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        card.add(em, BorderLayout.NORTH);

        return card;
    }

    private JPanel createFindView() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xEDE3D8), 1, true),
            new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel title = new JLabel("Available Donations Near You 🗺️");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(title, BorderLayout.NORTH);

        availableTableModel = new DefaultTableModel(new String[]{"ID", "Donor", "Item", "Qty", "Category", "Status"}, 0);
        availableTable = new JTable(availableTableModel);
        styleTable(availableTable, "Available");
        card.add(new JScrollPane(availableTable), BorderLayout.CENTER);

        JButton claim = new JButton("Claim Selected 🤝");
        claim.setBackground(COLOR_ACCENT);
        claim.setForeground(Color.WHITE);
        claim.setFont(new Font("Segoe UI", Font.BOLD, 16));
        claim.setPreferredSize(new Dimension(0, 55));
        claim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        claim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { claim.setBackground(new Color(0x4F8299)); }
            public void mouseExited(java.awt.event.MouseEvent e) { claim.setBackground(COLOR_ACCENT); }
        });
        
        card.add(claim, BorderLayout.SOUTH);

        claim.addActionListener(e -> {
            int row = availableTable.getSelectedRow();
            if (row != -1) {
                int itemId = (int) availableTable.getValueAt(row, 0);
                String res = DBManager.claimFood(itemId, ngoId);
                if (res.equals("Success")) {
                    JOptionPane.showMessageDialog(this, "Claimed! Proceed to 'Claimed Items' to confirm pickup.", "Khaana", JOptionPane.INFORMATION_MESSAGE);
                    refreshData();
                    updateSidebarSelection("CLAIMS");
                } else JOptionPane.showMessageDialog(this, res);
            }
        });

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createClaimedItemsView() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xEDE3D8), 1, true),
            new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel title = new JLabel("Claimed Items (Pending Pickup) ⏳");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(title, BorderLayout.NORTH);

        claimedTableModel = new DefaultTableModel(new String[]{"ID", "Donor", "Item", "Qty", "Category", "Status"}, 0);
        claimedTable = new JTable(claimedTableModel);
        styleTable(claimedTable, "Claimed");
        card.add(new JScrollPane(claimedTable), BorderLayout.CENTER);

        JButton pickupBtn = new JButton("Confirm Pickup ✓");
        pickupBtn.setBackground(COLOR_TERRACOTTA);
        pickupBtn.setForeground(Color.WHITE);
        pickupBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pickupBtn.setPreferredSize(new Dimension(0, 55));
        pickupBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        pickupBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { pickupBtn.setBackground(new Color(0xB57B54)); }
            public void mouseExited(java.awt.event.MouseEvent e) { pickupBtn.setBackground(COLOR_TERRACOTTA); }
        });
        
        card.add(pickupBtn, BorderLayout.SOUTH);

        pickupBtn.addActionListener(e -> {
            int row = claimedTable.getSelectedRow();
            if (row != -1) {
                int itemId = (int) claimedTable.getValueAt(row, 0);
                String res = DBManager.markPickedUp(itemId);
                if (res.equals("Success")) {
                    JOptionPane.showMessageDialog(this, "Pickup Confirmed! Thank you for reducing waste.", "Khaana", JOptionPane.INFORMATION_MESSAGE);
                    refreshData();
                    updateSidebarSelection("DASHBOARD");
                } else JOptionPane.showMessageDialog(this, res);
            }
        });

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }


    private JPanel createSettingsView() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);
        
        JLabel title = new JLabel("NGO Profile Settings ⚙️");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xEDE3D8), 1, true),
            new EmptyBorder(40, 50, 40, 50)
        ));

        JPanel form = new JPanel(new GridLayout(6, 1, 0, 15));
        form.setOpaque(false);
        
        profileNameField = new JTextField(); profileNameField.setPreferredSize(new Dimension(400, 40));
        profileAddressField = new JTextField(); profileAddressField.setPreferredSize(new Dimension(400, 40));
        profileContactField = new JTextField(); profileContactField.setPreferredSize(new Dimension(400, 40));

        JLabel l1 = new JLabel("Organization Name"); l1.setFont(new Font("Segoe UI", Font.BOLD, 14)); l1.setForeground(COLOR_TEXT_SECONDARY);
        JLabel l2 = new JLabel("Organization Address"); l2.setFont(new Font("Segoe UI", Font.BOLD, 14)); l2.setForeground(COLOR_TEXT_SECONDARY);
        JLabel l3 = new JLabel("Contact Number"); l3.setFont(new Font("Segoe UI", Font.BOLD, 14)); l3.setForeground(COLOR_TEXT_SECONDARY);

        form.add(l1); form.add(profileNameField);
        form.add(l2); form.add(profileAddressField);
        form.add(l3); form.add(profileContactField);

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerWrapper.setOpaque(false);
        centerWrapper.add(form);
        card.add(centerWrapper, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save Changes ✓");
        saveBtn.setBackground(COLOR_TERRACOTTA);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        saveBtn.setPreferredSize(new Dimension(0, 50));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        saveBtn.addActionListener(e -> {
            String res = DBManager.updateNGOProfile(ngoId, profileNameField.getText(), profileAddressField.getText(), profileContactField.getText());
            if (res.equals("Success")) {
                JOptionPane.showMessageDialog(this, "Profile Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        card.add(saveBtn, BorderLayout.SOUTH);
        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private void styleTable(JTable table, String type) {
        table.setRowHeight(56);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0,0));
        table.getTableHeader().setBackground(new Color(0xFDF3E8));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(0xFDF3E8));
        table.setSelectionForeground(COLOR_TEXT_PRIMARY);
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        leftRenderer.setBorder(new EmptyBorder(0, 15, 0, 15));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, c);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setOpaque(true);
                String val = String.valueOf(v);
                if (val.contains("Meal")) { l.setBackground(COLOR_SUCCESS_BG); l.setForeground(new Color(0x3B6B41)); }
                else if (val.contains("Packaged")) { l.setBackground(new Color(0xFDF3E8)); l.setForeground(new Color(0x8B5E3C)); }
                else { l.setBackground(new Color(0xE8F0F5)); l.setForeground(new Color(0x2F5F72)); }
                return l;
            }
        });
    }

    private void addNavButton(JPanel sidebar, String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setActionCommand(cardName);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMinimumSize(new Dimension(280, 54));
        btn.setPreferredSize(new Dimension(280, 54));
        btn.setMaximumSize(new Dimension(280, 54));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> updateSidebarSelection(cardName));
        navButtons.add(btn);
        sidebar.add(btn);
    }

    private void refreshData() {
        // Update Available Table
        availableTableModel.setRowCount(0);
        List<Map<String, Object>> data = DBManager.getAvailableFood();
        for (Map<String, Object> row : data) {
            availableTableModel.addRow(new Object[]{
                row.get("itemid"), row.get("donorname"), row.get("foodtype"), row.get("quantity"), "Cooked Meal", "Available"
            });
        }
        
        // Update Claimed Table
        claimedTableModel.setRowCount(0);
        List<Map<String, Object>> claimedData = DBManager.getClaimedFood(ngoId);
        for (Map<String, Object> row : claimedData) {
            claimedTableModel.addRow(new Object[]{
                row.get("itemid"), row.get("donorname"), row.get("foodtype"), row.get("quantity"), "Cooked Meal", "Claimed"
            });
        }
        
        // Update KPIs
        Map<String, String> stats = DBManager.getNGOStats(ngoId);
        availableLbl.setText(stats.getOrDefault("availableNearYou", "0"));
        claimedLbl.setText(stats.getOrDefault("itemsClaimed", "0"));
        
        String mealsSvd = stats.getOrDefault("mealsServed", "0");
        servedLbl.setText(mealsSvd);
        
        activeDonorsLbl.setText(stats.getOrDefault("activeDonors", "00"));
        
        // Update Detailed Stats
        statTotalServedLbl.setText(mealsSvd);
        try {
            int meals = Integer.parseInt(mealsSvd);
            statProgressBar.setValue(Math.min(meals, 2000));
            statProgressBar.setString(meals + " / 2000 Meals");
        } catch (Exception ignored) {}
        
        // Fetch Profile
        Map<String, Object> profile = DBManager.getNGOProfile(ngoId);
        if (!profile.isEmpty()) {
            String pName = String.valueOf(profile.getOrDefault("orgname", ""));
            profileNameField.setText(pName);
            profileAddressField.setText(String.valueOf(profile.getOrDefault("address", "")));
            profileContactField.setText(String.valueOf(profile.getOrDefault("contact", "")));
            if (!pName.isEmpty()) sidebarNameLbl.setText("<html><div style=\"width: 150px; word-wrap: break-word;\">" + pName + "</div></html>");
        }
    }
}

