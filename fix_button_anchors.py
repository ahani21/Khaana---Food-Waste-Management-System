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

    # Force the buttons to stick to the left edge and fill the 280px width
    data = data.replace('btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));', 'btn.setAlignmentX(Component.LEFT_ALIGNMENT);\n        btn.setMinimumSize(new Dimension(280, 54));\n        btn.setPreferredSize(new Dimension(280, 54));\n        btn.setMaximumSize(new Dimension(280, 54));')
    
    # Also force logout button
    data = data.replace('logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);', 'logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);\n        logoutBtn.setMinimumSize(new Dimension(280, 54));\n        logoutBtn.setPreferredSize(new Dimension(280, 54));\n        logoutBtn.setMaximumSize(new Dimension(280, 54));')
    
    with open(path, 'w', encoding='utf-8') as f:
        f.write(data)

print("Button boundaries anchored to the left edge.")
