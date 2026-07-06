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
    
    # 1. Fix KPI cut off (increase height to 170)
    data = data.replace('kpiPanel.setPreferredSize(new Dimension(0, 130));', 'kpiPanel.setPreferredSize(new Dimension(0, 170));')
    
    # 2. Fix sidebar nav buttons (larger font, brighter inactive color)
    data = data.replace('btn.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 15));', 'btn.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 18));')
    data = data.replace('btn.setForeground(active ? Color.WHITE : new Color(0xA89880));', 'btn.setForeground(active ? Color.WHITE : new Color(0xEADBC8));')

    with open(path, 'w', encoding='utf-8') as f:
        f.write(data)

print("Minor UI tweaks applied.")
