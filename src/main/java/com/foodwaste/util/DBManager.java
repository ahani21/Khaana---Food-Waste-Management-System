package com.foodwaste.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class DBManager {
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
    }

    // Auth logic
    public static int registerDonor(String name, String location, String contact) throws SQLException {
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
    }

    public static int registerNGO(String name, String address, String contact) throws SQLException {
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
    }

    public static boolean validateDonor(int id) {
        String sql = "SELECT 1 FROM DONOR WHERE DonorID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateNGO(int id) {
        String sql = "SELECT 1 FROM NGO WHERE NGO_ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Donor actions
    public static void addFoodItem(int donorId, String foodType, int quantity, String expiryTime) throws SQLException {
        String sql = "INSERT INTO FOOD_ITEM (DonorID, FoodType, Quantity, ExpiryTime) VALUES (?, ?, ?, ?::timestamp)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, donorId);
            pstmt.setString(2, foodType);
            pstmt.setInt(3, quantity);
            pstmt.setString(4, expiryTime);
            pstmt.executeUpdate();
        }
    }

    public static List<Map<String, Object>> getDonorHistory(int donorId) {
        String sql = "SELECT f.ItemID, f.FoodType, f.Quantity, f.ExpiryTime, s.CurrentStatus " +
                     "FROM FOOD_ITEM f JOIN DONATION_STATUS s ON f.ItemID = s.ItemID " +
                     "WHERE f.DonorID = ? ORDER BY f.ItemID DESC";
        return executeQuery(sql, donorId);
    }

    // NGO actions
    public static List<Map<String, Object>> getAvailableFood() {
        // Only show non-expired available food
        String sql = "SELECT f.ItemID, d.Name as DonorName, f.FoodType, f.Quantity, f.ExpiryTime " +
                     "FROM FOOD_ITEM f " +
                     "JOIN DONOR d ON f.DonorID = d.DonorID " +
                     "JOIN DONATION_STATUS s ON f.ItemID = s.ItemID " +
                     "WHERE s.CurrentStatus = 'Available' AND f.ExpiryTime > CURRENT_TIMESTAMP";
        return executeQuery(sql);
    }

    public static List<Map<String, Object>> getClaimedFood(int ngoId) {
        String sql = "SELECT f.ItemID, d.Name as DonorName, f.FoodType, f.Quantity, p.Timestamp " +
                     "FROM FOOD_ITEM f " +
                     "JOIN DONOR d ON f.DonorID = d.DonorID " +
                     "JOIN DONATION_STATUS s ON f.ItemID = s.ItemID " +
                     "JOIN PICKUP_LOG p ON f.ItemID = p.ItemID " +
                     "WHERE s.CurrentStatus = 'Claimed' AND p.NGO_ID = ?";
        return executeQuery(sql, ngoId);
    }

    public static String claimFood(int itemId, int ngoId) {
        try (Connection conn = getConnection();
             CallableStatement cstmt = conn.prepareCall("{call claim_food_item(?, ?)}")) {
            cstmt.setInt(1, itemId);
            cstmt.setInt(2, ngoId);
            cstmt.execute();
            return "Success";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public static String markPickedUp(int itemId) {
        try (Connection conn = getConnection();
             CallableStatement cstmt = conn.prepareCall("{call mark_picked_up(?)}")) {
            cstmt.setInt(1, itemId);
            cstmt.execute();
            return "Success";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public static Map<String, Object> getDonorProfile(int donorId) {
        String sql = "SELECT Name, Location, Contact FROM DONOR WHERE DonorID = ?";
        List<Map<String, Object>> result = executeQuery(sql, donorId);
        return result.isEmpty() ? new HashMap<>() : result.get(0);
    }

    public static String updateDonorProfile(int donorId, String name, String location, String contact) {
        String sql = "UPDATE DONOR SET Name = ?, Location = ?, Contact = ? WHERE DonorID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, location);
            pstmt.setString(3, contact);
            pstmt.setInt(4, donorId);
            pstmt.executeUpdate();
            return "Success";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public static Map<String, Object> getNGOProfile(int ngoId) {
        String sql = "SELECT OrgName, Address, Contact FROM NGO WHERE NGO_ID = ?";
        List<Map<String, Object>> result = executeQuery(sql, ngoId);
        return result.isEmpty() ? new HashMap<>() : result.get(0);
    }

    public static String updateNGOProfile(int ngoId, String name, String address, String contact) {
        String sql = "UPDATE NGO SET OrgName = ?, Address = ?, Contact = ? WHERE NGO_ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, contact);
            pstmt.setInt(4, ngoId);
            pstmt.executeUpdate();
            return "Success";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public static Map<String, String> getDonorStats(int donorId) {
        Map<String, String> stats = new HashMap<>();
        try (Connection conn = getConnection()) {
            // Total Donations
            PreparedStatement p1 = conn.prepareStatement("SELECT COUNT(*) FROM FOOD_ITEM WHERE DonorID = ?");
            p1.setInt(1, donorId);
            ResultSet r1 = p1.executeQuery();
            if (r1.next()) stats.put("totalDonations", String.valueOf(r1.getInt(1)));

            // Active Listings
            PreparedStatement p2 = conn.prepareStatement("SELECT COUNT(*) FROM FOOD_ITEM f JOIN DONATION_STATUS s ON f.ItemID = s.ItemID WHERE f.DonorID = ? AND s.CurrentStatus = 'Available' AND f.ExpiryTime > CURRENT_TIMESTAMP");
            p2.setInt(1, donorId);
            ResultSet r2 = p2.executeQuery();
            if (r2.next()) stats.put("activeListings", String.format("%02d", r2.getInt(1)));

            // Items Collected
            PreparedStatement p3 = conn.prepareStatement("SELECT COUNT(*) FROM FOOD_ITEM f JOIN DONATION_STATUS s ON f.ItemID = s.ItemID WHERE f.DonorID = ? AND s.CurrentStatus = 'Picked Up'");
            p3.setInt(1, donorId);
            ResultSet r3 = p3.executeQuery();
            if (r3.next()) stats.put("itemsCollected", String.valueOf(r3.getInt(1)));

            // Meals Estimated
            PreparedStatement p4 = conn.prepareStatement("SELECT COALESCE(SUM(Quantity), 0) FROM FOOD_ITEM WHERE DonorID = ?");
            p4.setInt(1, donorId);
            ResultSet r4 = p4.executeQuery();
            if (r4.next()) stats.put("mealsEstimated", String.valueOf(r4.getInt(1)));

        } catch (SQLException e) {
            e.printStackTrace();
            stats.put("totalDonations", "0");
            stats.put("activeListings", "00");
            stats.put("itemsCollected", "0");
            stats.put("mealsEstimated", "0");
        }
        return stats;
    }

    public static Map<String, String> getNGOStats(int ngoId) {
        Map<String, String> stats = new HashMap<>();
        try (Connection conn = getConnection()) {
            // Available Near You
            PreparedStatement p1 = conn.prepareStatement("SELECT COUNT(*) FROM FOOD_ITEM f JOIN DONATION_STATUS s ON f.ItemID = s.ItemID WHERE s.CurrentStatus = 'Available' AND f.ExpiryTime > CURRENT_TIMESTAMP");
            ResultSet r1 = p1.executeQuery();
            if (r1.next()) stats.put("availableNearYou", String.valueOf(r1.getInt(1)));

            // Items Claimed
            PreparedStatement p2 = conn.prepareStatement("SELECT COUNT(*) FROM PICKUP_LOG WHERE NGO_ID = ?");
            p2.setInt(1, ngoId);
            ResultSet r2 = p2.executeQuery();
            if (r2.next()) stats.put("itemsClaimed", String.valueOf(r2.getInt(1)));

            // Meals Served
            PreparedStatement p3 = conn.prepareStatement("SELECT COALESCE(SUM(f.Quantity), 0) FROM PICKUP_LOG p JOIN FOOD_ITEM f ON p.ItemID = f.ItemID WHERE p.NGO_ID = ?");
            p3.setInt(1, ngoId);
            ResultSet r3 = p3.executeQuery();
            if (r3.next()) stats.put("mealsServed", String.valueOf(r3.getInt(1)));

            // Active Donors
            PreparedStatement p4 = conn.prepareStatement("SELECT COUNT(DISTINCT f.DonorID) FROM FOOD_ITEM f JOIN DONATION_STATUS s ON f.ItemID = s.ItemID WHERE s.CurrentStatus = 'Available' AND f.ExpiryTime > CURRENT_TIMESTAMP");
            ResultSet r4 = p4.executeQuery();
            if (r4.next()) stats.put("activeDonors", String.format("%02d", r4.getInt(1)));

        } catch (SQLException e) {
            e.printStackTrace();
            stats.put("availableNearYou", "0");
            stats.put("itemsClaimed", "0");
            stats.put("mealsServed", "0");
            stats.put("activeDonors", "00");
        }
        return stats;
    }

    private static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= cols; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
