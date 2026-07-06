import os

target_dir = r"C:\Users\Ahani Shetty\OneDrive\Desktop\DBMS Project MySQL"
sql_path = os.path.join(target_dir, "mysql_setup.sql")

mysql_sql = """-- ==============================================================================
-- KHAANA - MYSQL SETUP & SEED DATA SCRIPT (MANGALORE)
-- ==============================================================================

-- 1. Create Tables
CREATE TABLE IF NOT EXISTS DONOR (
    DonorID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Location VARCHAR(255),
    Contact VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS NGO (
    NGO_ID INT AUTO_INCREMENT PRIMARY KEY,
    OrgName VARCHAR(255) NOT NULL,
    Address VARCHAR(255),
    Contact VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS FOOD_ITEM (
    ItemID INT AUTO_INCREMENT PRIMARY KEY,
    DonorID INT NOT NULL,
    FoodType VARCHAR(255) NOT NULL,
    Quantity INT NOT NULL,
    ExpiryTime DATETIME NOT NULL,
    FOREIGN KEY (DonorID) REFERENCES DONOR(DonorID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS DONATION_STATUS (
    ItemID INT PRIMARY KEY,
    CurrentStatus VARCHAR(50) NOT NULL DEFAULT 'Available',
    Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ItemID) REFERENCES FOOD_ITEM(ItemID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS PICKUP_LOG (
    LogID INT AUTO_INCREMENT PRIMARY KEY,
    ItemID INT NOT NULL,
    NGO_ID INT NOT NULL,
    Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ItemID) REFERENCES FOOD_ITEM(ItemID) ON DELETE CASCADE,
    FOREIGN KEY (NGO_ID) REFERENCES NGO(NGO_ID) ON DELETE CASCADE
);

-- 2. Clear Existing Data
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE PICKUP_LOG;
TRUNCATE TABLE DONATION_STATUS;
TRUNCATE TABLE FOOD_ITEM;
TRUNCATE TABLE NGO;
TRUNCATE TABLE DONOR;
SET FOREIGN_KEY_CHECKS = 1;

-- 3. Create Triggers
DELIMITER //
DROP TRIGGER IF EXISTS after_food_insert //
CREATE TRIGGER after_food_insert
AFTER INSERT ON FOOD_ITEM
FOR EACH ROW
BEGIN
    INSERT INTO DONATION_STATUS (ItemID, CurrentStatus, Timestamp)
    VALUES (NEW.ItemID, 'Available', CURRENT_TIMESTAMP);
END //
DELIMITER ;

-- 4. Create Stored Procedures
DELIMITER //

DROP PROCEDURE IF EXISTS claim_food_item //
CREATE PROCEDURE claim_food_item(IN p_itemId INT, IN p_ngoId INT)
BEGIN
    UPDATE DONATION_STATUS 
    SET CurrentStatus = 'Claimed', Timestamp = CURRENT_TIMESTAMP
    WHERE ItemID = p_itemId AND CurrentStatus = 'Available';
    
    IF ROW_COUNT() > 0 THEN
        INSERT INTO PICKUP_LOG (ItemID, NGO_ID, Timestamp)
        VALUES (p_itemId, p_ngoId, CURRENT_TIMESTAMP);
    END IF;
END //

DROP PROCEDURE IF EXISTS mark_picked_up //
CREATE PROCEDURE mark_picked_up(IN p_itemId INT)
BEGIN
    UPDATE DONATION_STATUS 
    SET CurrentStatus = 'Picked Up', Timestamp = CURRENT_TIMESTAMP
    WHERE ItemID = p_itemId AND CurrentStatus = 'Claimed';
    
    IF ROW_COUNT() > 0 THEN
        UPDATE PICKUP_LOG 
        SET Timestamp = CURRENT_TIMESTAMP
        WHERE ItemID = p_itemId;
    END IF;
END //

DELIMITER ;

-- 5. Seed Data
INSERT INTO DONOR (Name, Location, Contact) VALUES 
('NITK Surathkal', 'Surathkal, Mangalore', '0824-2474000'),
('St. Aloysius College', 'Light House Hill Road', '0824-2286881'),
('Sahyadri College', 'Adyar, Mangalore', '0824-2277222'),
('Vas Bakery', 'Bendoor, Mangalore', '0824-2422234'),
('Crave Bakes & Desserts', 'Balmatta, Mangalore', '0824-2443444'),
('Vivanta Mangalore', 'Oldport Road, Mangalore', '0824-6660420'),
('The Ocean Pearl', 'Navabharath Circle', '0824-2413800');

INSERT INTO NGO (OrgName, Address, Contact) VALUES 
('Snehalaya Charitable Trust', 'Pavoor, Kasaragod', '0499-8286200'),
('St. Antonys Charity Institutes', 'Jeppu, Mangalore', '0824-2418065'),
('Shishu Nilaya', 'Mangalore City', '0824-2423333'),
('Robin Hood Army Mangalore', 'Citywide', '0824-2111111'),
('The Akshaya Patra Foundation', 'Mangalore', '0824-2222222'),
('Pephands Foundation', 'Mangalore', '0824-2333333'),
('Riya Foundation', 'Kulshekar', '0824-2444444');

INSERT INTO FOOD_ITEM (DonorID, FoodType, Quantity, ExpiryTime) VALUES 
(1, 'Hostel Mess Surplus', 100, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 8 HOUR)),
(2, 'Event Buffet Leftovers', 50, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 DAY)),
(4, 'Day-end Breads and Puffs', 30, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 2 DAY)),
(5, 'Pastries and Cakes', 15, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 3 DAY)),
(6, 'Banquet Surplus', 150, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 6 HOUR)),
(7, 'Breakfast Buffet Surplus', 80, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 12 HOUR)),
(3, 'Canteen Leftovers', 40, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 4 HOUR)),
(4, 'Unsold Plums and Biscuits', 25, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 DAY)),
(1, 'Raw Vegetables', 30, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 3 DAY)),
(6, 'Lunch Buffet Rice and Curries', 60, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 4 HOUR));

CALL claim_food_item(7, 1);
CALL claim_food_item(8, 2);
CALL claim_food_item(9, 5);
CALL mark_picked_up(9);

"""

with open(sql_path, "w", encoding="utf-8") as f:
    f.write(mysql_sql)

print("mysql_setup.sql created successfully with Mangalore data.")
