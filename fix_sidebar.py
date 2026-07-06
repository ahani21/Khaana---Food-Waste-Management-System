import os

files_to_fix = [
    r'C:\Users\Ahani Shetty\OneDrive\Desktop\DBMS Project\src\main\java\com\foodwaste\ui\DonorDashboard.java',
    r'C:\Users\Ahani Shetty\OneDrive\Desktop\DBMS Project\src\main\java\com\foodwaste\ui\NGODashboard.java',
    r'C:\Users\Ahani Shetty\OneDrive\Desktop\DBMS Project MySQL\src\main\java\com\foodwaste\ui\DonorDashboard.java',
    r'C:\Users\Ahani Shetty\OneDrive\Desktop\DBMS Project MySQL\src\main\java\com\foodwaste\ui\NGODashboard.java'
]

for path in files_to_fix:
    if not os.path.exists(path): continue
    with open(path, 'r', encoding='utf-8') as f:
        data = f.read()
    
    # 1. Fix nav buttons border (reduce left padding to move text left)
    # NGO uses COLOR_WHEAT, Donor uses COLOR_TERRACOTTA
    if 'COLOR_TERRACOTTA' in data and 'DonorDashboard' in path:
        data = data.replace(
            'btn.setBorder(active ? BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_TERRACOTTA) : new EmptyBorder(0, 30, 0, 0));',
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_TERRACOTTA), new EmptyBorder(0, 15, 0, 0)) : new EmptyBorder(0, 18, 0, 0));'
        )
    if 'COLOR_WHEAT' in data and 'NGODashboard' in path:
        data = data.replace(
            'btn.setBorder(active ? BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_WHEAT) : new EmptyBorder(0, 30, 0, 0));',
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_WHEAT), new EmptyBorder(0, 15, 0, 0)) : new EmptyBorder(0, 18, 0, 0));'
        )
        
    # 2. Fix Logout button
    old_logout = '''        JButton logoutBtn = new JButton("??  Sign Out");
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setForeground(new Color(0xA89880));
        logoutBtn.setBackground(COLOR_SIDEBAR);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));'''
        
    new_logout = '''        JButton logoutBtn = new JButton("??  Sign Out");
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setHorizontalAlignment(SwingConstants.LEFT);
        logoutBtn.setMaximumSize(new Dimension(270, 54));
        logoutBtn.setForeground(new Color(0xEADBC8));
        logoutBtn.setBackground(COLOR_SIDEBAR);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        logoutBtn.setBorder(new EmptyBorder(0, 18, 0, 0));'''
        
    data = data.replace(old_logout, new_logout)

    with open(path, 'w', encoding='utf-8') as f:
        f.write(data)

print("Sidebar text shifted left.")
