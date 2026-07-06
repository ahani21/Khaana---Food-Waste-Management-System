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

    # Logo alignment
    data = data.replace('logo.setAlignmentX(Component.CENTER_ALIGNMENT);', 'logo.setAlignmentX(Component.LEFT_ALIGNMENT);\n        logo.setBorder(new EmptyBorder(0, 15, 0, 0));')
    
    # Sublogo alignment (if exists, DonorDashboard has it)
    data = data.replace('sublogo.setAlignmentX(Component.CENTER_ALIGNMENT);', 'sublogo.setAlignmentX(Component.LEFT_ALIGNMENT);\n        sublogo.setBorder(new EmptyBorder(0, 15, 0, 0));')

    # Button paddings (15px flush left)
    if 'COLOR_TERRACOTTA' in data and 'DonorDashboard' in path:
        data = data.replace(
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, COLOR_TERRACOTTA), new EmptyBorder(0, 65, 0, 0)) : new EmptyBorder(0, 70, 0, 0));',
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, COLOR_TERRACOTTA), new EmptyBorder(0, 10, 0, 0)) : new EmptyBorder(0, 15, 0, 0));'
        )
    if 'COLOR_WHEAT' in data and 'NGODashboard' in path:
        data = data.replace(
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, COLOR_WHEAT), new EmptyBorder(0, 65, 0, 0)) : new EmptyBorder(0, 70, 0, 0));',
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, COLOR_WHEAT), new EmptyBorder(0, 10, 0, 0)) : new EmptyBorder(0, 15, 0, 0));'
        )
        
    # Logout button
    data = data.replace('logoutBtn.setBorder(new EmptyBorder(0, 70, 0, 0));', 'logoutBtn.setBorder(new EmptyBorder(0, 15, 0, 0));')
    data = data.replace('logoutBtn.setBorder(new EmptyBorder(0, 65, 0, 0));', 'logoutBtn.setBorder(new EmptyBorder(0, 15, 0, 0));')
    data = data.replace('logoutBtn.setBorder(new EmptyBorder(0, 0, 0, 0));', 'logoutBtn.setBorder(new EmptyBorder(0, 15, 0, 0));')
    
    # Profile panel alignment
    data = data.replace(
        'JPanel profile = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));\n        profile.setBorder(new EmptyBorder(0, 0, 0, 0));',
        'JPanel profile = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));\n        profile.setBorder(new EmptyBorder(0, 15, 0, 0));\n        profile.setAlignmentX(Component.LEFT_ALIGNMENT);'
    )

    with open(path, 'w', encoding='utf-8') as f:
        f.write(data)

print("Flush left alignment applied.")
