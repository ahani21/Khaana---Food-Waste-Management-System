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
    
    # Revert width
    data = data.replace('sidebar.setPreferredSize(new Dimension(310, 0));', 'sidebar.setPreferredSize(new Dimension(280, 0));')

    # Revert logo and sublogo back to center
    data = data.replace(
        'logo.setAlignmentX(Component.LEFT_ALIGNMENT);\n        logo.setBorder(new EmptyBorder(0, 30, 0, 0));',
        'logo.setAlignmentX(Component.CENTER_ALIGNMENT);'
    )
    data = data.replace(
        'sublogo.setAlignmentX(Component.LEFT_ALIGNMENT);\n        sublogo.setBorder(new EmptyBorder(0, 30, 0, 0));',
        'sublogo.setAlignmentX(Component.CENTER_ALIGNMENT);'
    )

    # Shift buttons exactly under centered logo (using 65px padding)
    if 'COLOR_TERRACOTTA' in data and 'DonorDashboard' in path:
        data = data.replace(
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_TERRACOTTA), new EmptyBorder(0, 27, 0, 0)) : new EmptyBorder(0, 30, 0, 0));',
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_TERRACOTTA), new EmptyBorder(0, 62, 0, 0)) : new EmptyBorder(0, 65, 0, 0));'
        )
    if 'COLOR_WHEAT' in data and 'NGODashboard' in path:
        data = data.replace(
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_WHEAT), new EmptyBorder(0, 27, 0, 0)) : new EmptyBorder(0, 30, 0, 0));',
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_WHEAT), new EmptyBorder(0, 62, 0, 0)) : new EmptyBorder(0, 65, 0, 0));'
        )
        
    data = data.replace('logoutBtn.setBorder(new EmptyBorder(0, 30, 0, 0));', 'logoutBtn.setBorder(new EmptyBorder(0, 65, 0, 0));')
    
    # Profile centering
    data = data.replace(
        'JPanel profile = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));\n        profile.setBorder(new EmptyBorder(0, 15, 0, 0));',
        'JPanel profile = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));\n        profile.setBorder(new EmptyBorder(0, 50, 0, 0));'
    )

    with open(path, 'w', encoding='utf-8') as f:
        f.write(data)

print("Centered UI applied.")
