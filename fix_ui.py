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
    
    # 1. Increase sidebar width
    data = data.replace('sidebar.setPreferredSize(new Dimension(240, 0));', 'sidebar.setPreferredSize(new Dimension(270, 0));')
    
    # 2. Fix sidebar text cut off
    data = data.replace(
        'if (!pName.isEmpty()) sidebarNameLbl.setText(pName);',
        'if (!pName.isEmpty()) sidebarNameLbl.setText("<html><div style=\\"width: 150px; word-wrap: break-word;\\">" + pName + "</div></html>");'
    )

    # 3. Fix table alignments
    old_style = '''        table.setSelectionForeground(COLOR_TEXT_PRIMARY);

        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {'''
        
    new_style = '''        table.setSelectionForeground(COLOR_TEXT_PRIMARY);
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        leftRenderer.setBorder(new EmptyBorder(0, 15, 0, 15));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {'''
    
    data = data.replace(old_style, new_style)
    
    with open(path, 'w', encoding='utf-8') as f:
        f.write(data)

print("UI fixes applied to all dashboards.")
