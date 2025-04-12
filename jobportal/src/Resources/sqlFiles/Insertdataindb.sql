use Job_Portal;

-- INSERT INTO User_Roles (Role_ID, Role_type) VALUES
-- (1, 'Admin'),
-- (2, 'Recruiter'),
-- (3, 'User');

-- INSERT INTO Security_Ques (Que_ID, Security_Question) VALUES
-- (1, 'What is your pet’s name?'),
-- (2, 'What is your favorite book?'),
-- (3, 'What is your favorite color?'),
-- (4, 'What is your favorite food?'),
-- (5, 'What is your favorite Sports?');

INSERT INTO Application_Status (Status_ID, Status_type) VALUES
-- (1, 'Pending'),
-- (2, 'Accepted'),
-- (3, 'Rejected'),
-- (4, 'Under Review'),
-- (5, 'Shortlisted'),
-- (6, 'Interview Scheduled'),
-- (7, 'Offer Sent'),
-- (8, 'Offer Declined'),
-- (9, 'On Hold'),
-- (10, 'Withdrawn'),
(11, 'Open'),
(12, 'Closed'),
(13, 'Paused');

-- INSERT INTO Job_Type (JobType_ID, Job_Type) VALUES
-- (1, 'Onsite'),
-- (2, 'Remote'),
-- (3, 'Hybrid');

-- INSERT INTO Company (Company_name, Company_location) VALUES
-- ('SuperStore', 'Regina'),
-- ('Saskjobs', 'Moose Jaw'),
-- ('Wallmart', 'Moose Jaw'),
-- ('Saskpoly', 'Saskatoon'),
-- ('library', 'Moose Jaw'),
-- ('RBC', 'Regina'),
-- ('Tim Horttens', 'Saskatoon'),
-- ('Starbugs', 'Regina'),
-- ('Dollarama', 'Saskatoon'),
-- ('No phrills', 'Moose Jaw');

-- INSERT INTO Users (
--     First_name, Last_name, Email, Mobile, Dob, Password, Security_Que, Security_Ans,
--     Role_ID, Company_ID, Job_role, Skill_1, Skill_2, Skill_3, Skill_4, Field_Of_Study, Duration, Course_Type, Experience, LinkedIN_url,
--     Profile_pic, Resume_default, Availability
-- )
-- VALUES
-- ('Poojan', 'Gandhi', 'Pg991@example.com', '1234567890', '1990-05-01', 'pass1234', 1, 'Fluffy', 1, 1001, 'Developer', 'Java', 'SQL', 'HTML', 'CSS', 'EC', '4 years', 'bachelor''s''Degree','3 years','https://linkedin.com/in/johndoe', NULL, NULL, 'Part - Time'),
-- ('Janaki', 'Katharotiya', 'JK787@example.com', '2345678901', '1992-03-15', 'alice@321', 2, 'Blue', 2, 1002, 'Manager', 'Leadership', 'Planning', 'Scrum', 'Communication', 'BCA', '3 years', 'bachelor''s''Degree','2 years', 'https://linkedin.com/in/alicesmith', NULL, NULL, 'Full Time'),
-- ('habeeb', 'Adesina', 'bee754@example.com', '3456789012', '1985-11-22', 'bob!pass', 3, 'Pizza', 3, 1003, 'Data Engineer', 'Python', 'SQL', 'ETL', 'Airflow', 'BSC IT', '4 years', 'Certificate','1 years', 'https://linkedin.com/in/bobtaylor', NULL, NULL, 'Part - Time'),
-- ('Suryadarshini', 'Ganeshamoorthy', 'Sg111@example.com', '4567890123', '1998-07-30', 'carolpass', 4, 'Fries', 3, 1004, 'DevOps Engineer', 'Docker', 'Kubernetes', 'AWS', 'Terraform', 'MCA', '2 years', 'Master''s''Degree','4 years','https://linkedin.com/in/caroljones', NULL, NULL, 'Full Time'),
-- ('Sudharshan', 'Murali', 'SM456@example.com', '5678901234', '1995-09-10', 'dav!234', 5, 'Batman', 2, 1005, 'Frontend Dev', 'React', 'JS', 'CSS', 'Figma', 'Computer Science', '4 years', 'Diploma','3 years','https://linkedin.com/in/davidwhite', NULL, NULL, 'Full Time'),
-- ('Krishna', 'Patel', 'KP191@example.com', '6789012345', '1993-02-28', 'ell@pass', 2, 'Rome', 3, 1006, 'Security Analyst', 'Networking', 'Ethical Hacking', 'Linux', 'SIEM', 'Technology Management', '1 years', 'bachelor''s''Degree','2 years','https://linkedin.com/in/ellabrown', NULL, NULL, 'Full Time'),
-- ('Urvashee', 'Brambhatt', 'UB222@example.com', '7890123456', '1991-08-12', 'frank123', 3, 'Gray', 2, 1007, 'ML Engineer', 'Python', 'TensorFlow', 'Data Analysis', 'Pandas', 'Data Analysts', '4 years', 'Master''s''Degree','3 years','https://linkedin.com/in/frankwilson', NULL, NULL, 'Part - Time'),
-- ('Vandan', 'Amin', 'VA455@example.com', '8901234567', '1996-06-18', 'grace#123', 4, 'Rice', 3, 1008, 'Fintech Dev', 'Java', 'Spring', 'SQL', 'APIs', 'Software development', '3 years', 'bachelor''s''Degree','1 years','https://linkedin.com/in/gracedavis', NULL, NULL, 'Full Time'),
-- ('Prabhnoor', 'Singh', 'PS556@example.com', '9012345678', '1989-01-01', 'henry@xyz', 4, 'Chocolate', 2, 1009, 'Blockchain Dev', 'Solidity', 'Ethereum', 'Smart Contracts', 'Node.js', 'Cybersecurity', '4 years', 'Certificate','3 years','https://linkedin.com/in/henryevans', NULL, NULL, 'Full Time'),
-- ('Harshvi', 'Modi', 'HM656@example.com', '1122334455', '1994-10-25', 'ivypass!', 3, 'White', 3, 1010, 'Game Designer', 'Unity', 'C#', 'UX Design', '2D Art', 'Machine Learning', '3 years', 'bachelor''s''Degree','2 years','https://linkedin.com/in/ivygreen', NULL, NULL, 'Part - Time');

-- INSERT INTO Recruiters_Applications (
--     Job_Title, Job_Type, Salary_Min, Salary_max, Job_location, Job_Description, Required_Experience, Date_Of_Application
-- )
-- VALUES
-- ('Backend Developer', 1, 50000.00, 80000.00, 'Moose Jaw', 'Backend role using Java and Spring', '2 years', '2025-01-10'),
-- ('Data Analyst', 2, 40000.00, 70000.00, 'PA', 'Analyze large data sets', '1 years', '2025-01-15'),
-- ('ML Engineer', 3, 60000.00, 100000.00, 'Regina', 'Work on ML projects', '3 years', '2025-01-20'),
-- ('Frontend Developer', 1, 45000.00, 85000.00, 'Saskatoon', 'React and Angular projects', '2+ years', '2025-02-01'),
-- ('Cloud Engineer', 2, 55000.00, 90000.00, 'Torronto', 'AWS, GCP expertise', '3+ years', '2025-02-10'),
-- ('Security Analyst', 1, 60000.00, 95000.00, 'Chicago', 'Network and application security', '2+ years', '2025-02-20'),
-- ('DevOps Engineer', 3, 65000.00, 100000.00, 'Remote', 'CI/CD pipelines', '3 years', '2025-03-01'),
-- ('Blockchain Developer', 1, 70000.00, 110000.00, 'Remote', 'Work on smart contracts', '3 years', '2025-03-10'),
-- ('Game Designer', 2, 40000.00, 80000.00, 'Remote', 'Unity and game assets', '2 years', '2025-03-15'),
-- ('Product Manager', 1, 75000.00, 120000.00, 'San Francisco', 'Team leadership and product strategy', '1 years', '2025-03-20');



-- INSERT INTO Applications_User (Job_Title, Application_Date, User_ID, Company_ID, Status_ID) VALUES
-- ('Backend Developer', '2025-02-01', 1, 1001, 1),
-- ('Data Analyst', '2025-02-02', 2, 1002, 2),
-- ('ML Engineer', '2025-02-03', 3, 1003, 1),
-- ('Frontend Developer', '2025-02-04', 4, 1004, 4),
-- ('Cloud Engineer', '2025-02-05', 5, 1005, 2),
-- ('Security Analyst', '2025-02-06', 6, 1006, 3),
-- ('DevOps Engineer', '2025-02-07', 7, 1007, 1),
-- ('Blockchain Developer', '2025-02-08', 8, 1008, 5),
-- ('Game Designer', '2025-02-09', 9, 1009, 2),
-- ('Product Manager', '2025-02-10', 10, 1010, 1); 
