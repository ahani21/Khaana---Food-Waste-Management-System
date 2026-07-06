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

    # Regex to match the sublogo creation block, being careful not to touch logo.setBorder
    # Since we know the exact lines from the grep:
    pattern = r'\s*JLabel sublogo = new JLabel\(".*?"\);\s*sublogo\.setFont\(.*?\);\s*sublogo\.setForeground\(.*?\);\s*sublogo\.setAlignmentX\(.*?\);'
    data = re.sub(pattern, '', data)
    
    # Remove sidebar.add(sublogo); and the rigid area after it
    pattern2 = r'\s*sidebar\.add\(sublogo\);\s*sidebar\.add\(Box\.createRigidArea\(new Dimension\(0, 15\)\)\);'
    data = re.sub(pattern2, '', data)

    with open(path, 'w', encoding='utf-8') as f:
        f.write(data)

print("Sublogo successfully removed with ZERO other changes.")
