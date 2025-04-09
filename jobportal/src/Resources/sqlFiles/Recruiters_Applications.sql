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




