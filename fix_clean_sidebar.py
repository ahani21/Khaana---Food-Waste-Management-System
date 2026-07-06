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

    # 1. Revert Nav Buttons to LEFT alignment
    data = data.replace('btn.setHorizontalAlignment(SwingConstants.CENTER);', 'btn.setHorizontalAlignment(SwingConstants.LEFT);')

    # 2. Fix the border padding so active and inactive buttons have the EXACT same 70px left alignment
    if 'COLOR_TERRACOTTA' in data and 'DonorDashboard' in path:
        data = data.replace(
            'btn.setBorder(active ? BorderFactory.createMatteBorder(0, 5, 0, 0, COLOR_TERRACOTTA) : new EmptyBorder(0, 5, 0, 0));',
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, COLOR_TERRACOTTA), new EmptyBorder(0, 65, 0, 0)) : new EmptyBorder(0, 70, 0, 0));'
        )
    if 'COLOR_WHEAT' in data and 'NGODashboard' in path:
        data = data.replace(
            'btn.setBorder(active ? BorderFactory.createMatteBorder(0, 5, 0, 0, COLOR_WHEAT) : new EmptyBorder(0, 5, 0, 0));',
            'btn.setBorder(active ? BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, COLOR_WHEAT), new EmptyBorder(0, 65, 0, 0)) : new EmptyBorder(0, 70, 0, 0));'
        )
        
    # 3. Fix Logout Button
    data = data.replace('logoutBtn.setHorizontalAlignment(SwingConstants.CENTER);', 'logoutBtn.setHorizontalAlignment(SwingConstants.LEFT);')
    data = data.replace('logoutBtn.setBorder(new EmptyBorder(0, 0, 0, 0));', 'logoutBtn.setBorder(new EmptyBorder(0, 70, 0, 0));')
    
    with open(path, 'w', encoding='utf-8') as f:
        f.write(data)

print("Consistent left alignment applied.")
