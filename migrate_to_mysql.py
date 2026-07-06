import os

target_dir = r"C:\Users\Ahani Shetty\OneDrive\Desktop\DBMS Project MySQL"

# 1. Update pom.xml
pom_path = os.path.join(target_dir, "pom.xml")
with open(pom_path, "r", encoding="utf-8") as f:
    pom_data = f.read()
pom_data = pom_data.replace(
    "<groupId>org.postgresql</groupId>\n            <artifactId>postgresql</artifactId>\n            <version>42.7.2</version>",
    "<groupId>mysql</groupId>\n            <artifactId>mysql-connector-java</artifactId>\n            <version>8.0.33</version>"
)
with open(pom_path, "w", encoding="utf-8") as f:
    f.write(pom_data)

# 2. Update run.ps1
run_path = os.path.join(target_dir, "run.ps1")
with open(run_path, "r", encoding="utf-8") as f:
    run_data = f.read()
run_data = run_data.replace(
    '$pgJar = "lib/postgresql-42.7.2.jar"',
    '$pgJar = "lib/mysql-connector-java-8.0.33.jar"'
).replace(
    'Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.2/postgresql-42.7.2.jar" -OutFile $pgJar',
    'Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar" -OutFile $pgJar'
)
with open(run_path, "w", encoding="utf-8") as f:
    f.write(run_data)

# 3. Update DatabaseConfig.java
config_path = os.path.join(target_dir, "src/main/java/com/foodwaste/util/DatabaseConfig.java")
with open(config_path, "r", encoding="utf-8") as f:
    config_data = f.read()
config_data = config_data.replace(
    'jdbc:postgresql://',
    'jdbc:mysql://'
)
with open(config_path, "w", encoding="utf-8") as f:
    f.write(config_data)

# 4. Update DBManager.java (org.postgresql.Driver to com.mysql.cj.jdbc.Driver)
db_path = os.path.join(target_dir, "src/main/java/com/foodwaste/util/DBManager.java")
with open(db_path, "r", encoding="utf-8") as f:
    db_data = f.read()
db_data = db_data.replace(
    'Class.forName("org.postgresql.Driver");',
    'Class.forName("com.mysql.cj.jdbc.Driver");'
).replace(
    '?::timestamp',
    '?'
).replace(
    'CURRENT_TIMESTAMP + INTERVAL \'1 day\'',
    'DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 DAY)'
).replace(
    'CURRENT_TIMESTAMP + INTERVAL \'8 hours\'',
    'DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 8 HOUR)'
).replace(
    'CURRENT_TIMESTAMP + INTERVAL \'2 days\'',
    'DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 2 DAY)'
).replace(
    'CURRENT_TIMESTAMP + INTERVAL \'5 days\'',
    'DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 DAY)'
).replace(
    'CURRENT_TIMESTAMP + INTERVAL \'6 hours\'',
    'DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 6 HOUR)'
).replace(
    'CURRENT_TIMESTAMP + INTERVAL \'4 hours\'',
    'DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 4 HOUR)'
).replace(
    'CURRENT_TIMESTAMP + INTERVAL \'12 hours\'',
    'DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 12 HOUR)'
)
# Note: Seed script in DBManager? No, seed data is in seed_data.sql. We will just write a new mysql_setup.sql.
with open(db_path, "w", encoding="utf-8") as f:
    f.write(db_data)

print("Migration script completed successfully.")
