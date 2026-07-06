import os
import re

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

    # 1. Fix buttons to be purely CENTER aligned with no left padding
    if 'COLOR_TERRACOTTA' in data and 'DonorDashboard' in path:
        data = data.replace(
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_TERRACOTTA), new EmptyBorder(0, 62, 0, 0)) : new EmptyBorder(0, 65, 0, 0));',
            'btn.setBorder(active ? BorderFactory.createMatteBorder(0, 5, 0, 0, COLOR_TERRACOTTA) : new EmptyBorder(0, 5, 0, 0));'
        )
    if 'COLOR_WHEAT' in data and 'NGODashboard' in path:
        data = data.replace(
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_WHEAT), new EmptyBorder(0, 62, 0, 0)) : new EmptyBorder(0, 65, 0, 0));',
            'btn.setBorder(active ? BorderFactory.createMatteBorder(0, 5, 0, 0, COLOR_WHEAT) : new EmptyBorder(0, 5, 0, 0));'
        )
        
    data = data.replace('btn.setHorizontalAlignment(SwingConstants.LEFT);', 'btn.setHorizontalAlignment(SwingConstants.CENTER);')
    data = data.replace('btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));', 'btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));') # Keep full width

    # 2. Fix Logout button to be centered
    data = data.replace('logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);', 'logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);')
    data = data.replace('logoutBtn.setHorizontalAlignment(SwingConstants.LEFT);', 'logoutBtn.setHorizontalAlignment(SwingConstants.CENTER);')
    data = data.replace('logoutBtn.setBorder(new EmptyBorder(0, 65, 0, 0));', 'logoutBtn.setBorder(new EmptyBorder(0, 0, 0, 0));')

    # 3. Fix Profile panel to stop wrapping and center it
    data = data.replace(
        'JPanel profile = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));\n        profile.setBorder(new EmptyBorder(0, 50, 0, 0));',
        'JPanel profile = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));\n        profile.setBorder(new EmptyBorder(0, 0, 0, 0));'
    )

    with open(path, 'w', encoding='utf-8') as f:
        f.write(data)

print("Pure center alignment applied.")
