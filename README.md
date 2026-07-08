# Smart Institutional Food Waste Management and Donation System

A high-performance, concurrency-safe, database-driven desktop portal engineered to bridge the logistical gap between large-scale institutional food donors (canteens, canteens, bakeries, hostels) and verified Non-Governmental Organizations (NGOs) or social shelters.

---

## 🛠️ Tech Stack
**Frontend:** Java SE Swing (FlatLaf 3.4.1 Modern Look & Feel)  
**Backend:** MySQL 8.0 (3NF Normalized Relational Database)  
**Connectivity:** JDBC (Java Database Connectivity) with parameterized queries  
**Build System:** Apache Maven  

---

## 🚀 Key Features

*   **Role-Based Access Control:** Distinct, protected dashboards for **Donors** and **NGOs** using unique registration IDs.
*   **Surplus Food Posting:** Donors can instantly log surplus food details including quantity (servings), food type, and perishing deadlines.
*   **Real-Time Listing Feed:** NGOs browse a real-time reactive feed of available, unexpired food posts.
*   **Two-Step Transaction Workflow:** Secure workflow allowing NGOs to **Claim** an item first to secure it, then mark it **Picked Up** upon physical recovery.
*   **Automated Audit Trail:** Tracks all transaction histories, status changes, and claiming timestamps automatically on the server.
*   **Social Impact Statistics:** Real-time dashboards calculating total food saved, meals estimated, and active network participants for both donors and charity organizations.

---

## 📊 Database Architecture (3NF)

The database schema is strictly normalized to **Third Normal Form (3NF)** to eliminate data redundancy and prevent insert, update, or delete anomalies. 

### Core Relational Schema:
*   **`DONOR`:** Contact and identification profiles of food donating organizations.
*   **`NGO`:** Contact and verification details of charity organizations.
*   **`FOOD_ITEM`:** Active listings of surplus food posted by donors.
*   **`DONATION_STATUS`:** A state-machine tracking table managing item availability (`Available`, `Claimed`, `Picked Up`).
*   **`PICKUP_LOG`:** Associative audit trail mapping items to claiming NGOs and tracking transaction timestamps.

---

## ⚡ Advanced Database Logic (Security & Concurrency)

The application utilizes server-side database automation to enforce business rules and secure the platform against network dropouts or transaction race conditions:

*   **State Automation Trigger (`after_food_insert`):** Fires automatically on `FOOD_ITEM` insertion to generate a corresponding `Available` row in `DONATION_STATUS`. This guarantees data integrity even if the Java client crashes mid-transaction.
*   **Race-Condition Safe Stored Procedure (`claim_food_item`):** Wraps the NGO claim action inside an atomic transaction block. It uses row-level locking to verify the status is strictly `'Available'` before committing a claim, ensuring that if two users click "Claim" at the exact same millisecond, only one transaction succeeds.

---

## ⚙️ Installation & Setup

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- MySQL Server 8.0+
- Apache Maven (optional, for compiling)

### Database Configuration
1. Run the database setup script (`mysql_setup.sql` or run the database migration files) to initialize the tables, triggers, and stored procedures on your MySQL instance.
2. Open the file `src/main/java/com/foodwaste/util/DatabaseConfig.java`.
3. Update the credentials with your local MySQL server connection details:
   ```java
   public static final String URL = "jdbc:mysql://localhost:3306/YOUR_DATABASE_NAME";
   public static final String USER = "YOUR_MYSQL_USERNAME";
   public static final String PASSWORD = "YOUR_MYSQL_PASSWORD";
   ```

### Running the Application
To run the pre-configured project, execute the PowerShell run script in the repository root:
```powershell
.\run.ps1
```
Alternatively, compile and package the project into an executable JAR using Maven:
```bash
mvn clean package
java -jar target/food-waste-management-1.0-SNAPSHOT.jar
```
