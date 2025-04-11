USE job_portal;

CREATE TABLE IF NOT EXISTS Company (
    Company_ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    Company_name VARCHAR(50) NOT NULL,
    Company_location VARCHAR(100)NOT NULL
)AUTO_INCREMENT = 1001;

ALTER TABLE Company
ADD Company_Website VARCHAR(100);

UPDATE Company SET Company_Website = 'https://SuperStore.com' WHERE Company_ID = 1001;
UPDATE Company SET Company_Website = 'https://Saskjobs.com' WHERE Company_ID = 1002;
UPDATE Company SET Company_Website = 'https://Wallmart.com' WHERE Company_ID = 1003;
UPDATE Company SET Company_Website = 'https://Saskpoly.com' WHERE Company_ID = 1004;
UPDATE Company SET Company_Website = 'https://library.com' WHERE Company_ID = 1005;
UPDATE Company SET Company_Website = 'https://RBC.com' WHERE Company_ID = 1006;
UPDATE Company SET Company_Website = 'https://Tim Hottens.com' WHERE Company_ID = 1007;
UPDATE Company SET Company_Website = 'https://Starbugs.com' WHERE Company_ID = 1008;
UPDATE Company SET Company_Website = 'https://Dollarama.com' WHERE Company_ID = 1009;
UPDATE Company SET Company_Website = 'https://Nophrills.com' WHERE Company_ID = 1010;