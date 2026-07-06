package com.foodwaste.ui;

import com.foodwaste.util.DBManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DonorDashboard extends JFrame {
    private int donorId;
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private JPanel contentCards;
    private CardLayout cardLayout;
    
    // Sidebar state
    private JPanel sidebar;
    private java.util.List<JButton> navButtons = new java.util.ArrayList<>();
    private JLabel sidebarNameLbl;

    // KPI Labels
    private JLabel totalDonationsLbl = new JLabel("0");
    private JLabel activeListingsLbl = new JLabel("0");
    private JLabel itemsCollectedLbl = new JLabel("0");
    private JLabel mealsEstimatedLbl = new JLabel("0");
    
    // Detailed Stats (Stats Tab)
    private JLabel statTotalMealsLbl = new JLabel("0");
    private JProgressBar statProgressBar = new JProgressBar(0, 500);
    
    // Profile Fields
    private JTextField profileNameField;
    private JTextField profileLocField;
    private JTextField profileContactField;

    // Design Colors
    private final Color COLOR_BG = new Color(0xFAF6F1);
    private final Color COLOR_SIDEBAR = new Color(0x2C2016);
    private final Color COLOR_ACCENT = new Color(0x7BAE7F);
    private final Color COLOR_WHEAT = new Color(0xE8C99A);
    private final Color COLOR_TEXT_PRIMARY = new Color(0x3B2F2F);
    private final Color COLOR_TEXT_SECONDARY = new Color(0x7A6A5A);
    private final Color COLOR_TERRACOTTA = new Color(0xD4956A);

    public DonorDashboard(int donorId) {
        this.donorId = donorId;
        setTitle("Khaana Donor Portal");
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
        addNavButton(sidebar, "🍱  Post Food", "POST");
        addNavButton(sidebar, "📋  My Donations", "HISTORY");
        addNavButton(sidebar, "⚙️  Settings", "SETTINGS");

        // Profile & Logout Bottom
        sidebar.add(Box.createVerticalGlue());
        
        JPanel profile = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        profile.setBorder(new EmptyBorder(0, 15, 0, 0));
        profile.setAlignmentX(Component.LEFT_ALIGNMENT);
        profile.setOpaque(false);
        JLabel avatar = new JLabel("D", SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(35, 35));
        avatar.setOpaque(true);
        avatar.setBackground(COLOR_TERRACOTTA);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        profile.add(avatar);
        
        sidebarNameLbl = new JLabel("Donor Profile");
        sidebarNameLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sidebarNameLbl.setForeground(COLOR_WHEAT);
        profile.add(sidebarNameLbl);
        sidebar.add(profile);

        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        logo.setBorder(new EmptyBorder(0, 15, 0, 0));

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
        contentCards.add(createPostView(), "POST");
        contentCards.add(createHistoryView(), "HISTORY");
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
            btn.setBackground(active ? new Color(232, 201, 154, 30) : COLOR_SIDEBAR);
            btn.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 18));
            btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, COLOR_TERRACOTTA), new EmptyBorder(0, 10, 0, 0)) : new EmptyBorder(0, 15, 0, 0));
        }
        cardLayout.show(contentCards, targetCard);
    }

    private JPanel createDashboardView() {
        JPanel panel = new JPanel(new BorderLayout(0, 30));
        panel.setOpaque(false);

        // Header
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        JLabel welcome = new JLabel("Good morning, Donor #" + donorId + " 👋");
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

        // KPIs
        JPanel kpiPanel = new JPanel(new GridLayout(1, 4, 25, 0));
        kpiPanel.setOpaque(false);
        kpiPanel.setPreferredSize(new Dimension(0, 170));
        
        totalDonationsLbl.setFont(new Font("Segoe UI", Font.BOLD, 48));
        totalDonationsLbl.setForeground(COLOR_ACCENT);
        activeListingsLbl.setFont(new Font("Segoe UI", Font.BOLD, 48));
        activeListingsLbl.setForeground(COLOR_TERRACOTTA);
        itemsCollectedLbl.setFont(new Font("Segoe UI", Font.BOLD, 48));
        itemsCollectedLbl.setForeground(COLOR_WHEAT);
        mealsEstimatedLbl.setFont(new Font("Segoe UI", Font.BOLD, 48));
        mealsEstimatedLbl.setForeground(new Color(0x6A9AB0));
        
        kpiPanel.add(buildStatCard("Total Donations", totalDonationsLbl, "🍱", COLOR_ACCENT));
        kpiPanel.add(buildStatCard("Active Listings", activeListingsLbl, "📦", COLOR_TERRACOTTA));
        kpiPanel.add(buildStatCard("Items Collected", itemsCollectedLbl, "🌿", COLOR_WHEAT));
        kpiPanel.add(buildStatCard("Meals Estimated", mealsEstimatedLbl, "🤝", new Color(0x6A9AB0)));
        
        centerContainer.add(kpiPanel, BorderLayout.NORTH);

        // Statistics Progress Bar
        JPanel statsCard = new JPanel(new GridBagLayout());
        statsCard.setBackground(Color.WHITE);
        statsCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xEDE3D8), 1, true),
            new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0; g.insets = new Insets(10, 10, 10, 10);
        g.anchor = GridBagConstraints.CENTER;

        JLabel titleLbl = new JLabel("Total Meals Donated");
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        titleLbl.setForeground(COLOR_TEXT_SECONDARY);
        statsCard.add(titleLbl, g);

        g.gridy = 1;
        statTotalMealsLbl.setFont(new Font("Segoe UI", Font.BOLD, 64));
        statTotalMealsLbl.setForeground(COLOR_ACCENT);
        statsCard.add(statTotalMealsLbl, g);

        g.gridy = 2;
        JLabel goalLbl = new JLabel("Community Milestone: 500 Meals");
        goalLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        goalLbl.setForeground(COLOR_TERRACOTTA);
        statsCard.add(goalLbl, g);

        g.gridy = 3;
        g.fill = GridBagConstraints.HORIZONTAL;
        statProgressBar.setPreferredSize(new Dimension(600, 35));
        statProgressBar.setForeground(COLOR_ACCENT);
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

    private JPanel createPostView() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xEDE3D8), 1, true),
            new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel title = new JLabel("Post a Donation 🍱");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 2, 50, 0));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(30, 0, 0, 0));

        JPanel col1 = new JPanel(new GridLayout(8, 1, 0, 10)); col1.setOpaque(false);
        col1.add(createInputLabel("Food Item Name"));
        JTextField nameF = new JTextField(); nameF.setPreferredSize(new Dimension(0, 40)); col1.add(nameF);
        col1.add(createInputLabel("Quantity"));
        JTextField qtyF = new JTextField(); qtyF.setPreferredSize(new Dimension(0, 40)); col1.add(qtyF);
        col1.add(createInputLabel("Category"));
        JComboBox<String> catF = new JComboBox<>(new String[]{"Cooked Meal", "Raw Ingredients", "Packaged Food"}); col1.add(catF);
        col1.add(createInputLabel("Pickup Location 📍"));
        JTextField locF = new JTextField(); locF.setPreferredSize(new Dimension(0, 40)); col1.add(locF);

        JPanel col2 = new JPanel(new GridLayout(8, 1, 0, 10)); col2.setOpaque(false);
        col2.add(createInputLabel("Available From"));
        JLabel autoChip = new JLabel(" Auto-filled ✓");
        autoChip.setOpaque(true); autoChip.setBackground(new Color(0xD6EAD7)); autoChip.setForeground(new Color(0x3B6B41));
        col2.add(autoChip);
        col2.add(createInputLabel("Best Before / Expiry"));
        JTextField expF = new JTextField(LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))); 
        expF.setPreferredSize(new Dimension(0, 40)); col2.add(expF);
        col2.add(createInputLabel("Special Notes"));
        JTextArea notes = new JTextArea(4, 20); col2.add(new JScrollPane(notes));

        grid.add(col1);
        grid.add(col2);
        card.add(grid, BorderLayout.CENTER);

        JButton submit = new JButton("Post Donation 🌿");
        submit.setBackground(COLOR_ACCENT);
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submit.setPreferredSize(new Dimension(0, 55));
        submit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect for submit button
        submit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { submit.setBackground(new Color(0x5F9463)); }
            public void mouseExited(java.awt.event.MouseEvent e) { submit.setBackground(COLOR_ACCENT); }
        });
        
        card.add(submit, BorderLayout.SOUTH);

        submit.addActionListener(e -> {
            try {
                DBManager.addFoodItem(donorId, nameF.getText(), Integer.parseInt(qtyF.getText()), expF.getText());
                JOptionPane.showMessageDialog(this, "Successfully listed!");
                refreshData();
                updateSidebarSelection("HISTORY");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }
    
    private JLabel createInputLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(COLOR_TEXT_SECONDARY);
        return l;
    }

    private JPanel createHistoryView() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);
        
        JLabel title = new JLabel("My Donations 📋");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panel.add(title, BorderLayout.NORTH);
        
        tableModel = new DefaultTableModel(new String[]{"ID", "Item", "Qty", "Status"}, 0);
        historyTable = new JTable(tableModel);
        styleTable(historyTable);
        panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        return panel;
    }


    private JPanel createSettingsView() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);
        
        JLabel title = new JLabel("Profile Settings ⚙️");
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
        profileLocField = new JTextField(); profileLocField.setPreferredSize(new Dimension(400, 40));
        profileContactField = new JTextField(); profileContactField.setPreferredSize(new Dimension(400, 40));

        form.add(createInputLabel("Full Name / Organization Name"));
        form.add(profileNameField);
        form.add(createInputLabel("Location Address"));
        form.add(profileLocField);
        form.add(createInputLabel("Contact Number"));
        form.add(profileContactField);

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
            String res = DBManager.updateDonorProfile(donorId, profileNameField.getText(), profileLocField.getText(), profileContactField.getText());
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

    private void styleTable(JTable table) {
        table.setRowHeight(56);
        table.setShowGrid(true);
        table.setGridColor(new Color(0xE0E0E0));
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setBackground(new Color(0xE8C99A));
        table.getTableHeader().setForeground(new Color(0x3B2F2F));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.setSelectionBackground(new Color(0xD4956A));
        table.setSelectionForeground(Color.WHITE);
        table.setFocusable(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
    }

    private void refreshData() {
        // Update Table
        tableModel.setRowCount(0);
        List<Map<String, Object>> data = DBManager.getDonorHistory(donorId);
        for (Map<String, Object> row : data) {
            tableModel.addRow(new Object[]{
                row.get("itemid"), row.get("foodtype"), row.get("quantity"), row.get("currentstatus")
            });
        }
        
        // Update KPIs
        Map<String, String> stats = DBManager.getDonorStats(donorId);
        totalDonationsLbl.setText(stats.getOrDefault("totalDonations", "0"));
        activeListingsLbl.setText(stats.getOrDefault("activeListings", "00"));
        itemsCollectedLbl.setText(stats.getOrDefault("itemsCollected", "0"));
        
        String mealsEst = stats.getOrDefault("mealsEstimated", "0");
        mealsEstimatedLbl.setText(mealsEst);
        
        // Update Detailed Stats
        statTotalMealsLbl.setText(mealsEst);
        try {
            int meals = Integer.parseInt(mealsEst);
            statProgressBar.setValue(Math.min(meals, 500));
            statProgressBar.setString(meals + " / 500 Meals");
        } catch (Exception ignored) {}
        
        // Fetch Profile
        Map<String, Object> profile = DBManager.getDonorProfile(donorId);
        if (!profile.isEmpty()) {
            String pName = String.valueOf(profile.getOrDefault("name", ""));
            profileNameField.setText(pName);
            profileLocField.setText(String.valueOf(profile.getOrDefault("location", "")));
            profileContactField.setText(String.valueOf(profile.getOrDefault("contact", "")));
            if (!pName.isEmpty()) sidebarNameLbl.setText("<html><div style=\"width: 150px; word-wrap: break-word;\">" + pName + "</div></html>");
        }
    }
}
