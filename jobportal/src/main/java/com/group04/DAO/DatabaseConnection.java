package com.group04.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Replace "job_portal" with your actual DB name if different
    //private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String URL = "jdbc:mysql://localhost:3306/job_portal?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";  // Your MySQL username
    private static final String PASSWORD = "root";  // Your MySQL password

    /**
     * Establishes and returns a connection to the job_portal MySQL database.
     * @return Connection object or null if connection fails.
     */
    public static Connection getConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Return DB connection
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Connection failed! Check your DB name, user, and password.");
            e.printStackTrace();
        }
        return null;
    }
}
