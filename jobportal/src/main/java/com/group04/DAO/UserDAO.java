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
    public boolean registerUser(User user) {
        String query = "INSERT INTO users (first_name, last_name, email, mobile, dob, password, role_id, security_question, security_answer) "
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
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If user found, login success
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Recover password
    public String recoverPassword(String email, String question, String answer) {
        String query = "SELECT password FROM users WHERE email = ? AND security_question = ? AND security_answer = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, question);
            stmt.setString(3, answer);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Get role ID by email
    public int getRoleId(String email) {
        String query = "SELECT role_id FROM users WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("role_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no role is found (error case)
    }

    // ✅ Retrieve user profile data based on email
    public User getUserProfile(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        User user = null;

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setMobile(rs.getString("mobile"));
                user.setDob(rs.getString("dob"));
                user.setRole(rs.getInt("role_id"));
                user.setSecurityQuestion(rs.getString("security_question"));
                user.setSecurityAnswer(rs.getString("security_answer"));
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
        String query = "UPDATE users SET first_name = ?, last_name = ?, mobile = ?, dob = ?, password = ?, security_question = ?, security_answer = ? WHERE email = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getMobile());
            stmt.setString(4, user.getDob());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, user.getSecurityQuestion());
            stmt.setString(7, user.getSecurityAnswer());
            stmt.setString(8, user.getEmail());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
