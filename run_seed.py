# -*- coding: utf-8 -*-
import psycopg2
import mysql.connector

def get_pg_conn():
    return psycopg2.connect(
        dbname="postgres",
        user="postgres.plntjpbcmdyckuzzjonr",
        password="YOUR_POSTGRES_PASSWORD",
        host="aws-1-ap-south-1.pooler.supabase.com",
        port="6543"
    )

def get_my_conn():
    return mysql.connector.connect(
        host="localhost",
        user="root",
        password="YOUR_MYSQL_PASSWORD",
        database="khaana_db"
    )

# POSTGRES
try:
    with open('seed_data.sql', 'r', encoding='utf-8-sig') as f:
        pg_sql = f.read()
    conn = get_pg_conn()
    cur = conn.cursor()
    cur.execute(pg_sql)
    conn.commit()
    cur.close()
    conn.close()
    print("PostgreSQL Seeded Successfully.")
except Exception as e:
    print(f"Error seeding PostgreSQL: {e}")

# MYSQL
try:
    my_conn = get_my_conn()
    my_cur = my_conn.cursor()
    
    my_cur.execute("SET FOREIGN_KEY_CHECKS = 0;")
    my_cur.execute("TRUNCATE TABLE PICKUP_LOG;")
    my_cur.execute("TRUNCATE TABLE DONATION_STATUS;")
    my_cur.execute("TRUNCATE TABLE FOOD_ITEM;")
    my_cur.execute("TRUNCATE TABLE NGO;")
    my_cur.execute("TRUNCATE TABLE DONOR;")
    my_cur.execute("SET FOREIGN_KEY_CHECKS = 1;")
    
    donors = [
        ('NITK Surathkal', 'Surathkal, Mangalore', '0824-2474000'),
        ('St. Aloysius College', 'Light House Hill Road', '0824-2286881'),
        ('Sahyadri College', 'Adyar, Mangalore', '0824-2277222'),
        ('Vas Bakery', 'Bendoor, Mangalore', '0824-2422234'),
        ('Crave Bakes & Desserts', 'Balmatta, Mangalore', '0824-2443444'),
        ('Vivanta Mangalore', 'Oldport Road, Mangalore', '0824-6660420'),
        ('The Ocean Pearl', 'Navabharath Circle', '0824-2413800')
    ]
    my_cur.executemany("INSERT INTO DONOR (Name, Location, Contact) VALUES (%s, %s, %s)", donors)
    
    ngos = [
        ('Snehalaya Charitable Trust', 'Pavoor, Kasaragod', '0499-8286200'),
        ('St. Antonys Charity Institutes', 'Jeppu, Mangalore', '0824-2418065'),
        ('Shishu Nilaya', 'Mangalore City', '0824-2423333'),
        ('Robin Hood Army Mangalore', 'Citywide', '0824-2111111'),
        ('The Akshaya Patra Foundation', 'Mangalore', '0824-2222222'),
        ('Pephands Foundation', 'Mangalore', '0824-2333333'),
        ('Riya Foundation', 'Kulshekar', '0824-2444444')
    ]
    my_cur.executemany("INSERT INTO NGO (OrgName, Address, Contact) VALUES (%s, %s, %s)", ngos)
    
    food_items = [
        (1, 'Hostel Mess Surplus', 100, 8),
        (2, 'Event Buffet Leftovers', 50, 24),
        (4, 'Day-end Breads and Puffs', 30, 48),
        (5, 'Pastries and Cakes', 15, 72),
        (6, 'Banquet Surplus', 150, 6),
        (7, 'Breakfast Buffet Surplus', 80, 12),
        (3, 'Canteen Leftovers', 40, 4),
        (4, 'Unsold Plums and Biscuits', 25, 120),
        (1, 'Raw Vegetables', 30, 72),
        (6, 'Lunch Buffet Rice and Curries', 60, 4)
    ]
    for (did, ft, qty, hours) in food_items:
        my_cur.execute(f"INSERT INTO FOOD_ITEM (DonorID, FoodType, Quantity, ExpiryTime) VALUES ({did}, '{ft}', {qty}, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL {hours} HOUR))")
    
    my_cur.execute("CALL claim_food_item(7, 1);")
    my_cur.execute("CALL claim_food_item(8, 2);")
    my_cur.execute("CALL claim_food_item(9, 5);")
    my_cur.execute("CALL mark_picked_up(9);")
    
    my_conn.commit()
    my_cur.close()
    my_conn.close()
    print("MySQL Seeded Successfully.")
except Exception as e:
    print(f"Error seeding MySQL: {e}")
