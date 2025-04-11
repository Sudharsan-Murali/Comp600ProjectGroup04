package com.group04.DAO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
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
    
    // Helper method: convert a File to a byte array.
    private byte[] fileToByteArray(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ------------------------ Registration, Login, Recover, getRoleId ------------------------
    
    // ✅ Register a new user (Registration currently does not include extra fields.)
    public boolean registerUser(User user) {
        String query = "INSERT INTO Users (First_name, Last_name, Email, Mobile, Dob, Password, Role_ID, Security_Que, Security_Ans) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
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
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Login is successful if a record exists.
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ✅ Recover password
    public String recoverPassword(String email, String question, String answer) {
        String query = "SELECT Password FROM Users WHERE Email = ? AND Security_Que = ? AND Security_Ans = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
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
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Role_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Error: No role found.
    }
    
    // ------------------------ Helper Method for Company Lookup ------------------------
    
    // Helper method: given a company name, look up its Company_ID from the Company table.
    public String getCompanyId(String companyName) {
        String query = "SELECT Company_ID FROM Company WHERE Company_name = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, companyName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Company_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ------------------------ Retrieve and Update User Profile (User Section) ------------------------
    
    // ✅ Retrieve user profile data based on email.
    // Uses a LEFT JOIN with the Company table to fetch the company name.
    public User getUserProfile(String email) {
        String query = "SELECT u.First_name, u.Last_name, u.Email, u.Mobile, u.Dob, u.Role_ID, u.Security_Que, u.Security_Ans, " +
                       "c.Company_name, u.Job_role, u.Skill_1, u.Skill_2, u.Skill_3, u.Skill_4, u.LinkedIN_url, u.Availability, u.Profile_pic " +
                       "FROM Users u LEFT JOIN Company c ON u.Company_ID = c.Company_ID " +
                       "WHERE u.Email = ?";
        User user = null;
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
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
                user.setRole(rs.getInt("Role_ID"));
                user.setSecurityQuestion(rs.getString("Security_Que"));
                user.setSecurityAnswer(rs.getString("Security_Ans"));
                // Extra fields.
                user.setCompany(rs.getString("Company_name")); // Joined company name.
                user.setJobRole(rs.getString("Job_role"));
                // Combine skill fields.
                String skill1 = rs.getString("Skill_1");
                String skill2 = rs.getString("Skill_2");
                String skill3 = rs.getString("Skill_3");
                String skill4 = rs.getString("Skill_4");
                StringBuilder prefs = new StringBuilder();
                if (skill1 != null && !skill1.isEmpty()) prefs.append(skill1);
                if (skill2 != null && !skill2.isEmpty()) {
                    if (prefs.length() > 0) prefs.append(", ");
                    prefs.append(skill2);
                }
                if (skill3 != null && !skill3.isEmpty()) {
                    if (prefs.length() > 0) prefs.append(", ");
                    prefs.append(skill3);
                }
                if (skill4 != null && !skill4.isEmpty()) {
                    if (prefs.length() > 0) prefs.append(", ");
                    prefs.append(skill4);
                }
                user.setPreferences(prefs.toString());
                user.setLinkedInUrl(rs.getString("LinkedIN_url"));
                user.setAvailability(rs.getString("Availability"));
                // Retrieve profile picture as binary data.
                user.setProfilePic(rs.getBytes("Profile_pic"));
            } else {
                System.out.println("No user found with email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while fetching user profile.");
        }
        return user;
    }
    
    // ✅ Update user profile.
    // This method updates the editable fields: Company, Job_role, LinkedIN_url, Availability,
    // as well as the binary data for Profile_pic and the Resume_default field.
    public boolean updateUserProfile(User user) {
        // Look up the Company_ID from the Company table using the provided company name.
        String companyId = getCompanyId(user.getCompany());
        if (companyId == null) {
            System.out.println("Error: Company not found for: " + user.getCompany());
            return false;
        }
        String query = "UPDATE Users SET Company_ID = ?, Job_role = ?, LinkedIN_url = ?, Availability = ?, Profile_pic = ?, Resume_default = ? WHERE Email = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, companyId);
            stmt.setString(2, user.getJobRole());
            stmt.setString(3, user.getLinkedInUrl());
            stmt.setString(4, user.getAvailability());
            // For the profile picture, set the byte array.
            stmt.setBytes(5, user.getProfilePic());
            // For the resume, if you are storing it as binary, convert using the helper.
            byte[] resumeBytes = null;
            if (user.getResumePath() != null && !user.getResumePath().isEmpty()) {
                resumeBytes = fileToByteArray(new File(user.getResumePath()));
            }
            stmt.setBytes(6, resumeBytes);
            stmt.setString(7, user.getEmail());
            int count = stmt.executeUpdate();
            System.out.println("DEBUG: Updated row count = " + count);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ------------------------ Recruiter Section (unchanged from incoming) ------------------------
    
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
                recruiterInfo.put("Company Phone", rs.getString("Mobile")); // using user mobile
                recruiterInfo.put("Company LinkedIn", rs.getString("LinkedIN_url"));
                recruiterInfo.put("Company Website", rs.getString("Company_Website"));
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
                     "SET c.Company_name = ?, c.Company_location = ?, u.Mobile = ?, u.LinkedIN_url = ?, c.Company_Website = ? " +
                     "WHERE u.Email = ? AND u.Role_ID = 2";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, companyName);
            stmt.setString(2, companyAddress);
            stmt.setString(3, companyPhone);
            stmt.setString(4, linkedinUrl);
            stmt.setString(5, website);
            stmt.setString(6, email);
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
                return rs.getBytes("Profile_pic");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
