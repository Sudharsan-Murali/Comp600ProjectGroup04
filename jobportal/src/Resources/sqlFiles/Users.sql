USE job_portal;

CREATE TABLE IF NOT EXISTS Users (
    User_ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    First_name varchar(20) not null,
    Last_name varchar(20) not null,
    Email nvarchar(320) not null,
    Mobile nvarchar(18) ,
    Dob date,
    Password nvarchar(16) not null,
    Confirm_password nvarchar(16) not null,
    Security_Que nvarchar(255) not null,
    Security_Ans nvarchar(255) not null,
    Role_ID int(4),
    Company_ID int(20),
    Job_role nvarchar(35),
    Skills nvarchar(1000),
    LinkedIN_url varchar(2048),
    Profile_pic longblob,
    Resume_default longblob,
    Availability varchar(50),
    
    FOREIGN KEY (Role_ID) REFERENCES User_roles(Role_ID)ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Company_ID) REFERENCES Company(Company_ID)ON DELETE CASCADE ON UPDATE CASCADE
)AUTO_INCREMENT = 1;
