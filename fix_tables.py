import os
import re

donor_path = r'C:\Users\Ahani Shetty\OneDrive\Desktop\DBMS Project\src\main\java\com\foodwaste\ui\DonorDashboard.java'
ngo_path = r'C:\Users\Ahani Shetty\OneDrive\Desktop\DBMS Project\src\main\java\com\foodwaste\ui\NGODashboard.java'

# 1. Update styleTable in BOTH
def update_style_table(content):
    # Find styleTable method
    pattern = r'(private void styleTable\(JTable table\.*?\) \{)(.*?)(^\s*\})'
    # Wait, NGODashboard has styleTable(JTable table, String type)
    pattern_donor = r'(private void styleTable\(JTable table\) \{)(.*?)(^\s*\})'
    pattern_ngo = r'(private void styleTable\(JTable table, String type\) \{)(.*?)(^\s*\})'
    
    new_body = '''
        table.setRowHeight(56);
        table.setShowGrid(true);
        table.setGridColor(new Color(0xE0E0E0));
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setBackground(new Color(0xE8C99A));
        table.getTableHeader().setForeground(new Color(0x3B2F2F));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.setSelectionBackground(new Color(0xD4956A));
        table.setSelectionForeground(Color.WHITE);
        table.setFocusable(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);'''

    # For NGO, we have special column 4 renderer
    new_body_ngo = new_body + '''
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, c);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setOpaque(true);
                String val = String.valueOf(v);
                if (val.contains("Meal")) { l.setBackground(new Color(0xD6EAD7)); l.setForeground(new Color(0x3B6B41)); }
                else if (val.contains("Packaged")) { l.setBackground(new Color(0xFDF3E8)); l.setForeground(new Color(0x8B5E3C)); }
                else { l.setBackground(new Color(0xE8F0F5)); l.setForeground(new Color(0x2F5F72)); }
                return l;
            }
        });
'''
    
    if "DonorDashboard" in content:
        content = re.sub(pattern_donor, r'\1' + new_body + r'\n\3', content, flags=re.DOTALL | re.MULTILINE)
    else:
        content = re.sub(pattern_ngo, r'\1' + new_body_ngo + r'\n\3', content, flags=re.DOTALL | re.MULTILINE)
    return content

# Read files
with open(donor_path, 'r', encoding='utf-8') as f: donor_data = f.read()
with open(ngo_path, 'r', encoding='utf-8') as f: ngo_data = f.read()

# Update Donor styles (the chunk that failed)
donor_data = update_style_table(donor_data)
with open(donor_path, 'w', encoding='utf-8') as f: f.write(donor_data)

print("Script compiled successfully for UI styling.")
