use Job_Portal;

INSERT INTO User_Roles (Role_ID, Role_type) VALUES
(3, 'Admin'),
(2, 'Recruiter'),
(1, 'User');

INSERT INTO Security_Ques (Que_ID, Security_Question) VALUES
(1, 'What is your pet’s name?'),
(2, 'What is your favorite book?'),
(3, 'What is your favorite color?'),
(4, 'What is your favorite food?'),
(5, 'What is your favorite Sports?');

INSERT INTO Application_Status (Status_ID, Status_type) VALUES
(1, 'Pending'),
(2, 'Accepted'),
(3, 'Rejected'),
(4, 'Under Review'),
(5, 'Shortlisted'),
(6, 'Interview Scheduled'),
(7, 'Offer Sent'),
(8, 'Offer Declined'),
(9, 'On Hold'),
(10, 'Withdrawn'),
(11, 'Open'),
(12, 'Closed'),
(13, 'Paused');

INSERT INTO Job_Type (JobType_ID, Job_Type) VALUES
(1, 'Onsite'),
(2, 'Remote'),
(3, 'Hybrid');


INSERT INTO company (Company_name, Company_location, Company_Website)
VALUES
('SuperStore', 'Regina, SK', 'https://www.superstore.ca'),
('Saskjobs', 'Moose Jaw, SK', 'https://www.saskjobs.ca'),
('Walmart', 'Moose Jaw, SK', 'https://www.walmart.ca'),
('SaskPolytechnic', 'Saskatoon, SK', 'https://www.saskpolytechnic.ca'),
('Library', 'Moose Jaw, SK', NULL),
('RBC', 'Regina, SK', 'https://www.rbc.com'),
('Tim Hortons', 'Saskatoon, SK', 'https://www.timhortons.ca'),
('Starbucks', 'Regina, SK', 'https://www.starbucks.ca'),
('Dollarama', 'Saskatoon, SK', 'https://www.dollarama.com'),
('No Frills', 'Moose Jaw, SK', 'https://www.nofrills.ca'),
('SaskTech Solutions', 'Saskatoon, SK', 'https://www.sasktech.com'),
('Prairie Foods Inc.', 'Regina, SK', 'https://www.prairiefoods.com'),
('Northern Energy Corp.', 'Moose Jaw, SK', 'https://www.northernenergy.com'),
('Great Plains Construction', 'Prince Albert, SK', 'https://www.gpconstruction.com'),
('Sunrise Financial', 'Saskatoon, SK', 'https://www.sunrisefinancial.com'),
('Swift Current Logistics', 'Swift Current, SK', 'https://www.swiftcurrentlogistics.com'),
('Mosaic Minerals', 'Estevan, SK', 'https://www.mosaicminerals.com'),
('Prairie Wind Energy', 'Regina, SK', 'https://www.prairiewindenergy.com'),
('Lakeview Health', 'Saskatoon, SK', 'https://www.lakeviewhealth.com'),
('Maple Leaf Innovations', 'Moose Jaw, SK', 'https://www.mapleleafinnovations.com'),
('Prairie Cloud Tech', 'Regina, SK', 'https://www.prairiecloudtech.com'),
('Frontier Farming', 'Swift Current, SK', 'https://www.frontierfarming.com'),
('Granite Building Materials', 'Prince Albert, SK', 'https://www.granitebuild.com'),
('Boreal IT Services', 'Saskatoon, SK', 'https://www.borealits.com'),
('River Valley Resources', 'Moose Jaw, SK', 'https://www.rivervalleyresources.com'),
('Northern Lights Media', 'Regina, SK', 'https://www.northernlightsmedia.com'),
('Prairie Gold Mining', 'Saskatoon, SK', 'https://www.prairiegoldmining.com'),
('Heritage Engineering', 'Prince Albert, SK', 'https://www.heritageeng.com'),
('Capital Banking', 'Regina, SK', 'https://www.capitalbanking.com'),
('Rural Communications', 'Swift Current, SK', 'https://www.ruralcomm.ca');

INSERT INTO Users (
    First_name, Last_name, Email, Mobile, Dob, Password, Security_Que, Security_Ans,
    Role_ID, Company_ID, Job_role, Skill_1, Skill_2, Skill_3, Skill_4, Field_Of_Study, Duration, Course_Type, Experience, LinkedIN_url,
    Profile_pic, Resume_default, Availability
)
VALUES
('Poojan', 'Gandhi', 'Pg991@example.com', '1234567890', '1990-05-01', 'pass1234', 1, 'Fluffy', 1, 1001, 'Developer', 'Java', 'SQL', 'HTML', 'CSS', 'EC', '4 years', 'bachelor''s Degree', '3 years', 'https://linkedin.com/in/johndoe', NULL, NULL, 'Part - Time'),
('Janaki', 'Katharotiya', 'JK787@example.com', '2345678901', '1992-03-15', 'alice@321', 2, 'Blue', 2, 1002, 'Manager', 'Leadership', 'Planning', 'Scrum', 'Communication', 'BCA', '3 years', 'bachelor''s Degree', '2 years', 'https://linkedin.com/in/alicesmith', NULL, NULL, 'Full Time'),
('habeeb', 'Adesina', 'bee754@example.com', '3456789012', '1985-11-22', 'bob!pass', 3, 'Pizza', 3, 1003, 'Data Engineer', 'Python', 'SQL', 'ETL', 'Airflow', 'BSC IT', '4 years', 'Certificate', '1 years', 'https://linkedin.com/in/bobtaylor', NULL, NULL, 'Part - Time'),
('Suryadarshini', 'Ganeshamoorthy', 'Sg111@example.com', '4567890123', '1998-07-30', 'carolpass', 4, 'Fries', 2, 1004, 'DevOps Engineer', 'Docker', 'Kubernetes', 'AWS', 'Terraform', 'MCA', '2 years', 'Master''s Degree', '4 years', 'https://linkedin.com/in/caroljones', NULL, NULL, 'Full Time'),
('Sudharshan', 'Murali', 'SM456@example.com', '5678901234', '1995-09-10', 'dav!234', 5, 'Batman', 1, 1005, 'Frontend Dev', 'React', 'JS', 'CSS', 'Figma', 'Computer Science', '4 years', 'Diploma', '3 years', 'https://linkedin.com/in/davidwhite', NULL, NULL, 'Full Time'),
('Krishna', 'Patel', 'KP191@example.com', '6789012345', '1993-02-28', 'ell@pass', 2, 'Rome', 2, 1006, 'Security Analyst', 'Networking', 'Ethical Hacking', 'Linux', 'SIEM', 'Technology Management', '1 years', 'bachelor''s Degree', '2 years', 'https://linkedin.com/in/ellabrown', NULL, NULL, 'Full Time'),
('Urvashee', 'Brambhatt', 'UB222@example.com', '7890123456', '1991-08-12', 'frank123', 3, 'Gray', 1, 1007, 'ML Engineer', 'Python', 'TensorFlow', 'Data Analysis', 'Pandas', 'Data Analysts', '4 years', 'Master''s Degree', '3 years', 'https://linkedin.com/in/frankwilson', NULL, NULL, 'Part - Time'),
('Vandan', 'Amin', 'VA455@example.com', '8901234567', '1996-06-18', 'grace#123', 4, 'Rice', 2, 1008, 'Fintech Dev', 'Java', 'Spring', 'SQL', 'APIs', 'Software development', '3 years', 'bachelor''s Degree', '1 years', 'https://linkedin.com/in/gracedavis', NULL, NULL, 'Full Time'),
('Prabhnoor', 'Singh', 'PS556@example.com', '9012345678', '1989-01-01', 'henry@xyz', 4, 'Chocolate', 1, 1009, 'Blockchain Dev', 'Solidity', 'Ethereum', 'Smart Contracts', 'Node.js', 'Cybersecurity', '4 years', 'Certificate', '3 years', 'https://linkedin.com/in/henryevans', NULL, NULL, 'Full Time'),
('Harshvi', 'Modi', 'HM656@example.com', '1122334455', '1994-10-25', 'ivypass!', 3, 'White', 2, 1010, 'Game Designer', 'Unity', 'C#', 'UX Design', '2D Art', 'Machine Learning', '3 years', 'bachelor''s Degree', '2 years', 'https://linkedin.com/in/ivygreen', NULL, NULL, 'Part - Time'),
('John', 'Doe', 'john.doe@example.ca', '306-555-0001', '1985-06-15', 'Password123', 1, 'Fluffy', 1, 1011, 'Software Developer', 'Java', 'SQL', 'HTML', 'CSS', 'Computer Science', '4 years', 'Full Time', '5 years', 'https://www.linkedin.com/in/john-doe', NULL, NULL, 'Yes'),
('Emily', 'Smith', 'emily.smith@example.ca', '306-555-0002', '1990-03-22', 'SecurePass456', 1, 'Buddy', 2, 1012, 'Data Analyst', 'Python', 'R', 'SQL', 'Excel', 'Statistics', '4 years', 'Full Time', '3 years', 'https://www.linkedin.com/in/emily-smith', NULL, NULL, 'Yes'),
('Michael', 'Johnson', 'michael.johnson@example.ca', '306-555-0003', '1982-11-08', 'MyPass789', 1, 'Spike', 1, 1013, 'Project Manager', 'Leadership', 'Communication', 'Planning', 'Budgeting', 'Business Admin', '5 years', 'Full Time', '7 years', 'https://www.linkedin.com/in/michael-johnson', NULL, NULL, 'Yes'),
('Sarah', 'Williams', 'sarah.williams@example.ca', '306-555-0004', '1988-01-17', 'NurseSecure1', 1, 'Whiskers', 2, 1014, 'Registered Nurse', 'Patient Care', 'CPR', 'Record Keeping', 'Comms', 'Nursing', '3 years', 'Full Time', '4 years', 'https://www.linkedin.com/in/sarah-williams', NULL, NULL, 'Yes'),
('Robert', 'Brown', 'robert.brown@example.ca', '306-555-0005', '1979-05-30', 'AcctPass234', 1, 'Max', 1, 1015, 'Accountant', 'Accounting', 'Excel', 'Taxation', 'Auditing', 'Accounting', '4 years', 'Full Time', '6 years', 'https://www.linkedin.com/in/robert-brown', NULL, NULL, 'Yes'),
('Jessica', 'Davis', 'jessica.davis@example.ca', '306-555-0006', '1992-09-12', 'Market789', 1, 'Charlie', 2, 1016, 'Marketing Spec', 'SEO', 'Content', 'Social Media', 'Analytics', 'Marketing', '4 years', 'Full Time', '4 years', 'https://www.linkedin.com/in/jessica-davis', NULL, NULL, 'Yes'),
('David', 'Miller', 'david.miller@example.ca', '306-555-0007', '1984-04-25', 'Eng!neer321', 1, 'Buddy', 1, 1017, 'Mech Engineer', 'CAD', 'MATLAB', 'Problem Solving', 'Proj Mgmt', 'Mechanical Eng', '4 years', 'Full Time', '5 years', 'https://www.linkedin.com/in/david-miller', NULL, NULL, 'Yes'),
('Linda', 'Wilson', 'linda.wilson@example.ca', '306-555-0008', '1975-07-03', 'HRsecure007', 1, 'Daisy', 2, 1018, 'HR Manager', 'Recruitment', 'Employment Law', 'Conflict Res', 'Comms', 'HR', '4 years', 'Full Time', '8 years', 'https://www.linkedin.com/in/linda-wilson', NULL, NULL, 'Yes'),
('James', 'Moore', 'james.moore@example.ca', '306-555-0009', '1987-12-19', 'SalesPro321', 1, 'Buddy', 1, 1019, 'Sales Manager', 'Negotiation', 'CRM', 'Lead Gen', 'Comms', 'Business Admin', '4 years', 'Full Time', '6 years', 'https://www.linkedin.com/in/james-moore', NULL, NULL, 'Yes'),
('Karen', 'Taylor', 'karen.taylor@example.ca', '306-555-0010', '1993-08-05', 'ITSupPort9', 1, 'Shadow', 2, 1020, 'IT Support Spec', 'Troubleshoot', 'Networking', 'Windows', 'Cust Serv', 'Info Tech', '3 years', 'Full Time', '3 years', 'https://www.linkedin.com/in/karen-taylor', NULL, NULL, 'Yes'),
('William', 'Anderson', 'william.anderson@example.ca', '306-555-0011', '1980-10-10', 'BizAnalyst42', 1, 'Luna', 1, 1011, 'Business Analyst', 'Data Analysis', 'Proc Improve', 'SQL', 'Excel', 'Business', '4 years', 'Full Time', '4 years', 'https://www.linkedin.com/in/william-anderson', NULL, NULL, 'Yes'),
('Patricia', 'Thomas', 'patricia.thomas@example.ca', '306-555-0012', '1983-02-28', 'CivEngPass88', 1, 'Bella', 2, 1012, 'Civil Engineer', 'AutoCAD', 'Proj Mgmt', 'Analysis', 'Design', 'Civil Eng', '4 years', 'Full Time', '5 years', 'https://www.linkedin.com/in/patricia-thomas', NULL, NULL, 'Yes'),
('Christopher', 'Jackson', 'christopher.jackson@example.ca', '306-555-0013', '1991-06-06', 'Graph!cs2020', 1, 'Rocky', 1, 1013, 'Graphic Designer', 'Photoshop', 'Illustrator', 'Creativity', 'Typography', 'Graphic Design', '4 years', 'Full Time', '3 years', 'https://www.linkedin.com/in/christopher-jackson', NULL, NULL, 'Yes'),
('Barbara', 'White', 'barbara.white@example.ca', '306-555-0014', '1978-11-11', 'Construct#1', 1, 'Molly', 2, 1014, 'Constr Manager', 'Proj Mgmt', 'Safety', 'Planning', 'Team Lead', 'Constr Mgmt', '4 years', 'Full Time', '8 years', 'https://www.linkedin.com/in/barbara-white', NULL, NULL, 'Yes'),
('Daniel', 'Harris', 'daniel.harris@example.ca', '306-555-0015', '1986-03-03', 'ElecSafe007', 1, 'Coco', 1, 1015, 'Electrician', 'Wiring', 'Circuits', 'Maintenance', 'Safety', 'Elect Eng', '3 years', 'Apprentice', '6 years', 'https://www.linkedin.com/in/daniel-harris', NULL, NULL, 'Yes'),
('Jennifer', 'Martin', 'jennifer.martin@example.ca', '306-555-0016', '1994-07-14', 'ChefDelight!', 1, 'Peaches', 2, 1016, 'Chef', 'Culinary', 'Menu Plan', 'Food Safety', 'Creativity', 'Culinary Arts', '2 years', 'Full Time', '7 years', 'https://www.linkedin.com/in/jennifer-martin', NULL, NULL, 'Yes'),
('Matthew', 'Thompson', 'matthew.thompson@example.ca', '306-555-0017', '1989-12-02', 'Pharm@Safe', 1, 'Shadow', 1, 1017, 'Pharmacist', 'Med Mgmt', 'Comms', 'Detail', 'Cust Serv', 'Pharmacy', '5 years', 'Full Time', '8 years', 'https://www.linkedin.com/in/matthew-thompson', NULL, NULL, 'Yes'),
('Elizabeth', 'Garcia', 'elizabeth.garcia@example.ca', '306-555-0018', '1993-04-21', 'DentalPro!', 1, 'Buddy', 2, 1018, 'Dental Hygienist', 'Cleaning', 'Pat Care', 'Sterilize', 'Comms', 'Dentistry', '3 years', 'Part Time', '4 years', 'https://www.linkedin.com/in/elizabeth-garcia', NULL, NULL, 'Yes'),
('Anthony', 'Martinez', 'anthony.martinez@example.ca', '306-555-0019', '1987-09-09', 'ITconsult99', 1, 'Rocky', 1, 1019, 'IT Consultant', 'Sys Analysis', 'IT Strategy', 'Cloud', 'Security', 'Info Tech', '4 years', 'Full Time', '6 years', 'https://www.linkedin.com/in/anthony-martinez', NULL, NULL, 'Yes'),
('Susan', 'Robinson', 'susan.robinson@example.ca', '306-555-0020', '1990-02-14', 'EnvSci2021', 1, 'Luna', 2, 1020, 'Env Scientist', 'Data Analysis', 'Field Res', 'Reports', 'Sustainability', 'Env Sci', '4 years', 'Full Time', '4 years', 'https://www.linkedin.com/in/susan-robinson', NULL, NULL, 'Yes'),
('Mark', 'Clark', 'mark.clark@example.ca', '306-555-0021', '1982-08-08', 'SocialWork!21', 1, 'Max', 1, 1011, 'Social Worker', 'Empathy', 'Crisis Mgmt', 'Counseling', 'Comms', 'Social Work', '4 years', 'Full Time', '5 years', 'https://www.linkedin.com/in/mark-clark', NULL, NULL, 'Yes'),
('Nancy', 'Rodriguez', 'nancy.rodriguez@example.ca', '306-555-0022', '1985-05-05', 'LawyerSafe5', 1, 'Buddy', 2, 1012, 'Lawyer', 'Legal Research', 'Negotiation', 'Comms', 'Writing', 'Law', '5 years', 'Full Time', '7 years', 'https://www.linkedin.com/in/nancy-rodriguez', NULL, NULL, 'Yes');



INSERT INTO recruiters_applications 
  (Job_Title, Job_Type, Salary_Min, Salary_max, Job_location, Job_Description, Required_Experience, Date_Of_Application, user_ID, company_ID)
VALUES
  ('Backend Developer', 1, 50000.00, 80000.00, 'Moose Jaw, SK', 'Backend role using Java and Spring', '2 years', '2025-01-10', 1, 1013),
  ('Data Analyst', 2, 40000.00, 70000.00, 'Regina, SK', 'Analyze large data sets', '1 year', '2025-01-15', 2, 1011),
  ('ML Engineer', 3, 60000.00, 100000.00, 'Regina, SK', 'Work on ML projects', '3 years', '2025-01-20', 3, 1016),
  ('Frontend Developer', 1, 45000.00, 85000.00, 'Saskatoon, SK', 'React and Angular projects', '2+ years', '2025-02-01', 4, 1014),
  ('Cloud Engineer', 2, 55000.00, 90000.00, 'Saskatoon, SK', 'Expertise in AWS and GCP cloud services', '3+ years', '2025-02-10', 5, 1019),
  ('Security Analyst', 1, 60000.00, 95000.00, 'Regina, SK', 'Focus on network and application security', '2+ years', '2025-02-20', 6, 1011),
  ('DevOps Engineer', 3, 65000.00, 100000.00, 'Saskatoon, SK', 'CI/CD pipelines implementation', '3 years', '2025-03-01', 7, 1014),
  ('Blockchain Developer', 1, 70000.00, 110000.00, 'Saskatoon, SK', 'Develop smart contracts for blockchain platforms', '3 years', '2025-03-10', 8, 1019),
  ('Game Designer', 2, 40000.00, 80000.00, 'Saskatoon, SK', 'Design Unity-based games and assets', '2 years', '2025-03-15', 9, 1014),
  ('Product Manager', 1, 75000.00, 120000.00, 'Regina, SK', 'Team leadership and product strategy', '1 year', '2025-03-20', 10, 1018),
  ('Senior Software Developer', 1, 75000.00, 95000.00, 'Saskatoon, SK', 'Develop and maintain enterprise applications.', '5+ years', '2025-06-01', 1, 1011),
  ('Junior Data Analyst', 2, 40000.00, 60000.00, 'Regina, SK', 'Analyze datasets and produce reports.', '1-2 years', '2025-06-02', 2, 1012),
  ('Project Manager', 1, 70000.00, 90000.00, 'Moose Jaw, SK', 'Oversee projects from inception to delivery.', '3+ years', '2025-06-03', 3, 1013),
  ('Systems Engineer', 1, 65000.00, 85000.00, 'Prince Albert, SK', 'Design and manage network systems.', '4+ years', '2025-06-04', 4, 1014),
  ('UX/UI Designer', 2, 50000.00, 70000.00, 'Saskatoon, SK', 'Create intuitive user interfaces.', '3+ years', '2025-06-05', 5, 1015),
  ('QA Automation Engineer', 1, 60000.00, 80000.00, 'Swift Current, SK', 'Build and execute automated tests.', '3+ years', '2025-06-06', 6, 1016),
  ('Network Administrator', 1, 55000.00, 75000.00, 'Estevan, SK', 'Maintain and troubleshoot network issues.', '2+ years', '2025-06-07', 7, 1017),
  ('DevOps Engineer', 1, 70000.00, 90000.00, 'Regina, SK', 'Manage CI/CD pipelines and cloud systems.', '4+ years', '2025-06-08', 8, 1018),
  ('Business Analyst', 2, 55000.00, 75000.00, 'Saskatoon, SK', 'Document and analyze business requirements.', '3+ years', '2025-06-09', 9, 1019),
  ('Database Administrator', 1, 60000.00, 80000.00, 'Moose Jaw, SK', 'Administer and optimize database systems.', '3+ years', '2025-06-10', 10, 1020),
  ('HR Manager', 2, 65000.00, 85000.00, 'Saskatoon, SK', 'Oversee HR functions and recruitment.', '5+ years', '2025-06-11', 11, 1011),
  ('Marketing Specialist', 2, 45000.00, 65000.00, 'Regina, SK', 'Plan and execute marketing campaigns.', '2+ years', '2025-06-12', 12, 1012),
  ('Finance Analyst', 1, 60000.00, 80000.00, 'Moose Jaw, SK', 'Analyze financial data and trends.', '3+ years', '2025-06-13', 13, 1013),
  ('Sales Manager', 1, 65000.00, 90000.00, 'Prince Albert, SK', 'Lead a team of sales professionals.', '4+ years', '2025-06-14', 14, 1014),
  ('Technical Support Specialist', 2, 40000.00, 55000.00, 'Saskatoon, SK', 'Provide technical assistance to clients.', '1-2 years', '2025-06-15', 15, 1015),
  ('Product Manager', 1, 70000.00, 95000.00, 'Swift Current, SK', 'Manage product lifecycle and strategy.', '5+ years', '2025-06-16', 16, 1016),
  ('Content Writer', 2, 40000.00, 60000.00, 'Estevan, SK', 'Develop content for digital channels.', '2+ years', '2025-06-17', 17, 1017),
  ('Operations Manager', 1, 65000.00, 85000.00, 'Regina, SK', 'Oversee daily operations and processes.', '4+ years', '2025-06-18', 18, 1018),
  ('Software Tester', 1, 50000.00, 70000.00, 'Saskatoon, SK', 'Test software for quality and reliability.', '2+ years', '2025-06-19', 19, 1019),
  ('Customer Service Representative', 2, 35000.00, 50000.00, 'Moose Jaw, SK', 'Assist customers and handle inquiries.', '1+ year', '2025-06-20', 20, 1020);



INSERT INTO Applications_User (Job_Title, Application_Date, User_ID, Company_ID, Status_ID) VALUES
('Backend Developer', '2025-02-01', 1, 1001, 1),
('Data Analyst', '2025-02-02', 2, 1002, 2),
('ML Engineer', '2025-02-03', 3, 1003, 1),
('Frontend Developer', '2025-02-04', 4, 1004, 4),
('Cloud Engineer', '2025-02-05', 5, 1005, 2),
('Security Analyst', '2025-02-06', 6, 1006, 3),
('DevOps Engineer', '2025-02-07', 7, 1007, 1),
('Blockchain Developer', '2025-02-08', 8, 1008, 5),
('Game Designer', '2025-02-09', 9, 1009, 2),
('Product Manager', '2025-02-10', 10, 1010, 1),
('Software Developer', '2025-05-01', 1, 1011, 1),
('Data Analyst', '2025-05-02', 2, 1012, 2),
('Project Manager', '2025-05-03', 3, 1013, 3),
('Systems Engineer', '2025-05-04', 4, 1014, 1),
('UX Designer', '2025-05-05', 5, 1015, 2),
('QA Tester', '2025-05-06', 6, 1016, 3),
('Network Administrator', '2025-05-07', 7, 1017, 4),
('DevOps Engineer', '2025-05-08', 8, 1018, 5),
('Business Analyst', '2025-05-09', 9, 1019, 1),
('Database Administrator', '2025-05-10', 10, 1020, 2),
('HR Manager', '2025-05-11', 11, 1011, 3),
('Marketing Specialist', '2025-05-12', 12, 1012, 4),
('Finance Analyst', '2025-05-13', 13, 1013, 5),
('Sales Manager', '2025-05-14', 14, 1014, 1),
('Technical Support', '2025-05-15', 15, 1015, 2),
('Product Manager', '2025-05-16', 16, 1016, 3),
('Content Writer', '2025-05-17', 17, 1017, 4),
('Operations Manager', '2025-05-18', 18, 1018, 5),
('Software Tester', '2025-05-19', 19, 1019, 1),
('Customer Service Rep', '2025-05-20', 20, 1020, 2);
