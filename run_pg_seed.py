import psycopg2

def get_pg_conn():
    return psycopg2.connect(
        dbname="postgres",
        user="postgres.plntjpbcmdyckuzzjonr",
        password="YOUR_POSTGRES_PASSWORD",
        host="aws-1-ap-south-1.pooler.supabase.com",
        port="6543"
    )

with open('seed_data.sql', 'r', encoding='utf-8') as f:
    sql = f.read()

try:
    conn = get_pg_conn()
    cur = conn.cursor()
    cur.execute(sql)
    conn.commit()
    cur.close()
    conn.close()
    print("PostgreSQL Seeded Successfully.")
except Exception as e:
    print(f"Error seeding PostgreSQL: {e}")
