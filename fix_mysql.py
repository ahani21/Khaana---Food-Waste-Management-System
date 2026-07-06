import os
db_path = r'C:\Users\Ahani Shetty\OneDrive\Desktop\DBMS Project MySQL\src\main\java\com\foodwaste\util\DBManager.java'
with open(db_path, 'r', encoding='utf-8') as f:
    data = f.read()

# Fix registerDonor
old_donor = '''    public static int registerDonor(String name, String location, String contact) throws SQLException {
        String sql = "INSERT INTO DONOR (Name, Location, Contact) VALUES (?, ?, ?) RETURNING DonorID";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, location);
            pstmt.setString(3, contact);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }'''

new_donor = '''    public static int registerDonor(String name, String location, String contact) throws SQLException {
        String sql = "INSERT INTO DONOR (Name, Location, Contact) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, location);
            pstmt.setString(3, contact);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }'''
data = data.replace(old_donor, new_donor)

# Fix registerNGO
old_ngo = '''    public static int registerNGO(String name, String address, String contact) throws SQLException {
        String sql = "INSERT INTO NGO (OrgName, Address, Contact) VALUES (?, ?, ?) RETURNING NGO_ID";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, contact);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }'''

new_ngo = '''    public static int registerNGO(String name, String address, String contact) throws SQLException {
        String sql = "INSERT INTO NGO (OrgName, Address, Contact) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, contact);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }'''
data = data.replace(old_ngo, new_ngo)

with open(db_path, 'w', encoding='utf-8') as f:
    f.write(data)
print('Fixed!')
