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

    // ✅ Register a new user
    // (Registration currently does not include extra fields.)
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

    // ✅ Login a user
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

    // ✅ Recover password
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

    // ✅ Get role ID by email
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
        return -1; // Return -1 if no role is found.
    }

    // ✅ Retrieve user profile data based on email
    public User getUserProfile(String email) {
        // Updated query to also fetch Resume_default.
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
                // Basic fields.
                user.setFirstName(rs.getString("First_name"));
                user.setLastName(rs.getString("Last_name"));
                user.setEmail(rs.getString("Email"));
                user.setMobile(rs.getString("Mobile"));
                user.setDob(rs.getString("Dob"));
                user.setPassword(rs.getString("Password"));
                user.setRole(rs.getInt("Role_ID"));
                user.setSecurityQuestion(rs.getString("Security_Que"));
                user.setSecurityAnswer(rs.getString("Security_Ans"));
                // Instead of Company_ID, set the company name.
                user.setCompany(rs.getString("Company_name"));
                user.setJobRole(rs.getString("Job_role"));
                // Combine Skill_1..Skill_4 into one string preferences.
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
                // NEW: Retrieve the resume bytes.
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
        return -1; // Return -1 if company not found. You can handle this as an error.
    }

    // ✅ Update user profile
    public boolean updateUserProfile(User user) {
        // Updated query to include extra fields.
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
                // Handle error, for example:
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

    /*
     * USER SECTION
     */
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
                return rs.getBytes("Profile_pic"); // Returns null if not set.
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

    public int getUserIdByEmail(String email) {
        String query = "SELECT User_ID FROM Users WHERE Email = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
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
        String sql = "SELECT company_ID FROM Users WHERE Email = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("company_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Map<String, Object>> getJobPostsByUserId(int userId) {
        List<Map<String, Object>> jobPosts = new ArrayList<>();
        String sql = "SELECT Job_ID, Job_Title, Date_Of_Application, Job_Type, Job_location FROM recruiters_applications WHERE user_ID = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
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

}
