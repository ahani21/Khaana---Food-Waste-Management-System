import os

with open('seed_data.sql', 'r', encoding='utf-8-sig') as f:
    sql = f.read()

sql = sql.replace('$', '')

with open('seed_data.sql', 'w', encoding='utf-8') as f:
    f.write(sql)
