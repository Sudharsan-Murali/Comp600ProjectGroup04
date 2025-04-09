USE job_portal;

CREATE TABLE IF NOT EXISTS Users (
    User_ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    First_name VARCHAR(20) NOT NULL,
    Last_name VARCHAR(20) NOT NULL,
    Email NVARCHAR(320) NOT NULL,
    Mobile VARCHAR(18) ,
    Dob Date,
    Password NVARCHAR(16) NOT NULL,
    Security_Que INT,
    Security_Ans NVARCHAR(255) NOT NULL,
    Role_ID INT(4),
    Company_ID INT(20),
    Job_role VARCHAR(35),
    Skill_1 VARCHAR(100), 
    Skill_2 VARCHAR(100), 
    Skill_3 VARCHAR(100), 
    Skill_4 VARCHAR(100), 
    Field_Of_Study VARCHAR(100),
    Duration VARCHAR(100),
    Course_Type VARCHAR(100),
    Experience VARCHAR(100),
    LinkedIN_url VARCHAR(2048),
    Profile_pic LONGBLOB,
    Resume_default LONGBLOB,
    Availability VARCHAR(50),
    
    FOREIGN KEY (Security_Que) REFERENCES security_ques(Que_ID)ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Role_ID) REFERENCES User_roles(Role_ID)ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Company_ID) REFERENCES Company(Company_ID)ON DELETE CASCADE ON UPDATE CASCADE
    
)AUTO_INCREMENT = 1;


