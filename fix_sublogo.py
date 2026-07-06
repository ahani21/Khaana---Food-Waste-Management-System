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

    # Extract the sublogo block
    # It might look like:
    # JLabel sublogo = new JLabel("Donor Portal");
    # sublogo.setFont(new Font("Segoe UI", Font.BOLD, 14));
    # sublogo.setForeground(COLOR_ACCENT);
    # sublogo.setAlignmentX(Component.LEFT_ALIGNMENT);
    # sublogo.setBorder(new EmptyBorder(0, 15, 0, 0));
    # sidebar.add(sublogo);
    
    # Or in NGO:
    # JLabel sublogo = new JLabel("NGO Impact Portal"); (Wait, does NGO have a sublogo? I don't remember adding one there.)
    
    # Let's check if sublogo is in data
    if 'JLabel sublogo =' in data:
        # Remove it from top
        pattern_top = r'(\s*JLabel sublogo = new JLabel\(".*?"\);.*?sidebar\.add\(sublogo\);)'
        match = re.search(pattern_top, data, re.DOTALL)
        if match:
            sublogo_block = match.group(1)
            data = data.replace(sublogo_block, '')
            
            # Change alignment to center and remove border
            sublogo_new = sublogo_block.replace('Component.LEFT_ALIGNMENT', 'Component.CENTER_ALIGNMENT')
            sublogo_new = re.sub(r'sublogo\.setBorder\(.*?\);\n?', '', sublogo_new)
            
            # Insert at bottom
            target = 'sidebar.add(Box.createRigidArea(new Dimension(0, 15)));\n\n        JButton logoutBtn ='
            replacement = 'sidebar.add(Box.createRigidArea(new Dimension(0, 15)));\n' + sublogo_new + '\n        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));\n\n        JButton logoutBtn ='
            
            data = data.replace(target, replacement)
    
    with open(path, 'w', encoding='utf-8') as f:
        f.write(data)

print("Sublogo moved to bottom.")
