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
    
    # 1. Allow buttons to stretch to full 270 width so text doesn't cut off
    data = data.replace('btn.setMaximumSize(new Dimension(240, 54));', 'btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));')

    # 2. Shift text to exactly under the logo (50px padding)
    if 'COLOR_TERRACOTTA' in data and 'DonorDashboard' in path:
        data = data.replace(
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_TERRACOTTA), new EmptyBorder(0, 15, 0, 0)) : new EmptyBorder(0, 18, 0, 0));',
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_TERRACOTTA), new EmptyBorder(0, 47, 0, 0)) : new EmptyBorder(0, 50, 0, 0));'
        )
    if 'COLOR_WHEAT' in data and 'NGODashboard' in path:
        data = data.replace(
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_WHEAT), new EmptyBorder(0, 15, 0, 0)) : new EmptyBorder(0, 18, 0, 0));',
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_WHEAT), new EmptyBorder(0, 47, 0, 0)) : new EmptyBorder(0, 50, 0, 0));'
        )
        
    # 3. Shift the logout button to match
    data = data.replace('logoutBtn.setBorder(new EmptyBorder(0, 18, 0, 0));', 'logoutBtn.setBorder(new EmptyBorder(0, 50, 0, 0));')

    with open(path, 'w', encoding='utf-8') as f:
        f.write(data)

print("Sidebar text alignment and bounds fixed.")
