CREATE DATABASE IF NOT EXISTS campusfix_db;
USE campusfix_db;

-- Remove old tables so this script can be rerun safely
DROP TABLE IF EXISTS repair_jobs;
DROP TABLE IF EXISTS services;

-- Columns match the fields in Services.java
CREATE TABLE services (
    service_id INT AUTO_INCREMENT,
    service_name VARCHAR(100) NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    customer_email VARCHAR(100),
    customer_phone VARCHAR(20) NOT NULL,
    device_name VARCHAR(60) NOT NULL,
    device_brand VARCHAR(60),
    issue_description TEXT NOT NULL,
    date_received DATE NOT NULL,
    status ENUM(
        'Received',
        'Diagnosing',
        'In Progress',
        'Ready for Collection',
        'Collected'
    ) NOT NULL DEFAULT 'Received',
    cost DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    PRIMARY KEY (service_id),
    CHECK (cost >= 0)
);

-- Sample data
INSERT INTO services
(service_name, customer_name, customer_email, customer_phone,
 device_name, device_brand, issue_description, date_received, status, cost)
VALUES
('Power Fault Repair', 'John Mwangi', 'john.mwangi@example.com', '0712345678',
 'Laptop', 'HP', 'Laptop not powering on', '2026-11-10', 'Received', 2500.00),

('Screen Replacement', 'Mary Wanjiku', 'mary.wanjiku@example.com', '0723456789',
 'Laptop', 'Dell', 'Broken laptop screen', '2026-11-11', 'Diagnosing', 4500.00),

('Performance Tune-up', 'Peter Otieno', 'peter.otieno@example.com', '0734567890',
 'Desktop', 'Lenovo', 'Computer running slowly', '2026-11-12', 'In Progress', 1500.00),

('Keyboard Replacement', 'Grace Achieng', 'grace.achieng@example.com', '0745678901',
 'Laptop', 'Lenovo', 'Keyboard not working', '2026-11-13', 'Ready for Collection', 2000.00),

('OS Installation', 'David Kamau', 'david.kamau@example.com', '0756789012',
 'Desktop', 'HP', 'Operating system installation', '2026-11-14', 'Collected', 1000.00);

-- Quick check
SELECT * FROM services;