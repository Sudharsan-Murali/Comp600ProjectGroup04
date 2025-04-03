package com.group04;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 *
 */
public class App {
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/jobportal"; // Replace with your DB name
    private static final String USER = "root"; // Your MySQL username
    private static final String PASSWORD = "root"; // Your MySQL password

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL Database!");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed! Check database credentials.");
            e.printStackTrace();
        }
        return conn;
    }

    public static void fetchData() {
        Connection conn = getConnection();
        if (conn == null) {
            System.out.println("❌ Database connection failed.");
            return;
        }

        String query = "SELECT * FROM users"; // SQL Query

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("📄 User Data:");
            System.out.println("----------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.println("ID: " + id + " | Name: " + name + " | Email: " + email);
            }

            System.out.println("----------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        fetchData(); // Test the connection
    }
}
