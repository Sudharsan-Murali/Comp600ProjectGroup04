USE job_portal;

CREATE TABLE IF NOT EXISTS Recruiters_Applications (
    Job_ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    Job_Title VARCHAR(100),
    Job_Type int,
    Salary_Min DECIMAL(10,2),
    Salary_max DECIMAL(10,2),
    Job_location VARCHAR(50),
    Job_Description NVARCHAR(400),
    Required_Experience VARCHAR(100),
    Date_Of_Application DATE,
	FOREIGN KEY (Job_Type) REFERENCES Job_Type(JobType_ID)ON DELETE CASCADE ON UPDATE CASCADE

)AUTO_INCREMENT = 11;

ALTER TABLE Recruiters_Applications
ADD COLUMN user_ID INT,
ADD COLUMN company_ID INT;

UPDATE Recruiters_Applications SET user_ID = 13, company_ID = 1003 WHERE Job_ID = 11;
UPDATE Recruiters_Applications SET user_ID = 14, company_ID = 1004 WHERE Job_ID = 12;
UPDATE Recruiters_Applications SET user_ID = 16, company_ID = 1006 WHERE Job_ID = 13;
UPDATE Recruiters_Applications SET user_ID = 18, company_ID = 1008 WHERE Job_ID = 14;
UPDATE Recruiters_Applications SET user_ID = 20, company_ID = 1010 WHERE Job_ID = 15;
UPDATE Recruiters_Applications SET user_ID = 18, company_ID = 1008 WHERE Job_ID = 16;
UPDATE Recruiters_Applications SET user_ID = 14, company_ID = 1004 WHERE Job_ID = 17;
UPDATE Recruiters_Applications SET user_ID = 13, company_ID = 1003 WHERE Job_ID = 18;

UPDATE Recruiters_Applications SET user_ID = 20, company_ID = 1010 WHERE Job_ID = 19;

UPDATE Recruiters_Applications SET user_ID = 16, company_ID = 1006 WHERE Job_ID = 20;

ALTER TABLE Recruiters_Applications
ADD CONSTRAINT fk_user_id
FOREIGN KEY (user_id) REFERENCES Users(User_ID)
ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT fk_company_id
FOREIGN KEY (company_ID) REFERENCES Company(Company_ID)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Recruiters_Applications
MODIFY user_id INT NOT NULL,
MODIFY company_ID INT NOT NULL;



