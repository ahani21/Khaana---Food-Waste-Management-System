-- ==============================================================================
-- KHAANA - REALISTIC SEED DATA SCRIPT (MANGALORE)
-- ==============================================================================

TRUNCATE TABLE PICKUP_LOG RESTART IDENTITY CASCADE;
TRUNCATE TABLE DONATION_STATUS RESTART IDENTITY CASCADE;
TRUNCATE TABLE FOOD_ITEM RESTART IDENTITY CASCADE;
TRUNCATE TABLE NGO RESTART IDENTITY CASCADE;
TRUNCATE TABLE DONOR RESTART IDENTITY CASCADE;

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
(1, 'Hostel Mess Surplus', 100, CURRENT_TIMESTAMP + INTERVAL '8 hours'),
(2, 'Event Buffet Leftovers', 50, CURRENT_TIMESTAMP + INTERVAL '1 day'),
(4, 'Day-end Breads and Puffs', 30, CURRENT_TIMESTAMP + INTERVAL '2 days'),
(5, 'Pastries and Cakes', 15, CURRENT_TIMESTAMP + INTERVAL '3 days'),
(6, 'Banquet Surplus', 150, CURRENT_TIMESTAMP + INTERVAL '6 hours'),
(7, 'Breakfast Buffet Surplus', 80, CURRENT_TIMESTAMP + INTERVAL '12 hours'),
(3, 'Canteen Leftovers', 40, CURRENT_TIMESTAMP + INTERVAL '4 hours'),
(4, 'Unsold Plums and Biscuits', 25, CURRENT_TIMESTAMP + INTERVAL '5 days'),
(1, 'Raw Vegetables', 30, CURRENT_TIMESTAMP + INTERVAL '3 days'),
(6, 'Lunch Buffet Rice and Curries', 60, CURRENT_TIMESTAMP + INTERVAL '4 hours');

DO 
BEGIN
    PERFORM claim_food_item(7, 1);
    PERFORM claim_food_item(8, 2);
    PERFORM claim_food_item(9, 5);
    PERFORM mark_picked_up(9);
END ;
