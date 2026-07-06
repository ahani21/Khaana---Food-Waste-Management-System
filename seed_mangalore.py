# -*- coding: utf-8 -*-
import psycopg2
import mysql.connector
import datetime

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

donors = [
    (1, "NITK Surathkal", "College", "Surathkal, Mangalore", "0824-2474000"),
    (2, "St. Aloysius College", "College", "Light House Hill Road", "0824-2286881"),
    (3, "Sahyadri College", "College", "Adyar, Mangalore", "0824-2277222"),
    (4, "Vas Bakery", "Bakery", "Bendoor, Mangalore", "0824-2422234"),
    (5, "Crave Bakes & Desserts", "Bakery", "Balmatta, Mangalore", "0824-2443444"),
    (6, "Vivanta Mangalore", "Hotel", "Oldport Road, Mangalore", "0824-6660420"),
    (7, "The Ocean Pearl", "Hotel", "Navabharath Circle", "0824-2413800")
]

ngos = [
    (1, "Snehalaya Charitable Trust", "Pavoor, Kasaragod", "0499-8286200"),
    (2, "St. Antonys Charity Institutes", "Jeppu, Mangalore", "0824-2418065"),
    (3, "Shishu Nilaya", "Mangalore City", "0824-2423333"),
    (4, "Robin Hood Army Mangalore", "Citywide", "0824-2111111"),
    (5, "The Akshaya Patra Foundation", "Mangalore", "0824-2222222"),
    (6, "Pephands Foundation", "Mangalore", "0824-2333333"),
    (7, "Riya Foundation", "Kulshekar", "0824-2444444")
]

food_items = [
    (1, 1, "Hostel Mess Surplus", 100, "Cooked Meal", "Pending", None),
    (2, 2, "Event Buffet Leftovers", 50, "Cooked Meal", "Pending", None),
    (3, 4, "Day-end Breads and Puffs", 30, "Packaged Food", "Pending", None),
    (4, 5, "Pastries and Cakes", 15, "Packaged Food", "Pending", None),
    (5, 6, "Banquet Surplus", 150, "Cooked Meal", "Pending", None),
    (6, 7, "Breakfast Buffet Surplus", 80, "Cooked Meal", "Pending", None),
    (7, 3, "Canteen Leftovers", 40, "Cooked Meal", "Claimed", 1),
    (8, 4, "Unsold Plums and Biscuits", 25, "Packaged Food", "Claimed", 4),
    (9, 1, "Raw Vegetables", 30, "Raw Ingredients", "Claimed", 5),
    (10, 6, "Lunch Buffet Rice and Curries", 60, "Cooked Meal", "Claimed", 2)
]

# === SEED POSTGRESQL ===
try:
    conn = get_pg_conn()
    cur = conn.cursor()
    cur.execute("TRUNCATE TABLE food_items, donors, ngos RESTART IDENTITY CASCADE;")
    
    for d in donors:
        cur.execute("INSERT INTO donors (donorid, name, donortype, location, contact) VALUES (%s, %s, %s, %s, %s)", d)
    for n in ngos:
        cur.execute("INSERT INTO ngos (ngoid, name, location, contact) VALUES (%s, %s, %s, %s)", n)
    for f in food_items:
        cur.execute("INSERT INTO food_items (itemid, donorid, foodtype, quantity, category, currentstatus, claimedby) VALUES (%s, %s, %s, %s, %s, %s, %s)", f)
    
    cur.execute("SELECT setval('donors_donorid_seq', (SELECT MAX(donorid) FROM donors));")
    cur.execute("SELECT setval('ngos_ngoid_seq', (SELECT MAX(ngoid) FROM ngos));")
    cur.execute("SELECT setval('food_items_itemid_seq', (SELECT MAX(itemid) FROM food_items));")

    conn.commit()
    cur.close()
    conn.close()
    print("PostgreSQL Seeded Successfully.")
except Exception as e:
    print(f"Error seeding PostgreSQL: {e}")

# === SEED MYSQL ===
try:
    my_conn = get_my_conn()
    my_cur = my_conn.cursor()
    
    my_cur.execute("SET FOREIGN_KEY_CHECKS = 0;")
    my_cur.execute("TRUNCATE TABLE food_items;")
    my_cur.execute("TRUNCATE TABLE donors;")
    my_cur.execute("TRUNCATE TABLE ngos;")
    my_cur.execute("SET FOREIGN_KEY_CHECKS = 1;")
    
    for d in donors:
        my_cur.execute("INSERT INTO donors (donorid, name, donortype, location, contact) VALUES (%s, %s, %s, %s, %s)", d)
    for n in ngos:
        my_cur.execute("INSERT INTO ngos (ngoid, name, location, contact) VALUES (%s, %s, %s, %s)", n)
    
    for f in food_items:
        expiry = (datetime.datetime.now() + datetime.timedelta(days=1)).strftime('%Y-%m-%d %H:%M:%S')
        my_cur.execute("INSERT INTO food_items (itemid, donorid, foodtype, quantity, category, currentstatus, claimedby, expirytime) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)", f + (expiry,))
        
    my_conn.commit()
    my_cur.close()
    my_conn.close()
    print("MySQL Seeded Successfully.")
except Exception as e:
    print(f"Error seeding MySQL: {e}")
