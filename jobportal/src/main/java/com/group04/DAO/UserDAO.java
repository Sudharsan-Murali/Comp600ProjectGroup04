package com.group04.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/job_portal";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // === Registration, Login, and Profile Methods ===

    // Register a new user.
    public boolean registerUser(User user) {
        String query = "INSERT INTO Users (First_name, Last_name, Email, Mobile, Dob, Password, Role_ID, Security_Que, Security_Ans) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getMobile());
            stmt.setString(5, user.getDob());
            stmt.setString(6, user.getPassword());
            stmt.setInt(7, user.getRoleId());
            stmt.setString(8, user.getSecurityQuestion());
            stmt.setString(9, user.getSecurityAnswer());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Login using plain-text password.
    public boolean loginUser(String email, String password) {
        String query = "SELECT * FROM Users WHERE Email = ? AND Password = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Login success if a record is found.
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Recover password based on email, security question, and answer.
    public String recoverPassword(String email, String question, String answer) {
        String query = "SELECT Password FROM Users WHERE Email = ? AND Security_Que = ? AND Security_Ans = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, question);
            stmt.setString(3, answer);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Utility: Map role String to Role_ID.
    private int getRoleIdFromRole(String role) {
        // Note: Based on your inserts, Admin = 3, Recruiter = 2, and User = 1.
        if ("Admin".equalsIgnoreCase(role)) {
            return 3;
        } else if ("Recruiter".equalsIgnoreCase(role)) {
            return 2;
        } else if ("User".equalsIgnoreCase(role)) {
            return 1;
        }
        return -1;
    }

    // === Security Question Methods ===

    // Helper: Fetch actual security question from Security_Ques table.
    private String getQuestionText(String questionId) {
        String query = "SELECT Security_Question FROM `Security_Ques` WHERE Que_ID = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, questionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Security_Question");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get security question text for a given user and role.
    public String getSecurityQuestion(String email, String role) {
        int roleId = getRoleIdFromRole(role);
        if (roleId == -1) {
            System.err.println("Invalid role provided: " + role);
            return null;
        }
        String query = "SELECT `Security_Que` FROM Users WHERE Email = ? AND Role_ID = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setInt(2, roleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String questionId = rs.getString("Security_Que");
                return getQuestionText(questionId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get the security answer for a given user and role.
    public String getSecurityAnswer(String email, String role) {
        int roleId = getRoleIdFromRole(role);
        if (roleId == -1) {
            System.err.println("Invalid role provided: " + role);
            return null;
        }
        String query = "SELECT Security_Ans FROM Users WHERE Email = ? AND Role_ID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setInt(2, roleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Security_Ans");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update password for a user (using plain text).
    public boolean updatePassword(String email, String password, String role) {
        int roleId = getRoleIdFromRole(role);
        if (roleId == -1) {
            System.err.println("Invalid role provided: " + role);
            return false;
        }
        String query = "UPDATE Users SET Password = ? WHERE Email = ? AND Role_ID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, password);
            stmt.setString(2, email);
            stmt.setInt(3, roleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get role ID by email.
    public int getRoleId(String email) {
        String query = "SELECT Role_ID FROM Users WHERE Email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Role_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Retrieve user profile data.
    public User getUserProfile(String email) {
        String query = "SELECT u.First_name, u.Last_name, u.Email, u.Mobile, u.Dob, u.Password, u.Role_ID, " +
                "u.Security_Que, u.Security_Ans, c.Company_name, u.Job_role, u.Skill_1, u.Skill_2, u.Skill_3, " +
                "u.Skill_4, u.LinkedIN_url, u.Availability, u.Resume_default " +
                "FROM Users u " +
                "LEFT JOIN Company c ON u.Company_ID = c.Company_ID " +
                "WHERE u.Email = ?";
        User user = null;
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setFirstName(rs.getString("First_name"));
                user.setLastName(rs.getString("Last_name"));
                user.setEmail(rs.getString("Email"));
                user.setMobile(rs.getString("Mobile"));
                user.setDob(rs.getString("Dob"));
                user.setPassword(rs.getString("Password"));
                user.setRole(rs.getInt("Role_ID"));
                user.setSecurityQuestion(rs.getString("Security_Que"));
                user.setSecurityAnswer(rs.getString("Security_Ans"));
                user.setCompany(rs.getString("Company_name"));
                user.setJobRole(rs.getString("Job_role"));

                String skill1 = rs.getString("Skill_1");
                String skill2 = rs.getString("Skill_2");
                String skill3 = rs.getString("Skill_3");
                String skill4 = rs.getString("Skill_4");
                StringBuilder prefs = new StringBuilder();
                if (skill1 != null && !skill1.isEmpty()) {
                    prefs.append(skill1);
                }
                if (skill2 != null && !skill2.isEmpty()) {
                    if (prefs.length() > 0) {
                        prefs.append(", ");
                    }
                    prefs.append(skill2);
                }
                if (skill3 != null && !skill3.isEmpty()) {
                    if (prefs.length() > 0) {
                        prefs.append(", ");
                    }
                    prefs.append(skill3);
                }
                if (skill4 != null && !skill4.isEmpty()) {
                    if (prefs.length() > 0) {
                        prefs.append(", ");
                    }
                    prefs.append(skill4);
                }
                user.setPreferences(prefs.toString());
                user.setLinkedInUrl(rs.getString("LinkedIN_url"));
                user.setAvailability(rs.getString("Availability"));
                user.setResume(rs.getBytes("Resume_default"));
            } else {
                System.out.println("No user found with email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while fetching user profile.");
        }
        return user;
    }

    public int getCompanyIdByName(String companyName) {
        String query = "SELECT Company_ID FROM Company WHERE Company_name = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, companyName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Company_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Object[][] getUserApplications(String email) {
        // Default: Page 1 with a fixed page size (you can define your default pageSize)
        int defaultPage = 1;
        int defaultPageSize = 10;
        return getUserApplications(email, defaultPage, defaultPageSize);
    }

    // Update user profile.
    public boolean updateUserProfile(User user) {
        String query = "UPDATE Users SET First_name = ?, Last_name = ?, Mobile = ?, Dob = ?, Password = ?, " +
                "Security_Que = ?, Security_Ans = ?, Company_ID = ?, Job_role = ?, LinkedIN_url = ?, Availability = ? "
                +
                "WHERE Email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getMobile());
            stmt.setString(4, user.getDob());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, user.getSecurityQuestion());
            stmt.setString(7, user.getSecurityAnswer());
            int companyId = getCompanyIdByName(user.getCompany());
            if (companyId == -1) {
                System.err.println("Company not found for: " + user.getCompany());
                return false;
            }
            stmt.setInt(8, companyId);
            stmt.setString(9, user.getJobRole());
            stmt.setString(10, user.getLinkedInUrl());
            stmt.setString(11, user.getAvailability());
            stmt.setString(12, user.getEmail());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update user profile picture.
    public boolean updateUserProfilePicture(String email, byte[] imageBytes) {
        String sql = "UPDATE Users SET Profile_pic = ? WHERE Email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBytes(1, imageBytes);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] getUserProfilePicture(String email) {
        String sql = "SELECT Profile_pic FROM Users WHERE Email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("Profile_pic");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUserResume(String email, byte[] resumeBytes) {
        String sql = "UPDATE Users SET Resume_default = ? WHERE Email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBytes(1, resumeBytes);
            stmt.setString(2, email);
            int affected = stmt.executeUpdate();
            System.out.println("updateUserResume affected rows: " + affected);
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] getUserResume(String email) {
        String sql = "SELECT Resume_default FROM Users WHERE Email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                byte[] resumeData = rs.getBytes("Resume_default");
                System.out.println("Retrieved resume size: " + (resumeData != null ? resumeData.length : 0));
                return resumeData;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // === APPLICATIONS FUNCTIONALITY ===

    // 1. Get Job Listings (for search jobs screen) with pagination.
    public Object[][] getJobListings(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String query = "SELECT ra.Job_Title, c.Company_name AS Company, ra.Job_location, jt.Job_Type AS Job_Type " +
                "FROM Recruiters_Applications ra " +
                "LEFT JOIN Job_Type jt ON ra.Job_Type = jt.JobType_ID " +
                "LEFT JOIN Company c ON ra.Company_ID = c.Company_ID " +
                "LIMIT ? OFFSET ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();
            Object[][] data = new Object[rowCount][4];
            int row = 0;
            while (rs.next()) {
                data[row][0] = rs.getString("Job_Title");
                data[row][1] = rs.getString("Company");
                data[row][2] = rs.getString("Job_location");
                data[row][3] = rs.getString("Job_Type");
                row++;
            }
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }

    // 2. Get Filtered Job Listings
    public Object[][] getFilteredJobListings(String title, String company, String location, String jobType) {
        String baseQuery = "SELECT ra.Job_Title, c.Company_name AS Company, ra.Job_location, jt.Job_Type AS Job_Type " +
                "FROM Recruiters_Applications ra " +
                "LEFT JOIN Job_Type jt ON ra.Job_Type = jt.JobType_ID " +
                "LEFT JOIN Company c ON ra.Company_ID = c.Company_ID";
        StringBuilder queryBuilder = new StringBuilder(baseQuery);
        List<String> conditions = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            conditions.add("ra.Job_Title LIKE ?");
            parameters.add("%" + title + "%");
        }
        if (company != null && !company.isEmpty()) {
            conditions.add("c.Company_name LIKE ?");
            parameters.add("%" + company + "%");
        }
        if (location != null && !location.isEmpty()) {
            conditions.add("ra.Job_location LIKE ?");
            parameters.add("%" + location + "%");
        }
        if (jobType != null && !jobType.isEmpty()) {
            conditions.add("jt.Job_Type LIKE ?");
            parameters.add("%" + jobType + "%");
        }
        if (!conditions.isEmpty()) {
            queryBuilder.append(" WHERE ");
            queryBuilder.append(String.join(" AND ", conditions));
        }

        String query = queryBuilder.toString();
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();
            Object[][] data = new Object[rowCount][4];
            int row = 0;
            while (rs.next()) {
                data[row][0] = rs.getString("Job_Title");
                data[row][1] = rs.getString("Company");
                data[row][2] = rs.getString("Job_location");
                data[row][3] = rs.getString("Job_Type");
                row++;
            }
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }

    // 3. Get detailed information for a job post by Job Title.
    public Map<String, String> getJobDetail(String jobTitle) {
        // Correct SQL: Note the "SELECT" keyword at the beginning and no reference to a
        // missing Due_Date column.
        String query = "SELECT ra.Job_Title, c.Company_name, ra.Job_location, jt.Job_Type, " +
                "ra.Job_Description, ra.Required_Experience, ra.Date_Of_Application " +
                "FROM Recruiters_Applications ra " +
                "LEFT JOIN Job_Type jt ON ra.Job_Type = jt.JobType_ID " +
                "LEFT JOIN Company c ON ra.Company_ID = c.Company_ID " +
                "WHERE ra.Job_Title = ?";
        Map<String, String> details = new HashMap<>();
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, jobTitle);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                details.put("Job_Title", rs.getString("Job_Title"));
                details.put("Company_name", rs.getString("Company_name"));
                details.put("Job_location", rs.getString("Job_location"));
                details.put("Job_Type", rs.getString("Job_Type"));
                details.put("Job_Description", rs.getString("Job_Description"));
                details.put("Required_Experience", rs.getString("Required_Experience"));
                details.put("Date_Of_Application", rs.getString("Date_Of_Application"));
                return details;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean applyForJob(String jobTitle, String userEmail) {
        // First, fetch the User_ID and Company_ID for this user.
        int userId = getUserIdByEmail(userEmail);
        int companyId = getCompanyIdByEmail(userEmail);
        System.out.println("Applying for job: " + jobTitle + " with User_ID: " + userId + ", Company_ID: " + companyId);

        if (userId == -1) {
            System.err.println("User not found for email: " + userEmail);
            return false;
        }
        companyId = getCompanyIdByEmail(userEmail); // Or retrieve from Users table.
        if (companyId == -1) {
            System.err.println("Company not found for user email: " + userEmail);
            return false;
        }

        // Insert record with default Status_ID 1 = Pending.
        String insertQuery = "INSERT INTO Applications_User (Job_Title, Application_Date, User_ID, Company_ID, Status_ID) "
                +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, jobTitle);
            stmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            stmt.setInt(3, userId);
            stmt.setInt(4, companyId);
            stmt.setInt(5, 1); // Assume 1 represents 'Pending'

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows inserted: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Get user applications for a given user email.
    public Object[][] getUserApplications(String email, int page, int pageSize) {
        List<Object[]> list = new ArrayList<>();
        int userId = getUserIdByEmail(email);
        String query = "SELECT au.Application_ID as appNo, au.Job_Title, au.Application_Date, aps.Status_type " +
                "FROM Applications_User au " +
                "JOIN Application_Status aps ON au.Status_ID = aps.Status_ID " +
                "JOIN Users u ON u.User_ID = au.User_ID " +
                "WHERE u.Email = ?" +
                "ORDER BY au.application_ID DESC " +
                "LIMIT ? OFFSET ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setInt(2, pageSize);
            stmt.setInt(3, (page - 1) * pageSize);

            ResultSet rs = stmt.executeQuery();
            List<Object[]> rows = new ArrayList<>();
            while (rs.next()) {
                Object[] row = new Object[4];
                row[0] = rs.getInt("appNo");
                row[1] = rs.getString("job_title");
                row[2] = rs.getDate("application_date");
                // If s.status_type is null, you might want to handle it (for example,
                // "Unknown")
                row[3] = rs.getString("status_type") != null ? rs.getString("status_type") : "Unknown";
                rows.add(row);
            }
            return rows.toArray(new Object[rows.size()][]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Object[0][0];
    }

    public boolean withdrawApplication(int applicationId, String userEmail) {
        // Update the application's status to "Withdrawn". Here we assume that 10
        // represents "Withdrawn".
        String query = "UPDATE Applications_User SET Status_ID = 10 " +
                "WHERE Application_ID = ? AND User_ID = (SELECT User_ID FROM Users WHERE Email = ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, applicationId);
            stmt.setString(2, userEmail);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Additional helper methods.

    public int getUserIdByEmail(String email) {
        String query = "SELECT User_ID FROM Users WHERE Email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("User_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCompanyIdByEmail(String email) {
        String sql = "SELECT Company_ID FROM Users WHERE Email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Company_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Map<String, Object>> getJobPostsByUserId(int userId, int page, int pageSize) {
        List<Map<String, Object>> jobPosts = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT Job_ID, Job_Title, Date_Of_Application, Job_Type, Job_location " +
                     "FROM recruiters_applications WHERE user_ID = ? " +
                     "ORDER BY Date_Of_Application DESC " +
                     "LIMIT ? OFFSET ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, pageSize);
            stmt.setInt(3, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("Job_ID", rs.getInt("Job_ID"));
                row.put("Job_Title", rs.getString("Job_Title"));
                row.put("Date_Of_Application", rs.getDate("Date_Of_Application"));
                row.put("Job_Type", rs.getInt("Job_Type"));
                row.put("Job_location", rs.getString("Job_location"));
                jobPosts.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobPosts;
    }
    
    public int countJobPostsByUserId(int userId) {
        String sql = "SELECT COUNT(*) AS total FROM recruiters_applications WHERE user_ID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /*
     * RECRUITER SECTION
     */
    public Map<String, String> getRecruiterInfoByEmail(String email) {
        Map<String, String> recruiterInfo = new HashMap<>();

        String sql = "SELECT u.First_name, u.Last_name, u.Email, c.Company_name, c.Company_location, " +
                "u.Mobile, u.LinkedIN_url, c.Company_Website " +
                "FROM Users u " +
                "JOIN Company c ON u.Company_ID = c.Company_ID " +
                "WHERE u.Email = ? AND u.Role_ID = 2";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                recruiterInfo.put("First Name", rs.getString("First_name"));
                recruiterInfo.put("Last Name", rs.getString("Last_name"));
                recruiterInfo.put("Email", rs.getString("Email"));
                recruiterInfo.put("Company Name", rs.getString("Company_name"));
                recruiterInfo.put("Company Address", rs.getString("Company_location"));
                recruiterInfo.put("Company Phone", rs.getString("Mobile")); // treating user mobile as company phone
                recruiterInfo.put("Company LinkedIn", rs.getString("LinkedIN_url"));
                recruiterInfo.put("Company Website", rs.getString("Company_Website")); // Optional placeholder
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recruiterInfo;
    }

    public boolean updateRecruiterProfile(String email, String companyName, String companyAddress,
            String companyPhone, String linkedinUrl, String website) {

        String sql = "UPDATE Company c " +
                "JOIN Users u ON u.Company_ID = c.Company_ID " +
                "SET c.Company_name = ?, c.Company_location = ?, u.Mobile = ?, u.LinkedIN_url = ?, c.Company_Website = ? "
                +
                "WHERE u.Email = ? AND u.Role_ID = 2";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, companyName);
            stmt.setString(2, companyAddress);
            stmt.setString(3, companyPhone);
            stmt.setString(4, linkedinUrl);
            stmt.setString(5, website); // <-- new field added
            stmt.setString(6, email); // <-- now at index 6 instead of 5
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRecruiterProfilePicture(String email, byte[] imageBytes) {
        String sql = "UPDATE Users SET Profile_pic = ? WHERE Email = ? AND Role_ID = 2";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBytes(1, imageBytes);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] getRecruiterProfilePicture(String email) {
        String sql = "SELECT Profile_pic FROM Users WHERE Email = ? AND Role_ID = 2";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("Profile_pic"); // Might return null if no image saved yet
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add Job Posts Screen
    public int getNextGlobalJobId() {
        String sql = "SELECT MAX(Job_ID) AS maxId FROM recruiters_applications";
        int nextId = 1;

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int maxId = rs.getInt("maxId");
                if (!rs.wasNull()) {
                    nextId = maxId + 1;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nextId;
    }

    public boolean addJobPost(int userId, int companyId, String jobTitle, int jobTypeId,
            double minSalary, double maxSalary, String jobDescription,
            String jobLocation, String requiredExperience, Date dateOfApplication) {

        String sql = "INSERT INTO recruiters_applications " +
                "(user_ID, company_ID, Job_Title, Job_Type, Salary_Min, Salary_Max, " +
                "Job_Description, Job_location, Required_Experience, Date_Of_Application) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, companyId);
            stmt.setString(3, jobTitle);
            stmt.setInt(4, jobTypeId);
            stmt.setDouble(5, minSalary);
            stmt.setDouble(6, maxSalary);
            stmt.setString(7, jobDescription);
            stmt.setString(8, jobLocation);
            stmt.setString(9, requiredExperience);
            stmt.setDate(10, dateOfApplication);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateJobPost(int jobId, int userId, String jobTitle, int jobTypeId,
            double minSalary, double maxSalary, String jobDesc,
            String jobLocation, String experience, Date dateOfApplication) {
        String sql = "UPDATE recruiters_applications SET Job_Title = ?, Job_Type_ID = ?, Min_Salary = ?, Max_Salary = ?, "
                +
                "Job_Description = ?, Job_location = ?, Required_Experience = ?, Date_Of_Application = ? " +
                "WHERE Job_ID = ? AND User_ID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jobTitle);
            stmt.setInt(2, jobTypeId);
            stmt.setDouble(3, minSalary);
            stmt.setDouble(4, maxSalary);
            stmt.setString(5, jobDesc);
            stmt.setString(6, jobLocation);
            stmt.setString(7, experience);
            stmt.setDate(8, dateOfApplication);
            stmt.setInt(9, jobId);
            stmt.setInt(10, userId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteJobPostById(int jobId, int userId) {
        String sql = "DELETE FROM recruiters_applications WHERE Job_ID = ? AND User_ID = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jobId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // For adding admin dashboard
    // All users (for Admin dashboard table)
    public static List<Map<String, String>> getAllUsers() {
        List<Map<String, String>> userList = new ArrayList<>();
        String query = "SELECT User_ID, CONCAT(First_name, ' ', Last_name) AS Name, Email FROM Users WHERE Role_ID = 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, String> user = new HashMap<>();
                user.put("user_id", rs.getString("User_ID"));
                user.put("name", rs.getString("Name"));
                user.put("email", rs.getString("Email"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
    // Delete user by ID
    public static boolean deleteUserById(String userId) {
        String query = "DELETE FROM Users WHERE User_ID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Update user email by ID
    public static boolean updateUserEmail(String userId, String newEmail) {
        String query = "UPDATE Users SET Email = ? WHERE User_ID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newEmail);
            stmt.setString(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Get all recruiters (Role_ID = 2)
    public static List<Map<String, String>> getAllRecruiters() {
        List<Map<String, String>> recruiterList = new ArrayList<>();
        String query = "SELECT u.User_ID, CONCAT(u.First_name, ' ', u.Last_name) AS Name, u.Email, c.Company_name " +
                "FROM Users u " +
                "LEFT JOIN Company c ON u.Company_ID = c.Company_ID " +
                "WHERE u.Role_ID = 2";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, String> recruiter = new HashMap<>();
                recruiter.put("user_id", rs.getString("User_ID"));
                recruiter.put("name", rs.getString("Name"));
                recruiter.put("email", rs.getString("Email"));
                recruiter.put("company", rs.getString("Company_name"));
                recruiterList.add(recruiter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recruiterList;
    }
    // Update recruiter email
    public static boolean updateRecruiterEmail(String recruiterId, String newEmail) {
        String query = "UPDATE Users SET Email = ? WHERE User_ID = ? AND Role_ID = 2";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newEmail);
            stmt.setString(2, recruiterId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Delete recruiter by ID
    public static boolean deleteRecruiterById(String recruiterId) {
        String query = "DELETE FROM Users WHERE User_ID = ? AND Role_ID = 2";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, recruiterId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // for the stats tab
    public static Map<String, Integer> getPlatformStats() {
        Map<String, Integer> stats = new HashMap<>();

        String userCountQuery = "SELECT COUNT(*) FROM Users WHERE Role_ID = 1";
        String recruiterCountQuery = "SELECT COUNT(*) FROM Users WHERE Role_ID = 2";
        String jobsAppliedQuery = "SELECT COUNT(*) FROM Applications_User";
        String jobsPostedQuery = "SELECT COUNT(*) FROM recruiters_applications";
        String rejectedQuery = "SELECT COUNT(*) FROM Applications_User WHERE Status_ID = 3";
        String acceptedQuery = "SELECT COUNT(*) FROM Applications_User WHERE Status_ID = 2";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            stats.put("users", getCount(conn, userCountQuery));
            stats.put("recruiters", getCount(conn, recruiterCountQuery));
            stats.put("jobs_applied", getCount(conn, jobsAppliedQuery));
            stats.put("jobs_posted", getCount(conn, jobsPostedQuery));
            stats.put("rejected", getCount(conn, rejectedQuery));
            stats.put("accepted", getCount(conn, acceptedQuery));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }
    // Helper method for reuse
    private static int getCount(Connection conn, String query) {
        try (PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
