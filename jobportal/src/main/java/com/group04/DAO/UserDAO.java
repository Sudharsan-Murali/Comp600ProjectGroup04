package com.group04.DAO;

import java.sql.*;

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
        // Updated query to fetch extra columns.
        String query = "SELECT First_name, Last_name, Email, Mobile, Dob, Role_ID, Security_Que, Security_Ans, " +
                       "Company_ID, Job_role, Skill_1, Skill_2, Skill_3, Skill_4, LinkedIN_url, Availability " +
                       "FROM Users WHERE Email = ?";
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
                user.setRole(rs.getInt("Role_ID"));
                user.setSecurityQuestion(rs.getString("Security_Que"));
                user.setSecurityAnswer(rs.getString("Security_Ans"));
                // Extra fields:
                // For company, we simply set the Company_ID as a string.
                user.setCompany(rs.getString("Company_ID"));
                user.setJobRole(rs.getString("Job_role"));
                // Combine Skill_1..Skill_4 into one string for preferences.
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
            } else {
                System.out.println("No user found with email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while fetching user profile.");
        }
        return user;
    }

    // ✅ Update user profile
    public boolean updateUserProfile(User user) {
        // Updated query to include extra fields.
        String query = "UPDATE Users SET First_name = ?, Last_name = ?, Mobile = ?, Dob = ?, Password = ?, " +
                       "Security_Que = ?, Security_Ans = ?, Company_ID = ?, Job_role = ?, LinkedIN_url = ?, Availability = ? " +
                       "WHERE Email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getMobile());
            stmt.setString(4, user.getDob());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, user.getSecurityQuestion());
            stmt.setString(7, user.getSecurityAnswer());
            stmt.setString(8, user.getCompany());
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
}
