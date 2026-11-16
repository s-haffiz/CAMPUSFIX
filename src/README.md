# CampusFix

**Computer Repair Centre Data Management System**

CampusFix is a Java desktop application for recording and tracking computer repair requests at a campus ICT support centre. Technicians can register devices, store customer details, record faults and costs, and update the repair status as work progresses.


## Features

- **Create:** register a new repair service
- **Read:** view all services in a table
- **Update:** select a row, edit its details and save (with confirmation)
- **Delete:** remove a service after a confirmation prompt
- **Search and filter:** search by customer, device or service, and filter by status
- **Summary line:** live totals of jobs per status
- **Status tracking:** Received, Diagnosing, In Progress, Ready for Collection, Collected
- **Input validation:** required fields, phone, email, date format and cost checks
- **Error handling:** database errors are shown in dialog boxes

## Technologies

- Java (JDK 17 or later)
- Java Swing (GUI)
- JDBC with MySQL Connector/J
- MySQL 8

## Project Structure

```
```
CAMPUSFIX
├── Databases
│   └── campusfix-DBS.sql        Database and table creation script
├── lib
│   └── mysql-connector-j.jar    MySQL JDBC driver
├── src
│   ├── Main.java                Starts the application
│   ├── Frame.java               Swing GUI and button actions
│   ├── Services.java            Model class (one repair service)
│   ├── ServicesDAO.java         CRUD database operations
│   └── DBConnect.java           JDBC connection (reads db.properties)
├── db.properties.example        Template for database login settings
├── db.properties                Your own login settings (not on GitHub)
└── .gitignore
```

## Setup

### 1. Requirements

- JDK 17 or later (check with `java -version` and `javac -version`)
- MySQL Server and MySQL Workbench
- MySQL Connector/J (already included in `lib`)

### 2. Create the database

1. Open MySQL Workbench and connect to your local server.
2. Open `Databases/campusfix-DBS.sql`.
3. Run the whole script (lightning bolt icon).

This creates the `campusfix_db` database, the `services` table and five sample records. Rerunning the script resets the table to the sample data.

### 3. Set your MySQL login

The database login is kept in a file that is not uploaded to GitHub, so each person uses their own password.

1. In the `CAMPUSFIX` folder, copy `db.properties.example` and name the copy `db.properties`.
2. Open `db.properties` and replace `your_password_here` with your MySQL password:

```
db.url=jdbc:mysql://localhost:3306/campusfix_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=your_password_here
```

Do not use quotes or spaces around the `=`.

### 4. Run the application

From the `src` folder:

```
javac -cp "..\lib\mysql-connector-j.jar" *.java
java -cp ".;..\lib\mysql-connector-j.jar" Main
```

(On macOS or Linux, use `:` instead of `;` in the second command and `/` instead of `\`.)

In VS Code you can also open `Main.java` and click **Run Java**. Make sure the JAR appears under **Referenced Libraries** in the Java Projects panel.

## Database Design

Table: `services`

| Column | Type | Notes |
| --- | --- | --- |
| service_id | INT | Primary key, auto-increment |
| service_name | VARCHAR(100) | Required |
| customer_name | VARCHAR(100) | Required |
| customer_email | VARCHAR(100) | Optional |
| customer_phone | VARCHAR(20) | Required |
| device_name | VARCHAR(60) | Required |
| device_brand | VARCHAR(60) | Optional |
| issue_description | TEXT | Required |
| date_received | DATE | Required |
| status | ENUM | Five repair statuses, default `Received` |
| cost | DECIMAL(10,2) | Cannot be negative |

## How to Use

1. **Add:** fill in the fields marked `*` and click **Add**.
2. **Update:** click a row in the table, change the fields and click **Update**.
3. **Delete:** click a row and click **Delete**, then confirm.
4. **Clear:** empties the form and deselects the row.

The date must be entered as `yyyy-MM-dd` (for example `2026-11-15`).

## Troubleshooting

| Problem | Fix |
| --- | --- |
| No suitable driver found | Add the JAR in `lib` to the classpath or Referenced Libraries |
| Access denied for user | Check the username and password in `DBConnect.java` |
| Unknown database / table doesn't exist | Run `campusfix-DBS.sql` in MySQL Workbench |
| Communications link failure | Make sure the MySQL server is running |
| Public Key Retrieval is not allowed | Add `allowPublicKeyRetrieval=true` to `db.url` in `db.properties` |
| db.properties not found | Copy `db.properties.example` to `db.properties` and enter your password |
